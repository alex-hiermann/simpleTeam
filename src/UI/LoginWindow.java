package UI;

import Client.ClientMain;
import Client.Client;
import Client.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.Socket;


public class LoginWindow {
    public TextField username;
    public PasswordField password;
    public TextField server;

    @FXML
    protected void loginAction(ActionEvent actionEvent) {
        if (username.getText().equals("Alegs") && password.getText().equals("Mags")) {
            Socket temp = ClientMain.connectToServer(server.getText());
            Client client = new Client(temp, new User(username.getText()));
            Thread thread = new Thread(client);
            thread.start();
        }
    }

}