import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReassignWindow extends JFrame {

    private Home home;
    private Window window = new Window("Reassign Window", 400, 300);
    private List<String> doctors;

    /**
     * Makes the window that the user can use to reassign a patient that is selected to a new doctor.
     * @param user is the user currently logged into the system.
     * @param pid is the patient id.
     */
    ReassignWindow(Home home, BookingSystem user, String pid) {
        this.home = home;
        window.setContentPane(getFrame(user, pid));
    }

    /**
     * Makes the frame for the reassign window.
     * @param user is the user currently logged into the system.
     * @param pid is the patient id.
     * @return the frame for the reassign window.
     */
    private JComponent getFrame(BookingSystem user, String pid) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(getDoctors(user, pid));
        mainPanel.add(getButton());
        return mainPanel;
    }

    /**
     * Makes the doctors panel that will list all the doctors to the user.
     * @param user is the user currently logged into the system.
     * @param pid is the patient id.
     * @return the doctor panel.
     */
    private JComponent getDoctors(BookingSystem user, String pid) {
        JPanel doctorPanel = new JPanel();
        doctorPanel.setLayout(new BoxLayout(doctorPanel, BoxLayout.Y_AXIS));
        doctors = user.listAssign();
        for (String doctor : doctors) {
            System.out.println(doctor);
        }
        for(String doctor : doctors) {
            JPanel docPanel = new JPanel(new FlowLayout());
            JLabel nameLabel = new JLabel(doctor);
            JLabel spaceLabel = new JLabel("       ");
            JButton assignButton = new JButton("Assign");
            assignButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message = user.assignPatients(doctor, pid);
                    home.updateWindow();
                    ConfirmationMessage patientMessage = new ConfirmationMessage(message);
                    NewMessages newMessages = new NewMessages(user);
                    window.dispose();
                }
            });
            docPanel.add(nameLabel);
            docPanel.add(spaceLabel);
            docPanel.add(assignButton);
            doctorPanel.add(docPanel);
        }
        JScrollPane contentPane = new JScrollPane(doctorPanel);
        contentPane.setBounds(50,30,300,350);
        contentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return contentPane;
    }

    /**
     * Makes the button panel that will allow the user to close the window.
     * @return the button panel.
     */
    private JComponent getButton() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
            }
        });
        buttonPanel.add(closeButton);
        return buttonPanel;
    }
}
