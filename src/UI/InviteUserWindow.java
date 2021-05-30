package UI;

import Client.Client;
import Client.Team;
import Client.User;
import Utils.Configuration;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InviteUserWindow {
    public AnchorPane pane;
    public TextField email;
    public Text infoText;

    public static Team selectedTeam;

    public void inviteUserAction(ActionEvent actionEvent) {
        Pattern pattern = Pattern.compile(Configuration.INVITATION_REGEX);

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
