package Server;

import Client.Client;
import Client.Team;
import Utils.BasicFunctionLibrary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Listener implements Runnable {

    static Socket socket;

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String data = "";

            //STRequest handling
            synchronized (this) {
                try {
                    while (!(data = dataInputStream.readUTF()).isEmpty()) {
                        String command = data.split(":")[0];
                        String args[] = data.split(":")[1].split(",");
                        switch (command) {
                            case "createTeam" -> {
                                Team team = new Team(BasicFunctionLibrary.findValueFromArgs("name", args), BasicFunctionLibrary.findValueFromArgs("desc", args));
                                Server.teams.add(team);
                                sendSTRequestToClient("createTeam:" + team);
                            }
                            case "getTeams" -> {
                                String request = "";
                                for (Team team : Server.teams) {
                                    //TODO
                                }
                            }
                        }
                    }
                } catch (SocketException sE) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Listener(Socket s) {
        socket = s;
    }

    public static boolean sendSTRequestToClient(String message) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
