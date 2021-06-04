package Client;

import java.time.LocalDate;
import java.util.Date;

public class Task {

    public enum E_TASK_TYPE {
        TASK, REMINDER, MILESTONE
    }

    public enum E_TASK_STATE {
        OPEN, STARTED, FINISHED, DUE
    }

    public enum E_TASK_DIFFICULTY {
        EASY, MEDIUM, HARD, EXTREME
    }

    private String name;
    private String description;
    private LocalDate till;
    private E_TASK_TYPE type;
    private E_TASK_STATE state = E_TASK_STATE.OPEN;
    private E_TASK_DIFFICULTY difficulty;

    @Override
    public String toString() {
        return  "taskName=ꠦ" + name + "ꠦ" +
                ",taskDescription='" + description + 'ꠦ' +
                ",TaskDue=ꠦ" + till +
                "ꠦ,TaskType=ꠦ" + type +
                "ꠦ,taskState=ꠦ" + state +
                "ꠦ,taskDifficulty=" + difficulty +
                'ꠦ';
    }

    public Task(String name, String note, LocalDate till, E_TASK_TYPE type, E_TASK_DIFFICULTY difficulty) {
        this.name = name;
        this.description = note;
        this.till = till;
        this.type = type;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return description;
    }

    public void setNote(String note) {
        this.description = note;
    }

    public LocalDate getTill() {
        return till;
    }

    public void setTill(LocalDate till) {
        this.till = till;
    }

    public E_TASK_TYPE getType() {
        return type;
    }

    public void setType(E_TASK_TYPE type) {
        this.type = type;
    }

    public E_TASK_STATE getState() {
        return state;
    }

    public void setState(E_TASK_STATE state) {
        this.state = state;
    }

    public E_TASK_DIFFICULTY getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(E_TASK_DIFFICULTY difficulty) {
        this.difficulty = difficulty;
    }
}
