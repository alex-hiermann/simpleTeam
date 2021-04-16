package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.Socket;

public class ClientMain extends Application {
    public static void main(String[] args) {
        System.out.println("Initializing Simple Team");
        launch(args);

    }

    public static Socket connectToServer(String ip) {
        try {
            Socket s = new Socket(ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));
            System.out.println("Connection successful: " + s.toString());
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../UI/LoginWindow.fxml"));
        Parent root2 = FXMLLoader.load(getClass().getResource("../UI/MainWindow.fxml"));

        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/icon-1.png")));
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

        Stage stage2 = new Stage();
        stage2.getIcons().add(new Image(this.getClass().getResourceAsStream("/icon-1.png")));
        stage2.setTitle("SimpleTeam");
        stage2.setScene(new Scene(root2));
        stage2.show();
    }
}