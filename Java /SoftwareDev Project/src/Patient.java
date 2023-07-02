import java.util.Date;

/**
 * Represents a patient and their information in the system.
 */
public class Patient {
    String patientID, doctorName, name, phoneNumber;
    Date dob, last_booking;

    /**
     * Getter for patient's ID
     * @return ID of patient
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Setter for patient's ID
     * @param patientID to set
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * Getter for the name of the patient's doctor
     * @return the name of the patient's doctor
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * Setter for the name of the patient's doctor
     * @param doctorName name of the patient's doctor to set
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    /**
     * Getter for the patient's name
     * @return name of the patient
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the patient's name
     * @param name of the patient to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the patient's phone number
     * @return the patient's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Setter for the patient's phone number
     * @param phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Getter for the patient's date of birth
     * @return the patient's date of birth
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Setter for the patient's date of birth
     * @param dob date of birth to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Getter for the patient's date of last booking
     * @return date of last booking
     */
    public Date getLast_booking() {
        return last_booking;
    }

    /**
     * Setter for the patient's date of last booking
     * @param last_booking date to set
     */
    public void setLast_booking(Date last_booking) {
        this.last_booking = last_booking;
    }
}
