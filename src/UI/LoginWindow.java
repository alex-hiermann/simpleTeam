package UI;

import Client.ClientMain;
import Client.Client;
import Client.User;
import Utils.BasicFunctionLibrary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.Socket;


public class LoginWindow {
    public TextField email;
    public PasswordField password;
    public TextField server;
    public Button button;
    public VBox vbox;
    public ProgressBar progressBar;
    public AnchorPane pane;
    public Button register;

    @FXML
    protected void loginAction(ActionEvent actionEvent) {
//        button.setDisable(true);
        Socket temp = ClientMain.connectToServer(server.getText());
        Client client = new Client(temp, new User("tempUser05070201"));
        new Thread(client).start();
        Client.sendSTRequest("login:email='" + email.getText() + "',password='" + BasicFunctionLibrary.hashPassword(password.getText()) + "'");
//            service.start();
    }

    public void registerAction(ActionEvent actionEvent) {
        ClientMain.currentStage.close();
        try {
            new ClientMain().showRegisterWindow();
        } catch (IOException e) {
            e.printStackTrace();
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