package UI;

import Client.Chat.Message;
import Client.Client;
import Client.ClientMain;
import Client.Team;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainWindow {

    @FXML
    public static Button addTeamButton;
    @FXML
    public VBox teams = new VBox();
    public Button sendButton;
    public TextField messageField;

    public void initialize() {
        Platform.runLater(() -> teams.getChildren().removeAll(teams.getChildren()));
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
        Platform.runLater(() -> {
            TitledPane teamPane = new TitledPane();
            teamPane.setText(team.getName());

            GridPane grid = new GridPane();
            Button chatRoom = new Button("Select Team");

            grid.setVgap(4);
            grid.setPadding(new Insets(5, 5, 5, 5));
            grid.add(new Label("Description: "), 0, 0);
            grid.add(new Text(team.getDescription()), 1, 0);
//            grid.add(new Label("empty space"), 0, 1);
//            grid.add(new Label("empty space"), 1, 1);
            grid.add(chatRoom, 1, 2);

            teamPane.setContent(grid);
            teams.getChildren().add(teamPane);
        });
    }

    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        Message message = new Message(ClientMain.client, messageField.getText());

    }

    public void setAddTeamButtonActive(boolean isActive) {
        addTeamButton.setDisable(!isActive);
    }
}
