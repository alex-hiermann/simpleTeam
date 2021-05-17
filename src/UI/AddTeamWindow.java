package UI;

import Client.Client;
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
        if ((!teamname.getText().isEmpty() && !teamname.getText().isBlank()) || !teamname.getText().contains("'")) {
            Client.sendSTRequest("createTeam:" + new Team(teamname.getText(), teamdesc.getText()) + ",email='" + Client.user.getEmail() + "'");
            System.out.println("createTeam:" + new Team(teamname.getText(), teamdesc.getText()) + ",email='" + Client.user.getEmail() + "'");
        }
    }
}