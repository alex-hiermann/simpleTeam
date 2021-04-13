package Client;

import java.net.Socket;

public class Client implements Runnable {

    Socket socket;
    User user;

    public Client(Socket socket, User user) {
        this.socket = socket;
        this.user = user;
    }

    @Override
    public void run() {
        System.out.println("Hello I'm " + user.getUsername());
    }
}
