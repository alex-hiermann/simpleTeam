package UI;

import Client.Client;
import Client.Task;
import Client.Team;
import Utils.BasicFunctionLibrary;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

public class AddNewTaskWindow {
    public TextField name;
    public TextArea description;
    public DatePicker due;
    public MenuButton type;
    public MenuButton diff;

    public static Team team;
    public static boolean admin;


    public void addTask(ActionEvent actionEvent) {
        if (!admin) {
            if (!type.getText().equalsIgnoreCase("Reminder")) {
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

    private void sendRequest() {
        Task tempTask = new Task(name.getText(), description.getText(), due.getValue(),
                BasicFunctionLibrary.extractTaskTypeFromText(type.getText()),
                BasicFunctionLibrary.extractTaskDifficultyFromText(type.getText()));
        Client.sendSTRequest("addTask:" + tempTask);
        Client.user.addTask(tempTask);
        BasicFunctionLibrary.getEntryFromLinkedList(Client.user.myTeams, team).tasks.add(tempTask);
    }
}
