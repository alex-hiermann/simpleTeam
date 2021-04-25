package Client;

import Client.Chat.Chatroom;

import java.util.LinkedList;

public class Team {

    String name;
    String description;
    private User admin;
    private Chatroom chatroom = new Chatroom(this);
    public LinkedList<User> members = new LinkedList<>();

    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }


    @Override
    public String toString() {
        return "teamname='" + name + "',teamdesc='" + description + "'";
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
