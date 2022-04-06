package Controller;

import DAO.ModelImpl;
import Model.User;
import Utilities.Helper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class Login implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField passwordLoginTxt;

    @FXML
    private TextField userNameLoginTxt;

    @FXML
    private Label headerLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Label zoneIdLabelTxt;

    /** Variable to keep track of failed login attempts. */
    static int loginCount = 0;

    /** Date Time format used to format time for display. */
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");

    public static ResourceBundle rb = ResourceBundle.getBundle("Utilities/LoginToFrench_fr", Locale.getDefault());
    boolean loggedIn = false; //Set to true on successful login attempt

    /** If Username and Password text fields matches a user in database then login else display error message and record failed login attempts. */
    @FXML
    void onActionLoginBtn(ActionEvent event) throws SQLException, IOException {
        String username = userNameLoginTxt.getText();
        String password = passwordLoginTxt.getText();

        String filename = "login_activity.txt"; //Will keep a record of successful and unsuccessful login attempts

        FileWriter fileWriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);

        for (User user : ModelImpl.getAllUsers()) {
            if (user.getName().equals(username) && user.getPassword().equals(password))
            {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

                outputFile.println("Successful Log In Attempt At: " + LocalDateTime.now().format(dtf) + " From: " + ZoneId.systemDefault());
                loggedIn = true; //Set loggedIn to true if username and password input matches a user in the database

                break;
            }
            else {
                loggedIn = false; //loggedIn will be false if the loop ends with no user matching the username and password input
            }
        }

        if (!loggedIn && Locale.getDefault().getLanguage().equals("fr")) {
            Helper.errorDialog(rb.getString("Wrong_Password_and/or_Username"));
            outputFile.println("Unsuccessful Log In Attempt At: " + LocalDateTime.now().format(dtf) + " From: " + ZoneId.systemDefault());
            loginCount++;
        }
        else if (!loggedIn){
            Helper.errorDialog("Wrong Password and/or Username");
            outputFile.println("Unsuccessful Log In Attempt At: " + LocalDateTime.now().format(dtf) + " From: " + ZoneId.systemDefault());
            loginCount++;
            }
        outputFile.close(); //Close Login_activity.txt
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLoginTxt.setFocusTraversable(false);
        passwordLoginTxt.setFocusTraversable(false);
        loginButton.setFocusTraversable(false);
        try {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                userNameLoginTxt.setPromptText(rb.getString("Username"));
                passwordLoginTxt.setPromptText(rb.getString("Password"));
                loginButton.setText(rb.getString("Login"));
                headerLabel.setText(rb.getString("Appointment_Scheduler"));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        zoneIdLabelTxt.setText(ZoneId.systemDefault() + " " + LocalDateTime.now().format(dtf));
    }
}
