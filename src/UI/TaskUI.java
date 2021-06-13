package UI;

import Client.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Objects;

public class TaskUI {
    public ImageView prioImg;
    public TitledPane type;
    public CheckBox taskButton;
    public Text prio;
    public Task task;
    public RadioButton started;
    public RadioButton finished;
    public Text dueDate;

    public void initialize() {
        type.setText(task.getType().toString() + ": " + task.getName());
        taskButton.setText(task.getNote());
        prio.setText(task.getDifficulty().toString());
        dueDate.setText("Due to: " + task.getTill().toString());

        started.setOnAction(actionEvent -> {
            task.setState(Task.E_TASK_STATE.STARTED);
            finished.setDisable(!finished.isDisabled());
        });
        finished.setOnAction(actionEvent -> {
            task.setState(Task.E_TASK_STATE.FINISHED);
            started.setDisable(!started.isDisabled());
        });

        switch (task.getDifficulty().toString().toUpperCase()) {
            case "EASY" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/easy.png"))));
            case "MEDIUM" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/medium.png"))));
            case "HARD" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/hard.png"))));
            case "EXTREME" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/extreme.png"))));
        }
    }

    public TaskUI(Task task) {
        this.task = task;
    }
}
