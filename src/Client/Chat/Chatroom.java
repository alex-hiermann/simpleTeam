package Client.Chat;

import Client.Team;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Chatroom class, used to identify the Chatroom, used in the database and UI for each Team
 *
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class Chatroom {

    /**
     * List of messages used to store all messages, which have been sent in a chatroom
     */
    public final LinkedList<Message> messages = new LinkedList<>();

    /**
     * Saves the Team, where this chatroom belongs to
     */
    private Team team;

    /**
     * used to get all messages for a STRequest with specific output
     * @return formatted list of messages for STRequests
     */
    public ArrayList<String> generateMessages() {
        ArrayList<String> generatedMessages = new ArrayList<>();
        final StringBuilder[] stringBuilder = {new StringBuilder()};
        messages.forEach(l -> {
            generatedMessages.add(stringBuilder[0].append(l.toString()).append(",teamId=ꠦ").append(team.getId()).append("ꠦ").toString());
            stringBuilder[0] = new StringBuilder();
        });
        return generatedMessages;
    }

    /**
     * constructor used to initialize a new chatroom with a team
     * @param team reference to where the chatroom belongs to
     */
    public Chatroom(Team team) {
        this.team = team;
    }

    /**
     * deletes the last written message
     */
    public void deleteLastMessage() {
        this.messages.removeLast();
    }

    /**
     * adds a new message to the message list
     * @param message message that will be added
     */
    public void addMessage(Message message) {
        this.messages.add(message);
    }

    /**
     * @return list of messages
     */
    public LinkedList<Message> getMessages() {
        return messages;
    }

    /**
     * @return team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * set the team, where this chatroom is paired with
     * @param team team to be changed
     */
    public void setTeam(Team team) {
        this.team = team;
    }
}
