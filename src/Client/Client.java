package Client;

import Utils.BasicFunctionLibrary;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;

public class Client implements Runnable {

    static Socket socket;
    public static User user;


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
                String command = data.split(":")[0];    //WER HAT GEFRAGT???????????
                String[] args = new String[0];
                try {
                    String[] temp = data.split(":");
                    StringBuilder arguments = new StringBuilder();
                    for (int i = 1; i < temp.length; i++) {
                        if (i == 1) {
                            arguments.append(temp[i]);
                        } else {
                            arguments.append(":").append(temp[i]);
                        }
                    }
                    args = arguments.toString().split(",");
                } catch (Exception ignored) {
                }
                switch (command) {
                    case "createTeam" -> {
                        Team team = new Team(BasicFunctionLibrary.findValueFromArgs("teamname", args), BasicFunctionLibrary.findValueFromArgs("teamdesc", args), Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("teamid", args)));
                        team.setAdmin(user);
                        team.members.add(user);
                        user.myTeams.add(team);
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
                    case "userExists" -> {
                        Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("User already exists!");
                                    alert.showAndWait();
                                }
                        );
                    }
                    case "canLogin" -> {
                        user = new User(
                                BasicFunctionLibrary.findValueFromArgs("username", args),
                                BasicFunctionLibrary.findValueFromArgs("name", args),
                                BasicFunctionLibrary.findValueFromArgs("lastname", args),
                                BasicFunctionLibrary.findValueFromArgs("email", args),
                                LocalDate.parse(BasicFunctionLibrary.findValueFromArgs("birth", args)),
                                BasicFunctionLibrary.findValueFromArgs("password", args));
                        user.setId(Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("userId", args)));
                        Platform.runLater(() -> {
                            ClientMain.currentStage.close();
                            try {
                                new ClientMain().showMainWindow();
                                Client.sendSTRequest("getTeams:" + user);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    case "rejectedLogin" -> {
                        Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("User credentials are invalid!");
                                    alert.setContentText("Try again!");
                                    alert.showAndWait();
                                }
                        );
                    }
                    case "userTeams" -> {
                        String[] teamRequests = data.split(":")[1].split(";");
                        user.myTeams.clear();
                        for (String team : teamRequests) {
                            String[] tempArgs = team.split(",");
                            Team team1 = new Team(BasicFunctionLibrary.findValueFromArgs("teamname", tempArgs), BasicFunctionLibrary.findValueFromArgs("teamdesc", tempArgs));
                            user.myTeams.add(team1);
                        }
                        ClientMain.mainWindow.initialize();
                    }
                    case "requestTeams" -> {
                        System.out.println("Hello I'm " + user.getEmail());
                        sendSTRequest("getTeams:" + user);
                    }
//                    case "fetchMessages" -> {
//                        String messageRequest[] = data.split(":")[1].split(";");
//                        user.myTeams.forEach(l -> l.getChatroom().getMessages().clear());
//                        //TODO messages per team
//                    }
                    case "" -> {

                    }
                }
            }
        } catch (IOException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
