package Server;

import Client.Team;
import Client.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Server implements Runnable{

    public static int port = 7274;

    public static LinkedList<Socket> clients = new LinkedList<>();

    public static LinkedList<User> users = new LinkedList<>();

    public static LinkedList<Team> teams = new LinkedList<>();

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
                    break;
                case "getTeams":
                    for (Team team : teams) {
                        System.out.println(team);
                    }
                    break;
                case "getUsers":
                    for (User user : users) {
                        System.err.println(user);
                    }
                    break;
                case "clearTeams":
                    teams = null;
                    break;
                default:
                    System.err.println(("Unexpected value: " + command));
                    break;
            }
        }
    }
}
