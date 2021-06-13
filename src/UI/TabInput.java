package UI;

import Client.Chat.Chatroom;
import Client.Chat.Message;
import Client.Client;
import Client.ClientMain;
import Client.Team;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Date;

public class TabInput {

    public Button addTaskButton;
    public Button inviteButton;

    public Team selectedTeam;
    public Text teamName;
    public TextField messageField;
    public VBox chat;
    public VBox tasks;

    public void initialize() {
        selectedTeam = Client.user.myTeams.getFirst();
        if (selectedTeam.getChatroom().getMessages().size() > 0) {
            printMessages(selectedTeam.getChatroom());
        }

        addTaskButton.setOnAction(l -> {
            try {
                Client.sendSTRequest("requestUsers:teamId=ꠦ" + selectedTeam.getId() + "ꠦ");
                new ClientMain().showAddNewTaskWindow(selectedTeam, selectedTeam.getAdmin().equals(Client.user));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        switch (teamName.getText().length()) {
            case 2 -> teamName.setText(selectedTeam.getName().substring(0, 2).toUpperCase());
            case 1 -> teamName.setText(selectedTeam.getName().substring(0, 1).toUpperCase());
            default -> teamName.setText(selectedTeam.getName().substring(0, 3).toUpperCase());
        }

        messageField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                sendMessage(new ActionEvent());
            }
        });
    }

    @FXML
    public void createTeamDialog(ActionEvent actionEvent) {
        if (selectedTeam.getAdmin().equals(Client.user)) {
            inviteButton.setDisable(false);
            inviteButton.setOnAction(l -> {
                try {
                    new ClientMain().showInviteUserWindow(selectedTeam);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            inviteButton.setDisable(true);
        }
        Client.sendSTRequest("requestUsers:teamId=ꠦ" + selectedTeam.getId() + "ꠦ");
    }

    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        if (selectedTeam == null) return;
        Message message = new Message(Client.user, messageField.getText(), new Date());
        messageField.clear();
        selectedTeam.getChatroom().addMessage(message);
        printMessages(selectedTeam.getChatroom());
        String request = "sendMessage:" + message + ",teamid=ꠦ" + selectedTeam.getId() + "ꠦ";
        Client.sendSTRequest(request);
    }

    @FXML
    public void printMessages(Chatroom chatroom) {
        Platform.runLater(() -> {
            try {
                chat.getChildren().clear();
                for (Message message : chatroom.getMessages()) {
                    chat.getChildren().add(new Text(message.getUser().getUsername() + ": " + message.getText()));
                }
            } catch (NullPointerException ignored) {
            }
        });
    }
}
