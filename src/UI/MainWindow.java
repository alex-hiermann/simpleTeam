package UI;

import Client.Client;
import Client.ClientMain;
import Client.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainWindow {

    @FXML
    public static Button addTeamButton;

    public VBox teams = new VBox();
    public TextField chatMessage;

    public void initialize() {
        System.out.println(Client.myTeams.size());
        if (Client.myTeams.size() > 0) {
            for (Team team : Client.myTeams) {
                addTeam(team);
            }
        }
    }

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
        Text text = new Text(team.getName());
//        HBox entry = new HBox();
//        entry.getChildren().add(text);
        teams.getChildren().add(text);
    }

    public void setAddTeamButtonActive(boolean isActive) {
        addTeamButton.setDisable(!isActive);
    }
}
