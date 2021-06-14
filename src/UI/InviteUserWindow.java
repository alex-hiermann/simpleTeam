package UI;

import Client.Client;
import Client.Team;
import Client.User;
import Utils.Configuration;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InviteUserWindow {
    /**
     * AnchorPane for the pane
     */
    public AnchorPane pane;

    /**
     * TextField for the email
     */
    public TextField email;

    /**
     * Text for the infoText
     */
    public Text infoText;

    /**
     * Team where the user should be invited to
     */
    public static Team selectedTeam;

    /**
     * initialize method called to create a textfield-keylistener
     */
    public void initialize() {
        email.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                inviteUserAction();
            }
        });
    }

    /**
     * method used to invite a new user with sending a STRequest
     */
    public void inviteUserAction() {
        Pattern pattern = Pattern.compile(Configuration.CHECK_EMAIL_REGEX);

        Matcher matcher = pattern.matcher(email.getText());
        if (matcher.matches()) {
            Client.sendSTRequest("addUserToTeam:" + selectedTeam + "," + new User(email.getText()));
            infoText.setText("Provided there is a user with this email, he will be added to your team");
        } else {
            System.err.println("Invalid Email");
            infoText.setText("Invalid Email. Try again");
        }
    }
}
