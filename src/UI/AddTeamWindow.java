package UI;

import Client.Client;
import Client.Team;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import Client.Team;
import javafx.scene.layout.AnchorPane;

public class AddTeamWindow {

    public TextField teamname;
    public TextArea teamdesc;
    public AnchorPane pane;

    public void createTeam(ActionEvent actionEvent) {
        if ((!teamname.getText().isEmpty() && !teamname.getText().isBlank())) {
            Team tempTeam = new Team(teamname.getText(), teamdesc.getText());
            tempTeam.setAdmin(Client.user);
            Client.sendSTRequest("createTeam:" + tempTeam + ",email=ꠦ" + Client.user.getEmail() + "ꠦ");
        }
    }
}