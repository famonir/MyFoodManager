package controller.ProfileControllers;

import controller.BaseControllers.MainAppController;
import controller.ResourcesAccess.Resources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

/**Class controlling all the methods on the FXML view of the profile window
 *
 */
public class ProfileController extends MainAppController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private TextField newPasswordField;
    @FXML
    private Button saveNewPasswordButton;
    @FXML
    private CheckBox isVegetarianCheckbox;

    /**
     * Initialise the different buttons
     */
    public void init(){
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getName());
        surnameLabel.setText(user.getSurname());
        saveNewPasswordButton.setStyle("-fx-border-color: black;  -fx-border-width :1 1 1 1");
        isVegetarianCheckbox.setSelected(user.getVegetarian());
    }

    /**
     * Changes the user diet in database when the checkbox is (un)selected
     */
    public void onCheckBoxClicked() {
        user.setVegetarian(isVegetarianCheckbox.isSelected());
    }

    /**
     * Changes the user password in database when the button is clicked.
     * Button borders change depending on the success of the action.
     */
    public void onSaveNewPasswordPressed() {
        String newPassword;
        newPassword = newPasswordField.getText();
        if (Objects.equals(newPassword, "")){
            saveNewPasswordButton.setStyle("-fx-border-color: red; -fx-border-width :4 4 4 4");
        }
        else {
            user.setPassword(newPassword);
            newPasswordField.clear();
            saveNewPasswordButton.setStyle("-fx-border-color: green; -fx-border-width :4 4 4 4");
        }
    }

    /**Method changing the window to the home location picker menu
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onSaveHomeAddressPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.HOMELOCATIONMENU.toString());
        HomeLocationController homeLocationController = loader.getController();
        homeLocationController.loadUser(user);
        homeLocationController.setEngine();
        homeLocationController.setupHomeChange();
        setStage(actionEvent);
    }
}
