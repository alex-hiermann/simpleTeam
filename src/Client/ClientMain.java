package Client;

import UI.AddNewTaskWindow;
import UI.InviteUserWindow;
import UI.MainWindow;
import Utils.Configuration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

/**
 * Class to create and start all used FXML files for the graphical user interface
 * <p>
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class ClientMain extends Application {

    /**
     * Stage of the window
     */
    public static Stage currentStage = new Stage();

    /**
     * Client which uses the gui
     */
    public static Client client;

    /**
     * Defines the MainWindow to access it from other Classes
     */
    public static MainWindow mainWindow;

    /**
     * main method to start the first window
     *
     * @param args args for javafx and FXML launches
     */
    public static void main(String[] args) {
        System.out.println(Configuration.ANSI_GREEN + "Initializing Simple Team" + Configuration.ANSI_RESET);
        launch(args);
    }

    /**
     * used to connect the client to the server using the IP Address
     *
     * @param ip string of ip address
     * @return socket used to connect to the server
     */
    public static Socket connectToServer(String ip) {
        try {
            Socket s = new Socket(ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));
            System.out.println(Configuration.ANSI_GREEN + "Connection successful: " + s + Configuration.ANSI_RESET);
            return s;
        } catch (Exception ignored) {
            System.out.println(Configuration.ANSI_GREEN + "Invalid IP Address" + Configuration.ANSI_RESET);
        }
        return null;
    }

    /**
     * starts after first window has been initialized
     * login window used to login
     *
     * @param stage where your scene should be build in
     * @throws Exception exception thrown when FXML errors appear
     */
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

    /**
     * login window used to login
     *
     * @throws Exception exception thrown when FXML errors appear
     */
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

    /**
     * register window to register a new user profile
     *
     * @throws IOException exception thrown when FXML errors appear
     */
    public void showRegisterWindow() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/RegisterWindow.fxml")));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon-1.png"))));
        stage.setTitle("Register");
        stage.setScene(new Scene(root));
        stage.show();
        currentStage = stage;
    }

    /**
     * main window where most of the things are displayed
     *
     * @throws IOException exception thrown when FXML errors appear
     */
    public void showMainWindow() throws IOException {
        FXMLLoader main = new FXMLLoader(getClass().getResource("/UI/MainWindow.fxml"));
        Parent root = main.load();
        mainWindow = main.getController();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon-1.png"))));
        stage.setTitle("SimpleTeam");
        stage.setScene(new Scene(root));
        stage.show();
        currentStage = stage;
    }

    /**
     * create team window used to create a new team
     *
     * @throws IOException exception thrown when FXML errors appear
     */
    public void showAddTeamWindow() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/AddTeamWindow.fxml")));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon-1.png"))));
        stage.setTitle("Add Team");
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);
        currentStage = stage;
    }

    /**
     * invite user window used to invite a user to a team
     *
     * @param team team where the user should be invited to
     * @throws IOException exception thrown when FXML errors appear
     */
    public void showInviteUserWindow(Team team) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/InviteUserWindow.fxml")));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon-1.png"))));
        stage.setTitle("Add a user to the team");
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);
        InviteUserWindow.selectedTeam = team;
        currentStage = stage;
    }

    /**
     * new task window to create a new task
     *
     * @param team  the team where this task should be assigned to
     * @param admin if this client.user is the admin or not
     * @throws IOException exception thrown when FXML errors appear
     */
    public void showAddNewTaskWindow(Team team, boolean admin) throws IOException {
        AddNewTaskWindow.team = team;
        AddNewTaskWindow.admin = admin;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/AddNewTaskWindow.fxml")));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon-1.png"))));
        stage.setTitle("Add a new Task");
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);
        currentStage = stage;
    }
}