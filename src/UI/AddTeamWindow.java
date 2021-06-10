package UI;

import Client.Client;
import Client.Team;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import Client.Team;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class AddTeamWindow {

    public TextField teamname;
    public TextArea teamdesc;
    public AnchorPane pane;
    public Button button;

    public void initialize() {
        teamname.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                createTeam(new ActionEvent());
            }
        });
    }

    public void createTeam(ActionEvent actionEvent) {
        if ((!teamname.getText().isEmpty() && !teamname.getText().isBlank())) {
            Team tempTeam = new Team(teamname.getText(), teamdesc.getText());
            tempTeam.setAdmin(Client.user);
            Client.sendSTRequest("createTeam:" + tempTeam + ",email=ꠦ" + Client.user.getEmail() + "ꠦ");
        }
    }
}