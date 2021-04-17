package Client;

import Server.Server;
import UI.MainWindow;
import Utils.BasicFunctionLibrary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class Client implements Runnable {

    static Socket socket;
    static User user;

    static LinkedList<Team> myTeams = new LinkedList<>();

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
            String data;
            while (!(data = dis.readUTF()).isEmpty()) {    //Until data is not empty
                String command = data.split(":")[0];
                String args[] = data.split(":")[1].split(",");
                switch (command) {
                    case "createTeam" -> {
                        Team team = new Team(BasicFunctionLibrary.findValueFromArgs("name", args), BasicFunctionLibrary.findValueFromArgs("desc", args));
                        team.setAdmin(user);
                        myTeams.add(team);
                        new MainWindow().addTeam(team);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
