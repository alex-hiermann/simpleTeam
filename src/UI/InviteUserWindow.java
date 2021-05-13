package UI;

import Client.Client;
import Client.User;
import Client.Team;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InviteUserWindow {
    public AnchorPane pane;
    public TextField email;
    public Button addUserButton;
    public Text infoText;

    public static Team selectedTeam;

    public void inviteUserAction(ActionEvent actionEvent) {
        String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        Pattern pattern = Pattern.compile(regex);

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
