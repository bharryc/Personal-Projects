import java.util.Date;

public class Booking {
    private String patientName, doctorName, prescription, visitDetails, booking_ID;
    Date date;

    /**
     * Constructor for Booking
     */
    public Booking() {
    }

    /**
     * Getter for doctor's name
     * @return the username of the doctor
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * Setter for doctors name
     * @param doctorName the new name for the doctor in the booking
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    /**
     * Getter for the prescription prescribed in the booking
     * @return the prescription prescribed in the booking
     */
    public String getPrescription() {
        return prescription;
    }

    /**
     * Setter for the prescriptions prescribed in the booking
     * @param prescription the new prescriptions prescribed in the booking
     */
    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    /**
     * Getter for the details of the visit
     * @return the details of the visit
     */
    public String getVisitDetails() {
        return visitDetails;
    }

    /**
     * Setter for the visit details
     * @param visitDetails the new visit details
     */
    public void setVisitDetails(String visitDetails) {
        this.visitDetails = visitDetails;
    }

    /**
     * Getter for the booking's date
     * @return the date of the booking
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for the date of the booking
     * @param date the new date of the booking
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for the patient's name
     * @return the patient's name
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * Setter for the patient's name
     * @param patientName the patient's new name
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    /**
     * Getter for the booking ID
     * @return booking ID
     */
    public String getBooking_ID(){return booking_ID;}


    /**
     * Setter for the booking ID
     * @param booking_ID the booking ID
     */
    public void setBooking_ID(String booking_ID) { this.booking_ID = booking_ID;}


}
