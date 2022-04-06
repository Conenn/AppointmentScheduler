package Controller;

import DAO.ModelImpl;
import Model.Appointment;
import Model.Contact;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Reports implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomerIdCol;

    @FXML
    private TableColumn<Appointment, Time> appointmentEndCol;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;

    @FXML
    private TableColumn<Appointment, Time> appointmentStartCol;

    @FXML
    private TableColumn<Appointment, String> appointmentTitleCol;

    @FXML
    private TableColumn<Appointment, String> appointmentTypeCol;

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private ComboBox<String> allTypesComboBox;
    @FXML

    private ComboBox<String> allMonthsComboBox;

    @FXML
    private Label totalAppointmentNumber;

    @FXML
    private Label numberOfWeekendAppointments;

    @FXML
    private Label weekendAppointmentMessage;

    @FXML
    private TableView<Appointment> contactScheduleTableView;

    ObservableList<String> allMonths = FXCollections.observableArrayList();

    @FXML
    void onActionBackToAppointments(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Displays a chosen contacts appointment schedule. */
    public void onActionDisplayAppointments(ActionEvent event) throws SQLException {
        Contact c = contactComboBox.getSelectionModel().getSelectedItem();

        contactScheduleTableView.setItems(Helper.displayAppointmentsByContact(c));

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointment_id"));
        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
    }

    /** Displays total number of appointment corresponding to month selected in combo box. */
    @FXML
    void onActionTotalByMonth(ActionEvent event) throws SQLException {
        allTypesComboBox.getSelectionModel().clearSelection();
        String month = allMonthsComboBox.getSelectionModel().getSelectedItem();
        totalAppointmentNumber.setText(ModelImpl.totalAppointmentsByMonth(month) + " in " + month + " " + LocalDate.now().getYear());//Prints total appointments and month/year
    }

    /** Displays total number of appointments corresponding to type selected in combo box. */
    @FXML
    void onActionTotalByType(ActionEvent event) throws SQLException {
        String type = allTypesComboBox.getSelectionModel().getSelectedItem();
        int total = ModelImpl.totalAppointmentsByType(type);
        totalAppointmentNumber.setText(String.valueOf(total) + " " + type + " Appointment");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allMonths.addAll("January", "February", "March",
                "April", "May", "June",
                "July", "August", "September",
                "October", "November", "December");
        allMonthsComboBox.setItems(allMonths); //Add all Months as string to combo box

        try {
            numberOfWeekendAppointments.setText(String.valueOf(Helper.appointmentOnWeekend()));
            if (Helper.appointmentOnWeekend() > 0)
                weekendAppointmentMessage.setText("Take a break and have some fun!");
            else
                weekendAppointmentMessage.setText("Good for you!");

            allTypesComboBox.setItems(ModelImpl.allTypes());
            contactComboBox.setItems(ModelImpl.getAllContacts());//Initializes combo box with all available contacts
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
