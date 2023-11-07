package controller.ProfileControllers;
import controller.BaseControllers.MainAppController;
import controller.MainMenuControllers.MainMenuController;
import controller.ResourcesAccess.Resources;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.IOException;
import java.net.URL;

/**
 * Controller of the home address map. Used to set the address during registration and allows users to change their address later in their profile.
 * Uses an html of the map and a js for the actions on the map.
 */
public class HomeLocationController extends MainAppController {
    @FXML
    private WebView homeView;
    private static final double latitudeBrussels = 50.850346;
    private static final double longitudeBrussels = 4.351721;
    WebEngine engine;

    /**
     * Defines the engine for the WebView
     */
    public void setEngine() {
        this.engine = homeView.getEngine();
        engine.load("https://www.google.com/maps");
    }

    /**
     * Method that sets up the map to be displayed and calls the first javascript method
     */
    public void setupMap() {
        final URL urlGoogleMaps = getClass().getResource(Resources.HOMELOCATIONMAPHTML.toString());
        assert urlGoogleMaps != null;
        engine.setJavaScriptEnabled(true);
        engine.load(urlGoogleMaps.toExternalForm());
        zoomOnBrussels(engine);
    }

    /**
     * Method that sets up the map to be displayed and calls the home change method in javascript
     */
    public void setupHomeChange() {
        final URL urlGoogleMaps = getClass().getResource(Resources.HOMELOCATIONMAPHTML.toString());
        assert urlGoogleMaps != null;
        engine.setJavaScriptEnabled(true);
        engine.load(urlGoogleMaps.toExternalForm());
        locateCurrentHome(engine);
    }

    /**
     * Calls method locateHome in the javascript file
     * @param engine web engine for the connection
     */
    private void locateCurrentHome(WebEngine engine) {
        engine.getLoadWorker().stateProperty().addListener((observableValue, state, t1) ->
                engine.executeScript(String.format("homeLocationMapHandler.locateHome(%s, %s)", user.getLatitude(), user.getLongitude())));
    }

    /**
     * Calls method zoomOnBrussels in the javascript file
     * @param engine web engine for the connection
     */
    private void zoomOnBrussels(WebEngine engine) {
        engine.getLoadWorker().stateProperty().addListener((observableValue, state, t1) ->
                engine.executeScript(String.format("homeLocationMapHandler.zoomOnCoordinates(%s, %s)", latitudeBrussels, longitudeBrussels)));
    }

    /**
     * Calls method getLatitude in the javascript file
     * @param engine web engine for the connection
     */
    private double getUserSelectedLatitude(WebEngine engine) {
        return (double) engine.executeScript("homeLocationMapHandler.getLatitude()");
    }

    /**
     * Calls method getLongitude in the javascript file
     * @param engine web engine for the connection
     */
    private double getUserSelectedLongitude(WebEngine engine) {
        return (double) engine.executeScript("homeLocationMapHandler.getLongitude()");
    }

    /**
     * Gets the coordinates from the map (javascript) and saves it in the database
     */
    private void saveUserCoordinates() {
        double userLatitude = getUserSelectedLatitude(engine);
        double userLongitude = getUserSelectedLongitude(engine);
        if (userLatitude != 0 && userLongitude != 0) {
            user.setLatitude(userLatitude);
            user.setLongitude(userLongitude);
        } else {
            sameAddressPopUp();
        }
    }

    /**
     * Pop up window to warn the user that the new address is the same as the old one
     */
    private void sameAddressPopUp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Address change");
        alert.setHeaderText("New address cannot be the same as the old one.");
        alert.setContentText("You will be redirected to the main menu.\n");
        alert.showAndWait();
    }

    /**Method changing the window to main menu after setting user coordinates
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onSavePressed(ActionEvent actionEvent) throws IOException {
        saveUserCoordinates();
        FXMLLoader loader = setRoot(Resources.MAINMENU.toString());
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.loadUser(user);
        setStage(actionEvent);
    }
}
