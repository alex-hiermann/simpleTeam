package Client;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Application implements Runnable {

    Socket socket;
    User user;

    public Client(Socket socket, User user) {
        this.socket = socket;
        this.user = user;

    }

    @Override
    public void run() {
        System.out.println("Hello I'm " + user.getUsername());
        launch();
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String data = "";
            while (!(data = (String) dis.readUTF()).isEmpty()) {    //Until data is not empty

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root2 = FXMLLoader.load(getClass().getResource("../UI/MainWindow.fxml"));
        Stage stage2 = new Stage();
        stage2.getIcons().add(new Image(this.getClass().getResourceAsStream("/icon-1.png")));
        stage2.setTitle("SimpleTeam");
        stage2.setScene(new Scene(root2));
        stage2.show();
    }
}
