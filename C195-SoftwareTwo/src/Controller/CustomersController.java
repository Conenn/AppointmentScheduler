package Controller;

import DAO.Query;
import DAO.ModelImpl;
import DAO.PopulateCustomerTable;
import Model.Customer;
import Utilities.Helper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableColumn<Customer, String> customerAddressCol;

    @FXML
    private TableColumn<Customer, Integer> customerDivisionIdCol;

    @FXML
    private TableColumn<Customer, Integer> customerIdCol;

    @FXML
    private TableColumn<Customer, String> customerNameCol;

    @FXML
    private TableColumn<Customer, String> customerPhoneCol;

    @FXML
    private TableColumn<Customer, String> customerZipCol;

    @FXML
    private TableView<Customer> customersTableView;

    @FXML
    private Label deletedCustomerLabel;

    PopulateCustomerTable populate = list -> {
        customersTableView.setItems(list);

        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        customerDivisionIdCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerZipCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
    };

    /** Populate table view with all customers using an lambda expression. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populate.populateTable(ModelImpl.getAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Deletes currently selected Customer object and uses an lambda expression to refresh table upon successful deletion. */
    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws SQLException {
        Customer c = customersTableView.getSelectionModel().getSelectedItem(); //Assign selected Customer object to c

        if (Helper.customerIdsByAppointment(c.getCustomer_id())) //Checks if there is an appointment with the customer id that is being deleted
            {
                Helper.errorDialog("Please delete customers appointment before deleting customer");
            }
        else {
            if (Query.deleteCustomer(c.getCustomer_id()) > 0) //Call delete method and print to console if successful or not
            {
                deletedCustomerLabel.setText("Deleted Customer: " + c.getCustomerName());
                System.out.println("Delete Successful");

                //Refreshes table after a customer is deleted
                populate.populateTable(ModelImpl.getAllCustomers());
            } else {
                Helper.errorDialog("Delete Not Successful");
            }
        }
    }

    /** Retrieves selected customer object from table view and sends data to update form and auto-populates text fields with customer data. */
    @FXML
    void onActionUpdateCustomer(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/UpdateCustomer.fxml"));
        loader.load();

        UpdateCustomer ADMcontroller = loader.getController();
        ADMcontroller.sendCustomerData(customersTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionViewCustomers(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
