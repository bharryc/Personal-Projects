import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener{
    // All the JComponents used to make the login page
    private Window window = new Window("Doctor Login", 500, 170);
    private JPasswordField passwordField;
    private JTextField userField;
    private JButton loginButton;
    private Font font = new Font("Arial", Font.BOLD, 15);

    /**
     * Makes the instance of the login window.
     */
    Login() {
        window.setContentPane(getFrame());
    }

    /**
     * Makes the frame for the window.
     * @return the frame for the window.
     */
    private JComponent getFrame() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(getUsername());
        main.add(getPassword());
        main.add(getSeePassword());
        main.add(getButtonPanel());
        return main;
    }

    /**
     * Makes the username panel.
     * @return the username panel.
     */
    private JComponent getUsername() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout());
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(font);
        userField = new JTextField();
        userField.setColumns(20);
        loginPanel.add(userLabel);
        loginPanel.add(userField);
        return loginPanel;
    }

    /**
     * Makes the password panel.
     * @return the password panel.
     */
    private JComponent getPassword() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(font);
        passwordField = new JPasswordField();
        passwordField.setColumns(20);
        passwordField.setEchoChar('*');
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);
        return centerPanel;
    }

    /**
     * Makes the see password checkbox.
     * @return the panel that holds the see password checkbox.
     */
    private JComponent getSeePassword() {
        JPanel seePasswordPanel = new JPanel();
        seePasswordPanel.setLayout(new FlowLayout());
        JCheckBox seePasswordCheck = new JCheckBox("See password",false);
        seePasswordCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(seePasswordCheck.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                }else {
                    passwordField.setEchoChar('*');
                }
            }
        });
        seePasswordPanel.add(seePasswordCheck);
        return seePasswordPanel;
    }

    /**
     * Makes the button panel.
     * @return the button panel.
     */
    private JComponent getButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);
        return buttonPanel;
    }

    /**
     * Checks the credentials of the user and then either allows access or sends an error message.
     * @param e is the button being passed in.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        BookingSystem user = null;
        user = new BookingSystem();
        String username = userField.getText();
        String password = new String(passwordField.getPassword());
        if(source == loginButton){
            if(user.login(username, password)) {
                window.dispose();
                Home newHome = new Home(user);
                NewMessages newMessages = new NewMessages(user);
            }else {
                String errorMessage = "Incorrect username or password.";
                ErrorWindow errorWindow = new ErrorWindow("Login Error", errorMessage, 500, 90);
                userField.setText("");
                passwordField.setText("");
            }
        }
    }
}
