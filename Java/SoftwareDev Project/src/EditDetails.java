import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditDetails extends JFrame {

    private Window window;
    private Font font = new Font("Arial", Font.BOLD, 15);
    private JTextField prescriptionTextField;
    private JTextField visitTextField;
    private String bID;
    private BookingSystem doctor;
    private Home home;

    /**
     * Makes an instance of a window that will allow the user to edit the details of a booking.
     * @param user is the user currently logged in to the system.
     */
    EditDetails(Home home, BookingSystem user, String bid, String prescription, String visitDetails) {
        bID = bid;
        doctor = user;
        this.home = home;
        window = new Window("Edit Booking", 500, 150);
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(getPrescription(prescription));
        main.add(getVisitDetails(visitDetails));
        main.add(getButtons());
        window.setContentPane(main);
    }

    /**
     * Makes the panel that will hold the prescription textfield.
     * @return the prescription panel.
     */
    private JComponent getPrescription(String prescription) {
        JPanel prescriptionPanel = new JPanel(new FlowLayout());
        JLabel prescriptionLabel = new JLabel("Prescription: ");
        prescriptionLabel.setFont(font);
        prescriptionTextField = new JTextField();
        prescriptionTextField.setColumns(20);
        prescriptionTextField.setText(prescription);
        prescriptionPanel.add(prescriptionLabel);
        prescriptionPanel.add(prescriptionTextField);
        return prescriptionPanel;
    }

    /**
     * Makes the panel that will hold the visit details textfield.
     * @return the visit panel.
     */
    private JComponent getVisitDetails(String visitDetails) {
        JPanel visitPanel = new JPanel(new FlowLayout());
        JLabel visitLabel = new JLabel("Visit Details: ");
        visitLabel.setFont(font);
        visitTextField = new JTextField();
        visitTextField.setColumns(20);
        visitTextField.setText(visitDetails);
        visitPanel.add(visitLabel);
        visitPanel.add(visitTextField);
        return visitPanel;
    }

    /**
     * Makes the panel that will hold all the buttons for the window.
     * @return the button panel.
     */
    private JComponent getButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton applyButton = new JButton("Apply");
        // When pressed the information on the booking will be changed to what they entered into the text field.
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String presc = prescriptionTextField.getText();
                String visit = visitTextField.getText();
                if(presc.equals("") || visit.equals("")) {
                    String errorMessage = "You must fill in prescription and visit details to apply changes.";
                    ErrorWindow errorWindow = new ErrorWindow("Error", errorMessage, 500, 90);
                }else {
                    String message = doctor.enterDetails(visit, presc, bID);
                    home.updateWindow();
                    NewMessages newMessages = new NewMessages(doctor);
                    ConfirmationMessage confirmationMessage = new ConfirmationMessage(message);
                    window.dispose();
                }
            }
        });
        JButton cancelButton = new JButton("Cancel");
        // When pressed the window will be closed and all content will be removed.
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
            }
        });
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }
}
