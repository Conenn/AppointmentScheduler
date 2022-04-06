package Controller;

import DAO.PopulateAppointmentTable;
import DAO.Query;
import DAO.ModelImpl;
import Model.Appointment;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Label appointmentWarningLabel;

    @FXML
    private Label deletedAppointmentLabel;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;

    @FXML
    private TableView<Appointment> appointmentsTableView;

    @FXML
    private TableColumn<Appointment, Integer> contactCol;

    @FXML
    private TableColumn<Appointment, Integer> customerIdCol;

    @FXML
    private TableColumn<Appointment, String> descriptionCol;

    @FXML
    private TableColumn<Appointment, Time> endTimeCol;

    @FXML
    private TableColumn<Appointment, String> locationCol;

    @FXML
    private TableColumn<Appointment, Time> startTimeCol;

    @FXML
    private TableColumn<Appointment, String> titleCol;

    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<Appointment, Integer> userIdCol;

    @FXML
    private TableView<Appointment> appointmentsTableViewMonthly;

    @FXML
    private TableView<Appointment> appointmentsTableViewWeekly;

    //weekly
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColWeekly;

    @FXML
    private TableColumn<Appointment, Integer> contactColWeekly;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColWeekly;

    @FXML
    private TableColumn<Appointment, String> descriptionColWeekly;

    @FXML
    private TableColumn<Appointment, Time> endTimeColWeekly;

    @FXML
    private TableColumn<Appointment, String> locationColWeekly;

    @FXML
    private TableColumn<Appointment, Time> startTimeColWeekly;

    @FXML
    private TableColumn<Appointment, String> titleColWeekly;

    @FXML
    private TableColumn<Appointment, String> typeColWeekly;

    @FXML
    private TableColumn<Appointment, Integer> userIdColWeekly;

    //monthly
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColMonthly;

    @FXML
    private TableColumn<Appointment, Integer> contactColMonthly;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColMonthly;

    @FXML
    private TableColumn<Appointment, String> descriptionColMonthly;

    @FXML
    private TableColumn<Appointment, Time> endTimeColMonthly;

    @FXML
    private TableColumn<Appointment, String> locationColMonthly;

    @FXML
    private TableColumn<Appointment, Time> startTimeColMonthly;

    @FXML
    private TableColumn<Appointment, String> titleColMonthly;

    @FXML
    private TableColumn<Appointment, String> typeColMonthly;

    @FXML
    private TableColumn<Appointment, Integer> userIdColMonthly;

    public static LocalTime startEST = LocalTime.parse("08:00"); //Business opening hour
    public static LocalTime endEST = LocalTime.parse("22:00"); //Business closing hour

    PopulateAppointmentTable populateTable = (list) -> {
        appointmentsTableView.setItems(list);

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointment_id"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact_id"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
    };

    @FXML
    void onActionViewCustomers(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionAddAppointment(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionUpdateAppointment(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/UpdateAppointments.fxml"));
        loader.load();

        UpdateAppointment ADMcontroller = loader.getController();
        ADMcontroller.sendAppointment(appointmentsTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionDeleteAppointment(ActionEvent event) throws SQLException {
        Appointment a = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (Query.deleteAppointment(a.getAppointment_id()) > 0) {
            System.out.println("Delete successful");
            deletedAppointmentLabel.setText("Deleted Appointment: " + a.getAppointment_id());

            populateTable.populateTable((ModelImpl.getAllAppointments())); //refresh table view
        }
        else {
            System.out.println("Delete unsuccessful");
        }
    }


    @FXML
    void onActionDisplayReports(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionLogOut(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Using a lambda expression to populate my appointments table view showing all appointments upon initialization. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            for (Appointment a : ModelImpl.getAllAppointments()){

                LocalDateTime appointmentStart = a.getStart().toLocalDateTime(); //Turn appointment start time of current iteration to Local Date Time and store it in appointmentStart
                LocalDateTime now = LocalDateTime.now(); //store current local date time in variable now

                if (appointmentStart.isAfter(now) && appointmentStart.isBefore(now.plusMinutes(15))) //Check if there is an appointment within 15 minutes
                {
                    appointmentWarningLabel.setText("Appointment: " + a.getAppointment_id() + " Starting within 15 Minutes!");
                }
                else{
                    appointmentWarningLabel.setText("No Appointments Within the Next 15 Minutes");
                }
            }

            populateTable.populateTable((ModelImpl.getAllAppointments()));

            appointmentsTableViewMonthly.setItems(ModelImpl.getMonthlyAppointments());

            appointmentIdColMonthly.setCellValueFactory(new PropertyValueFactory<>("appointment_id"));
            descriptionColMonthly.setCellValueFactory(new PropertyValueFactory<>("description"));
            startTimeColMonthly.setCellValueFactory(new PropertyValueFactory<>("start"));
            endTimeColMonthly.setCellValueFactory(new PropertyValueFactory<>("end"));
            titleColMonthly.setCellValueFactory(new PropertyValueFactory<>("title"));
            typeColMonthly.setCellValueFactory(new PropertyValueFactory<>("type"));
            contactColMonthly.setCellValueFactory(new PropertyValueFactory<>("contact_id"));
            userIdColMonthly.setCellValueFactory(new PropertyValueFactory<>("user_id"));
            customerIdColMonthly.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
            locationColMonthly.setCellValueFactory(new PropertyValueFactory<>("location"));

            appointmentsTableViewWeekly.setItems(ModelImpl.getWeeklyAppointments());

            appointmentIdColWeekly.setCellValueFactory(new PropertyValueFactory<>("appointment_id"));
            descriptionColWeekly.setCellValueFactory(new PropertyValueFactory<>("description"));
            startTimeColWeekly.setCellValueFactory(new PropertyValueFactory<>("start"));
            endTimeColWeekly.setCellValueFactory(new PropertyValueFactory<>("end"));
            titleColWeekly.setCellValueFactory(new PropertyValueFactory<>("title"));
            typeColWeekly.setCellValueFactory(new PropertyValueFactory<>("type"));
            contactColWeekly.setCellValueFactory(new PropertyValueFactory<>("contact_id"));
            userIdColWeekly.setCellValueFactory(new PropertyValueFactory<>("user_id"));
            customerIdColWeekly.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
            locationColWeekly.setCellValueFactory(new PropertyValueFactory<>("location"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
