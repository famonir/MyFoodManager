package controller.RecipeControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import model.FundamentalClasses.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

/**Class controlling all the methods on the FXML view of the new recipe window
 *
 */
public class NewRecipeController extends ExceptionAlertController {
    private User user;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField dishTypeTextField;
    @FXML
    private TextField peopleCountTextField;

    /**FXML method
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void checkEnter(ActionEvent actionEvent) {
        confirmNewRecipe(actionEvent);
    }

    /**Method checking the prerequisites to add a new recipe
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void confirmNewRecipe(ActionEvent actionEvent) {
        if (Objects.equals(nameTextField.getText(), "")) {return;}
        if (Objects.equals(dishTypeTextField.getText(), "")) {return;}
        try {
            int peopleCount = Integer.parseInt(peopleCountTextField.getText());
            user.createRecipe(nameTextField.getText(), peopleCount, dishTypeTextField.getText());
            closeStage(actionEvent);
        } catch (Exception throwables) {
            showExceptionWindow(throwables);
        }
    }

    /**Method closing the stage when user cancels
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void cancelCreation(ActionEvent actionEvent) {
        closeStage(actionEvent);
    }

    /**User setter
     * @param userToSet User to set
     */
    public void setUser(User userToSet) {this.user = userToSet;}

    /**Method closing the window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void closeStage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        stage.close();
    }

    /**Stage getter
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @return stage (window) of actions
     */
    public Stage getStage(ActionEvent actionEvent) {
        Node source = (Node)  actionEvent.getSource();
        return (Stage) source.getScene().getWindow();
    }
}
