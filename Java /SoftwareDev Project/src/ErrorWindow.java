import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ErrorWindow extends JFrame implements ActionListener {

    // Window used to make the error message shown to the user.
    private Window window;
    private JButton closeButton;

    /**
     * Makes an instance of an error message that will be shown to the user depending on the error.
     * @param title is the title of the message.
     * @param content is the error message to give better clarification to the user.
     * @param w is the width of the window.
     * @param h is the height of the window.
     */
    ErrorWindow(String title, String content, int w, int h) {
        window = new Window(title, w, h);
        window.setContentPane(getFrame(content));
    }

    /**
     * Makes the frame for the whole error window.
     * @param message is the error message being passed to the getError function.
     * @return the frame for the error window.
     */
    private JComponent getFrame(String message) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(getError(message));
        mainPanel.add(getClose());
        return mainPanel;
    }

    /**
     * Makes the error panel for the error window which holds the error message.
     * @param content is the error message shown to the user.
     * @return the error panel.
     */
    private JComponent getError(String content) {
        JPanel errorPanel = new JPanel();
        errorPanel.setLayout(new FlowLayout());
        JLabel errorLabel = new JLabel(content);
        errorPanel.add(errorLabel);
        return errorPanel;
    }

    /**
     * Makes the close button panel that allows the user to close the window instance.
     * @return the close button panel.
     */
    private JComponent getClose() {
        JPanel closePanel = new JPanel();
        closePanel.setLayout(new FlowLayout());
        closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        closePanel.add(closeButton);
        return closePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == closeButton) {
            window.dispose();
        }
    }
}

