package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Server implements Runnable{

    public static int port = 7274;

    public static LinkedList<Socket> clients;



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

        Thread thread = new Thread(new Server());
        thread.start();

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket s = serverSocket.accept();
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
                    System.exit(2);
                    break;
                case "refresh":
                    break;
            }
        }
    }
}
