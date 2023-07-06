import javax.swing.*;
import java.awt.*;

public class MessageGui extends JPanel{

    /**
     * Makes an instance of a message made by a user.
     * @param author is the author of the message.
     * @param message is the content of the message.
     */
    MessageGui(String author, String message) {
        this.setLayout(new BorderLayout());
        this.add(getAuthor(author), BorderLayout.NORTH);
        this.add(getContent(message), BorderLayout.CENTER);
    }

    /**
     * Makes the author panel for the message window
     * @param user is the user writing the message.
     * @return the author label.
     */
    private JComponent getAuthor(String user) {
        JPanel authorPanel = new JPanel();
        authorPanel.setLayout(new FlowLayout());
        JLabel authorLabel = new JLabel(user);
        Font font = new Font("Arial", Font.BOLD, 12);
        authorLabel.setFont(font);
        authorPanel.add(authorLabel);
        return authorPanel;
    }

    /**
     * Makes the content panel for the message shown to the user.
     * @param message is the message shown to the user.
     * @return the content of the message.
     */
    private JComponent getContent(String message) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout());
        JLabel content = new JLabel(message);
        Font font = new Font("Arial", Font.PLAIN, 10);
        content.setFont(font);
        contentPanel.add(content);
        return contentPanel;
    }
}
