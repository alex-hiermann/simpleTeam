package Server;

import Client.Team;
import Client.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Server implements Runnable{

    public static int port = 7274; //default server port

    public static Path path = Paths.get("./simpleTeam/data.db"); //default database path

    public static LinkedList<Socket> clients = new LinkedList<>(); //list with the clients

    public static LinkedList<User> users = new LinkedList<>(); //list with the users

    public static HashMap<User, Listener> listeners = new HashMap<User, Listener>(); //map with each user with its listener

    public static LinkedList<Team> teams = new LinkedList<>(); //list with the teams

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
        } catch (Exception ignored) {}

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
        } catch (Exception ignored) {}
        //TODO delete users 'a' and 'b'! :(
        users.add(new User("a", "a", "a", "a", new Date(), "0CC175B9C0F1B6A831C399E269772661"));
        users.add(new User("b", "b", "b", "b", new Date(), "92EB5FFEE6AE2FEC3AD71C777531578F"));
        System.err.println("Starting Server");
        Thread thread = new Thread(new Server());
        thread.start();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
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
            switch (command) {
                case "shutdown":
                    System.err.println("Server shutdowning in 5 seconds!");
                    try {
                        Thread.sleep(5000);
                        System.exit(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case "refresh":
                    System.out.println("Refreshed");
                    break;
                case "getTeams":
                    System.out.print("Teams:{");
                    for (Team team : teams) {
                        System.out.print(team);
                        System.out.print(", ");
                    }
                    System.out.println("};");
                    break;
                case "getUsers":
                    System.out.println("Users:{");
                    for (User user : users) {
                        System.out.println(user);
                    }
                    System.out.println("};");
                    break;
                case "clearTeams":
                    System.out.println("Cleared all teams");
                    teams.clear();
                    break;
                case "setPath":
                    //TODO insert path-mechanics
                    break;
                default:
                    System.err.println(("Unexpected value: " + command));
                    break;
            }
        }
    }
}
