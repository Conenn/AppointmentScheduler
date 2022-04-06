package Controller;

import DAO.Query;
import DAO.ModelImpl;
import Model.Country;
import Model.Customer;
import Model.FirstLevelDivision;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateCustomer implements Initializable {

    Stage stage;
    Parent scene;

    Customer currentCustomer; //Will store current user object in scene

    @FXML
    private TextField addCustomerAddressTxt1;

    @FXML
    private TextField addCustomerNameTxt1;

    @FXML
    private TextField addCustomerPhoneTxt1;

    @FXML
    private TextField addCustomerZipTxt1;

    @FXML
    private ComboBox<Country> countryCombo;

    @FXML
    private ComboBox<FirstLevelDivision> divisionCombo;

    @FXML
    void onActionDisplayAppts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Display divisions corresponding with the selected country in the first combo box. */
    @FXML
    void onActionDisplayDivisions(ActionEvent event) throws SQLException {
        ModelImpl.setDivisions(countryCombo, divisionCombo);
    }

    @FXML
    void onActionUpdateCustomer(ActionEvent event) throws IOException, SQLException {
        FirstLevelDivision div = divisionCombo.getSelectionModel().getSelectedItem();  //Assign selected Division object to a variable to later get its Division_ID

        String name = addCustomerNameTxt1.getText();
        String address = addCustomerAddressTxt1.getText();
        String zip = addCustomerZipTxt1.getText();
        String phone = addCustomerPhoneTxt1.getText();
        int division_FK = div.getDivision_id();

        if (Query.updateCustomer(currentCustomer.getCustomer_id(), name, address, zip, phone, division_FK) > 0) //Call update method and print to console if successful or not
        {
            System.out.println("Update Successful");
        }
        else {
            System.out.println("Update Not Successful");
        }

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Sends customer data from main menu view and receives it in updateCustomer, used to prepopulate text fields and combo boxes. */
    public void sendCustomerData(Customer c) throws SQLException {
        currentCustomer = c;

        int divisionFK = c.getDivision_id_FK(); //Get division foreign key of Customer c
        int countryFK = 0; //Initialize country foreign key to 0 for later use

        addCustomerNameTxt1.setText(c.getCustomerName());
        addCustomerAddressTxt1.setText(c.getAddress());
        addCustomerPhoneTxt1.setText(c.getPhoneNumber());
        addCustomerZipTxt1.setText(c.getPostalCode());

        for (FirstLevelDivision div : ModelImpl.getAllDivisions())
        {
            if (divisionFK == div.getDivision_id())
            {
                divisionCombo.getSelectionModel().select(div); // Select the division corresponding to customer foreign key
                countryFK = div.getCountry_id_FK();
            }
        }
        countryCombo.getSelectionModel().select(countryFK - 1);

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            countryCombo.setItems(ModelImpl.getAllCountries()); //Populates combo box with available country selections
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

