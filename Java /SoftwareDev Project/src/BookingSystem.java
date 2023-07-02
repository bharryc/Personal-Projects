import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class BookingSystem {
    private static final String dbURL = "jdbc:mysql://dragon.kent.ac.uk/na499";
    private static final String dbUser = "na499";
    private static final String dbPassword = "suis4gu";
    private User user;

    /**
     * logs user into the booking system.
     * Returns true if the username and password are correct.
     * Sets the user if correct.
     *
     * @param username of user logging in.
     * @param password of user logging in.
     * @return true if correct credentials false if not
     */
    public boolean login(String username, String password) {
        if (areCorrectCredentials(username, password)) {
            user = new User(username);
            logAction(user.getUsername(), "Doctor Login");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes all information stored about a logged in user.
     */
    public void logout() {
        if (isLoggedIn()) {
            logAction(user.getUsername(), "Doctor Logout");
            user = null;
        }
    }

    /**
     * Checks if a user is logged in.
     *
     * @return true if a user is logged in. False if not.
     */
    public boolean isLoggedIn() {
        return user != null;
    }

    /**
     * Checks the database to check if there is a user with the given password and username.
     *
     * @param username of the user.
     * @param password pf the user.
     * @return True if the credentials are correct. False if not.
     */
    private boolean areCorrectCredentials(String username, String password) {
        password = Hash.getHash(password);
        try {
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            Statement statement = connection.createStatement();
            // Gets number of Doctors with the username and password. 1 Should mean they are correct
            String query = "SELECT COUNT(username) " +
                    "FROM Doctors " +
                    "WHERE password = '" + password + "' AND username = '" + username + "';";
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            int nMatches = rs.getInt(1);
            connection.close();
            return nMatches > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all the users new messages as a List of Message
     * Returns null if not logged in.
     *
     * @return List of messages
     */
    public LinkedList<Message> getNewMessages() {


        if (isLoggedIn()) {
            try {
                Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
                Statement statement = connection.createStatement();
                // Gets all messages which were sent after the doctor last accessed the system
                String query = "SELECT m.doctor_name, m.message, m.time_sent " +
                        "FROM Messages m, Doctors d " +
                        "WHERE " +
                        "m.doctor_name = " + "'" + user.getUsername() + "' " +
                        "AND m.doctor_name = d.username " +
                        "AND m.time_sent > d.last_accessed;";
                ResultSet rs = statement.executeQuery(query);
                // Creates a Message object for each message tuple
                LinkedList<Message> messages = new LinkedList<>();
                while (rs.next()) {
                    Message message = new Message();
                    message.setUsername(rs.getString(1));
                    message.setContent(rs.getString(2));
                    Timestamp time_sent = rs.getTimestamp(3);
                    message.setDate(new Date(time_sent.getTime()));
                    messages.add(message);
                }
                connection.close();
                update_last_accessed();
                logAction(user.getUsername(), "Messages Retrieved");
                return messages;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Updates the last_accessed attribute in the database for the currently logged in user
     */
    private void update_last_accessed() {
        try {
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            String accessed = new Timestamp(System.currentTimeMillis()).toString();
            Statement statement = connection.createStatement();
            String query = "UPDATE Doctors " +
                           "SET last_accessed = '" + accessed + "' " +
                           "WHERE username = '" + user.getUsername() + "';";
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates that the number of the month is within range 1-12 inclusive
     *
     * @param month the number of the month to validate
     * @return true if valid false if not
     */
    public boolean validateMonth(int month) {
        return (month > 0 && month < 13);
    }

    /**
     * Validates that the year given is withing the range supported by SQL Date of 1000 to 9999 inclusive.
     *
     * @param year the year number to validate
     * @return true if valid false if not
     */
    public boolean validateYearWithinSQLDateRange(int year) {
        return (year > 999 && year < 10000);
    }

    /**
     * Validates that the year given year and month is withing the range supported by SQL Date.
     *
     * @param year  the year number to validate
     * @param month the month number to validate
     * @return true if the given month and year is valid, false if not
     */
    public boolean validateYearMonthWithinSQLDateRange(int year, int month) {
        return (validateMonth(month) && validateYearWithinSQLDateRange(year));
    }

    /**
     * Determines if the year given is a leap year
     *
     * @param year to check if it is a leap year
     * @return true if a leap year false if not
     */
    private boolean isLeapYear(int year) {
        // A leap year must be evenly divisible by 4
        if (year % 4 == 0) {
            // It may not be a leap year if divisible by 100
            if (year % 100 == 0) {
                // If it is divisible by 100 and 400 it is a leap year. If just divisible by 100 it is not a leap year
                return year % 400 == 0;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Gives the last numerical day of the month.
     * Requires a valid numerical month (1-12)
     *
     * @param month to find the last day of
     * @param year  the month is in
     * @return last day of the month
     */
    private int getLastDayOfMonth(int month, int year) {
        switch (month) {
            case 2:
                // February has 29 days if a leap year otherwise has 28
                if (isLeapYear(year)) {
                    return 29;
                } else {
                    return 28;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }


    /**
     * Returns all bookings the logged in doctor has within the month and year given
     *
     * @param month to find bookings taking place in
     * @param year  of the month
     * @return list of all booking taking place within the month. null if invalid (month or year given or not logged in)
     */
    public List<Booking> getBookings(int month, int year) {
        if (isLoggedIn()) {
            if (!validateYearMonthWithinSQLDateRange(year, month)) {
                return null;
            } else {
                try {
                    // Get YYYY-MM-DD format for the start of the month and end of the month
                    String stringYear = Integer.toString(year);
                    String stringMonth = Integer.toString(month);
                    // Add leading 0 to month if it is a single digit
                    if (stringMonth.length() == 1) {
                        stringMonth = "0" + stringMonth;
                    }
                    String startRange = stringYear + "-" + stringMonth + "-" + "00";
                    String endRange = stringYear + "-" + stringMonth + "-" +
                            getLastDayOfMonth(month, year);
                    // Query database for booking tuples within the month and year
                    Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
                    Statement statement = connection.createStatement();
                    String query =
                            "SELECT p.name, p.doctor_name, b.prescription, b.visit_details, b.booking_date, " +
                                    "b.booking_ID " +
                                    "FROM Patient p " +
                                    "INNER JOIN Bookings b ON b.patient_ID = p.patient_ID " +
                                    "WHERE " +
                                    "p.doctor_name = '" + user.getUsername() + "' " +
                                    "AND b.booking_date >= '" + startRange + "' " +
                                    "AND b.booking_date <= '" + endRange + "'";
                    ResultSet rs = statement.executeQuery(query);
                    // Creates a booking object for each booking tuple returned
                    List<Booking> bookings = new LinkedList<>();
                    while (rs.next()) {
                        Booking booking = new Booking();
                        booking.setPatientName(rs.getString(1));
                        booking.setDoctorName(rs.getString(2));
                        booking.setPrescription(rs.getString(3));
                        booking.setVisitDetails(rs.getString(4));
                        booking.setDate(rs.getDate(5));
                        booking.setBooking_ID(rs.getString(6));
                        bookings.add(booking);
                    }
                    connection.close();
                    logAction(user.getUsername(), "Bookings Retrieved");
                    return bookings;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Gets all the patients assigned to the logged in doctor
     *
     * @return List of all the patients assigned to logged in doctor. Null if not logged in or can't connect to database
     */
    public List<Patient> getOwnPatients() {
        if (isLoggedIn()) {
            try {
                // Query database for every patient who's doctor is the one logged in
                Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
                Statement statement = connection.createStatement();
                String query =
                        "SELECT * " +
                                "FROM Patient " +
                                "WHERE doctor_name = '" + user.getUsername() + "';";
                ResultSet rs = statement.executeQuery(query);
                // Create a Patient object for every tuple returned
                List<Patient> patients = new LinkedList<>();
                while (rs.next()) {
                    Patient patient = new Patient();
                    patient.setPatientID(rs.getString(1));
                    patient.setDoctorName(rs.getString(2));
                    patient.setName(rs.getString(3));
                    patient.setPhoneNumber(rs.getString(4));
                    patient.setDob(rs.getDate(5));
                    patient.setLast_booking(rs.getDate(6));
                    patients.add(patient);
                }
                connection.close();
                logAction(user.getUsername(), "Patients Retrieved");
                return patients;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Edit visit details and prescription information for a past booking.
     * Sends confirmation message to the patient and the doctor.
     */
    public String enterDetails(String visit_details, String prescription, String booking_ID) {
        if (isLoggedIn()) {
            try {
                String currentTime = formatCurrentTime();

                Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
                Statement statement = connection.createStatement();

                //Query the database to update the prescription and visit fields in the Bookings table.
                String query = "UPDATE Bookings " +
                        "SET prescription = '" + prescription + "', visit_details = '" + visit_details + "' " +
                        "WHERE booking_ID = '" + booking_ID + "';";
                statement.executeUpdate(query);

                //Query the database to return all from a specific booking.
                String query2 = "SELECT * FROM Bookings WHERE booking_ID = '" + booking_ID + "';";
                ResultSet rs = statement.executeQuery(query2);
                rs.next();
                //Creates strings using information from the database.
                String booking_date = rs.getString(2);
                String patient_ID = rs.getString(5);

                //Query the database to add a message into the messages table.
                String query3 = "INSERT INTO Messages (doctor_name, patient_ID, message, time_sent) " +
                        "VALUES ('" + user.getUsername() + "', '" + patient_ID +
                        "', 'Information updated for booking: " + booking_ID + ".', '" + currentTime + "');";
                statement.executeUpdate(query3);

                connection.close();
                //String containing the confirmation message for the doctor.
                String userMessage = "The information for patient: " + patient_ID + " has been updated for the " +
                        "booking which happened on: " + booking_date + ". The new reason for visit is: " +
                        visit_details + ", and the new prescription given is: " + prescription + ". If there has " +
                        "been a mistake please contact reception immediately.";

                logAction(user.getUsername(), "Details Updated");
                return userMessage;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Lists the doctors in the assign table
     */

    public List<String> listAssign() {
        if (isLoggedIn()) {
            try {
                // Query database for every patient who's doctor is the one logged in
                Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
                Statement statement = connection.createStatement();

                //Query database
                String query = "SELECT username FROM Doctors;";
                ResultSet rs = statement.executeQuery(query);
                List<String> doctorList = new LinkedList<>();

                while (rs.next()) {
                    String doctorName = rs.getString(1);
                    doctorList.add(doctorName);

                }
                connection.close();
                return doctorList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Assigns a new Doctor to patient from list of all the doctors
     * Sends confirmation messages to both doctors and the patient
     */
    public String assignPatients(String dName, String pID) {
        if (isLoggedIn()) {
            try {
                String currentTime = formatCurrentTime();

                // Query database for every patient who's doctor is the one logged in
                Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
                Statement statement = connection.createStatement();

                //Query the database to update the doctor's name in the Assign table
                String query = "UPDATE Patient " +
                        "SET doctor_name = '" + dName + "' " +
                        "WHERE patient_ID = '" + pID + "';";
                statement.executeUpdate(query);

                //Query the database to add a message into the messages table.
                String query3 = "INSERT INTO Messages (doctor_name, patient_ID, message, time_sent) VALUES ('" +
                        user.getUsername() + "', '" + pID + "', 'Patient: " + pID + " is now assigned to Doctor: " +
                        dName + "', '" + currentTime + "');";

                String query4 = "INSERT INTO Messages (doctor_name, patient_ID, message, time_sent) VALUES ('" + dName +
                        "', '" + pID + "', 'Patient: " + pID + " is now assigned to Doctor: " +
                        dName + "', '" + currentTime + "');";

                statement.executeUpdate(query3);
                statement.executeUpdate(query4);
                connection.close();

                String addMessage = "Doctor: " + dName + " has been assigned to Patient: " + pID +
                        ". If you would like to make any changes " +
                        "please contact reception immediately.";

                logAction(user.getUsername(), "Patient assigned to Doctor");
                return addMessage;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    /**
     *
     * Logs the users access to the database.
     *
     * @param username the doctor that is logged in.
     * @param action the action that the doctor is taking.
     */
    public void logAction(String username, String action) {
        int intID = 0;
        int newId = 0;
        if( isLoggedIn()) {
            String currentTime = formatCurrentTime();
            int newID;
            try {
                Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
                Statement statement = connection.createStatement();

                //Query the database to see if there are any records.
                //If there is then get the last entered ID.
                //If there isn't then start the ID at L0.
                ResultSet rs1 = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM Logs);");
                rs1.next();
                int count = rs1.getInt(1);
                if (count == 0){
                    newId= 1;
                } else {
                    //Query the database to get the ID of the last entry, and increments it for a new entry
                    String query = "SELECT logID FROM Logs " +
                                   "ORDER BY logID DESC " +
                                   "LIMIT 1;";
                    ResultSet rs2 = statement.executeQuery(query);
                    rs2.next();
                    String currentID = rs2.getString(1);
                    intID = Integer.parseInt(currentID);
                    newId = intID + 1;
                }
                //Query the database to add a log of an action.
                String query2 = "INSERT INTO Logs VALUES ('" + newId + "', '" + username + "', '" + action + "', '" +
                        currentTime + "');" ;
                statement.executeUpdate(query2);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Formats the date and time to the correct format for the database.
     *
     * @return date and time in format yyyy-mm-dd hh:mm:ss"
     */
    public String formatCurrentTime() {
        final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        return SDF.format(timeStamp);
    }
}
