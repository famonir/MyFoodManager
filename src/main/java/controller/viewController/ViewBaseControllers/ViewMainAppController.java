package controller.viewController.ViewBaseControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ViewMainAppController {
    @FXML
    private Button storeButton;
    @FXML
    private Button mapButton;
    @FXML
    private Button shoppingListButton;
    @FXML
    private Button archivesButton;
    @FXML
    private Button recipesButton;
    @FXML
    private Button weekPlanningButton;

    private ViewMainAppControllerListener listener;
    private static final String selectedButtonSettings = "-fx-background-color : rgba(0, 200, 256, 1);";

    public void setListener(ViewMainAppControllerListener listener) {
        this.listener = listener;
    }

    public void onArchivesPressed(ActionEvent event) {
        archivesButton.setStyle(selectedButtonSettings);
        listener.onArchivesPressed();
    }

    public void onMapPressed(ActionEvent event) {
        mapButton.setStyle(selectedButtonSettings);
        listener.onMapPressed();
    }

    public void onShoppingListPressed(ActionEvent event) {
        shoppingListButton.setStyle(selectedButtonSettings);
        listener.onShoppingListPressed();
    }

    public void onStoresPressed(ActionEvent event) {
        storeButton.setStyle(selectedButtonSettings);
        listener.onStoresPressed();
    }

    public void onRecipesPressed(ActionEvent event) {
        recipesButton.setStyle(selectedButtonSettings);
        listener.onRecipesPressed();
    }

    public void onPlanningPressed(ActionEvent event) {
        weekPlanningButton.setStyle(selectedButtonSettings);
        listener.onPlanningPressed();
    }

    public void onMainMenuPressed(ActionEvent event) {
        listener.onMainMenuPressed();
    }

    public void onMyProfilePressed(ActionEvent event) {
        listener.onMyProfilePressed();
    }
    public void onHelpPressed(ActionEvent event) {
        listener.onHelpPressed();
    }

    public interface ViewMainAppControllerListener {
        void onArchivesPressed();
        void onShoppingListPressed();
        void onRecipesPressed();
        void onPlanningPressed();
        void onStoresPressed();
        void onMapPressed();
        void onMainMenuPressed();
        void onMyProfilePressed();
        void onHelpPressed();
    }
}
