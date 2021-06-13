package Client;

import Client.Chat.Chatroom;
import Utils.Configuration;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;

public class Team {

    String name;
    String description;
    private User admin = new User("tempUser");
    private int id = 0;
    private Chatroom chatroom = new Chatroom(this);
    public LinkedHashSet<User> members = new LinkedHashSet<>();
    public LinkedList<Task> tasks = new LinkedList<>();

    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Team(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public Team(String name, String description, User admin) {
        this.name = name;
        this.description = description;
        this.admin = admin;
    }

    public Team(String name, String description, int id, User admin) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.admin = admin;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

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

    @Override
    public String toString() {
        return "teamname=ꠦ" + name + "ꠦ,teamdesc=ꠦ" + description + "ꠦ,teamId=ꠦ" + id + "ꠦ,adminEmail=ꠦ" + admin.getEmail() + "ꠦ";
    }

    public void setAdmin(User newAdmin) {
        this.admin = newAdmin;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAdmin() {
        return admin;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }
}
