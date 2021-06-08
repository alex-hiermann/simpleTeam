package UI;

import Client.Client;
import Client.*;
import Client.Task;
import Client.Team;
import Utils.BasicFunctionLibrary;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.LinkedList;

public class AddNewTaskWindow {
    public TextField name;
    public TextArea description;
    public DatePicker due;
    public ChoiceBox<String> type;
    public static Team team;
    public static boolean admin;
    public ChoiceBox<String> difficulty;
    public ChoiceBox<String> users;

    ObservableList<String> list = FXCollections.observableArrayList();


    public void addTask(ActionEvent actionEvent) {
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

    @FXML
    public void initialize() {
        list.removeAll(list);
        LinkedList<User> members = BasicFunctionLibrary.getEntryFromLinkedList(Client.user.myTeams, team).members;
        System.out.println("members = " + members);
        for (User user : members) {
            list.add(user.getUsername() + ":" + user.getEmail());
        }
        users.getItems().addAll(list);
    }

    private void sendRequest() {
        Task tempTask = new Task(name.getText(), description.getText(), due.getValue(),
                BasicFunctionLibrary.extractTaskTypeFromText(type.getValue()),
                BasicFunctionLibrary.extractTaskDifficultyFromText(difficulty.getValue()));
        tempTask.setTeam_id(team.getId());
        Client.sendSTRequest("addTask:" + tempTask);
        Client.user.addTask(tempTask);
        BasicFunctionLibrary.getEntryFromLinkedList(Client.user.myTeams, team).tasks.add(tempTask);
    }
}
