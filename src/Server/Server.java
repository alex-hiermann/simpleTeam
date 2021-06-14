package Server;

import Client.Chat.Message;
import Client.Task;
import Client.Team;
import Client.User;
import Utils.BasicFunctionLibrary;
import Utils.Configuration;
import Utils.SQLite.Connection;
import Utils.SQLite.SQLiteHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * SimpleTeam server class
 */
public class Server implements Runnable {

    /**
     * Server port. Can be set through the -port parameter
     */
    public static int port;

    /**
     * Clients
     */
    public static LinkedList<Socket> clients = new LinkedList<>();

    /**
     * Actual users
     */
    public static LinkedList<User> users = new LinkedList<>();

    /**
     * Listener HashMap in Order to properly send information to the appropriate users
     */
    public static HashMap<User, Listener> listeners = new HashMap<>();

    /**
     * Simple Team Teams List
     */
    public static LinkedList<Team> teams = new LinkedList<>();


    /**
     * Init server
     * @param args arguments. [-port %PORTNUMBER%] [-path %PATH TO DB%]
     */
    public static void main(String[] args) {
        try {
            if (args[0].equalsIgnoreCase("-port")) {
                try {
                    port = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    System.err.println(Configuration.ANSI_RED + e.getMessage() + Configuration.ANSI_RESET);
                    System.err.println(Configuration.ANSI_RED + "Invalid port input!" + Configuration.ANSI_RESET);
                    System.err.println(Configuration.ANSI_RED + "Shutting down" + Configuration.ANSI_RESET);
                    System.exit(1);
                }
            }
        } catch (Exception ignored) {
        }

        try {
            if (args[0].equalsIgnoreCase("-path")) {
                try {
                    Configuration.SERVER_DB_PATH = Paths.get(args[1]);
                    System.out.println(Configuration.ANSI_CYAN + "Successfully set the DB path to: " + Configuration.SERVER_DB_PATH + Configuration.ANSI_RESET);
                } catch (Exception e) {
                    System.err.println(Configuration.ANSI_RED + e.getMessage() + Configuration.ANSI_RESET);
                    System.err.println(Configuration.ANSI_RED + "Invalid path input!" + Configuration.ANSI_RESET);
                    System.err.println(Configuration.ANSI_RED + "Shutting down" + Configuration.ANSI_RESET);
                    System.exit(1);
                }
            }
        } catch (Exception ignored) {
        }
        System.out.println(Configuration.ANSI_RED + "Starting Server" + Configuration.ANSI_RESET);

        try {
            //Create simpleTeam Server file structure
            BasicFunctionLibrary.createServerFolderStructure();

            //Create and connect to a database
            SQLiteHandler.createNewDatabase();

            //Create Default Tables IF NOT EXIST
            SQLiteHandler.createDefaultTables();

            //SQLite establish connection
            Connection.connect();

            //Retrieve data
            SQLiteHandler.getAllUsers();
            SQLiteHandler.getAllTeams();
            SQLiteHandler.getAllMessages();
            SQLiteHandler.getAllTasks();
        } catch (Exception e) {
            System.err.println(Configuration.ANSI_RED + "--------------SQLite failure. Not using SQLite!--------------");
        }

        Thread thread = new Thread(new Server());
        thread.start();
        try {
            port = ((port == 0) ? Configuration.DEFAULT_PORT : port);
            ServerSocket serverSocket = new ServerSocket(Configuration.DEFAULT_PORT);
            while (true) {
                Socket s = serverSocket.accept();
                new Thread(new Listener(s)).start();
                clients.add(s);
                System.out.println(Configuration.CTR_BEFORE + Configuration.ANSI_RED + "New client connected: " + s.toString() + "\n" + Configuration.ANSI_RESET + Configuration.CTR_AFTER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Server command line
     */
    @Override
    public void run() {
        while (true) {
            System.out.print(Configuration.ANSI_RESET + "Server>");
            Scanner sc = new Scanner(System.in);
            String command = sc.nextLine();
            if (!(command.trim().isBlank() || command.trim().isEmpty() || command.trim().equalsIgnoreCase("\n"))) {
                switch (command.toLowerCase()) {
                    case "shutdown" -> {
                        System.out.println(Configuration.ANSI_RED + "Server shutdowning in 5 seconds!" + Configuration.ANSI_RESET);
                        try {
                            Thread.sleep(5000);
                            System.exit(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    case "refresh" -> System.out.println(Configuration.ANSI_RED + "Refreshed" + Configuration.ANSI_RESET);
                    case "getteams" -> {
                        System.out.println(Configuration.ANSI_RED + "Teams:{");
                        for (Team team : teams) {
                            System.out.print(team);
                            System.out.println(", ");
                        }
                        System.out.println("};" + Configuration.ANSI_RESET);
                    }
                    case "getusers" -> {
                        System.out.println(Configuration.ANSI_RED + "Users:{");
                        for (User user : users) {
                            System.out.println(user);
                        }
                        System.out.println("};" + Configuration.ANSI_RESET);
                    }
                    case "clearteams" -> {
                        System.out.println(Configuration.ANSI_RED + "Cleared all teams" + Configuration.ANSI_RESET);
                        teams.clear();
                    }
                    case "getmessages" -> {
                        System.out.println(Configuration.ANSI_RED + "####Messages####");
                        for (Team team : teams) {
                            System.out.println("##Messages from Team: " + team.getName() + " : " + team.getId());
                            for (Message message : team.getChatroom().getMessages()) {
                                System.out.println(message);
                            }
                            System.out.println(Configuration.ANSI_RESET);
                        }
                    }
                    case "getlisteners" -> {
                        System.out.println(Configuration.ANSI_RED + "#####Listeners#####");
                        for (User user : listeners.keySet()) {
                            System.out.println("User:" + user.getEmail() + "     =     " + listeners.get(user));
                        }
                        System.out.println(Configuration.ANSI_RESET);
                    }
                    case "gettasks" -> {
                        System.out.println(Configuration.ANSI_RED + "#####Tasks#####");
                        for (Team team : teams) {
                            System.out.println(Configuration.ANSI_RED + "   ###" + team.getName() + ":" + team.getId() + "###");
                            for (Task task : team.tasks) {
                                System.out.println(Configuration.ANSI_RED + "       #" + task.getName() + "," + task.getDifficulty() + ", for user: " + task.getUser().getId());
                            }
                            System.out.println();
                        }
                    }
                    default -> System.out.println(Configuration.ANSI_RED + ("Unexpected value: " + command) + Configuration.ANSI_RESET);
                }
            }
        }
    }
}
