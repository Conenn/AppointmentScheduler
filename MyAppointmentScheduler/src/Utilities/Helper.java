package Utilities;

import DAO.ModelImpl;
import Model.Appointment;
import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.sql.SQLException;
import java.time.*;

public class Helper {

    /** Turn a Local Date Time object into a Zoned Date Time in the EST time zone. */
    public static LocalTime localToEST(LocalDateTime time)
    {
        ZoneId origin = ZoneId.of(ZoneId.systemDefault().toString());
        ZoneId zoneID_EST = ZoneId.of("America/New_York");

        ZonedDateTime zdt = ZonedDateTime.of(time, origin);
        ZonedDateTime est = zdt.withZoneSameInstant(zoneID_EST);

        LocalTime estToLocal = est.toLocalTime();

        return estToLocal;
    }

    /** Adds all appointment starting times to an observable array list and returns the list excluding the appointment object was passed in as an argument. */
    public static ObservableList<LocalDateTime> appointmentStartTimes(Appointment appt) throws SQLException {
        ObservableList<LocalDateTime> startTimes = FXCollections.observableArrayList();

        for (Appointment a : ModelImpl.getAllAppointments()) {
            if (a.getAppointment_id() != appt.getAppointment_id()){
            startTimes.add(a.getStart().toLocalDateTime());
            }
        }
        return startTimes;
    }
    /** Adds all appointment starting times to an observable array list and returns the list. */
    public static ObservableList<LocalDateTime> appointmentStartTimes() throws SQLException {
        ObservableList<LocalDateTime> startTimes = FXCollections.observableArrayList();

        for (Appointment a : ModelImpl.getAllAppointments()) {
            startTimes.add(a.getStart().toLocalDateTime());
        }
        return startTimes;
    }

    /** Pops up an error dialog with the string argument as error message. */
    public static void errorDialog(String txt)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText(txt);
        alert.showAndWait();
    }


    /** Method used to see if a customer has an assigned appointment. */
    public static boolean customerIdsByAppointment(int id) throws SQLException {
        for (Appointment a : ModelImpl.getAllAppointments()) {
            if (a.getCustomer_id() == id) {
                return true;
            }
        }
        return false;
    }

    /** Gets all appointments for a given contact. */
    public static ObservableList<Appointment> displayAppointmentsByContact(Contact c) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        for (Appointment a : ModelImpl.getAllAppointments()) {
            if (c.getContact_id() == a.getContact_id()) {
                appointments.add(a);
            }
        }
        return appointments;
    }

    /** Returns total number of appointments that falls on a weekend. */
    public static int appointmentOnWeekend() throws SQLException {
        int appointmentsOnWeekend = 0;

        for (Appointment a : ModelImpl.getAllAppointments()) {
            String day = a.getStart().toLocalDateTime().getDayOfWeek().toString();
            //Check if an appointment is on either saturday or sunday
            if(day.equals("SATURDAY") || day.equals("SUNDAY")){
                appointmentsOnWeekend++;
            }
        }
        return appointmentsOnWeekend;
    }
}
