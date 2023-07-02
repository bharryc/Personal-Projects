import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Home extends JFrame implements ActionListener {

    // The instance of the user in the system.
    private BookingSystem doctor;
    // The window that will make the home page.
    private Window window;
    // The logout button that will allow the user to exit the system.
    private JButton logoutButton;
    // The search button that will find the bookings depending on the month and year.
    private JButton searchButton;
    // The list of bookings that is in the month and year the user wants.
    private List<BookingSummary> bookingGuis = new ArrayList<>();
    private List<PatientSummary> patientGuis = new ArrayList<>();
    // The JLabel used to display if there is no bookings.
    private JLabel emptyLabel = new JLabel("");

    private Home home;
    // Components used for global reference in the home page.
    private JPanel bookingPanel, bookingMainPanel, patientPanel, patientMainPanel, contentPanel;
    private JSpinner monthSpinner = new JSpinner(new SpinnerDateModel());
    private JSpinner yearSpinner = new JSpinner(new SpinnerDateModel());;
    private JSpinner.DateEditor yearEditor = new JSpinner.DateEditor(yearSpinner, "yyyy");
    private JSpinner.DateEditor monthEditor = new JSpinner.DateEditor(monthSpinner, "MM");

    /**
     * Makes an instance of the home screen the user will use to look for bookings.
     * @param user is the current user logged into the system.
     */
    Home(BookingSystem user) {
        home = this;
        doctor = user;
        window = new Window("Home Page", 1100, 600);
        window.setContentPane(getFrame());
    }

    /**
     * Makes the frame for the home window that will hold all of the bookings and messages for the user.
     * @return the frame for the home window.
     */
    private JComponent getFrame() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(getContent(), BorderLayout.CENTER);
        mainPanel.add(getLogout(), BorderLayout.SOUTH);
        return mainPanel;
    }

    /**
     * Makes the search bar panel that will search for the bookings by date.
     * @return the search bar panel.
     */
    private JComponent getSearch() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        JLabel monthLabel = new JLabel("Month:");
        JLabel yearLabel = new JLabel("Year:");
        yearSpinner.setSize(50, 25);
        monthSpinner.setSize(50, 25);
        monthSpinner.setEditor(monthEditor);
        yearSpinner.setEditor(yearEditor);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(monthLabel);
        searchPanel.add(monthSpinner);
        searchPanel.add(yearLabel);
        searchPanel.add(yearSpinner);
        searchPanel.add(searchButton);
        return searchPanel;
    }

    /**
     * Makes the main content of the screen, being the bookings and messages areas.
     * @return the content panel.
     */
    private JComponent getContent() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout());
        contentPanel.add(getBookingsGui());
        contentPanel.add(getMainBooking());
        contentPanel.add(getPatients());
        contentPanel.add(getPatient());
        JScrollPane contentPane = new JScrollPane(contentPanel);
        contentPane.setBounds(50,30,300,500);
        contentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return contentPane;
    }

    /**
     * Makes the bookings panel that will then populate the content panel.
     * @return the bookings panel.
     */
    private JComponent getBookingsGui() {
        bookingPanel = new JPanel();
        bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        JLabel titleLabel = new JLabel("BOOKINGS");
        Font font = new Font("Arial", Font.BOLD, 20);
        titleLabel.setFont(font);
        titlePanel.add(titleLabel);
        bookingPanel.add(titlePanel);
        bookingPanel.add(getSearch());
        JScrollPane contentPane = new JScrollPane(bookingPanel);
        contentPane.setBounds(50,30,300,500);
        contentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return contentPane;
    }

    /**
     * Makes the main booking panel that holds the booking selected by the user.
     * @return the main booking panel.
     */
    private JComponent getMainBooking() {
        bookingMainPanel = new JPanel(new FlowLayout());
        JScrollPane contentPane = new JScrollPane(bookingMainPanel);
        contentPane.setBounds(50,30,300,500);
        contentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return contentPane;
    }

    /**
     * Makes the bookings that will be put into the booking panel.
     * @param month is the month of the booking.
     * @param year is the year of the booking.
     * @return the list of bookingGuis that will populate the booking panel.
     */
    private List<BookingSummary> makeBookings(int month, int year) {
        bookingGuis.clear();
        List<Booking> bookings = doctor.getBookings(month, year);
        for(Booking booking : bookings) {
            BookingSummary bookingSummary = new BookingSummary(booking.getPatientName(), booking.getDoctorName(), booking.getPrescription(), booking.getVisitDetails(), booking.getDate().toString(), booking.getBooking_ID());
            JButton viewButton = new JButton("View");
            // When clicked the full booking information will be shown to the user.
            viewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bookingMainPanel.removeAll();
                    BookingGui bookingGui = new BookingGui(bookingSummary.getName(), bookingSummary.getDoctor(), bookingSummary.getPrescription(), bookingSummary.getVisitDetails(), bookingSummary.getDate(), bookingSummary.getBookingId());
                    JPanel buttonPanel = new JPanel(new FlowLayout());
                    JButton editButton = new JButton("Edit");
                    editButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            bookingMainPanel.removeAll();
                            window.revalidate();
                            window.repaint();
                            EditDetails editDetails = new EditDetails(home, doctor,bookingGui.getBookingID(), booking.getPrescription(), booking.getVisitDetails());
                        }
                    });
                    JButton closeButton = new JButton("Close");
                    closeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            bookingMainPanel.removeAll();
                            window.revalidate();
                            window.repaint();
                        }
                    });
                    buttonPanel.add(editButton);
                    buttonPanel.add(closeButton);
                    bookingGui.add(buttonPanel);
                    bookingMainPanel.add(bookingGui);
                    window.revalidate();
                    window.repaint();
                }
            });
            bookingSummary.add(viewButton);
            bookingGuis.add(bookingSummary);
        }
        return bookingGuis;
    }

    /**
     * Populates the patients list that will then be shown to the user when the home page loads.
     * @return the patient gui list.
     */
    private List<PatientSummary> makePatients() {
        patientGuis.clear();
        List<Patient> patients = doctor.getOwnPatients();
        for(Patient patient : patients) {
            PatientSummary patientSummary = new PatientSummary(patient.getName(), patient.getPatientID());
            JButton viewButton = new JButton("View");
            // When clicked the full patient information will be shown to the user.
            viewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    patientMainPanel.removeAll();
                    PatientGui patientGui = new PatientGui(patient.getPatientID(), patient.getName(), patient.getPhoneNumber());
                    JPanel buttonPanel = new JPanel(new FlowLayout());
                    JButton editButton = new JButton("Reassign");
                    editButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            patientMainPanel.removeAll();
                            window.revalidate();
                            window.repaint();
                            ReassignWindow reassignWindow = new ReassignWindow(home, doctor, patient.getPatientID());
                        }
                    });
                    JButton closeButton = new JButton("Close");
                    closeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            patientMainPanel.removeAll();
                            window.revalidate();
                            window.repaint();
                        }
                    });
                    buttonPanel.add(editButton);
                    buttonPanel.add(closeButton);
                    patientGui.add(buttonPanel);
                    patientMainPanel.add(patientGui);
                    window.revalidate();
                    window.repaint();
                }
            });
            patientSummary.add(viewButton);
            patientGuis.add(patientSummary);
        }
        return patientGuis;
    }

    /**
     * Makes the messages panel that will then populate the content panel.
     * @return the messages panel.
     */
    private JComponent getPatients() {
        patientGuis.clear();
        patientPanel = new JPanel();
        patientPanel.setLayout(new BoxLayout(patientPanel, BoxLayout.Y_AXIS));
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        JLabel titleLabel = new JLabel("PATIENTS");
        Font font = new Font("Arial", Font.BOLD, 20);
        titleLabel.setFont(font);
        titlePanel.add(titleLabel);
        patientPanel.add(titlePanel);
        makePatients();
        addPatients();
        JScrollPane contentPane = new JScrollPane(patientPanel);
        contentPane.setBounds(50,30,300,500);
        contentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return contentPane;
    }

    /**
     * Makes a panel that will display the full information of a patient to the user.
     * @return the main patient panel.
     */
    private JComponent getPatient() {
        patientMainPanel = new JPanel(new FlowLayout());
        JScrollPane contentPane = new JScrollPane(patientMainPanel);
        contentPane.setBounds(50,30,300,500);
        contentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return contentPane;
    }

    /**
     * Makes the logout panel of the home page.
     * @return the logout panel.
     */
    private JComponent getLogout() {
        JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new FlowLayout());
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
                Login login = new Login();
            }
        });
        logoutPanel.add(logoutButton);
        return logoutPanel;
    }

    /**
     * Turns the string version of month into digits.
     * @param month is the month of the year.
     * @return the digit format of month.
     */
    private int getMonthNum(String month) {
        int num;
        switch(month) {
            case "Jan":
                num = 1;
                break;
            case "Feb":
                num = 2;
                break;
            case "Mar":
                num = 3;
                break;
            case "Apr":
                num = 4;
                break;
            case "May":
                num = 5;
                break;
            case "Jun":
                num = 6;
                break;
            case "Jul":
                num = 7;
                break;
            case "Aug":
                num = 8;
                break;
            case "Sep":
                num = 9;
                break;
            case "Oct":
                num = 10;
                break;
            case "Nov":
                num = 11;
                break;
            case "Dec":
                num = 12;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + month);
        }
        return num;
    }

    /**
     * Adds all patients that are currently in the patient list to the patient panel.
     */
    private void addPatients() {
        if(patientGuis.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new FlowLayout());
            emptyLabel.setText("No Patients");
            emptyPanel.add(emptyLabel);
            patientPanel.add(emptyPanel);
        }
        for(PatientSummary patientSummary : patientGuis) {
            patientPanel.add(patientSummary);
        }
        window.revalidate();
        window.repaint();
    }

    /**
     * Removes all the patients in patient list from the patient panel.
     */
    private void removePatients() {
        for(PatientSummary patientSummary : patientGuis) {
            patientPanel.remove(patientSummary);
        }
        window.revalidate();
        window.repaint();
    }

    /**
     * Removes all the bookings from the booking panel.
     */
    private void removeBookings() {
        emptyLabel.setText("");
        for(BookingSummary bookingSummary : bookingGuis) {
            bookingPanel.remove(bookingSummary);
        }
        window.revalidate();
        window.repaint();
    }

    /**
     * Adds all the bookings that are in the bookingGuis into the booking panel.
     */
    private void addBookings() {
        if(bookingGuis.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new FlowLayout());
            emptyLabel.setText("No Bookings");
            emptyPanel.add(emptyLabel);
            bookingPanel.add(emptyPanel);
        }
        for(BookingSummary bookingSummary : bookingGuis) {
            bookingPanel.add(bookingSummary);
        }
        window.revalidate();
        window.repaint();
    }

    /**
     * Used solely for the purpose of when a new window changes the state of the home page this method will update the window.
     */
    public void updateWindow() {
        removePatients();
        removeBookings();
        bookingGuis = makeBookings(getMonthNum(monthSpinner.getValue().toString().substring(4,7)), Integer.parseInt(yearSpinner.getValue().toString().substring(24,28)));
        patientGuis = makePatients();
        addBookings();
        addPatients();
        window.repaint();
    }

    /**
     * Searches for the bookings depending on the month and year they exist in.
     * @param e is the button being passed into the function.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        int monthValue = getMonthNum(monthSpinner.getValue().toString().substring(4,7));
        int yearValue = Integer.parseInt(yearSpinner.getValue().toString().substring(24,28));
        if(source == searchButton) {
            removeBookings();
            bookingGuis = makeBookings(monthValue, yearValue);
            addBookings();
            window.repaint();
        }
    }
}
