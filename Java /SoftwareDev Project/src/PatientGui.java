import javax.swing.*;
import java.awt.*;

public class PatientGui extends JPanel {

    // The font used for titles in the patient panel.
    private Font font = new Font("Arial", Font.BOLD, 15);

    /**
     * Makes an instance of a patient panel which will then be shown to the user.
     * @param id is the patients id
     * @param name is the patients name
     * @param phoneNumber is the patients phone number
     */
    PatientGui(String id, String name, String phoneNumber) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(getID(id));
        this.add(getName(name));
        this.add(getPhoneNumber(phoneNumber));
    }

    /**
     * Makes the panel that will hold the id of the patient.
     * @param id is the patients id.
     * @return the id panel.
     */
    private JComponent getID(String id) {
        JPanel idPanel = new JPanel();
        idPanel.setLayout(new FlowLayout());
        JLabel idLabel = new JLabel("Patient ID: ");
        idLabel.setFont(font);
        JLabel idLabel2 = new JLabel(id);
        idPanel.add(idLabel);
        idPanel.add(idLabel2);
        return idPanel;
    }

    /**
     * Makes the panel that will hold the name of the patient.
     * @param name is the name of the patient.
     * @return the name panel.
     */
    private JComponent getName(String name) {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setFont(font);
        JLabel nameLabel2 = new JLabel(name);
        namePanel.add(nameLabel);
        namePanel.add(nameLabel2);
        return namePanel;
    }

    /**
     * Makes the panel that will hold the phone number of the patient.
     * @param number is the phone number of the patient.
     * @return the number panel.
     */
    private JComponent getPhoneNumber(String number) {
        JPanel numberPanel = new JPanel();
        numberPanel.setLayout(new FlowLayout());
        JLabel numberLabel = new JLabel("PhoneNumber: ");
        numberLabel.setFont(font);
        JLabel numberLabel2 = new JLabel(number);
        numberPanel.add(numberLabel);
        numberPanel.add(numberLabel2);
        return numberPanel;
    }
}