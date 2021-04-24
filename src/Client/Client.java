package Client;

import Utils.BasicFunctionLibrary;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class Client implements Runnable {

    static Socket socket;
    public static User user;

    public static LinkedList<Team> myTeams = new LinkedList<>();

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
        System.out.println("####WELCOME USER: " + user.getUsername() + "####");
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String data;
            while (!(data = dis.readUTF()).isEmpty()) {    //Until data is not empty
                String command = data.split(":")[0];
                String args[] = new String[0];
                try {
                    args = data.split(":")[1].split(",");
                } catch (Exception ignored) {
                }
                switch (command) {
                    case "createTeam" -> {
                        Team team = new Team(BasicFunctionLibrary.findValueFromArgs("name", args), BasicFunctionLibrary.findValueFromArgs("desc", args));
                        team.setAdmin(user);
                        myTeams.add(team);
                        ClientMain.mainWindow.initialize();
                    }
                    case "userRegistered" -> {
                        Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Success");
                                    alert.setHeaderText("New User generated!");
                                    alert.showAndWait();
                                }
                        );
                        Platform.runLater(() -> {
                            ClientMain.currentStage.close();
                            try {
                                new ClientMain().showLoginWindow();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
        } catch (IOException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
