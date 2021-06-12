package UI;

import Client.Client;
import Client.ClientMain;
import Client.User;
import Utils.BasicFunctionLibrary;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.net.Socket;

public class RegisterWindow {

    public DatePicker date;
    public TextField lastname;
    public TextField name;
    public PasswordField password;
    public TextField email;
    public TextField username;
    public TextField server;
    public AnchorPane pane;

    public void initialize() {
        username.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                register(new ActionEvent());
            }
        });

        password.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                register(new ActionEvent());
            }
        });

        server.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                register(new ActionEvent());
            }
        });
    }

    public void register(ActionEvent actionEvent) {
        User tempUser = new User(username.getText(), name.getText(), lastname.getText(), email.getText(), date.getValue(), BasicFunctionLibrary.hashPassword(password.getText()));
        Socket temp = ClientMain.connectToServer(server.getText());
        Client client = new Client(temp, new User("tempUser05070201"));
        new Thread(client).start();
        Client.sendSTRequest("registerUser:" + tempUser);
    }
}