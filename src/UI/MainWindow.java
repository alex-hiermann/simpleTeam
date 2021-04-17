package UI;

import Client.ClientMain;
import Client.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainWindow {

    @FXML
    public static Button addTeamButton;
    @FXML
    public VBox teams = new VBox();

    @FXML
    protected void createTeamDialog(ActionEvent actionEvent) {
        try {
            new ClientMain().showAddTeamWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void addTeam(Team team) {
        System.err.println(team);
        HBox entry = new HBox();
        Text text = new Text(team.getName());
        entry.getChildren().add(text);
        teams.getChildren().add(entry);
    }

    public void setAddTeamButtonActive(boolean isActive) {
        addTeamButton.setDisable(!isActive);
    }
}
