package Client.Chat;

import Client.User;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for Messages, which are sent in each Chatroom of the Teams
 *
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class Message {

    /**
     * Defines the user, who has sent this message
     */
    private User user;

    /**
     * Text of the message
     */
    private String text;

    /**
     * Date when the message has been sent
     */
    private Date date;

    /**
     * Format used to convert date formats
     */
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * constructor to create a message with text and a date
     * @param text text of the message
     * @param date date when message was created
     */
    public Message(String text, Date date) {
        this.text = text;
        this.date = date;
    }

    /**
     * constructor to create a message with user, text and a date
     * @param user user who created the message
     * @param text text of the message
     * @param date date when message was created
     */
    public Message(User user, String text, Date date) {
        this.user = user;
        this.text = text;
        this.date = date;
    }

    @Override
    public String toString() {
        return "messageText=ꠦ" + getText() + "ꠦ,date=ꠦ" + getFormattedDate() + "ꠦ,email=ꠦ" + user.getEmail() + "ꠦ";
    }

    /**
     * @return user for the message
     */
    public User getUser() {
        return user;
    }

    /**
     * set the user
     * @param user for the message
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return text of the message
     */
    public String getText() {
        return text;
    }

    /**
     * set the text
     * @param text text of the message
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the format date of the messages date
     */
    public String getFormattedDate() {
        return dateFormat.format(getDate());
    }

    /**
     * @return date of the message
     */
    public Date getDate() {
        return date;
    }

    /**
     * set the date
     * @param date of the message
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the format used to convert the dates
     */
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * sets the format
     * @param dateFormat format used to convert the dates
     */
    public void setDateFormat(SimpleDateFormat dateFormat) {
        Message.dateFormat = dateFormat;
    }
}
