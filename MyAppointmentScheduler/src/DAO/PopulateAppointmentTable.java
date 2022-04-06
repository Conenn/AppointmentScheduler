package DAO;

import Model.Appointment;
import javafx.collections.ObservableList;
import java.sql.SQLException;

@FunctionalInterface
public interface PopulateAppointmentTable {
    void populateTable(ObservableList<Appointment> list) throws SQLException;
}
