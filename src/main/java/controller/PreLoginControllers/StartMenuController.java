package controller.PreLoginControllers;

import controller.BaseControllers.Controller;
import controller.ResourcesAccess.Resources;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class controlling all the methods on the FXML view of the register window
 */
public class StartMenuController extends Controller {

    /**Method switching to the login window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onLoginButtonPressed(ActionEvent actionEvent) throws IOException {
        changeStageWithoutLoads(actionEvent, Resources.LOGINMENU.toString());
    }

    /**Method switching to the register window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onRegisterButtonPressed(ActionEvent actionEvent) throws IOException {
        changeStageWithoutLoads(actionEvent, Resources.REGISTERMENU.toString());
    }

    /**
     * Method switching to the help window (not existing yet)
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onHelpButtonPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(Resources.HELP.toString()));
        Scene scene = new Scene(fxmlLoader.load(), 900, 900);
        Stage stage = new Stage();
        stage.setTitle("Help Section");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.getStage(actionEvent).getScene().getWindow());
        stage.showAndWait();
    }
}
