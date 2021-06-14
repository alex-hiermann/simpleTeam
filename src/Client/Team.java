package Client;

import Client.Chat.Chatroom;

import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Class for a Team where users and tasks can be assigned to
 * <p>
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class Team {

    /**
     * Name of the Team
     */
    String name;

    /**
     * Description of the Team
     */
    String description;

    /**
     * Admin of the Team
     */
    private User admin = new User("tempUser");

    /**
     * Unique ID of the Team
     */
    private int id = 0;

    /**
     * Chatroom of the Team
     */
    private Chatroom chatroom = new Chatroom(this);

    /**
     * Members assigned to the Team
     */
    public LinkedHashSet<User> members = new LinkedHashSet<>();

    /**
     * Tasks assigned to the Team
     */
    public LinkedList<Task> tasks = new LinkedList<>();

    /**
     * constructor used to create a team with a name and description
     *
     * @param name        name of the team
     * @param description description of the team
     */
    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * constructor used to create a team with a name and description
     *
     * @param name        name of the team
     * @param description description of the team
     * @param id          unique id of the team
     */
    public Team(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    /**
     * constructor used to create a team with a name and description
     *
     * @param name        name of the team
     * @param description description of the team
     * @param admin       admin of the team
     */
    public Team(String name, String description, User admin) {
        this.name = name;
        this.description = description;
        this.admin = admin;
    }

    /**
     * constructor used to create a team with a name and description
     *
     * @param name        name of the team
     * @param description description of the team
     * @param id          unique id of the team
     * @param admin       admin of the team
     */
    public Team(String name, String description, int id, User admin) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.admin = admin;
    }

    /**
     * sets the id
     *
     * @param id unique id of the team
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * @return unique id of the team
     */
    public int getId() {
        return id;
    }

    /**
     * @param o Other
     * @return Whether or not the id equals o.id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id;
    }


    /**
     * Only for indexing
     *
     * @param id Id
     */
    public Team(int id) {
        this.id = id;
    }

    /**
     * Returns a string conform for constructing a STRequest
     *
     * @return STRequest conform string
     */
    @Override
    public String toString() {
        return "teamname=ꠦ" + name + "ꠦ,teamdesc=ꠦ" + description + "ꠦ,teamId=ꠦ" + id + "ꠦ,adminEmail=ꠦ" + admin.getEmail() + "ꠦ";
    }

    /**
     * sets admin of the team
     *
     * @param newAdmin admin of the team
     */
    public void setAdmin(User newAdmin) {
        this.admin = newAdmin;
    }

    /**
     * @return name of the team
     */
    public String getName() {
        return name;
    }

    /**
     * @return description of the team
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the name
     *
     * @param name name of the team
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the description
     *
     * @param description description of the team
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return admin of the team
     */
    public User getAdmin() {
        return admin;
    }

    /**
     * @return chatroom of the team
     */
    public Chatroom getChatroom() {
        return chatroom;
    }

    /**
     * sets the chatroom
     *
     * @param chatroom chatroom of the team
     */
    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }
}
