package DAO;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import java.sql.*;
import java.time.LocalDate;

public class ModelImpl {

    public static ObservableList<User> getAllUsers() throws SQLException {

        ObservableList<User> allUsers = FXCollections.observableArrayList();

        String q = "SELECT * FROM users";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(q);

        ResultSet rs = ps.executeQuery();

        while (rs.next())
        {
            int id = rs.getInt("User_ID");
            String username = rs.getString("User_Name");
            String password = rs.getString("Password");

            User user = new User(id, username, password);
            allUsers.add(user);
        }
        return allUsers;
    }

    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();

        String q = "SELECT * FROM countries";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(q);

        ResultSet rs = ps.executeQuery();

        while (rs.next())
        {
            int id = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");

            Country c = new Country(id, countryName);
            allCountries.add(c);
        }
        return allCountries;
    }

    public static ObservableList<FirstLevelDivision> getAllDivisions() throws SQLException {
        ObservableList<FirstLevelDivision> allDivisions = FXCollections.observableArrayList();

        String q = "SELECT * FROM first_level_divisions";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(q);

        ResultSet rs = ps.executeQuery();

        while (rs.next())
        {
            int id = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int country_id_FK = rs.getInt("Country_ID");

            FirstLevelDivision d = new FirstLevelDivision(id, division, country_id_FK);
            allDivisions.add(d);
        }
        return allDivisions;
    }

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

        String q = "SELECT * FROM customers";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(q);

        ResultSet rs = ps.executeQuery();

        while (rs.next())
        {
            int id = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phoneNumber = rs.getString("Phone");
            int division_id_FK = rs.getInt("Division_ID");
            String division = null;

            ResultSet getDivision = Query.myQuery("first_level_divisions", "Division_ID", division_id_FK);
            while (getDivision.next())
            {
                division = getDivision.getString("Division");
            }

            Customer c = new Customer(id, name, address, postalCode, phoneNumber, division, division_id_FK);
            allCustomers.add(c);
        }
        return allCustomers;
    }

    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        String q = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(q);

        ResultSet rs = ps.executeQuery();

        while (rs.next())
        {
            int appt_id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            int customer_id_FK = rs.getInt("Customer_ID");
            int user_id_FK = rs.getInt("User_ID");
            int contact_id_FK = rs.getInt("Contact_ID");
            Timestamp start = rs.getObject("Start", Timestamp.class);
            Timestamp end = rs.getObject("End", Timestamp.class);

            Appointment appt = new Appointment(appt_id, title, description, location, type, start, end, customer_id_FK, user_id_FK, contact_id_FK);
            allAppointments.add(appt);
        }
        return allAppointments;
    }

    public static ObservableList<Appointment> getWeeklyAppointments() throws SQLException {
        ObservableList<LocalDate> localTimePlusSeven = FXCollections.observableArrayList();
        ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();

        LocalDate now = LocalDate.now();

        //add local date and add 6 other adding 1 day for each iteration to array list
        for (int i = 0; i < 7; i++){
            localTimePlusSeven.add(now.plusDays(i));
        }

        //Check if array list containing local date + 6 days after local date contains appointment
        for (Appointment a : getAllAppointments()) {
            if (localTimePlusSeven.contains(a.getStart().toLocalDateTime().toLocalDate())) {
                weeklyAppointments.add(a);
            }
        }
        return weeklyAppointments;
    }

    public static ObservableList<Appointment> getMonthlyAppointments() throws SQLException {
        ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();

        LocalDate now = LocalDate.now();

        for (Appointment a : getAllAppointments())
        {
            //Check if local date's month and year matches the current appointment in iteration if true then add to array list
           if (now.getYear() == a.getStart().toLocalDateTime().toLocalDate().getYear() && now.getMonth() == a.getStart().toLocalDateTime().toLocalDate().getMonth())
           {
               monthlyAppointments.add(a);
           }
        }
        return monthlyAppointments;
    }


    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();

        String q = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(q);

        ResultSet rs = ps.executeQuery();

        while (rs.next())
        {
            int id = rs.getInt("Contact_ID");
            String name = rs.getString("Contact_Name");
            String email = rs.getString("Email");

            Contact c = new Contact(id, name, email);
            allContacts.add(c);
        }
        return allContacts;
    }

    /** Will display divisions according to selected country. */
    public static void setDivisions(ComboBox<Country> countryCombo, ComboBox<FirstLevelDivision> divCombo) throws SQLException {
        ObservableList<FirstLevelDivision> filteredDivisions = FXCollections.observableArrayList();
        Country selectedCountry = countryCombo.getSelectionModel().getSelectedItem();

        for (FirstLevelDivision div : ModelImpl.getAllDivisions())
        {
            if (div.getCountry_id_FK() == selectedCountry.getCountry_id())
            {
                filteredDivisions.add(div);
            }
        }
        divCombo.setItems(filteredDivisions);
    }

    public static ObservableList<String> allTypes() throws SQLException {
        ObservableList<String> allTypes = FXCollections.observableArrayList();

        for (Appointment a : getAllAppointments()){
            allTypes.add(a.getType());
        }
        return  allTypes;
    }

    /** Returns total appointments for a given month. */
    public static int totalAppointmentsByMonth(String month) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        int total = 0;
        for (Appointment a : getAllAppointments()) {
            String m = a.getStart().toLocalDateTime().getMonth().toString().toLowerCase(); //Gets appointment month and turns it into a string and lowercases it
            int y = a.getStart().toLocalDateTime().getYear(); //Gets appointment year

            if (m.equals(month.toLowerCase()) && y == LocalDate.now().getYear()){
                total++; //Updates total if there is a month matching month parameter and year matches current year
            }
        }
        return total;
    }

    /** Returns total appointments for a given type. */
    public static int totalAppointmentsByType(String type) throws SQLException{
        int total = 0;

        for (Appointment a : getAllAppointments()) {
            if (a.getType().equals(type)){
                total++;
            }
        }
        return total;
    }
}
