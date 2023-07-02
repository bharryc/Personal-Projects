import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmationMessage extends JFrame {

    Window window;

    /**
     * Makes an instance of a confirmation message that is sent to the user.
     * @param message
     */
    ConfirmationMessage(String message) {
        window = new Window("Patient Confirmation Message",  500,200);
        window.setContentPane(getFrame(message));
    }

    /**
     * Makes the frame.
     * @return the frame.
     */
    private JComponent getFrame(String message) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(getMessage(message));
        mainPanel.add(getButton());
        return mainPanel;
    }

    /**
     * Makes the panel that will hold the message.
     * @return the message panel.
     */
    private JComponent getMessage(String message) {
        JPanel messagePanel = new JPanel(new FlowLayout());
        JLabel messageLabel = new JLabel(message);
        messageLabel.setText("<html><p style='width:350px'>" + message + "</p></html>");
        messagePanel.add(messageLabel);
        return messagePanel;
    }

    /**
     * Makes the panel that will hold the button.
     * @return the button panel.
     */
    private JComponent getButton() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okayButton = new JButton("Okay");
        okayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
            }
        });
        buttonPanel.add(okayButton);
        return buttonPanel;
    }
}
