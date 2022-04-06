package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Query {

    /** Inserts a customer into MySQL database with parameters as values. */
    public static int createCustomer(String Customer_Name, String Address, String Postal_Code, String Phone, int Division_ID) throws SQLException {

        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, Customer_Name);
        ps.setString(2, Address);
        ps.setString(3, Postal_Code);
        ps.setString(4, Phone);
        ps.setInt(5, Division_ID);

        int affectedRows = ps.executeUpdate();
        return affectedRows;
    }

    /** Updates a customer in MySQL database with parameters as values. */
    public static int updateCustomer(int Customer_ID, String Customer_Name, String Address, String Postal_Code, String Phone, int Division_ID) throws SQLException {

        String sql = "UPDATE customers set Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, Customer_Name);
        ps.setString(2, Address);
        ps.setString(3, Postal_Code);
        ps.setString(4, Phone);
        ps.setInt(5, Division_ID);
        ps.setInt(6, Customer_ID);

        int affectedRows = ps.executeUpdate();
        return affectedRows;
    }
    /** Deletes a customer in MySQL database if Customer_ID argument matches a customer. */
    public static int deleteCustomer(int Customer_ID) throws SQLException {

        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setInt(1, Customer_ID);

        int affectedRows = ps.executeUpdate();
        return affectedRows;
    }

    /** Inserts an appointment into MySQL database with parameters as values. */
    public static int createAppointment(String Title, String Description, String Location, String Type, LocalDateTime Start, LocalDateTime End, int Customer_ID, int User_ID, int Contact_ID) throws SQLException {

        Timestamp start = Timestamp.valueOf(Start);
        Timestamp end = Timestamp.valueOf(End);

        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, Title);
        ps.setString(2, Description);
        ps.setString(3, Location);
        ps.setString(4, Type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, Customer_ID);
        ps.setInt(8, User_ID);
        ps.setInt(9, Contact_ID);

        int affectedRows = ps.executeUpdate();
        return affectedRows;
    }

    /** Deletes an appointment in MySQL database if Appointment_ID argument matches an appointment. */
    public static int deleteAppointment(int Appointment_ID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setInt(1, Appointment_ID);

        int affectedRows = ps.executeUpdate();
        return affectedRows;
    }
    /** Updates an appointment in MySQL database with parameters as values. */
    public static int updateAppointment(int Appointment_ID, String Title, String Description, String Location, String Type, LocalDateTime Start, LocalDateTime End, int Customer_ID, int User_ID, int Contact_ID) throws SQLException {

        Timestamp start = Timestamp.valueOf(Start);
        Timestamp end = Timestamp.valueOf(End);

        String sql = "UPDATE appointments set Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, Title);
        ps.setString(2, Description);
        ps.setString(3, Location);
        ps.setString(4, Type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, Customer_ID);
        ps.setInt(8, User_ID);
        ps.setInt(9, Contact_ID);
        ps.setInt(10, Appointment_ID);

        int affectedRows = ps.executeUpdate();
        return affectedRows;
    }

    /** Will read from given table in MySQL db where id is equal to id argument. */
    public static ResultSet myQuery(String table, String where, int id) throws SQLException {
        String query = "SELECT * FROM " + table + " WHERE " + where + " = " + id + ";";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        return rs;
    }
}

