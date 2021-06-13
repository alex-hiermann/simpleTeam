package UI;

import Client.Client;
import Client.ClientMain;
import Client.Team;
import Utils.Configuration;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainWindow {

    @FXML
    public static Button addTeamButton;

    @FXML
    public MenuBar menubar;

    public Team selectedTeam;
    public Menu profileItem;
    public Button refreshButton;
    public TabPane tabPane;
    public Tab homeTab;

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
            System.out.print(Configuration.ANSI_PURPLE + "{Adding team " + team.getName() + " [" + team.getId() + "];}" + Configuration.ANSI_RESET);
            Tab teamTab = new Tab();
            Pane loadedPane = new Pane();
            try {
                loadedPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/TabInput.fxml")));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            teamTab.setClosable(true);
            teamTab.setText(team.getName());
            teamTab.setId(Integer.toString(team.getId()));

            teamTab.setContent(loadedPane);
            Pane finalLoadedPane = loadedPane;
            teamTab.setOnSelectionChanged(event -> {
                Text text = (Text) finalLoadedPane.lookup("#teamName");
                new TabInput().changeTeamText(text, team);

                System.out.println("finalLoadedPane.lookupAll(\"#selectedTeam\") = " + finalLoadedPane.lookupAll("#selectedTeam"));
                System.out.println("finalLoadedPane.lookupAll(\"#selectedTeam\") = " + finalLoadedPane.lookupAll("#teamName"));
            });
            tabPane.getTabs().add(teamTab);
        });
    }
}