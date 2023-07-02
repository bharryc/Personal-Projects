import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

class NewMessages {

    // The window that will make the new messages window.
    Window window;
    // The user that is logged into the system currently.
    BookingSystem doctor;

    /**
     * Makes an instance of new messages window.
     * @param user is the current user logged into the system currently.
     */
    NewMessages(BookingSystem user) {
        doctor = user;
        window = new Window("New Messages", 400, 600);
        window.setContentPane(getFrame());
    }

    /**
     * Makes the frame for the new messages window.
     * @return the frame for new messages.
     */
    private JComponent getFrame() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(getTitle(), BorderLayout.NORTH);
        mainPanel.add(getMessages(), BorderLayout.CENTER);
        mainPanel.add(getClose(), BorderLayout.SOUTH);
        return mainPanel;
    }

    /**
     * Makes the title panel for the new messages window.
     * @return the title panel.
     */
    private JComponent getTitle() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        JLabel titleLabel = new JLabel("New Messages");
        Font font = new Font("Arial", Font.BOLD, 20);
        titleLabel.setFont(font);
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    /**
     * Makes the panel that will hold all the messages inside of it.
     * @return the message panel.
     */
    private JComponent getMessages() {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        List<Message> messages = doctor.getNewMessages();
        if(messages.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new FlowLayout());
            JLabel empty = new JLabel("No new messages.");
            Font font = new Font("Arial", Font.BOLD, 20);
            empty.setFont(font);
            emptyPanel.add(empty);
            messagePanel.add(emptyPanel);
        }else {
            for(Message message : messages) {
                MessageGui messageGui = new MessageGui(message.getUsername(), message.getContent());
                messagePanel.add(messageGui);
            }
        }

        JScrollPane contentPane = new JScrollPane(messagePanel);
        contentPane.setBounds(50,30,300,500);
        contentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return contentPane;
    }

    /**
     * Makes the close button panel that will close the new messages window.
     * @return the close button panel.
     */
    private JComponent getClose() {
        JPanel closePanel = new JPanel();
        closePanel.setLayout(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
            }
        });
        closePanel.add(closeButton);
        return closePanel;
    }
}
