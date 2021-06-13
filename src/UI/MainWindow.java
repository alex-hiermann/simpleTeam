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

import java.io.IOException;
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
    public TabInput controller;

    public void initialize() {
        Platform.runLater(() -> tabPane.getTabs().removeAll(tabPane.getTabs().stream().filter(tab -> tab != homeTab).collect(Collectors.toList())));
        if (Client.user.myTeams.size() > 0) {
            selectedTeam = Client.user.myTeams.getFirst();
            for (Team team : Client.user.myTeams) {
                System.out.println(team);
                team.getChatroom().getMessages().forEach(System.out::println);
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
            System.out.println(Configuration.ANSI_PURPLE + "#Adding team " + team.getName() + " [" + team.getId() + "]#" + Configuration.ANSI_RESET);
            Tab teamTab = new Tab();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UI/TabInput.fxml"));
            fxmlLoader.setControllerFactory(l -> new TabInput(team));
            try {
                teamTab.setContent(fxmlLoader.load());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            teamTab.setClosable(true);
            teamTab.setText(team.getName());
            teamTab.setId(Integer.toString(team.getId()));

            teamTab.setOnSelectionChanged(event -> {
                if (teamTab.isSelected()) {
                    controller = fxmlLoader.getController();
                }
            });
            tabPane.getTabs().add(teamTab);
        });
    }
}