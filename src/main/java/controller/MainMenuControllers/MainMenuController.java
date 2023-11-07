package controller.MainMenuControllers;


import controller.BaseControllers.MainAppController;
import controller.ResourcesAccess.Resources;
import javafx.event.ActionEvent;
import java.io.IOException;

/**Public default constructor
 * Class controlling all the methods on the FXML view of the main menu window
 */
public class MainMenuController extends MainAppController {

    /**
     * Method logging out a user
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onLogOutButtonPressed(ActionEvent actionEvent) throws IOException {
        setRoot(Resources.STARTMENU.toString());
        setStage(actionEvent);
    }
}
