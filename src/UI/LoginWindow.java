package UI;

import Client.ClientMain;
import Client.Client;
import Client.User;
import Utils.BasicFunctionLibrary;
import Utils.Configuration;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class used to create the login window which lets the user create or login into a user profile
 *
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class LoginWindow {
    /**
     * TextField for the email
     */
    public TextField email;

    /**
     * PasswordField for the password
     */
    public PasswordField password;

    /**
     * TextField for the server
     */
    public TextField server;

    /**
     * Button for the button
     */
    public Button button;

    /**
     * VBox for the vbox
     */
    public VBox vbox;

    /**
     * AnchorPane for the pane
     */
    public AnchorPane pane;

    /**
     * Button for the register
     */
    public Button register;

    /**
     * initialize method called to create the needed textfield-keylistener
     */
    public void initialize() {
        password.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                loginAction();
            }
        });

        server.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                loginAction();
            }
        });
    }

    /**
     * needed to call the extracted method
     */
    @FXML
    public void loginAction() {
        extracted();
    }

    /**
     * used to login the user
     */
    public void extracted() {
        Matcher matcher = Pattern.compile(Configuration.CHECK_IP_AND_PORT_REGEX).matcher(server.getText());
        Matcher emailMatcher = Pattern.compile(Configuration.CHECK_EMAIL_REGEX).matcher(email.getText());
        if (!matcher.matches()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Server IP is invalid!");
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
        Socket temp = ClientMain.connectToServer(server.getText());
        Client client = new Client(temp, new User("tempUser05070201"));
        new Thread(client).start();
        Client.sendSTRequest("login:email=ꠦ" + email.getText() + "ꠦ,password=ꠦ" + BasicFunctionLibrary.hashPassword(password.getText()) + "ꠦ");
    }

    /**
     * used to register a user
     */
    public void registerAction() {
        ClientMain.currentStage.close();
        try {
            new ClientMain().showRegisterWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}