import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Runs the instance of the login screen.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Login login = new Login();
            }
        });
    }
}
