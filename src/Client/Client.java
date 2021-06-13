package Client;

import Client.Chat.Message;
import Utils.BasicFunctionLibrary;
import Utils.Configuration;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Arrays;

import static Utils.BasicFunctionLibrary.findValueFromArgs;
import static Utils.BasicFunctionLibrary.getEntryFromLinkedList;

/**
 * Client class which receives and handles STRequest sent from the server
 */
public class Client implements Runnable {

    /**
     * Socket connected to the listener
     */
    static Socket socket;
    /**
     * User object
     */
    public static User user;


    /**
     * @param socket Socket
     * @param user   User (Can also be tempUser for login purposes)
     */
    public Client(Socket socket, User user) {
        Client.socket = socket;
        Client.user = user;
    }

    /**
     * @param request Request in the following pattern: 'requestType':'option1'='value1','option2'='value2'
     */
    public static void sendSTRequest(String request) {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(request);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is the core of simpleTeam. It listens to server responses and replicates data
     */
    @Override
    public void run() {
        System.out.println(Configuration.ANSI_GREEN + "Client has been started!" + Configuration.ANSI_RESET);
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String data;
            while (!(data = dis.readUTF()).isEmpty()) {
                String command = data.split(":")[0];
                String[] args = new String[0];
                try {
                    args = BasicFunctionLibrary.getArgs(data);
                } catch (Exception ignored) {
                }
                switch (command) {
                    //Called when the server created your team
                    case "createTeam" -> {
                        Team team = new Team(BasicFunctionLibrary.findValueFromArgs("teamname", args),
                                BasicFunctionLibrary.findValueFromArgs("teamdesc", args),
                                Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("teamId", args)));
                        team.setAdmin(user);
                        team.members.add(user);
                        user.myTeams.add(team);
                        ClientMain.mainWindow.initialize();
                    }
                    //Alerts the user about a successful registration
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
                    //Alerts the user, that the email he used in the registration is already in use
                    case "userExists" -> Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("User already exists!");
                                alert.showAndWait();
                            }
                    );
                    //Logins the user
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
                    //Alerts the user that their credentials are incorrect
                    case "rejectedLogin" -> Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("User credentials are invalid!");
                                alert.setContentText("Try again!");
                                alert.showAndWait();
                            }
                    );
                    //Fetch the teams from the server
                    case "fetchTeam" -> {
                        Team team = new Team(BasicFunctionLibrary.findValueFromArgs("teamname", args),
                                findValueFromArgs("teamdesc", args),
                                Integer.parseInt(findValueFromArgs("teamId", args)),
                                new User(findValueFromArgs("adminEamil", args)));
                        user.myTeams.add(team);
                        ClientMain.mainWindow.initialize();
                    }
                    //Force refresh teams
                    case "requestTeams" -> sendSTRequest("getTeams:" + user);
                    //Fetches a single Message
                    case "fetchMessage" -> {
                        Team team = user.myTeams.get(user.myTeams.indexOf(new Team(Integer.parseInt(BasicFunctionLibrary
                                .findValueFromArgs("teamId", args)))));
                        team.getChatroom().addMessage(new Message(new User(BasicFunctionLibrary.findValueFromArgs("email", args)),
                                BasicFunctionLibrary.findValueFromArgs("messageText", args),
                                Message.dateFormat.parse(BasicFunctionLibrary.findValueFromArgs("date", args))));
                        if (ClientMain.mainWindow.selectedTeam.equals(team)) {
//                            printMessages(team.getChatroom());
                        }
                    }
                    //Fetches a single User
                    case "fetchedUser" -> {
                        BasicFunctionLibrary.getEntryFromLinkedList(user.myTeams, new Team(Integer.parseInt(
                                BasicFunctionLibrary.findValueFromArgs("teamId", args)))).members.add(BasicFunctionLibrary.extractUserFromArgs(args));
                    }
                    //Alerts the user about a successful task creation and sets the server parameters
                    case "taskAddSuccess" -> {
                        int teamId = Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("teamId", args));
                        int clientTaskId = Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("clientTaskId", args));
                        Task tempTask = new Task(clientTaskId);
                        int newTaskId = Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("taskId", args));
                        BasicFunctionLibrary.getEntryFromLinkedList(getEntryFromLinkedList(user.myTeams,
                                new Team(teamId)).tasks, tempTask).setTaskId(newTaskId);
                        tempTask.setTaskId(newTaskId);
                        Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Success");
                                    alert.setHeaderText("Your new task has been added to your team!");
                                    alert.setContentText("If the user is online he will receive a notification that a new Task has been assigned to him");
                                    alert.showAndWait();
                                }
                        );
                    }
                    //Alerts the user that the task creation failed
                    case "taskAddFail" -> Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Error");
                                alert.setHeaderText("There has been a problem on the server");
                                alert.setContentText("The server somehow couldn't add the task to the team!");
                                alert.showAndWait();
                            }
                    );

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
