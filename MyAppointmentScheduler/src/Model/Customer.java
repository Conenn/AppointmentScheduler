package Model;

import DAO.ModelImpl;

import java.sql.SQLException;

public class Customer {
    private int customer_id;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String division;
    private int division_id_FK;

    public Customer(int customer_id, String customerName, String address, String postalCode, String phoneNumber, String division, int division_id_FK) {
        this.customer_id = customer_id;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.division = division;
        this.division_id_FK = division_id_FK;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String country) {
        this.division = country;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getDivision_id_FK() {
        return division_id_FK;
    }

    public void setDivision_id_FK(int division_id_FK) {
        this.division_id_FK = division_id_FK;
    }

    public static Customer getCustomerById(int id) throws SQLException {
        for (Customer c : ModelImpl.getAllCustomers())
        {
            if (id == c.getCustomer_id()){
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString()
    {
        return (customerName);
    }
}

