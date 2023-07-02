import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BookingSystem
 *
 * Tests the methods:
 * login()
 * getNewMessages()
 * viewBookings()
 * getOwnPatients()
 * enterDetails()
 * assignPatients()
 * logAction()
 */

class BookingSystemTest {

    //Database login information.
    private static final String dbURL = "jdbc:mysql://dragon.kent.ac.uk/na499";
    private static final String dbUser = "na499";
    private static final String dbPassword = "suis4gu";

    BookingSystem bookingSystem;


    //Used to update the Messages time_sent field to perform tests.
    //Without having to send a new message to the database after a 'Doctor' has logged in and checked messages.
    //Formats the time stamp into a string with the correct format for the database.
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Timestamp timeStamp = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
    String newTime = SDF.format(timeStamp);

    //Linked lists to compare to each other which hold the messages, expected and actual.
    LinkedList<String> ls = new LinkedList<>();
    LinkedList<String> messages = new LinkedList<>();

    //Linked lists to hold strings which could contain booking information or patient information to be compared against
    //each other in test cases
    LinkedList<String> expectedStrings = new LinkedList<>();
    LinkedList<String> actualStrings = new LinkedList<>();

    //Variable to hold a single message.
    String mes;

    //Strings to hold the fields retrieved from the bookings and patients.
    String patientName, doctorName, prescription, visitDetails, date, patientID, phoneNumber,
            dateOfBirth, lastBooking, actualString, expectedString, expectedMessage,
            actualUserMessage, expectedUserMessage, bookingID, logID, action;


