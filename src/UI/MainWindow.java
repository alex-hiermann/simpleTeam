package UI;

import Client.Chat.Chatroom;
import Client.Chat.Message;
import Client.Client;
import Client.ClientMain;
import Client.Team;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Date;

public class MainWindow {

    @FXML
    public static Button addTeamButton;

    @FXML
    public VBox teams = new VBox();
    public Button sendButton;
    public TextField messageField;
    public TitledPane chatParentContainer;
    public VBox chat;
    public AnchorPane pane;
    public MenuBar menubar;
    public Button inviteButton;
    private Team selectedTeam;

    public void initialize() {
        Platform.runLater(() -> teams.getChildren().removeAll(teams.getChildren()));
        if (Client.user.myTeams.size() > 0) {
            selectedTeam = Client.user.myTeams.getFirst();
            for (Team team : Client.user.myTeams) {
                addTeam(team);
            }
            if (selectedTeam.getChatroom().getMessages().size() > 0) {
                printMessages(selectedTeam.getChatroom());
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
            Button chatRoomButton = new Button("Select Team");
            chatRoomButton.setOnAction(actionEvent -> {
//            if (selectedTeam.getAdmin().equals(Client.user)) {
//            }
                inviteButton.setDisable(false);
                inviteButton.setText("Add a user to your team");
                inviteButton.setOnAction(l -> {
                    try {
                        new ClientMain().showInviteUserWindow(selectedTeam);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                selectedTeam = team;
                chatParentContainer.setText(selectedTeam.getName());
                printMessages(selectedTeam.getChatroom());
            });

            grid.setVgap(4);
            grid.setPadding(new Insets(5, 5, 5, 5));
            grid.add(new Label("Description: "), 0, 0);
            grid.add(new Text(team.getDescription()), 1, 0);
//            grid.add(new Label("empty space"), 0, 1);
//            grid.add(new Label("empty space"), 1, 1);
            grid.add(chatRoomButton, 1, 2);

            teamPane.setContent(grid);
            teams.getChildren().add(teamPane);
        });
    }

    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        if (selectedTeam == null) return;
        Message message = new Message(Client.user, messageField.getText(), new Date());
        messageField.clear();
        selectedTeam.getChatroom().addMessage(message);
        printMessages(selectedTeam.getChatroom());
        String request = "sendMessage:" + message + ",teamid='" + selectedTeam.getId() + "'";
        System.out.println("Request = " + request);
        Client.sendSTRequest(request);
    }

    @FXML
    public void printMessages(Chatroom chatroom) {
        chat.getChildren().clear();
        Platform.runLater(() -> {
            for (Message message : chatroom.getMessages()) {
                System.out.println("message = " + message);
                chat.getChildren().add(new Text(message.getText()));
            }
        });
    }
    public void setAddTeamButtonActive(boolean isActive) {
        addTeamButton.setDisable(!isActive);
    }
}
