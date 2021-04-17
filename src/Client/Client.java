package Client;

import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {

    static Socket socket;
    static User user;

    public Client(Socket socket, User user) {
        Client.socket = socket;
        Client.user = user;

    }


    /**
     * @param request Request in the following pattern: 'requestType':'option1'='value1','option2'='value2'
     * @return Successful
     */
    public static boolean sendSTRequest(String request) {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(request);
            dos.flush();
            dos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
