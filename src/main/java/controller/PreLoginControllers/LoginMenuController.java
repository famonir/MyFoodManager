package controller.PreLoginControllers;

import controller.BaseControllers.Controller;
import controller.ResourcesAccess.Resources;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Dao.FundamentalClasses.UserDao;
import model.FundamentalClasses.User;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import controller.MainMenuControllers.MainMenuController;

/**
 * Class controlling all the methods on the FXML view of the login window
 */
public class LoginMenuController extends Controller {
    private final UserDao userDao = UserDao.getInstance();
    private User user = null;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPassField;

    /**
     * Method triggering the login section and the jump to main window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onLoginButtonPressed(ActionEvent actionEvent) throws IOException {
        if (Objects.equals(usernameTextField.getText(), "")) {
            return;
        }
        if (Objects.equals(passwordPassField.getText(), "")) {
            return;
        }
        user = userDao.login(usernameTextField.getText(), passwordPassField.getText());
        if (user != null) {
            goToMainMenu(actionEvent, user);
        } else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Information Incorrect");
            alert.setHeaderText("Wrong username and/or password");
            alert.setContentText("Please try again\n");
            alert.showAndWait();
        }
    }

    /**
     * Method switching back to the startup Window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onGoBackButtonPressed(ActionEvent actionEvent) throws IOException {
        changeStageWithoutLoads(actionEvent, Resources.STARTMENU.toString());
    }

    /**
     * Method to pressed button when passord is forget
     * @throws IOException exception thrown by never existing file
     */
    public void onForgotPasswordButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.PASSWORDMENU.toString()));
        Parent parent = loader.load();
//        ForgottenPasswordController forgottenPasswordController = loader.getController();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Forgot Password");
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * FXML action method
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void checkEnter(ActionEvent actionEvent) throws IOException {
        onLoginButtonPressed(actionEvent);
    }

    /**
     * User Getter
     * @return User attribute of the Controller
     */
    public User getUser() {return user;}

    /**
     * Method switching the window and loading its components
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @param userToSet User attribute sent to the Main Menu controller
     * @throws IOException exception thrown by never existing file
     */
    public void goToMainMenu(ActionEvent actionEvent, User userToSet) throws IOException {
        FXMLLoader loader = setRoot(Resources.MAINMENU.toString());
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.loadUser(userToSet);
        setStage(actionEvent);
    }
}