    /**
     * Sets up the test fixture.
     * <p>
     * Called before every test case method.
     * <p>
     * Adds values into the Doctors and Messages tables.
     */
    @BeforeEach
    void setUp() {

        bookingSystem = new BookingSystem();

        try {
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            Statement statement = connection.createStatement();

            String query = "INSERT INTO Doctors (username, password, last_accessed) " +
                    "VALUES ('TES1', '7345a6cf7874654658e582514155b61a38f76691ef438fc334f619dad270d7ac', " +
                    "'2021-02-26 02:52:00'), " +
                    "('TES2', '0e4ba149da13268f4c5cfebaa68e983eeafffe571d88bfc2e45d92350a95648d', " +
                    "'2021-02-26 02:52:00'), " +
                    "('TES3', '72ab39b895cfcc32cda2212fe9b9a94335b0323adfc4f779f1f4946b5068427a', " +
                    "'2021-02-26 02:52:00'), " +
                    "('TES4', '841eaac2fdf3c7f02dbcf149ce61977fae1f8889ab500d6ef97db934b86907dc', " +
                    "'2021-02-26 02:52:00'), " +
                    "('TES5', '0040142e00868609080c9b84de2ae62a1fec7545dd32c5c2bfbba55f35d5981b', " +
                    "'2021-02-26 02:52:00'); ";

            String query2 = "INSERT INTO Messages (doctor_name, patient_ID, message, time_sent) VALUES " +
                    "('TES1', 'TP01', 'D: TES1, P: TP01', '" + newTime + "'), " +
                    "('TES1', 'TP02', 'D: TES1, P: TP02', '" + newTime + "'), " +
                    "('TES1', 'TP03', 'D: TES1, P: TP03', '" + newTime + "'), " +
                    "('TES2', 'TP02', 'D: TES2, P: TP02', '" + newTime + "'), " +
                    "('TES3', 'TP03', 'D: TES3, P: TP03', '" + newTime + "'), " +
                    "('TES4', 'TP04', 'D: TES4, P: TP04', '" + newTime + "');";

            String query3 = "INSERT INTO Bookings (booking_ID, booking_date, prescription, visit_details, patient_ID) " +
                    "VALUES ('BT01', '2021-02-26', 'Calpol', 'Cholera', 'TP01'), " +
                    "('BT02', '2021-02-26', 'Eye Drops', 'Eye Irritation', 'TP02'), " +
                    "('BT03', '2021-02-26', 'Antibiotics', 'Chlamydia', 'TP03'), " +
                    "('BT04', '2021-02-26', 'Bloodletting', 'Bad Humours', 'TP04'), " +
                    "('BT05', '2021-02-25', 'Less Bacon', 'High Cholesterol', 'TP04');";

            String query4 = "INSERT INTO Patient (patient_ID, doctor_name, name, number, dob, last_booking) VALUES " +
                    "('TP01', 'TES1', 'Tony Stark', '07123456781', '1998-01-07', '2021-02-26'), " +
                    "('TP02', 'TES2', 'Steve Rogers', '07521678291', '1928-04-04', '2021-02-26'), " +
                    "('TP03', 'TES3', 'Thor SonOfOdin', '07722324618', '1997-09-05', '2021-02-26'), " +
                    "('TP04', 'TES4', 'Nick Fury', '07715678761', '1973-03-01', '2021-02-26'), " +
                    "('TP05', 'TES1', 'Bucky Barnes', '07865467891', '1940-05-09', '2021-02-26'), " +
                    "('TP06', 'TES1', 'Sam Wilson', '07123675381', '1989-03-03', '2021-02-26')";

            //Executes the above SQL statements.
            statement.executeUpdate(query);
            statement.executeUpdate(query2);
            statement.executeUpdate(query3);
            statement.executeUpdate(query4);
            statement.executeUpdate("DELETE FROM Logs;");

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Tears down the test fixture.
     * <p>
     * Called after every test case method.
     * <p>
     * Removes all records from the Doctor and Messages table.
     * Logs out the current doctor from the system.
     * Clears the linked lists.
     */
    @AfterEach
    void tearDown() {

        //Clears any lists that have been used in tests.
        ls.clear();
        messages.clear();
        expectedStrings.clear();
        actualStrings.clear();

        try {
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            Statement statement = connection.createStatement();

            String query = "DELETE FROM Doctors WHERE username IN ('TES1', 'TES2', 'TES3', 'TES4', 'TES5');";
            String query2 = "DELETE FROM Messages WHERE doctor_name IN ('TES1', 'TES2', 'TES3', 'TES4'); ";
            String query3 = "DELETE FROM Bookings WHERE booking_ID IN ('BT01', 'BT02', 'BT03', 'BT04', 'BT05');";
            String query4 = "DELETE FROM Patient WHERE patient_ID IN ('TP01', 'TP02', 'TP03', 'TP04', 'TP05', 'TP06');";

            statement.executeUpdate(query);
            statement.executeUpdate(query2);
            statement.executeUpdate(query3);
            statement.executeUpdate(query4);
            statement.executeUpdate("DELETE FROM Logs;");
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        bookingSystem.logout();
    }

    /**
     * Test records in the database using correct credentials.
     */
    @Test
    void loginTestCorrect() {
        assertTrue(bookingSystem.login("TES1", "ComputerApple5678"));
        assertTrue(bookingSystem.login("TES2", "SpacefRuit8711"));
        assertTrue(bookingSystem.login("TES3", "BottleBoX4339"));
        assertTrue(bookingSystem.login("TES4", "cArBoNFox079"));
        assertTrue(bookingSystem.login("TES5", "dOct0rChicken01"));
    }

    /**
     * Test records in the database using completely incorrect credentials.
     */
    @Test
    void loginTestIncorrect1() {
        assertFalse(bookingSystem.login("ABCD", "EfgHi2277"));
        assertFalse(bookingSystem.login("JKLM", "nMopQ90"));
        assertFalse(bookingSystem.login("RSTU", "vxYz7633"));
    }

    /**
     * Test records in the database with correct username but incorrect password.
     */
    @Test
    void loginTestIncorrect2() {
        assertFalse(bookingSystem.login("TES1", "AbCdE122"));
        assertFalse(bookingSystem.login("TES2", "fGhIj456"));
        assertFalse(bookingSystem.login("TES3", "kLmNopq69"));
    }

    /**
     * Test records in the database with a password that exists in the database but not the username.
     */
    @Test
    void loginTestIncorrect3() {
        assertFalse(bookingSystem.login("ABCD", "ComputerApple5678"));
        assertFalse(bookingSystem.login("EFGH", "SpacefRuit8711"));
        assertFalse(bookingSystem.login("IJKL", "BottleBoX4339"));
    }


    /**
     * Test that the correct message is pulled from the database.
     * Using a doctor that is logged in.
     * The message does exist in the database.
     * Testing for a single message.
     */
    @Test
    void getNewMessagesTest1() {

        bookingSystem.login("TES1", "ComputerApple5678");
        for (Message ms : bookingSystem.getNewMessages()) {
            messages.add(ms.getContent());
        }
        //Gets the first message which relates to test doctor and patient 1.
        mes = messages.getFirst();

        assertEquals("D: TES1, P: TP01", mes);

    }

    /**
     * Test that the correct message is pulled from the database.
     * Using a doctor that is logged in.
     * The message does exist in the database.
     * Testing for a single message.
     */
    @Test
    void getNewMessagesTest2() {
        bookingSystem.login("TES2", "SpacefRuit8711");
        for (Message ms : bookingSystem.getNewMessages()) {
            mes = ms.getContent();
        }
        assertEquals("D: TES2, P: TP02", mes);

    }

    /**
     * Test that the correct message is pulled from the database.
     * Using a doctor that is logged in.
     * The message does exist in the database.
     * Testing for a single message.
     */
    @Test
    void getNewMessagesTest3() {
        bookingSystem.login("TES3", "BottleBoX4339");
        for (Message ms : bookingSystem.getNewMessages()) {
            mes = ms.getContent();
        }
        assertEquals("D: TES3, P: TP03", mes);

    }

    /**
     * Test that the correct message is pulled from the database.
     * Using a doctor that is logged in.
     * The message does exist in the database.
     * Testing for a single message.
     */
    @Test
    void getNewMessagesTest4() {
        bookingSystem.login("TES4", "cArBoNFox079");
        for (Message ms : bookingSystem.getNewMessages()) {
            mes = ms.getContent();
        }
        assertEquals("D: TES4, P: TP04", mes);

    }

    /**
     * Test that all messages for a doctor are retrieved.
     * For a doctor that is logged into the system.
     * Testing for three messages.
     * Compares the linked list returned by the getNewMessages method and a list created holding the expected messages.
     */
    @Test
    void getNewMessageTest5() {

        ls.add("D: TES1, P: TP01");
        ls.add("D: TES1, P: TP02");
        ls.add("D: TES1, P: TP03");

        bookingSystem.login("TES1", "ComputerApple5678");

        for (Message ms : bookingSystem.getNewMessages()) {
            messages.add(ms.getContent());
        }
        assertEquals(ls, messages);
    }


    /**
     * Test that no messages are retrieved when no doctor is logged in.
     */
    @Test
    void getNewMessageTest6() {

        assertNull(bookingSystem.getNewMessages());

    }

    /**
     * Test that the correct booking is shown for the doctor logged in as 'TES1'.
     */
    @Test
    void viewBookingsTest1() {

        bookingSystem.login("TES1", "ComputerApple5678");

        //The expected string that should be created.
        expectedString = "Booking ID: BT01, Doctor Name: TES1, Patient Name: Tony Stark, Date: 2021-02-26, " +
                "Reason for Visit: Cholera, Prescription: Calpol.";

        //Loops through the list containing the bookings and builds a string to be compared.
        for (Booking b : bookingSystem.getBookings(2, 2021)) {
            doctorName = b.getDoctorName();
            patientName = b.getPatientName();
            date = b.getDate().toString();
            visitDetails = b.getVisitDetails();
            prescription = b.getPrescription();
            bookingID = b.getBooking_ID();

            actualString = "Booking ID: " + bookingID + ", Doctor Name: " + doctorName + ", Patient Name: " +
                    patientName + ", Date: " + date + ", Reason for Visit: " + visitDetails + ", Prescription: " +
                    prescription + ".";
        }

        assertEquals(expectedString, actualString);

    }

    /**
     * Tests that the correct booking is shown for the doctor logged in as 'TES2'.
     */
    @Test
    void viewBookingTest2() {

        bookingSystem.login("TES2", "SpacefRuit8711");

        //The expected string that should be created.
        expectedString = "Booking: Doctor Name: TES2, Patient Name: Steve Rogers, Date: 2021-02-26, " +
                "Reason for Visit: Eye Irritation, Prescription: Eye Drops.";

        //Loops through the list containing the bookings and builds a string to be compared.
        for (Booking b : bookingSystem.getBookings(2, 2021)) {
            doctorName = b.getDoctorName();
            patientName = b.getPatientName();
            date = b.getDate().toString();
            visitDetails = b.getVisitDetails();
            prescription = b.getPrescription();

            actualString = "Booking: Doctor Name: " + doctorName + ", Patient Name: " + patientName + ", Date: " +
                    date + ", Reason for Visit: " + visitDetails + ", Prescription: " + prescription + ".";
        }

        assertEquals(expectedString, actualString);

    }

    /**
     * Tests that the correct booking is shown for the doctor logged in as 'TES3'.
     */
    @Test
    void viewBookingTest3() {

        bookingSystem.login("TES3", "BottleBoX4339");

        //The expected string that should be created.
        expectedString = "Booking: Doctor Name: TES3, Patient Name: Thor SonOfOdin, Date: 2021-02-26, " +
                "Reason for Visit: Chlamydia, Prescription: Antibiotics.";

        //Loops through the list containing the bookings and builds a string to be compared.
        for (Booking b : bookingSystem.getBookings(2, 2021)) {
            doctorName = b.getDoctorName();
            patientName = b.getPatientName();
            date = b.getDate().toString();
            visitDetails = b.getVisitDetails();
            prescription = b.getPrescription();

            actualString = "Booking: Doctor Name: " + doctorName + ", Patient Name: " + patientName + ", Date: " +
                    date + ", Reason for Visit: " + visitDetails + ", Prescription: " + prescription + ".";
        }

        assertEquals(expectedString, actualString);
    }

    /**
     * Tests that the correct bookings are shown for the doctor logged in as 'TES4'.
     * Two bookings should be shown.
     */
    @Test
    void viewBookingTest4() {

        bookingSystem.login("TES4", "cArBoNFox079");

        //The expected string that should be created.
        expectedString = "Booking: Doctor Name: TES4, Patient Name: Nick Fury, Date: 2021-02-26, " +
                "Reason for Visit: Bad Humours, Prescription: Bloodletting.";

        String expectedString2 = "Booking: Doctor Name: TES4, Patient Name: Nick Fury, Date: 2021-02-25, " +
                "Reason for Visit: High Cholesterol, Prescription: Less Bacon.";

        expectedStrings.add(expectedString);
        expectedStrings.add(expectedString2);

        //Loops through the list containing the bookings and builds a string to be compared.
        for (Booking b : bookingSystem.getBookings(2, 2021)) {
            doctorName = b.getDoctorName();
            patientName = b.getPatientName();
            date = b.getDate().toString();
            visitDetails = b.getVisitDetails();
            prescription = b.getPrescription();

            actualString = "Booking: Doctor Name: " + doctorName + ", Patient Name: " + patientName + ", Date: " +
                    date + ", Reason for Visit: " + visitDetails + ", Prescription: " + prescription + ".";
            actualStrings.add(actualString);
        }

        assertEquals(expectedStrings, actualStrings);
    }

    /**
     * Tests that no bookings are retrieved as no doctor is logged in.
     */
    @Test
    void viewBookingsTest5() {
        assertNull(bookingSystem.getBookings(2, 2021));
    }

    /**
     * Tests that the correct patients are returned for the doctor logged in as 'TES1'.
     * Three patients should be returned.
     */
    @Test
    void viewPatientsTest1() {

        bookingSystem.login("TES1", "ComputerApple5678");

        //The three expected strings relating to the three patients in the database registered to the doctor 'TES1'.
        expectedString = "Patient: Patient ID: TP01, Doctor Name: TES1, Patient Name: Tony Stark, " +
                "Phone Number: 07123456781, Date of Birth: 1998-01-07, Last Booking: 2021-02-26.";

        String expectedString2 = "Patient: Patient ID: TP05, Doctor Name: TES1, Patient Name: Bucky Barnes, " +
                "Phone Number: 07865467891, Date of Birth: 1940-05-09, Last Booking: 2021-02-26.";

        String expectedString3 = "Patient: Patient ID: TP06, Doctor Name: TES1, Patient Name: Sam Wilson, " +
                "Phone Number: 07123675381, Date of Birth: 1989-03-03, Last Booking: 2021-02-26.";

        expectedStrings.add(expectedString);
        expectedStrings.add(expectedString2);
        expectedStrings.add(expectedString3);

        //Loops through the string containing the results and builds a string.
        for (Patient p : bookingSystem.getOwnPatients()) {
            patientID = p.getPatientID();
            doctorName = p.getDoctorName();
            patientName = p.getName();
            phoneNumber = p.getPhoneNumber();
            dateOfBirth = p.getDob().toString();
            lastBooking = p.getLast_booking().toString();

            actualString = "Patient: Patient ID: " + patientID + ", Doctor Name: " + doctorName + ", Patient Name: "
                    + patientName + ", Phone Number: " + phoneNumber + ", Date of Birth: " + dateOfBirth
                    + ", Last Booking: " + lastBooking + ".";

            actualStrings.add(actualString);
        }

        assertEquals(expectedStrings, actualStrings);
    }

    /**
     * Tests that the correct patient is obtained for the doctor logged in as 'TES2'.
     * One patient should be retrieved.
     */
    @Test
    void viewPatientsTest2() {

        bookingSystem.login("TES2", "SpacefRuit8711");

        //The expected String that should be created.
        expectedString = "Patient: Patient ID: TP02, Doctor Name: TES2, Patient Name: Steve Rogers, " +
                "Phone Number: 07521678291, Date of Birth: 1928-04-04, Last Booking: 2021-02-26.";

        //Loops through the string containing the results and builds a string.
        for (Patient p : bookingSystem.getOwnPatients()) {
            patientID = p.getPatientID();
            doctorName = p.getDoctorName();
            patientName = p.getName();
            phoneNumber = p.getPhoneNumber();
            dateOfBirth = p.getDob().toString();
            lastBooking = p.getLast_booking().toString();

            actualString = "Patient: Patient ID: " + patientID + ", Doctor Name: " + doctorName + ", Patient Name: "
                    + patientName + ", Phone Number: " + phoneNumber + ", Date of Birth: " + dateOfBirth
                    + ", Last Booking: " + lastBooking + ".";
        }
        assertEquals(expectedString, actualString);
    }

    /**
     * Tests that the correct patient is obtained for the doctor logged in as 'TES3'.
     * One patient should be retrieved.
     */
    @Test
    void viewPatientsTest3() {

        bookingSystem.login("TES3", "BottleBoX4339");

        //The expected String that should be created.
        expectedString = "Patient: Patient ID: TP03, Doctor Name: TES3, Patient Name: Thor SonOfOdin, " +
                "Phone Number: 07722324618, Date of Birth: 1997-09-05, Last Booking: 2021-02-26.";

        //Loops through the string containing the results and builds a string.
        for (Patient p : bookingSystem.getOwnPatients()) {
            patientID = p.getPatientID();
            doctorName = p.getDoctorName();
            patientName = p.getName();
            phoneNumber = p.getPhoneNumber();
            dateOfBirth = p.getDob().toString();
            lastBooking = p.getLast_booking().toString();

            actualString = "Patient: Patient ID: " + patientID + ", Doctor Name: " + doctorName + ", Patient Name: "
                    + patientName + ", Phone Number: " + phoneNumber + ", Date of Birth: " + dateOfBirth
                    + ", Last Booking: " + lastBooking + ".";
        }
        assertEquals(expectedString, actualString);
    }

    /**
     * Tests that the correct patient is obtained for the doctor logged in as 'TES4'.
     * One patient should be retrieved.
     */
    @Test
    void viewPatientsTest4() {

        bookingSystem.login("TES4", "cArBoNFox079");

        //The expected String that should be created.
        expectedString = "Patient: Patient ID: TP04, Doctor Name: TES4, Patient Name: Nick Fury, " +
                "Phone Number: 07715678761, Date of Birth: 1973-03-01, Last Booking: 2021-02-26.";

        //Loops through the string containing the results and builds a string.
        for (Patient p : bookingSystem.getOwnPatients()) {
            patientID = p.getPatientID();
            doctorName = p.getDoctorName();
            patientName = p.getName();
            phoneNumber = p.getPhoneNumber();
            dateOfBirth = p.getDob().toString();
            lastBooking = p.getLast_booking().toString();

            actualString = "Patient: Patient ID: " + patientID + ", Doctor Name: " + doctorName + ", Patient Name: "
                    + patientName + ", Phone Number: " + phoneNumber + ", Date of Birth: " + dateOfBirth
                    + ", Last Booking: " + lastBooking + ".";
        }

        assertEquals(expectedString, actualString);
    }

    /**
     * Tests that no patients are retrieved as no doctor is logged in.
     */
    @Test
    void viewPatientsTest5() {
        assertNull(bookingSystem.getOwnPatients());
    }


    /**
     * Test that the enterDetails method works as intended. Records in the 'Bookings' table should be updated. A entry
     * should be added to the 'Message' table as well as a string returned for the patients confirmation message.
     * <p>
     * Three expected messages that should be created as a result of the method:
     * expectedString - the two fields in the database concatenated together.
     * expectedMessage - the message to be added in the message table.
     * expectedUserMessage - the users confirmation message.
     */
    @Test
    void enterDetailsTest1() {

        bookingSystem.login("TES1", "ComputerApple5678");
        actualUserMessage = bookingSystem.enterDetails("Visit Details Changed for BT01",
                "Prescription Changed for BT01", "BT01");

        //Expected strings that the method should generate.
        expectedString = "Prescription Changed for BT01 : Visit Details Changed for BT01";
        expectedMessage = "Information updated for booking: BT01.";
        expectedUserMessage = "The information for patient: TP01 has been updated for the booking which happened on:" +
                " 2021-02-26. The new reason for visit is: Visit Details Changed for BT01, and the new prescription" +
                " given is: Prescription Changed for BT01. If there has been a mistake please contact" +
                " reception immediately.";

        //Looping through the lists containing the bookings and the messages
        // to get the updated records as a result of the method.
        for (Booking b : bookingSystem.getBookings(2, 2021)) {
            prescription = b.getPrescription();
            visitDetails = b.getVisitDetails();
            actualString = prescription + " : " + visitDetails;
        }
        for (Message ms : bookingSystem.getNewMessages()) {
            messages.add(ms.getContent());
        }

        //Compares the actual strings against the expected ones.
        assertEquals(expectedString, actualString);
        assertEquals(expectedMessage, messages.get(3));
        assertEquals(expectedUserMessage, actualUserMessage);
    }

    /**
     * Test that the enterDetails method works as intended. Records in the 'Bookings' table should be updated. A entry
     * should be added to the 'Message' table as well as a string returned for the patients confirmation message.
     * <p>
     * Three expected messages that should be created as a result of the method:
     * expectedString - the two fields in the database concatenated together.
     * expectedMessage - the message to be added in the message table.
     * expectedUserMessage - the users confirmation message.
     */
    @Test
    void enterDetailsTest2() {

        bookingSystem.login("TES2", "SpacefRuit8711");
        actualUserMessage = bookingSystem.enterDetails("Visit Details Changed for BT02",
                "Prescription Changed for BT02", "BT02");

        //Expected strings that the method should generate.
        expectedString = "Prescription Changed for BT02 : Visit Details Changed for BT02";
        expectedMessage = "Information updated for booking: BT02.";
        expectedUserMessage = "The information for patient: TP02 has been updated for the booking which happened on:" +
                " 2021-02-26. The new reason for visit is: Visit Details Changed for BT02, and the new prescription" +
                " given is: Prescription Changed for BT02. If there has been a mistake please contact" +
                " reception immediately.";

        //Looping through the lists containing the bookings and the messages
        // to get the updated records as a result of the method.
        for (Booking b : bookingSystem.getBookings(2, 2021)) {
            prescription = b.getPrescription();
            visitDetails = b.getVisitDetails();

            actualString = prescription + " : " + visitDetails;
        }
        for (Message ms : bookingSystem.getNewMessages()) {
            messages.add(ms.getContent());
        }

        //Compares the actual strings against the expected ones.
        assertEquals(expectedString, actualString);
        assertEquals(expectedMessage, messages.get(1));
        assertEquals(expectedUserMessage, actualUserMessage);
    }

    /**
     * Test that the enterDetails method works as intended. Records in the 'Bookings' table should be updated. A entry
     * should be added to the 'Message' table as well as a string returned for the patients confirmation message.
     * <p>
     * Three expected messages that should be created as a result of the method:
     * expectedString - the two fields in the database concatenated together.
     * expectedMessage - the message to be added in the message table.
     * expectedUserMessage - the users confirmation message.
     */
    @Test
    void enterDetailsTest3() {

        bookingSystem.login("TES3", "BottleBoX4339");
        actualUserMessage = bookingSystem.enterDetails("Visit Details Changed for BT03",
                "Prescription Changed for BT03", "BT03");

        //Expected strings that the method should generate.
        expectedString = "Prescription Changed for BT03 : Visit Details Changed for BT03";
        expectedMessage = "Information updated for booking: BT03.";
        expectedUserMessage = "The information for patient: TP03 has been updated for the booking which happened on:" +
                " 2021-02-26. The new reason for visit is: Visit Details Changed for BT03, and the new prescription" +
                " given is: Prescription Changed for BT03. If there has been a mistake please contact" +
                " reception immediately.";

        //Looping through the lists containing the bookings and the messages
        // to get the updated records as a result of the method.
        for (Booking b : bookingSystem.getBookings(2, 2021)) {
            prescription = b.getPrescription();
            visitDetails = b.getVisitDetails();

            actualString = prescription + " : " + visitDetails;
        }
        for (Message ms : bookingSystem.getNewMessages()) {
            messages.add(ms.getContent());
        }

        //Compares the actual strings against the expected ones.
        assertEquals(expectedString, actualString);
        assertEquals(expectedMessage, messages.get(1));
        assertEquals(expectedUserMessage, actualUserMessage);

    }

    /**
     * Test that the enterDetails method works as intended. Records in the 'Bookings' table should be updated. A entry
     * should be added to the 'Message' table as well as a string returned for the patients confirmation message.
     * <p>
     * Three expected messages that should be created as a result of the method.
     * expectedString - the two fields in the database concatenated together.
     * expectedMessage - the message to be added in the message table.
     * expectedUserMessage - the users confirmation message.
     * <p>
     * One additional string for each will be created as two instances of enterDetails are being called.
     */
    @Test
    void enterDetailsTest4() {

        bookingSystem.login("TES4", "cArBoNFox079");
        actualUserMessage = bookingSystem.enterDetails("Visit Details Changed for BT04",
                "Prescription Changed for BT04", "BT04");
        String actualUserMessage2 = bookingSystem.enterDetails("Visit Details Changed for BT05",
                "Prescription Changed for BT05", "BT05");

        //Expected strings that the method should generate.
        expectedString = "Prescription Changed for BT04 : Visit Details Changed for BT04";
        String expectedString2 = "Prescription Changed for BT05 : Visit Details Changed for BT05";
        expectedMessage = "Information updated for booking: BT04.";
        String expectedMessage2 = "Information updated for booking: BT05.";
        expectedUserMessage = "The information for patient: TP04 has been updated for the booking which happened on:" +
                " 2021-02-26. The new reason for visit is: Visit Details Changed for BT04, and the new prescription" +
                " given is: Prescription Changed for BT04. If there has been a mistake please contact" +
                " reception immediately.";
        String expectedUserMessage2 = "The information for patient: TP04 has been updated for the booking which" +
                " happened on: 2021-02-25. The new reason for visit is: Visit Details Changed for BT05, and the" +
                " new prescription given is: Prescription Changed for BT05. If there has been a mistake please" +
                " contact reception immediately.";

        expectedStrings.add(expectedString);
        expectedStrings.add(expectedString2);

        //Looping through the lists containing the bookings and the messages
        // to get the updated records as a result of the method.
        for (Booking b : bookingSystem.getBookings(2, 2021)) {
            prescription = b.getPrescription();
            visitDetails = b.getVisitDetails();

            actualString = prescription + " : " + visitDetails;

            actualStrings.add(actualString);
        }
        for (Message ms : bookingSystem.getNewMessages()) {
            messages.add(ms.getContent());
        }

        //Compares the actual strings against the expected ones.
        assertEquals(expectedStrings, actualStrings);
        assertEquals(expectedMessage, messages.get(1));
        assertEquals(expectedMessage2, messages.get(2));
        assertEquals(expectedUserMessage, actualUserMessage);
        assertEquals(expectedUserMessage2, actualUserMessage2);
    }


    /**
     * Test that nothing is updated or added to the database when the enterDetails method is called as
     * no doctor is logged in
     */
    @Test
    void enterVisitDetails5() {
        assertNull(bookingSystem.enterDetails("Visit Details Changed for BT04",
                "Prescription Changed for BT04", "BT04"));
    }


    /**
     * Test that the assignPatient method works as intended. The 'Patient' table should be updated, a new message should be added to the
     * 'Message' table and a string should be returned for the patients confirmation message
     *
     * Three expected messages that should be created as a result of the method:
     * expectedString - the two fields in the database concatenated together.
     * expectedMessage - the message to be added in the message table.
     * expectedUserMessage - the users confirmation message.
     */

    @Test
    void assignPatient1() {
        bookingSystem.login("TES1", "ComputerApple5678");

        actualUserMessage = bookingSystem.assignPatients("TES1", "TP02");

        expectedString = "TES1 : TP02";
        expectedMessage = "Patient: TP02 is now assigned to Doctor: TES1";
        expectedUserMessage = "Doctor: TES1 has been assigned to Patient: TP02. If you would like to make any changes "+
                "please contact reception immediately.";

        //Looping through lists containing patient and messages

        for (Patient p : bookingSystem.getOwnPatients()) {
            doctorName = p.getDoctorName();
            patientName = p.getPatientID();
            actualString = doctorName + " : " + patientName;

            actualStrings.add(actualString);
        }

        for (Message ms : bookingSystem.getNewMessages()) {
            messages.add(ms.getContent());
        }

        assertEquals(expectedString, actualStrings.get(1));
        assertEquals(expectedMessage, messages.get(3));
        assertEquals(expectedUserMessage, actualUserMessage);

    }


    /**
     * Test that the assignPatient method works as intended. The 'Patient' table should be updated, a new message should be added to the
     * 'Message' table and a string should be returned for the patients confirmation message
     *
     * Three expected messages that should be created as a result of the method:
     * expectedString - the two fields in the database concatenated together.
     * expectedMessage - the message to be added in the message table.
     * expectedUserMessage - the users confirmation message.
     */

    @Test
    void assignPatient2() {
        bookingSystem.login("TES2", "SpacefRuit8711");

        actualUserMessage = bookingSystem.assignPatients("TES2", "TP01");

        expectedString = "TES2 : TP01";
        expectedMessage = "Patient: TP01 is now assigned to Doctor: TES2";
        expectedUserMessage = "Doctor: TES2 has been assigned to Patient: TP01. If you would like to make any changes "+
        "please contact reception immediately.";

        //Looping through lists containing patient and messages

        for (Patient p : bookingSystem.getOwnPatients()) {
            doctorName = p.getDoctorName();
            patientName = p.getPatientID();
            actualString = doctorName + " : " + patientName;

            actualStrings.add(actualString);
        }

        for (Message ms : bookingSystem.getNewMessages()) {
            messages.add(ms.getContent());
        }

        assertEquals(expectedString, actualStrings.get(0));
        assertEquals(expectedMessage, messages.get(2));
        assertEquals(expectedUserMessage, actualUserMessage);

    }


    /**
     * Test that the assignPatient method works as intended. The 'Patient' table should be updated, a new message should be added to the
     * 'Message' table and a string should be returned for the patients confirmation message
     *
     * Three expected messages that should be created as a result of the method:
     * expectedString - the two fields in the database concatenated together.
     * expectedMessage - the message to be added in the message table.
     * expectedUserMessage - the users confirmation message.
     */

    @Test
    void assignPatient3() {
        bookingSystem.login("TES3", "BottleBoX4339");

        actualUserMessage = bookingSystem.assignPatients("TES3", "TP04");

        expectedString = "TES3 : TP04";
        expectedMessage = "Patient: TP04 is now assigned to Doctor: TES3";
        expectedUserMessage = "Doctor: TES3 has been assigned to Patient: TP04. If you would like to make any changes "+
                "please contact reception immediately.";

        //Looping through lists containing patient and messages

        for (Patient p : bookingSystem.getOwnPatients()) {
            doctorName = p.getDoctorName();
            patientName = p.getPatientID();

            actualString = doctorName + " : " + patientName;
            actualStrings.add(actualString);
        }

        for (Message ms : bookingSystem.getNewMessages()) {
            messages.add(ms.getContent());
        }

        assertEquals(expectedString, actualStrings.get(1));
        assertEquals(expectedMessage, messages.get(2));
        assertEquals(expectedUserMessage, actualUserMessage);
    }


    /**
     * Test that the assignPatient method works as intended. The 'Patient' table should be updated, a new message should be added to the
     * 'Message' table and a string should be returned for the patients confirmation message
     *
     * Three expected messages that should be created as a result of the method:
     * expectedString - the two fields in the database concatenated together.
     * expectedMessage - the message to be added in the message table.
     * expectedUserMessage - the users confirmation message.
     * One additional string for each will be created as two instances of assignPatients are being called.
     */

    @Test
    void assignPatient4() {
        bookingSystem.login("TES4", "cArBoNFox079");

        actualUserMessage = bookingSystem.assignPatients("TES4", "TP02");
        String actualUserMessage2 = bookingSystem.assignPatients("TES4", "TP01");

        expectedString = "TES4 : TP01";
        String expectedString2 = "TES4 : TP02";
        expectedMessage = "Patient: TP01 is now assigned to Doctor: TES4";
        String expectedMessage2 = "Patient: TP02 is now assigned to Doctor: TES4";
        expectedUserMessage = "Doctor: TES4 has been assigned to Patient: TP01. If you would like to make any changes "+
                "please contact reception immediately.";
        String expectedUserMessage2 = "Doctor: TES4 has been assigned to Patient: TP02. If you would like to make any changes "+
                "please contact reception immediately.";

        //Looping through lists containing patient and messages
        for (Patient p : bookingSystem.getOwnPatients()) {
            doctorName = p.getDoctorName();
            patientName = p.getPatientID();

            actualString = doctorName + " : " + patientName;

            actualStrings.add(actualString);
        }

        for (Message ms : bookingSystem.getNewMessages()) {
            messages.add(ms.getContent());
        }

        assertEquals(expectedString, actualStrings.get(0));
        assertEquals(expectedString2, actualStrings.get(1));
        assertEquals(expectedMessage, messages.get(3));
        assertEquals(expectedMessage2, messages.get(2));
        assertEquals(expectedUserMessage, actualUserMessage2);
        assertEquals(expectedUserMessage2, actualUserMessage);
    }

    /**
     * Test that nothing is updated or added to the database when the assignPatients method is called as
     * no doctor is logged in
     */
    @Test
    void assignPatient5() {
        assertNull(bookingSystem.assignPatients("TES3", "TP04"));
    }

    /**
     * Test that the log in of a doctor is logged correctly.
     */
    @Test
    void logActionTest1(){
        bookingSystem.login("TES1","ComputerApple5678");
        //expected string that should be produced from the values in the database.
        expectedString = "1, TES1, Doctor Login";

        try{
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Logs WHERE logID = '1';";

            //Builds the string using values from the database query.
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            logID = rs.getString(1);
            doctorName = rs.getString(2);
            action = rs.getString(3);
            actualString = logID + ", " + doctorName + ", " + action;

            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(expectedString, actualString);
    }

    /**
     * Test that the retrieval of messages is logged correctly.
     */
    @Test
    void logActionTest2(){
        bookingSystem.login("TES1","ComputerApple5678");
        bookingSystem.getNewMessages();
        //expected string that should be produced from the values in the database.
        expectedString = "2, TES1, Messages Retrieved";

        try{
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Logs WHERE logID = '2';";

            //Builds the string using values from the database query.
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            logID = rs.getString(1);
            doctorName = rs.getString(2);
            action = rs.getString(3);
            actualString = logID + ", " + doctorName + ", " + action;

            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(expectedString, actualString);
    }

    /**
     * Test that the retrieval of bookings is logged correctly
     */
    @Test
    void logActionTest3(){
        bookingSystem.login("TES1","ComputerApple5678");
        bookingSystem.getBookings(2,2021);
        //expected string that should be produced from the values in the database.
        expectedString = "2, TES1, Bookings Retrieved";

        try{
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Logs WHERE logID = '2';";

            //Builds the string using values from the database query.
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            logID = rs.getString(1);
            doctorName = rs.getString(2);
            action = rs.getString(3);
            actualString = logID + ", " + doctorName + ", " + action;

            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(expectedString, actualString);
    }

    /**
     * Test the retrieval of own patients is logged correctly.
     */
    @Test
    void logActionTest4(){
        bookingSystem.login("TES1","ComputerApple5678");
        bookingSystem.getOwnPatients();
        //expected string that should be produced from the values in the database.
        expectedString = "2, TES1, Patients Retrieved";

        try{
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Logs WHERE logID = '2';";

            //Builds the string using values from the database query.
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            logID = rs.getString(1);
            doctorName = rs.getString(2);
            action = rs.getString(3);
            actualString = logID + ", " + doctorName + ", " + action;

            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(expectedString, actualString);
    }

    /**
     * Test that the updating of prescription and visit details are logged correctly.
     */
    @Test
    void logActionTest5(){
        bookingSystem.login("TES1","ComputerApple5678");
        bookingSystem.enterDetails("Visit Details Changed for BT01",
                "Prescription Changed for BT01","BT01");
        //expected string that should be produced from the values in the database.
        expectedString = "2, TES1, Details Updated";

        try{
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Logs WHERE logID = '2';";

            //Builds the string using values from the database query.
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            logID = rs.getString(1);
            doctorName = rs.getString(2);
            action = rs.getString(3);
            actualString = logID + ", " + doctorName + ", " + action;

            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(expectedString, actualString);
    }

    /**
     * Test that the assigning of patients to a doctor is logged correctly
     */
    @Test
    void logActionTest6(){
        bookingSystem.login("TES1","ComputerApple5678");
        bookingSystem.assignPatients("TES1", "TP02");
        //expected string that should be produced from the values in the database.
        expectedString = "2, TES1, Patient assigned to Doctor";

        try{
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Logs WHERE logID = '2';";

            //Builds the string using values from the database query.
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            logID = rs.getString(1);
            doctorName = rs.getString(2);
            action = rs.getString(3);
            actualString = logID + ", " + doctorName + ", " + action;

            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(expectedString, actualString);
    }

    /**
     * Test that the logout method is logged correctly.
     */
    @Test
    void logActionTest7(){

        bookingSystem.login("TES1","ComputerApple5678");
        bookingSystem.logout();

        expectedString = "2, TES1, Doctor Logout";

        try{
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Logs WHERE logID = '2';";

            //Builds the string using values from the database query.
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            logID = rs.getString(1);
            doctorName = rs.getString(2);
            action = rs.getString(3);
            actualString = logID + ", " + doctorName + ", " + action;

            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        assertEquals(expectedString, actualString);

    }




}