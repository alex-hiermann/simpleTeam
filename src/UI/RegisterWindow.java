package UI;

import Client.Client;
import Client.ClientMain;
import Client.User;
import Utils.BasicFunctionLibrary;
import Utils.Configuration;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for the register window which is used by the user to create a new user profile
 * <p>
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class RegisterWindow {

    /**
     * Datepicker for the date
     */
    public DatePicker date;

    /**
     * TextField for the lastname
     */
    public TextField lastname;

    /**
     * TextField for the name
     */
    public TextField name;

    /**
     * PasswordField for the password
     */
    public PasswordField password;

    /**
     * TextField for the email
     */
    public TextField email;

    /**
     * TextField for the username
     */
    public TextField username;

    /**
     * TextField for the server
     */
    public TextField server;

    /**
     * AnchorPane for the pane
     */
    public AnchorPane pane;

    /**
     * initialize method called to create a textfield-keylistener
     */
    public void initialize() {
        username.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                register();
            }
        });

        password.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                register();
            }
        });

        server.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                register();
            }
        });
    }

    /**
     * used to register a new user profile using STRequests and alerts
     */
    public void register() {

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