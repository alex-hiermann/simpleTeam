package UI;

import Client.Client;
import Client.ClientMain;
import Client.Team;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Class for the main window which is used to display most of the FXML
 * <p>
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class MainWindow {

    /**
     * Button for the addTeamButton
     */
    public static Button addTeamButton;

    /**
     * MenuBar for the menubar
     */
    public MenuBar menubar;

    /**
     * Team for the selectedTeam
     */
    public Team selectedTeam;

    /**
     * Menu for the profileItem
     */
    public Menu profileItem;

    /**
     * Button for the refreshButton
     */
    public Button refreshButton;

    /**
     * TabPane for the tabPane
     */
    public TabPane tabPane;

    /**
     * Tab for the homeTab
     */
    public Tab homeTab;

    /**
     * TabInput for the controller
     */
    public TabInput controller;

    /**
     * initialize method called to create a textfield-keylistener
     */
    public void initialize() {
        Platform.runLater(() -> tabPane.getTabs().removeAll(tabPane.getTabs().stream().filter(tab -> tab != homeTab)
                .collect(Collectors.toList())));
        if (Client.user.myTeams.size() > 0) {
            selectedTeam = Client.user.myTeams.getFirst();
            for (Team team : Client.user.myTeams) {

                addTeam(team);
            }
        }
    }

    /**
     * refreshes the main window
     */
    public void refresh() {
    }

    /**
     * shows the add team window
     */
    @FXML
    protected void createTeamDialog() {
        try {
            new ClientMain().showAddTeamWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates a new team in the main window
     *
     * @param team new team to create
     */
    @FXML
    public void addTeam(Team team) {
        Platform.runLater(() -> {
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
                    selectedTeam = team;
                    controller = fxmlLoader.getController();
                }
            });
            tabPane.getTabs().add(teamTab);
        });
    }
}