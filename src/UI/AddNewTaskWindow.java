package UI;

import Client.Client;
import Client.Task;
import Client.Team;
import Client.User;
import Utils.BasicFunctionLibrary;
import Utils.Configuration;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used for the add new task fxml window which is used to create a new task
 * <p>
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class AddNewTaskWindow {

    /**
     * TextField for the name
     */
    public TextField name;

    /**
     * TextArea for the description
     */
    public TextArea description;

    /**
     * DatePicker for the due
     */
    public DatePicker due;

    /**
     * ChoiceBox with String-inputs for the type
     */
    public ChoiceBox<String> type;

    /**
     * Team for the team
     */
    public static Team team;

    /**
     * If the user is the task-admin or not
     */
    public static boolean admin;

    /**
     * ChoiceBox with String-inputs for the difficulty
     */
    public ChoiceBox<String> difficulty;

    /**
     * ChoiceBox with String-inputs for the users
     */
    public ChoiceBox<String> users;

    /**
     * ObservableList with String-inputs for the users tasks
     */
    ObservableList<String> list = FXCollections.observableArrayList();

    /**
     * adds a new task using STRequests and alerts
     */
    @FXML
    public void addTask() {
        Pattern pattern = Pattern.compile(Configuration.CHECK_EMAIL_REGEX);
        Matcher matcher;
        try {
            matcher = pattern.matcher(users.getValue().split(":")[1]);
            if (!matcher.matches()) {
                Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Invalid user");
                            alert.setContentText("Please make sure that you have set the user in the bottom right corner of the window!");
                            alert.showAndWait();
                        }
                );
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid user");
                        alert.setContentText("Please make sure that you have set the user in the bottom right corner of the window!");
                        alert.showAndWait();
                    }
            );
        }

        if (!admin) {
            if (!type.getValue().equalsIgnoreCase("Reminder")) {
                Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("User access rights not sufficient to add a task of type: 'TASK' OR 'MILESTONE'");
                            alert.setContentText("Non admin users can only create tasks of type 'Reminder'. Your team admin assigns the tasks for you!");
                            alert.showAndWait();
                        }
                );
            } else {
                sendRequest();
            }
        } else {
            sendRequest();
        }
    }

    /**
     * initialize method called to create a textfield-keylistener
     */
    @FXML
    public void initialize() {
        list.clear();
        for (User user : BasicFunctionLibrary.getEntryFromLinkedList(Client.user.myTeams, team).members) {
            list.add(user.getUsername() + ":" + user.getEmail());
        }
        users.getItems().addAll(list);

        name.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                addTask();
            }
        });
    }

    /**
     * sends a STRequest for the tasks
     */
    private void sendRequest() {
        Task tempTask = new Task(name.getText(), description.getText(), due.getValue(),
                BasicFunctionLibrary.extractTaskTypeFromText(type.getValue()),
                BasicFunctionLibrary.extractTaskDifficultyFromText(difficulty.getValue()));
        tempTask.setTeam_id(team.getId());
        tempTask.setUser(new User(users.getValue().split(":")[1]));
        int rand = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        tempTask.setTaskId(rand);
        Client.sendSTRequest("addTask:" + tempTask + ",clientTaskId=???" + rand + "???");
        Client.user.addTask(tempTask);
        BasicFunctionLibrary.getEntryFromLinkedList(Client.user.myTeams, team).tasks.add(tempTask);
    }
}
