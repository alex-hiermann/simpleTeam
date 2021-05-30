package Server;

import Client.Chat.Message;
import Client.Team;
import Client.User;
import Utils.Configuration;

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

    public static HashMap<User, Listener> listeners = new HashMap<User, Listener>();

    public static LinkedList<Team> teams = new LinkedList<>();

    public static Path path = Paths.get("./simpleTeam/data.db"); //default database path


    public static void main(String[] args) {
        try {
            if (args[0].equalsIgnoreCase("-port")) {
                try {
                    port = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    System.err.println("Invalid port input!");
                    System.err.println("Shutting down");
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
                    System.err.println(e.getMessage());
                    System.err.println("Invalid path input!");
                    System.err.println("Shutting down");
                    System.exit(1);
                }
            }
        } catch (Exception ignored) {
        }
        //TODO Create TestFile with these Users, so that u dont need to delete them :) (just adopt them into another file)
        users.add(new User("a", "a", "a", "a", LocalDate.MIN, "0CC175B9C0F1B6A831C399E269772661", 1));
        users.add(new User("a", "a", "a", "aa@aa.at", LocalDate.MIN, "0CC175B9C0F1B6A831C399E269772661", 2));
        users.add(new User("b", "b", "b", "bb@bb.at", LocalDate.MAX, "92EB5FFEE6AE2FEC3AD71C777531578F", 3));
        System.err.println("Starting Server");
        Thread thread = new Thread(new Server());
        thread.start();
        try {
            port = ((port == 0) ? Configuration.DEFAULT_PORT : port);
            ServerSocket serverSocket = new ServerSocket(Configuration.DEFAULT_PORT);
            while (true) { //An infinity loop is exactly what we want! ;) @IntelliJ IDEA
                Socket s = serverSocket.accept();
                new Thread(new Listener(s)).start();
                clients.add(s);
                System.out.println("New client connected: " + s.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            Scanner sc = new Scanner(System.in);
            String command = sc.nextLine();
            switch (command.toLowerCase()) {
                case "shutdown" -> {
                    System.err.println("Server shutdowning in 5 seconds!");
                    try {
                        Thread.sleep(5000);
                        System.exit(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                case "refresh" -> System.out.println("Refreshed");
                case "getteams" -> {
                    System.out.print("Teams:{");
                    for (Team team : teams) {
                        System.out.print(team);
                        System.out.println(", ");
                    }
                    System.out.println("};");
                }
                case "getusers" -> {
                    System.out.println("Users:{");
                    for (User user : users) {
                        System.out.println(user);
                    }
                    System.out.println("};");
                }
                case "clearteams" -> {
                    System.out.println("Cleared all teams");
                    teams.clear();
                }
                case "getallmessages" -> {
                    System.out.println("####Messages####");
                    for (Team team : teams) {
                        System.out.println("##Messages from Team: " + team.getName() + " : " + team.getId());
                        for (Message message : team.getChatroom().getMessages()) {
                            System.out.println(message);
                        }
                        System.out.println();
                    }
                }
                case "getlisteners" -> {
                    System.out.println("#####Listeners#####");
                    for (User user : listeners.keySet()) {
                        System.out.println("User:" + user.getEmail() + "     =     " + listeners.get(user));
                    }
                    System.out.println();
                }
                default -> System.err.println(("Unexpected value: " + command));
            }
        }
    }
}
