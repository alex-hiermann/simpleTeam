package UI;

import Client.Client;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import Client.Team;

public class AddTeam {

    public TextField teamname;
    public TextArea teamdesc;

    public void createTeam(ActionEvent actionEvent) {
        if ((!teamname.getText().isEmpty() && !teamname.getText().isBlank()) || !teamname.getText().contains("'")) {
            Client.sendSTRequest("createTeam:" + new Team(teamname.getText(), teamdesc.getText()));
        }
    }
}