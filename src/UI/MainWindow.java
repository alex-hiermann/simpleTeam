package UI;

import Client.Client;
import Client.ClientMain;
import Client.Team;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainWindow {

    @FXML
    public static Button addTeamButton;

    @FXML
    public AnchorPane pane;
    public MenuBar menubar;

    public Team selectedTeam;
    public Menu profileItem;
    public Button refreshButton;
    public TabPane tabPane;
    public Tab homeTab;
    public TableView table;

    public void initialize() {
        Platform.runLater(() -> tabPane.getTabs().removeAll(tabPane.getTabs().stream().filter(tab -> tab != homeTab).collect(Collectors.toList())));
        if (Client.user.myTeams.size() > 0) {
            selectedTeam = Client.user.myTeams.getFirst();
            for (Team team : Client.user.myTeams) {
                addTeam(team);
            }
        }
    }

    public void refresh() {

    }

    @FXML
    protected void createTeamDialog(ActionEvent actionEvent) {
        try {
            new ClientMain().showAddTeamWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addTeam(Team team) {
        Platform.runLater(() -> {
            Tab teamTab = new Tab();
            Pane loadedPane = new Pane();
            try {
                loadedPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/TabInput.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
                loadedPane.getChildren().add(new Text("An error occurred while loading the Team, please try again!"));
            }
            teamTab.setClosable(true);
            teamTab.setText(team.getName());
            teamTab.setContent(loadedPane);
            teamTab.setId(Integer.toString(team.getId()));

            teamTab.setContent(loadedPane);
            tabPane.getTabs().add(teamTab);
        });
    }
}