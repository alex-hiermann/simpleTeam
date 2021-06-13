package UI;

import Client.Task;
import javafx.scene.control.CheckBox;
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

    public void initialize() {
        taskButton.setText(task.getName());
        prio.setText(task.getDifficulty().toString());

        switch (task.getDifficulty().toString().toUpperCase()) {
            case "EASY" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/easy"))));
            case "MEDIUM" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/medium"))));
            case "HARD" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/hard"))));
            case "EXTREME" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/extreme"))));
        }
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
