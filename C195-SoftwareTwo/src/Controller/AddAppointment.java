package Controller;

import DAO.Query;
import DAO.ModelImpl;
import Model.Contact;
import Model.Customer;
import Model.User;
import Utilities.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddAppointment implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField addAppointmentDescTxt;

    @FXML
    private TextField addAppointmentLocationTxt;

    @FXML
    private TextField addAppointmentTitleTxt;

    @FXML
    private TextField addAppointmentTypeTxt;

    @FXML
    private DatePicker appointmentDatePicker;

    @FXML
    private DatePicker appointmentEndDatePicker;

    @FXML
    private ComboBox<String> appointmentEndHoursCombo;

    @FXML
    private ComboBox<String> appointmentEndMinutesCombo;

    @FXML
    private ComboBox<String> appointmentHours;

    @FXML
    private ComboBox<String> appointmentMinutes;

    @FXML
    private ComboBox<Contact> contactCombo;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private ComboBox<User> userComboBox;


    ObservableList<String> hours = FXCollections.observableArrayList();
    ObservableList<String> minutes = FXCollections.observableArrayList();

    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Saves appointment by calling an insert sql query after getting data from text fields and combo boxes and running some logic checks. */
    @FXML
    void onActionSaveAppointment(ActionEvent event) throws SQLException, IOException {
        LocalDateTime startLdt = null;
        LocalDateTime endLdt = null;

        int contactID = contactCombo.getSelectionModel().getSelectedItem().getContact_id();
        int customerId = customerComboBox.getSelectionModel().getSelectedItem().getCustomer_id();
        int userId = userComboBox.getSelectionModel().getSelectedItem().getId();

        if (appointmentDatePicker.getValue() == null) {
            Helper.errorDialog("Please pick a start and end date");
        }
        else {
            LocalDate date = appointmentDatePicker.getValue();
            String hour = appointmentHours.getValue();
            String minute = appointmentMinutes.getValue();
            LocalDate endDate = appointmentEndDatePicker.getValue();
            String endHour = appointmentEndHoursCombo.getValue();
            String endMinutes = appointmentEndMinutesCombo.getValue();
            startLdt = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minute));
            endLdt = LocalDateTime.of(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth(), Integer.parseInt(endHour), Integer.parseInt(endMinutes));
        }

        String title = addAppointmentTitleTxt.getText();
        String type = addAppointmentTypeTxt.getText();
        String description = addAppointmentDescTxt.getText();
        String location = addAppointmentLocationTxt.getText();

        //Check if scheduled time is between opening and closing hour if not display error and re-prompt user
        if (Helper.localToEST(startLdt).isBefore(MainMenu.startEST) || Helper.localToEST(endLdt).isAfter(MainMenu.endEST)) {
            Helper.errorDialog("Schedule Appointments Between 8AM and 10PM");
        }
        else if (Helper.appointmentStartTimes().contains(startLdt)) {
           Helper.errorDialog("Can't schedule overlapping appointments");
        }
        else {
            if (Query.createAppointment(title, description, location, type, startLdt, endLdt, customerId, userId, contactID) > 0) {
                System.out.println("Insert Successful");

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else {
                System.out.println("Insert Not Successful");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            contactCombo.setItems(ModelImpl.getAllContacts());
            userComboBox.setItems(ModelImpl.getAllUsers());
            customerComboBox.setItems(ModelImpl.getAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Add an interval of hours and minutes into two separate Observable array list and then display them in combo boxes
        hours.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.addAll("00", "15", "30", "45");
        appointmentHours.setItems(hours);
        appointmentMinutes.setItems(minutes);
        appointmentEndHoursCombo.setItems(hours);
        appointmentEndMinutesCombo.setItems(minutes);
    }
}
