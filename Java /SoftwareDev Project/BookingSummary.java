import javax.swing.*;
import java.awt.*;

public class BookingSummary extends JPanel {

    /**
     * Field parameters needed to access the contents of the labels.
     */
    JLabel nameLabel;
    JLabel doctorLabel;
    JLabel prescriptionLabel;
    JLabel visitDetailsLabel;
    JLabel dateLabel;
    JLabel bookingID;

    /**
     * Makes an instance of the booking summary that will be displayed to the user on the home page.
     * @param name is the patients name.
     * @param date is the date of the appointment.
     */
    BookingSummary(String name, String doctorName, String prescription, String visitDetails, String date, String bid) {
        this.setLayout(new FlowLayout());
        this.setBackground(Color.WHITE);
        nameLabel = new JLabel(name);
        JLabel spaceLabel = new JLabel("   ");
        dateLabel = new JLabel(date);
        doctorLabel = new JLabel(doctorName);
        prescriptionLabel = new JLabel(prescription);
        visitDetailsLabel = new JLabel(visitDetails);
        bookingID = new JLabel(bid);
        this.add(nameLabel);
        this.add(spaceLabel);
        this.add(dateLabel);
    }

    /**
     * Getter for the patient name.
     * @return the patient name.
     */
    public String getName() {
        return nameLabel.getText();
    }

    /**
     * Getter for the doctor name.
     * @return the doctor name.
     */
    public String getDoctor() {
        return doctorLabel.getText();
    }

    /**
     * Getter for the prescription.
     * @return the prescription.
     */
    public String getPrescription() {
        return prescriptionLabel.getText();
    }

    /**
     * Getter for the visit details.
     * @return the visit details.
     */
    public String getVisitDetails() {
        return visitDetailsLabel.getText();
    }

    /**
     * Getter for the date.
     * @return the date.
     */
    public String getDate() {
        return dateLabel.getText();
    }

    /**
     * Getter for the booking ID.
     * @return the booking ID.
     */
    public String getBookingId() {
        return bookingID.getText();
    }
}
