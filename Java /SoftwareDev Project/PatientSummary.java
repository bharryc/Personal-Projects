import javax.swing.*;
import java.awt.*;

public class PatientSummary extends JPanel {

    /**
     * Makes a patient summary to be displayed to the user on the home page.
     * @param name is the name of the patient.
     * @param pid is the patient id.
     */
    PatientSummary(String name, String pid) {
        this.setLayout(new FlowLayout());
        this.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel(name);
        JLabel spaceLabel = new JLabel("             ");
        JLabel pidLabel = new JLabel(pid);
        this.add(nameLabel);
        this.add(spaceLabel);
        this.add(pidLabel);
    }
}
