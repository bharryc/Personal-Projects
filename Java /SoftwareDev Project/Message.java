import java.util.Date;

/**
 * Represents a message sent in the system.
 */
public class Message {
    private String username, content;
    private Date date;

    /**
     * Constructor for Message not setting fields on construction
     */
    public Message() {
    }

    /**
     * Constructor for Message setting fields on construction
     * @param username of the Doctor concerned
     * @param message contents of the message
     * @param date timestamp of when the message was sent
     */
    public Message(String username, String message, Date date) {
        this.username = username;
        this.content = message;
        this.date = date;
    }

    /**
     * Getter for username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username
     * @param username the new username of the message
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the date sent
     * @return date sent
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for the date sent
     * @param date message was sent
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for the content of the message
     * @return content of the message
     */
    public String getContent() {
        return content;
    }

    /**
     * Setter for the content of the message
     * @param content to be set
     */
    public void setContent(String content) {
        this.content = content;
    }

}
