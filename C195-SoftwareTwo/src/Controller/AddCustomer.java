package Controller;

import DAO.Query;
import DAO.ModelImpl;
import Model.Country;
import Model.FirstLevelDivision;
import Utilities.Helper;
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
import java.util.ResourceBundle;

public class AddCustomer implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField addCustomerAddressTxt;

    @FXML
    private TextField addCustomerNameTxt;

    @FXML
    private TextField addCustomerPhoneTxt;

    @FXML
    private TextField addCustomerZipTxt;

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

    /** Retrieves combo box and text field input and inserts a new customer into database. */
    public void onActionSaveCustomer(ActionEvent event) throws SQLException, IOException {

        FirstLevelDivision div = divisionCombo.getSelectionModel().getSelectedItem();  //Assign selected Division object to a variable to later get its Division_ID

        try {
            String name = addCustomerNameTxt.getText();
            String address = addCustomerAddressTxt.getText();
            String zip = addCustomerZipTxt.getText();
            String phone = addCustomerPhoneTxt.getText();
            int division_FK = div.getDivision_id();

            if (Query.createCustomer(name, address, zip, phone, division_FK) > 0) //Call insert method and print to console if successful or not
            {
                System.out.println("Insert Successful");

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            } else {
                System.out.println("Insert Not Successful");
            }
        }catch (NumberFormatException | NullPointerException e){
            Helper.errorDialog("Please make a choice in both combo boxes and/or input correct values in fields");
            System.out.println(e);
        }
    }

    /** Display divisions corresponding with the selected country in the first combo box. */
    public void onActionDisplayDivisions(ActionEvent event) throws SQLException {
        ModelImpl.setDivisions(countryCombo, divisionCombo);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            countryCombo.setItems(ModelImpl.getAllCountries()); //Populates combo box with available country selections
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
