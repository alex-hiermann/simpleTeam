import Client.Client;
import Client.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.Scanner;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
        System.out.println("Initializing Simple Team");
        System.out.print("LogIn: ");
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();

        User user = new User(username);

        System.out.println();
        System.out.print("Enter a valid IP for the server: ");
        Scanner sc1 = new Scanner(System.in);
        String ip = sc1.nextLine();
        try {
            Socket s = new Socket(ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));
            System.out.println("Connection successful: " + s.toString());
            Client client = new Client(s, user);
            Thread thread = new Thread(client);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("UI/LoginWindow.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}