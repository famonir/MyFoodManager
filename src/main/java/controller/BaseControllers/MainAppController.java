package controller.BaseControllers;


import controller.MainMenuControllers.MainMenuController;
import controller.ProfileControllers.ProfileController;
import controller.RecipeControllers.RecipeController;
import controller.ResourcesAccess.Resources;
import controller.ShoppingListControllers.ArchivesController;
import controller.ShoppingListControllers.ShoppingListController;
import controller.StoreControllers.StoreController;
import controller.WeekPlanningControllers.WeekPlanController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import controller.MapControllers.MapController;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

/**Class controlling all the methods on the FXML view of the side bar
 *
 */
public class MainAppController extends Controller {
    /**Store button of the sidebar from the fxml file
     *
     */
    @FXML
    private Button storeButton;
    /** Map button of the sidebar from the fxml file
     *
     */
    @FXML
    private Button mapButton;
    /** Shopping List button of the sidebar from the fxml file
     *
     */
    @FXML
    private Button shoppingListButton;
    /** Archives button of the sidebar from the fxml file
     *
     */
    @FXML
    private Button archivesButton;
    /** Recipes button of the sidebar from the fxml file
     *
     */
    @FXML
    private Button recipesButton;
    /**Week planning button of the sidebar from the fxml file
     *
     */
    @FXML
    private Button weekPlanningButton;
    private static final String selectedButtonSettings = "-fx-background-color : rgba(0, 200, 256, 1);";
    /**
     * User object for the databse changes
     */
    protected User user;

    /**Method loading the side bar of the window
     * @param paneLoader FXMLLoader of the side bar
     * @throws IOException exception thrown by never existing file
     * @return side bar controller of the current window
     */
    public MainAppController loadSideBar(FXMLLoader paneLoader) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL path = getClass().getResource(Resources.SIDEBAR.toString());
        loader.setLocation(path);
        VBox sideBar = loader.load();
        MainAppController sideBarController = loader.getController();
        sideBarController.loadUser(user);
        Pane pane = paneLoader.getRoot();
        pane.getChildren().add(sideBar);
        return sideBarController;
    }

    /**User loader
     * @param userToSet User to be loaded
     */
    public void loadUser(User userToSet) {this.user = userToSet;}

    /**Method Switching to the Shopping Lists window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onShoppingListPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.SHOPPINGMENU.toString());
        ShoppingListController shoppingListController = loader.getController();
        shoppingListController.loadUser(user);
        MainAppController sideBarController = shoppingListController.loadSideBar(loader);
        sideBarController.shoppingListButton.setStyle(selectedButtonSettings);
        shoppingListController.loadShoppingLists();
        setStage(actionEvent);
    }

    /**Method Switching to the Archives window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onArchivesPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.ARCHIVESMENU.toString());
        ArchivesController archivesController = loader.getController();
        archivesController.loadUser(user);
        MainAppController sideBarController = archivesController.loadSideBar(loader);
        sideBarController.archivesButton.setStyle(selectedButtonSettings);
        archivesController.loadArchives();
        setStage(actionEvent);
    }

    /**Method Switching to the Recipes window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onRecipesPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.RECIPESMENU.toString());
        RecipeController recipeController = loader.getController();
        recipeController.loadUser(user);
        MainAppController sideBarController = recipeController.loadSideBar(loader);
        sideBarController.recipesButton.setStyle(selectedButtonSettings);
        recipeController.loadRecipes();
        setStage(actionEvent);
    }

    /**Method Switching to the Main Menu window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onMainMenuPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.MAINMENU.toString());
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.loadUser(user);
        setStage(actionEvent);
    }

    /**Method Switching to the Profile window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onMyProfilePressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.PROFILEMENU.toString());
        ProfileController profileController = loader.getController();
        profileController.loadUser(user);
        profileController.loadSideBar(loader);
        profileController.init();
        setStage(actionEvent);
    }

    /**Method Switching to the Week Planning window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     * @throws SQLException database exception
     */
    public void onPlanningPressed(ActionEvent actionEvent) throws IOException, SQLException {
        if (user.getUsersRecipes().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Recipes");
            alert.setHeaderText("You do not have any recipes registered.");
            alert.setContentText("Please add some recipes to your account in order to move to this section.");
            alert.showAndWait();
        } else {
            FXMLLoader loader = setRoot(Resources.PLANNINGMENU.toString());
            WeekPlanController weekPlanController = loader.getController();
            weekPlanController.loadUser(user);
            MainAppController sideBarController = weekPlanController.loadSideBar(loader);
            sideBarController.weekPlanningButton.setStyle(selectedButtonSettings);
            weekPlanController.loadDaysLabels();
            setStage(actionEvent);
        }
    }

    /**
     * Method handling the actions to be taken when map is pressed
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onMapPressed(ActionEvent actionEvent) throws IOException{
        FXMLLoader loader = setRoot(Resources.MAP.toString());
        MapController mapController = loader.getController();
        mapController.loadUser(user);
        MainAppController sideBarController = mapController.loadSideBar(loader);
        sideBarController.mapButton.setStyle(selectedButtonSettings);
        mapController.setupMap();
        setStage(actionEvent);
    }

    /**
     * Method handling the actions to be taken when Stores is pressed
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void onStoresPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.STORES.toString());
        StoreController storeController = loader.getController();
        storeController.setStoresView();
        storeController.loadUser(user);
        MainAppController sideBarController = storeController.loadSideBar(loader);
        sideBarController.storeButton.setStyle(selectedButtonSettings);
        setStage(actionEvent);
    }

    /**Method creating the help window
     * @param actionEvent action triggering the help method
     * @throws IOException exception whenever the fxml file is not found
     */
    public void onHelpPressed(ActionEvent actionEvent) throws IOException {
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

    /**
     * Method adding products to the store
     * @param nameWidth width of the name (for the label)
     * @param storeProductsListView listView for the store products
     * @param storeProducts list of products
     */
    protected void addProducts(int nameWidth, ListView<HBox> storeProductsListView, List<Product> storeProducts) {
        int squareWidth = 30;
        for (Product product : storeProducts) {
            Label nameLabel = new Label(product.getName());
            nameLabel.setPrefSize(nameWidth, squareWidth);
            Label brandLabel = new Label(product.getBrand());
            brandLabel.setPrefSize(nameWidth, squareWidth);
            String quantity = (product.getQuantity() == 1) ? "" : String.valueOf(product.getQuantity());
            Label priceLabel = new Label(product.getPrice() + "€/" + quantity + product.getUnits());
            priceLabel.setPrefSize(nameWidth, squareWidth);
            HBox productHBox = new HBox(nameLabel, brandLabel, priceLabel);
            productHBox.setAlignment(Pos.CENTER);
            storeProductsListView.getItems().add(productHBox);
        }
    }


}
