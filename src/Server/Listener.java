package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Listener implements Runnable{

    Socket s;

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
            while (true) {
                dataInputStream.readUTF();
                // TODO accept STRequests
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Listener(Socket s) {
        this.s = s;
    }
}
