package UI;

import Client.Client;
import Client.Task;
import Client.Team;
import Utils.BasicFunctionLibrary;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddNewTaskWindow {
    public TextField name;
    public TextArea description;
    public DatePicker due;
    public ChoiceBox<String> type;
    public static Team team;
    public static boolean admin;
    public ChoiceBox<String> difficulty;


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
