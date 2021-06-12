package Client.Chat;

import Client.Team;

import java.util.ArrayList;
import java.util.LinkedList;

public class Chatroom {

    public final LinkedList<Message> messages = new LinkedList<>();
    private Team team;

    public ArrayList<String> generateMessages() {
        ArrayList<String> generatedMessages = new ArrayList<>();
        final StringBuilder[] stringBuilder = {new StringBuilder()};
        messages.forEach(l -> {
            generatedMessages.add(stringBuilder[0].append(l.toString()).append(",teamId=ꠦ").append(team.getId()).append("ꠦ").toString());
            stringBuilder[0] = new StringBuilder();
        });
        return generatedMessages;
    }

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
