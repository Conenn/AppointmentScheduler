package Controller;

import DAO.Query;
import DAO.ModelImpl;
import Model.Appointment;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class UpdateAppointment implements Initializable {
    Stage stage;
    Parent scene;

    Appointment currentAppointment;

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

    /** Gets called in previous scene to send appointment object from main menu to update form and uses the retrieved data to populate text fields and combo boxes. */
    public void sendAppointment(Appointment a) throws SQLException {
        currentAppointment = a;

        DateTimeFormatter dtfHours = DateTimeFormatter.ofPattern("HH");
        DateTimeFormatter dtfMinutes = DateTimeFormatter.ofPattern("mm");

        addAppointmentDescTxt.setText(a.getDescription());
        addAppointmentLocationTxt.setText(a.getLocation());
        addAppointmentTypeTxt.setText(a.getLocation());
        addAppointmentTitleTxt.setText(a.getTitle());

        LocalDate startDate = a.getStart().toLocalDateTime().toLocalDate();
        LocalDate endDate = a.getEnd().toLocalDateTime().toLocalDate();

        appointmentDatePicker.setValue(startDate);
        appointmentEndDatePicker.setValue(endDate);

        LocalTime startTime = a.getStart().toLocalDateTime().toLocalTime();
        LocalTime endTime = a.getEnd().toLocalDateTime().toLocalTime();

        appointmentHours.setValue(startTime.format(dtfHours));
        appointmentMinutes.setValue(startTime.format(dtfMinutes));

        appointmentEndHoursCombo.setValue(endTime.format(dtfHours));
        appointmentEndMinutesCombo.setValue(endTime.format(dtfMinutes));

        customerComboBox.getSelectionModel().select(Customer.getCustomerById(a.getCustomer_id()));
        userComboBox.getSelectionModel().select(User.getUserById(a.getUser_id()));
        contactCombo.getSelectionModel().select(Contact.getContactById(a.getContact_id()));
    }

    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /** Saves appointment by calling an insert sql query after getting data from text fields and combo boxes and running some logic checks. */
    @FXML
    void onActionSaveAppointment(ActionEvent event) throws IOException, SQLException {
        Contact contact = contactCombo.getSelectionModel().getSelectedItem();
        Customer customer = customerComboBox.getSelectionModel().getSelectedItem();
        User user = userComboBox.getSelectionModel().getSelectedItem();

        LocalDateTime startLdt = null;
        LocalDateTime endLdt = null;

        int contactID = contact.getContact_id();
        int customerId = customer.getCustomer_id();
        int userId = user.getId();

        if (appointmentDatePicker.getValue() == null)
        {
            System.out.println("No date picked");
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

        int id = currentAppointment.getAppointment_id();


        if (Helper.localToEST(startLdt).isBefore(MainMenu.startEST) || Helper.localToEST(endLdt).isAfter(MainMenu.endEST)) //Check if scheduled time is between opening and closing hour if not display error and re prompt user
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Schedule Appointments Between 8AM and 10PM");
            alert.showAndWait();
        }
        else if (Helper.appointmentStartTimes(currentAppointment).contains(startLdt))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Can't schedule overlapping appointments");
            alert.showAndWait();
        }
        else
        {
            if (Query.updateAppointment(id, title, description, location, type, startLdt, endLdt, customerId, userId, contactID) > 0)
            {
                System.out.println("Update Successful");
            }
            else {
                System.out.println("Update Not Successful");
            }
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            contactCombo.setItems(ModelImpl.getAllContacts()); //Populates contact combo box with contact objects
            userComboBox.setItems(ModelImpl.getAllUsers()); //Populates user combo box with user objects
            customerComboBox.setItems(ModelImpl.getAllCustomers()); //Populates customer combo box with customer objects
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Add an interval of hours and minutes into two separate observable array list and then display them in combo boxes
        hours.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.addAll("00", "15", "30", "45");
        appointmentHours.setItems(hours);
        appointmentMinutes.setItems(minutes);
        appointmentEndHoursCombo.setItems(hours);
        appointmentEndMinutesCombo.setItems(minutes);
    }
}
