package Client.Chat;

import Client.Client;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private Client client;
    private String text;
    private String clientName = Client.user.getUsername();
    private static int messageID;
    private Date date;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public Message(Client client, String text) {
        this.client = client;
        this.text = text;
        messageID = getMessageID();
        date = new Date();
    }

    @Override
    public String toString() {
        return "\"" + text + "\", written by " + Client.user.getUsername() + " on " + getFormattedDate();
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

    public String getFormattedDate() {
        return dateFormat.format(getDate());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
}
