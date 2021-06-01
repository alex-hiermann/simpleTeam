package Client.Chat;

import Client.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private User user;
    private String text;
    private static int messageID;
    private Date date;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public Message(String text, Date date) {
        this.text = text;
        messageID = getMessageID();
        this.date = date;
    }

    public Message(User user, String text, Date date) {
        this.user = user;
        this.text = text;
        this.date = date;
    }

    @Override
    public String toString() {
        return "messageText=ꠦ" + getText() + "ꠦ,date=ꠦ" + getFormattedDate() + "ꠦ,email=ꠦ" + user.getEmail() + "ꠦ";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
