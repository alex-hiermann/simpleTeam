package UI;

import Client.Task;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import Client.Client;
import java.util.Objects;

public class TaskUI {
    public ImageView prioImg;
    public TitledPane type;
    public Text prio;
    public Task task;
    public RadioButton started;
    public RadioButton finished;
    public Text dueDate;
    public Text taskNote;

    public void initialize() {

        if (task.getState().toString().equalsIgnoreCase("started")) {
            started.setSelected(true);
            finished.setDisable(true);
            started.setDisable(false);
        }

        if (task.getState().toString().equalsIgnoreCase("finished")) {
            finished.setSelected(true);
            finished.setDisable(false);
            started.setDisable(true);
        }

        updateValues();

        switch (task.getDifficulty().toString().toUpperCase()) {
            case "EASY" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/easy.png"))));
            case "MEDIUM" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/medium.png"))));
            case "HARD" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/hard.png"))));
            case "EXTREME" -> prioImg.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/difficulty/extreme.png"))));
        }
    }

    private void updateValues() {
        type.setText(task.getType().toString() + ": " + task.getName() + "     -     " + task.getState());
        taskNote.setText(task.getNote());
        prio.setText(task.getDifficulty().toString());
        dueDate.setText("Due to: " + task.getTill().toString());

        if (!task.getUser().equals(Client.user)) {
            started.setDisable(true);
            finished.setDisable(true);
        }

        started.setOnAction(actionEvent -> {
            task.setState(Task.E_TASK_STATE.STARTED);
            finished.setDisable(!finished.isDisabled());
            Client.sendSTRequest("changeTaskState:newTaskState=ꠦSTARTEDꠦ,taskId=ꠦ" + task.getTaskId() + "ꠦ,teamId=ꠦ" + task.getTeam_id() + "ꠦ");
            updateValues();
        });

        finished.setOnAction(actionEvent -> {
            task.setState(Task.E_TASK_STATE.FINISHED);
            started.setDisable(!started.isDisabled());
            Client.sendSTRequest("changeTaskState:newTaskState=ꠦFINISHEDꠦ,taskId=ꠦ" + task.getTaskId() + "ꠦ,teamId=ꠦ" + task.getTeam_id() + "ꠦ");
            updateValues();
        });
    }

    public TaskUI(Task task) {
        this.task = task;
    }
}
