package Client;

import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.IOException;
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
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String data = "";
            while (!(data = (String) dis.readUTF()).isEmpty()) {    //Until data is not empty

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
