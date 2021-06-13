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
            System.out.println(Client.user.myTeams.size());
            for (Team team : Client.user.myTeams) {
                addTeam(team);
            }
            System.out.println();
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UI/TabInput.fxml"));
            try {
                fxmlLoader.load();
                loadedPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/TabInput.fxml")));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            teamTab.setClosable(true);
            teamTab.setText(team.getName());
            teamTab.setId(Integer.toString(team.getId()));

            teamTab.setContent(loadedPane);
            teamTab.setOnSelectionChanged(event -> {
                System.out.println("team = " + team);
                TabInput tabInput = fxmlLoader.getController();
                System.out.println("tabInput.selectedTeam before = " + tabInput.selectedTeam);
                tabInput.selectedTeam = team;
                tabInput.initialize();
            });
            tabPane.getTabs().add(teamTab);
        });
    }
}