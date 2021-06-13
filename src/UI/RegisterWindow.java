package UI;

import Client.Client;
import Client.ClientMain;
import Client.User;
import Utils.BasicFunctionLibrary;
import Utils.Configuration;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        Matcher serverMatcher = Pattern.compile(Configuration.CHECK_IP_AND_PORT_REGEX).matcher(server.getText());
        Matcher emailMatcher = Pattern.compile(Configuration.CHECK_EMAIL_REGEX).matcher(email.getText());

        if (!serverMatcher.matches()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Server IP is invalid");
                alert.setContentText("Try again!");
                alert.showAndWait();
            });
            return;
        }
        if (!emailMatcher.matches()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Email is invalid");
                alert.setContentText("Try again!");
                alert.showAndWait();
            });
            return;
        }


        User tempUser = new User(username.getText(), name.getText(), lastname.getText(), email.getText(), date.getValue(), BasicFunctionLibrary.hashPassword(password.getText()));
        Socket temp = ClientMain.connectToServer(server.getText());
        Client client = new Client(temp, new User("tempUser05070201"));
        new Thread(client).start();
        Client.sendSTRequest("registerUser:" + tempUser);
    }
}