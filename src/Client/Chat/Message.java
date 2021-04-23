package Client.Chat;

import Client.Client;

public class Message {

    private Client client;
    private String text;
    private String clientName = Client.user.getUsername();
    private static int messageID;

    public Message(Client client, String text) {
        this.client = client;
        this.text = text;
        messageID = getMessageID();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public static int getMessageID() {
        return ++messageID;
    }

    public static void setMessageID(int messageID) {
        Message.messageID = messageID;
    }
}
