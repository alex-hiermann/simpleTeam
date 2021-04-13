package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static int port = 7274;

    public static void main(String[] args) {
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

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket s = serverSocket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
