package Client;

import UI.InviteUserWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import UI.MainWindow;


import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class ClientMain extends Application {

    public static Stage currentStage = new Stage();

    public static Client client;

    private static FXMLLoader main;

    public static MainWindow mainWindow;

    public static void main(String[] args) {
        System.out.println("Initializing Simple Team");
        launch(args);
    }

    public static Socket connectToServer(String ip) {
        try {
            Socket s = new Socket(ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));
            System.out.println("Connection successful: " + s);
            return s;
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/LoginWindow.fxml")));
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon-1.png"))));
        stage.setTitle("Login");
        scene.getStylesheets().add(String.valueOf(getClass().getResource("/UI/style/LoginWindow.css")));
        stage.setScene(scene);
        stage.show();
        currentStage = stage;
    }

    public void showLoginWindow() throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/LoginWindow.fxml")));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon-1.png"))));
        stage.setTitle("Login");
        scene.getStylesheets().add(String.valueOf(getClass().getResource("/UI/style/LoginWindow.css")));
        stage.setScene(scene);
        stage.show();
        currentStage = stage;
    }

    public void showRegisterWindow() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/RegisterWindow.fxml")));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon-1.png"))));
        stage.setTitle("Register");
        stage.setScene(new Scene(root));
        stage.show();
        currentStage = stage;
    }

    public void showMainWindow() throws IOException {
        main = new FXMLLoader(getClass().getResource("/UI/MainWindow.fxml"));
        Parent root = main.load();
        mainWindow = main.getController();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon-1.png"))));
        stage.setTitle("SimpleTeam");
        stage.setScene(new Scene(root));
        stage.show();
        currentStage = stage;
    }

    public void showAddTeamWindow() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/AddTeamWindow.fxml")));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon-1.png"))));
        stage.setTitle("Add Team");
        stage.setScene(new Scene(root));
        stage.show();
        currentStage = stage;
    }

    public void showInviteUserWindow(Team team) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/InviteUserWindow.fxml")));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon-1.png"))));
        stage.setTitle("Add a user to the team");
        stage.setScene(new Scene(root));
        stage.show();
        InviteUserWindow.selectedTeam = team;
        currentStage = stage;
    }

}