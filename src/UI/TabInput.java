package UI;


import Client.Chat.Chatroom;
import Client.Chat.Message;
import Client.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import static Utils.BasicFunctionLibrary.getEntryFromLinkedList;

/**
 * Class for the FXML of each tab used in the main window
 *
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class TabInput {

    /**
     * Button for the addTaskButton
     */
    public Button addTaskButton;

    /**
     * Button for the inviteButton
     */
    public Button inviteButton;

    /**
     * Team for the tabInput to be loaded
     */
    public Team selectedTeam;

    /**
     * Text for the teamName
     */
    public Text teamName;

    /**
     * TextField for the messageField
     */
    public TextField messageField;

    /**
     * VBox for the chat
     */
    public VBox chat;

    /**
     * VBox for the tasks
     */
    public VBox tasks;

    /**
     * initialize method called to print and load everything needed and also create a textfield-keylistener
     */
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
                sendMessage();
            }
        });
    }

    /**
     * constructor used to create a new tabinput with the selected team
     *
     * @param selectedTeam the selected team
     */
    public TabInput(Team selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    /**
     * method used to open the invite user window
     */
    @FXML
    public void createTeamDialog() {
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

    /**
     * used to send a message using a STRequest
     */
    @FXML
    public void sendMessage() {
        if (selectedTeam == null) return;
        Message message = new Message(Client.user, messageField.getText(), new Date());
        messageField.clear();
        selectedTeam.getChatroom().addMessage(message);
        printMessages(selectedTeam.getChatroom());
        String request = "sendMessage:" + message + ",teamid=ꠦ" + selectedTeam.getId() + "ꠦ";
        Client.sendSTRequest(request);
    }

    /**
     * used to print the messages from the provided chatroom
     *
     * @param chatroom chatroom of the team with the needed massages
     */
    @FXML
    public void printMessages(Chatroom chatroom) {
        Platform.runLater(() -> {
            try {
                chat.getChildren().clear();
                for (Message message : chatroom.getMessages()) {
                    LinkedHashSet<User> members = getEntryFromLinkedList(Client.user.myTeams, selectedTeam).members;
                    LinkedList<User> users = new LinkedList<>(members);
                    chat.getChildren().add(new Text(getEntryFromLinkedList(users, message.getUser()).getUsername() + ": " + message.getText()));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * change the name of the tabinput to a shorted version of the teams name
     *
     * @param text text to change
     * @param team team to get the correct team-name
     */
    public void changeTeamText(Text text, Team team) {
        switch (text.getText().length()) {
            case 2 -> text.setText(team.getName().substring(0, 2).toUpperCase());
            case 1 -> text.setText(team.getName().substring(0, 1).toUpperCase());
            default -> text.setText(team.getName().substring(0, 3).toUpperCase());
        }
    }

    /**
     * used to load all tasks into the UI
     */
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
