package UI;

import Client.ClientMain;
import Client.Client;
import Client.User;
import Utils.BasicFunctionLibrary;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.Socket;
import java.sql.Date;
import java.time.ZoneId;

public class RegisterWindow {

    public DatePicker date;
    public TextField lastname;
    public TextField name;
    public PasswordField password;
    public TextField email;
    public TextField username;
    public TextField server;
    public AnchorPane pane;

    public void register(ActionEvent actionEvent) {
        User tempUser = new User(username.getText(), name.getText(), lastname.getText(), email.getText(), date.getValue(), BasicFunctionLibrary.hashPassword(password.getText()));
        Socket temp = ClientMain.connectToServer(server.getText());
        Client client = new Client(temp, new User("tempUser05070201"));
        new Thread(client).start();
        Client.sendSTRequest("registerUser:" + tempUser);
    }
}
