package Client;

import Utils.Configuration;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Task class, which contains data about the given task for a specific user in a team
 */
public class Task {

    /**
     * @param taskId Task Id
     */
    public Task(int taskId) {
        this.taskId = taskId;
    }

    /**
     * @param o Other
     * @return Whether or not the taskId equals o.taskid
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    /**
     * Set the team Id
     *
     * @param teamId unique team Id. Usually received from the server
     */
    public void setTeam_id(int teamId) {
        this.team_id = teamId;
    }

    /**
     * Setting the assigned user
     *
     * @param user User Object
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return Unique teamId
     */
    public int getTeam_id() {
        return team_id;
    }

    /**
     * @return Assigned User
     */
    public User getUser() {
        return user;
    }

    /**
     * Enum storing the available task types
     */
    public enum E_TASK_TYPE {
        TASK, REMINDER, MILESTONE
    }

    /**
     * Task identifier
     */
    private int taskId = Configuration.taskId;

    /**
     * @return Task id
     */
    public int getTaskId() {
        return taskId;
    }

    /**
     * Setting the new task id
     * @param taskId new Id
     */
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Enum storing the available task states
     */
    public enum E_TASK_STATE {
        OPEN, STARTED, FINISHED, DUE
    }

    /**
     * Enum storing the available task difficulties
     */
    public enum E_TASK_DIFFICULTY {
        EASY, MEDIUM, HARD, EXTREME
    }

    /**
     * Task name
     */
    private String name;
    /**
     * Task note (Further task instructions)
     */
    private String description;
    /**
     * When the task is due
     */
    private LocalDate till;
    /**
     * Task type
     */
    private E_TASK_TYPE type;
    /**
     * Task state
     */
    private E_TASK_STATE state = E_TASK_STATE.OPEN;
    /**
     * Task difficulty
     */
    private E_TASK_DIFFICULTY difficulty;
    /**
     * Team the task is in
     */
    private int team_id;
    /**
     * Assigned User
     */
    private User user = new User("aa@aa.at");

    /**
     * Default constructor
     *
     * @param name       Taskname
     * @param note       Further task instructions
     * @param till       Task due
     * @param type       Tasktype
     * @param difficulty Taskdifficulty
     */
    public Task(String name, String note, LocalDate till, E_TASK_TYPE type, E_TASK_DIFFICULTY difficulty) {
        this.name = name;
        this.description = note;
        this.till = till;
        this.type = type;
        this.difficulty = difficulty;
    }

    /**
     * @return Taskname
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string conform for constructing a STRequest
     *
     * @return STRequest conform string
     */
    @Override
    public String toString() {
        return "taskName=ꠦ" + name + "ꠦ" +
                ",taskDescription=ꠦ" + description + "ꠦ" +
                ",taskDue=ꠦ" + till +
                "ꠦ,taskType=ꠦ" + type +
                "ꠦ,taskState=ꠦ" + state +
                "ꠦ,taskDifficulty=ꠦ" + difficulty +
                "ꠦ,teamId=ꠦ" + team_id +
                "ꠦ,email=ꠦ" + user.getEmail() + "ꠦ";
    }

    /**
     * Set the new task name
     *
     * @param name New Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Return the task description
     */
    public String getNote() {
        return description;
    }

    /**
     * Set the new task description
     *
     * @param note New note
     */
    public void setNote(String note) {
        this.description = note;
    }


    /**
     * @return Get the task due date
     */
    public LocalDate getTill() {
        return till;
    }

    /**
     * @param till New due date
     */
    public void setTill(LocalDate till) {
        this.till = till;
    }

    /**
     * @return Get the task type
     */
    public E_TASK_TYPE getType() {
        return type;
    }

    /**
     * @param type New Type
     */
    public void setType(E_TASK_TYPE type) {
        this.type = type;
    }

    /**
     * @return task state
     */
    public E_TASK_STATE getState() {
        return state;
    }

    /**
     * @param state New state
     */
    public void setState(E_TASK_STATE state) {
        this.state = state;
    }

    /**
     * @return Task difficulty
     */
    public E_TASK_DIFFICULTY getDifficulty() {
        return difficulty;
    }

    /**
     * @param difficulty New difficulty
     */
    public void setDifficulty(E_TASK_DIFFICULTY difficulty) {
        this.difficulty = difficulty;
    }
}
