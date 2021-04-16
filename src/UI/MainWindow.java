package UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import Client.ClientMain;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainWindow {

    public Button addTeamButton;

    @FXML
    protected void createTeamDialog(ActionEvent actionEvent) {
        try {
            new ClientMain().showAddTeamWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAddTeamButtonActive(boolean isActive) {
        addTeamButton.setDisable(!isActive);
    }

}
