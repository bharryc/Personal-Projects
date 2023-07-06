import javax.swing.*;
import java.awt.*;

public class BookingGui extends JPanel{

    private Font font = new Font("Arial", Font.BOLD, 15);
    JLabel bookingIDLabel;

    /**
     * Makes an instance of a booking for the user.
     * @param patientName is the name of the patient.
     * @param doctorName is the name of the doctor.
     * @param prescription is the prescription of the patient.
     * @param visitDetails is the details of the visit of the patient.
     * @param date is the date of the booking.
     */
    BookingGui(String patientName, String doctorName, String prescription, String visitDetails, String date, String bid) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        bookingIDLabel = new JLabel(bid);
        this.add(getName(patientName));
        this.add(getDoctor(doctorName));
        this.add(getPrescription(prescription));
        this.add(getVisit(visitDetails));
        this.add(getDate(date));
    }

    /**
     * Makes the name panel for the booking page.
     * @param patientName is the name of the patient.
     * @return the name panel.
     */
    private JComponent getName(String patientName) {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel("Patient name: ");
        nameLabel.setFont(font);
        JLabel patientLabel = new JLabel(patientName);
        namePanel.add(nameLabel);
        namePanel.add(patientLabel);
        return namePanel;
    }

    /**
     * Makes the doctor panel for the booking page.
     * @param doctorName is the name of the doctor.
     * @return the doctor panel.
     */
    private JComponent getDoctor(String doctorName) {
        JPanel doctorPanel = new JPanel();
        doctorPanel.setBackground(Color.WHITE);
        doctorPanel.setLayout(new FlowLayout());
        JLabel nameLabel = new JLabel("Doctor: ");
        nameLabel.setFont(font);
        JLabel doctorLabel = new JLabel(doctorName);
        doctorPanel.add(nameLabel);
        doctorPanel.add(doctorLabel);
        return doctorPanel;
    }

    /**
     * Makes the prescription panel for the booking.
     * @param prescription is the prescription for the patient.
     * @return the prescription panel.
     */
    private JComponent getPrescription(String prescription) {
        JPanel prescPanel = new JPanel();
        prescPanel.setBackground(Color.WHITE);
        prescPanel.setLayout(new FlowLayout());
        JLabel prescLabel = new JLabel("Prescription: ");
        prescLabel.setFont(font);
        JLabel detailLabel = new JLabel(prescription);
        prescPanel.add(prescLabel);
        prescPanel.add(detailLabel);
        return prescPanel;
    }

    /**
     * Makes the visit details panel for the booking.
     * @param visitDetails is the reason for visit.
     * @return the visit panel.
     */
    private JComponent getVisit(String visitDetails) {
        JPanel visitPanel = new JPanel();
        visitPanel.setBackground(Color.WHITE);
        visitPanel.setLayout(new FlowLayout());
        JLabel visitLabel = new JLabel("Visit details: ");
        visitLabel.setFont(font);
        JLabel detailLabel = new JLabel(visitDetails);
        visitPanel.add(visitLabel);
        visitPanel.add(detailLabel);
        return visitPanel;
    }

    /**
     * Makes the date panel for the booking.
     * @param date is the date of the booking.
     * @return the date panel.
     */
    private JComponent getDate(String date) {
        JPanel datePanel = new JPanel();
        datePanel.setBackground(Color.WHITE);
        datePanel.setLayout(new FlowLayout());
        JLabel dateLabel = new JLabel("Date: ");
        dateLabel.setFont(font);
        JLabel detailLabel = new JLabel(date);
        datePanel.add(dateLabel);
        datePanel.add(detailLabel);
        return datePanel;
    }

    /**
     * Getter for the booking ID.
     * @return the booking ID.
     */
    public String getBookingID() {
        return bookingIDLabel.getText();
    }
}
