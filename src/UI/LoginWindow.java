package UI;

import Client.ClientMain;
import Client.Client;
import Client.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;


public class LoginWindow {
    public TextField username;
    public PasswordField password;
    public TextField server;
    public ProgressBar progress;
    public Button button;

    @FXML
    protected void loginAction(ActionEvent actionEvent) {
        if (username.getText().equals("Alegs") && password.getText().equals("Mags")) {
            username.setText("BUTTON WAS PRESSED!");
            button.setDisable(true);
            Socket temp = ClientMain.connectToServer(server.getText());
            Client client = new Client(temp, new User(username.getText()));
            new Thread(client).start();

            for (int i = 0; i <= 100; i += 20) {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1250));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateProgress(i);
            }

            try {
                ClientMain.currentStage.close();
                new ClientMain().showMainWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateProgress(int val) {
//        Platform.runLater(() -> progress.setProgress(val));
//        Platform.runLater(() -> username.setText("REFRESH AT VALUE " + val));
    }
}