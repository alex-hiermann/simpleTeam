package UI;


import Client.Chat.Chatroom;
import Client.Chat.Message;
import Client.Client;
import Client.ClientMain;
import Client.Task;
import Client.Team;
import Utils.BasicFunctionLibrary;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static Utils.BasicFunctionLibrary.getEntryFromLinkedList;

public class TabInput {

    public Button addTaskButton;
    public Button inviteButton;

    public Team selectedTeam;
    public Text teamName;
    public TextField messageField;
    public VBox chat;
    public VBox tasks;

    public void initialize() {
        printMessages(selectedTeam.getChatroom());
        loadTasks();

        addTaskButton.setOnAction(l -> {
            try {
                Client.sendSTRequest("requestUsers:teamId=ꠦ" + selectedTeam.getId() + "ꠦ");
                new ClientMain().showAddNewTaskWindow(selectedTeam, selectedTeam.getAdmin().equals(Client.user));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        changeTeamText(teamName, selectedTeam);

        messageField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                sendMessage(new ActionEvent());
            }
        });
    }

    public TabInput(Team selectedTeam) {
        this.selectedTeam = selectedTeam;
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
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

    public void changeTeamText(Text text, Team team) {
        switch (text.getText().length()) {
            case 2 -> text.setText(team.getName().substring(0, 2).toUpperCase());
            case 1 -> text.setText(team.getName().substring(0, 1).toUpperCase());
            default -> text.setText(team.getName().substring(0, 3).toUpperCase());
        }
    }

    public void loadTasks() {
        Platform.runLater(() -> {
            try {
                tasks.getChildren().clear();
                for (Task task : getEntryFromLinkedList(Client.user.myTeams, new Team(selectedTeam.getId())).tasks) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UI/TaskUI.fxml"));
                    fxmlLoader.setControllerFactory(l -> new TaskUI(getEntryFromLinkedList(
                            getEntryFromLinkedList(Client.user.myTeams,
                                    new Team(selectedTeam.getId())).tasks, task)));

                    tasks.getChildren().add(fxmlLoader.load());
                }
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        });
    }
}
