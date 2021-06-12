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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Server implements Runnable {

    public static int port;

    public static LinkedList<Socket> clients = new LinkedList<>();

    public static LinkedList<User> users = new LinkedList<>();

    public static HashMap<User, Listener> listeners = new HashMap<>();

    public static LinkedList<Team> teams = new LinkedList<>();

    public static Path path = Configuration.SERVER_DB_PATH;

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
                    path = Paths.get(args[1]);
                } catch (Exception e) {
                    System.err.println(Configuration.ANSI_RED + e.getMessage() + Configuration.ANSI_RESET);
                    System.err.println(Configuration.ANSI_RED + "Invalid path input!" + Configuration.ANSI_RESET);
                    System.err.println(Configuration.ANSI_RED + "Shutting down" + Configuration.ANSI_RESET);
                    System.exit(1);
                }
            }
        } catch (Exception ignored) {
        }
        //TODO Create TestFile with these Users, so that u dont need to delete them :) (just adopt them into another file)
        users.add(new User("a", "a", "a", "a", LocalDate.MIN, "0CC175B9C0F1B6A831C399E269772661", 1));
        User coolUser = new User("a", "a", "a", "aa@aa.at", LocalDate.MIN, "0CC175B9C0F1B6A831C399E269772661", 2);
        users.add(coolUser);
        users.add(new User("b", "b", "b", "bb@bb.at", LocalDate.MAX, "92EB5FFEE6AE2FEC3AD71C777531578F", 3));

        Team team = new Team("Testteam", "Team for testing", 1);
        team.setAdmin(coolUser);
        team.members.add(coolUser);
        teams.add(team);

        System.out.println(Configuration.ANSI_RED + "Starting Server" + Configuration.ANSI_RESET);

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
        SQLiteHandler.getAllTasks();
        SQLiteHandler.getAllMessages();
        SQLiteHandler.getAllTeams();

        Thread thread = new Thread(new Server());
        thread.start();
        try {
            port = ((port == 0) ? Configuration.DEFAULT_PORT : port);

            ServerSocket serverSocket = new ServerSocket(Configuration.DEFAULT_PORT);
            while (true) {
                Socket s = serverSocket.accept();
                new Thread(new Listener(s)).start();
                clients.add(s);
                System.out.println(Configuration.CTR_BEFORE + Configuration.ANSI_RED + "New client connected: " + s.toString() + Configuration.ANSI_RESET + Configuration.CTR_AFTER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
