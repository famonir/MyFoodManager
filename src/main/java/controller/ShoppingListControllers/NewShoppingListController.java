package controller.ShoppingListControllers;

import model.Dao.FundamentalClasses.ShoppingListDao;
import model.FundamentalClasses.ShoppingList;
import model.FundamentalClasses.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Class controlling all the methods on the FXML view of the new shopping list window
 */
public class NewShoppingListController {
    private User user;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField dateTextField;

    /**
     * Method confirming the list insertion and closing the window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws SQLException database exception
     */
    public void confirmNewList(ActionEvent actionEvent) throws SQLException {
        if (Objects.equals(nameTextField.getText(), "")) {
            return;
        }
        if (Objects.equals(dateTextField.getText(), "")) {
            return;
        }
        ShoppingListDao.getInstance().create(new ShoppingList(0, nameTextField.getText(), user.getID(), dateTextField.getText(), false));
        closeStage(actionEvent);
    }

    /**
     * Stage getter
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @return Stage (window)
     */
    public Stage getStage(ActionEvent actionEvent) {
        Node source = (Node)  actionEvent.getSource();
        return (Stage) source.getScene().getWindow();
    }

    /**
     * Method canceling the insertion
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void cancelCreation(ActionEvent actionEvent) {
        closeStage(actionEvent);
    }

    /**
     * User setter
     * @param oldUser User to set
     */
    public void setUser(User oldUser) {this.user = oldUser;}

    /**
     * Method closing the window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void closeStage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        stage.close();
    }

    /**
     * Method trigerred by FXML widgets
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws SQLException database exception
     */
    public void checkEnter(ActionEvent actionEvent) throws SQLException {
        confirmNewList(actionEvent);
    }
}
