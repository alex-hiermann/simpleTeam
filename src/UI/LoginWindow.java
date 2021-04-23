package UI;

import Client.ClientMain;
import Client.Client;
import Client.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;


public class LoginWindow {
    public TextField username;
    public PasswordField password;
    public TextField server;
    public Button button;
    public VBox vbox;
    public ProgressBar progressBar;

    @FXML
    protected void loginAction(ActionEvent actionEvent) {
        if (username.getText().equals("Alegs") && password.getText().equals("Mags")) {
            button.setDisable(true);
            Socket temp = ClientMain.connectToServer(server.getText());
            Client client = new Client(temp, new User(username.getText()));
            new Thread(client).start();

//            service.start();

            try {
                ClientMain.currentStage.close();
                new ClientMain().showMainWindow();
                ClientMain.mainWindow.initialize();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    Service<Void> service = new Service<Void>() {
//        @Override
//        protected Task<Void> createTask() {
//            return new Task<Void>() {
//                @Override
//                protected Void call() throws Exception {
//                    final CountDownLatch countDownLatch = new CountDownLatch(1);
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                for (int i = 0; i <= 100; i += 20) {
//                                    try {
//                                        progressBar.setProgress(i);
//                                        Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1250));
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    System.out.println("Value is set to: " + i);
//                                }
//                            } finally {
//                                countDownLatch.countDown();
//                            }
//                        }
//                    });
//                    countDownLatch.await();
//                    return null;
//                }
//            };
//        }
//    };
}