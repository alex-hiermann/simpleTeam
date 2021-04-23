package Client.Chat;

import Client.Team;

import java.util.LinkedList;

public class Chatroom {

    private final LinkedList<Message> messages = new LinkedList<>();
    private Team team;

    public Chatroom(Team team) {
        this.team = team;
    }

    public void deleteLastMessage() {
        this.messages.removeLast();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public LinkedList<Message> getMessages() {
        return messages;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
