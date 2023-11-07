package controller.PreLoginControllers;

import controller.BaseControllers.Controller;
import controller.ProfileControllers.HomeLocationController;
import controller.ResourcesAccess.Resources;
import model.Dao.FundamentalClasses.UserDao;
import model.FundamentalClasses.User;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**Class controlling all the methods on the FXML view of the register window
 *
 */
public class RegisterMenuController extends Controller {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passField;
    @FXML
    private PasswordField confirmPassField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField emailField;
    @FXML
    private CheckBox vegetarianCheckBox;
    private final UserDao userDao = UserDao.getInstance();
    private User user;

    /**Method checking the prerequisites and switching to the main menu window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     * @throws SQLException database exception
     */
    public void onRegisterButtonPressed(ActionEvent actionEvent) throws IOException, SQLException {
        double defaultLatitude = 50.850346;     // Coordintaes of Brussel centrum by default
        double defaultLongitude = 4.351721;

        if (!Objects.equals(passField.getText(), confirmPassField.getText())) {return;}
        if (Objects.equals(usernameField.getText(), "")) {return;}
        if (Objects.equals(passField.getText(), "")) {return;}
        if (Objects.equals(confirmPassField.getText(), "")) {return;}
        if (Objects.equals(nameField.getText(), "")) {return;}
        if (Objects.equals(surnameField.getText(), "")) {return;}
        if (Objects.equals(emailField.getText(), "")) {return;}
        user = userDao.create(nameField.getText(),
               passField.getText(),
               surnameField.getText(),
               usernameField.getText(),
               emailField.getText(),
               vegetarianCheckBox.isSelected(),
                defaultLatitude,
                defaultLongitude);
        if (user != null) {
            goToHomeLocationPickerMenu(actionEvent, user);
        }
    }

    /**Method switching back to the startup window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onGoBackButtonPressed(ActionEvent actionEvent) throws IOException {
        changeStageWithoutLoads(actionEvent, Resources.STARTMENU.toString());
    }

    /**FXML method
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     * @throws SQLException database exception
     */
    public void checkEnter(ActionEvent actionEvent) throws IOException, SQLException {
        onRegisterButtonPressed(actionEvent);
    }

    /**User getter
     * @return User attribute
     */
    public User getUser() {return user;}

    /**Method changing the window to the home location picker menu
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @param userToSet User sent to the main menu controller
     * @throws IOException exception thrown by never existing file
     */
    public void goToHomeLocationPickerMenu(ActionEvent actionEvent, User userToSet) throws IOException {
        FXMLLoader loader = setRoot(Resources.HOMELOCATIONMENU.toString());
        HomeLocationController homeLocationController = loader.getController();
        homeLocationController.loadUser(userToSet);
        homeLocationController.setEngine();
        homeLocationController.setupMap();
        setStage(actionEvent);
    }

}
