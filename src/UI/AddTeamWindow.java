package UI;

import Client.Client;
import Client.Team;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

/**
 * Class for the Team Window
 *
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class AddTeamWindow {

    /**
     * TextField for the teamname
     */
    public TextField teamname;

    /**
     * TextArea for the teamdesc
     */
    public TextArea teamdesc;

    /**
     * AnchorPane for the pane
     */
    public AnchorPane pane;

    /**
     * Button for the button
     */
    public Button button;

    /**
     * initialize method called to create a textfield-keylistener
     */
    public void initialize() {
        teamname.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                createTeam();
            }
        });
    }

    /**
     * create a new team with sending a STRequest
     */
    public void createTeam() {
        if ((!teamname.getText().isEmpty() && !teamname.getText().isBlank())) {
            Team tempTeam = new Team(teamname.getText(), teamdesc.getText());
            tempTeam.setAdmin(Client.user);
            Client.sendSTRequest("createTeam:" + tempTeam + ",email=ꠦ" + Client.user.getEmail() + "ꠦ");
        }
    }
}