package controller.StoreControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Dao.Stores.StoreDao;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Controller of the window responsible for created of a new store
 */
public class NewStoreController extends ExceptionAlertController {
    @FXML
    private TextField storeNameTextField;

    /**
     * Method handling the confirmation after the press of the enter button
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws SQLException database exception
     */
    public void confirm(ActionEvent actionEvent) throws SQLException {
        if (Objects.equals(storeNameTextField.getText(), "")) {return;}
        StoreDao storeDao = StoreDao.getInstance();
        storeDao.create(storeNameTextField.getText());
        closeStage(actionEvent);
    }

    /**
     * Method handling the press of the back button
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void back(ActionEvent actionEvent) {
        closeStage(actionEvent);
    }

    /**
     * Method handling the press of the enter button
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws SQLException database exception
     */
    public void enterPressed(ActionEvent actionEvent) throws SQLException {
        confirm(actionEvent);
    }

    /**Method closing the window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    private void closeStage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        stage.close();
    }

    /**Stage getter
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @return stage (window) of actions
     */
    private Stage getStage(ActionEvent actionEvent) {
        Node source = (Node)  actionEvent.getSource();
        return (Stage) source.getScene().getWindow();
    }
}
