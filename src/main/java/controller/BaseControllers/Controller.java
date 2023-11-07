package controller.BaseControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Abstract class of controller
 */
public class Controller {
    private Parent root;

    /**
     * Method setting the content of the window
     * @param path path to the next window
     * @return FXMLLoader of the new window
     * @throws IOException exception thrown by never existing file
     */
    protected FXMLLoader setRoot(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        root = loader.load();
        return loader;
    }

    /**
     * Stage getter
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @return Stage (window)
     */
    protected Stage getStage(ActionEvent actionEvent) {
        return (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
    }

    /**
     * Stage Setter
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    protected void setStage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    /**
     * Method switching to a window without any loadings
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @param path path to the next window
     * @throws IOException exception thrown by never existing file
     */
    protected void changeStageWithoutLoads(ActionEvent actionEvent, String path) throws IOException {
        setRoot(path);
        setStage(actionEvent);
    }
}
