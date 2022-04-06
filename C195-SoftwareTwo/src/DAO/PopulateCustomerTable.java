package DAO;


import Model.Customer;
import javafx.collections.ObservableList;

import java.sql.SQLException;

@FunctionalInterface
public interface PopulateCustomerTable {
    void populateTable(ObservableList<Customer> list) throws SQLException;
}
