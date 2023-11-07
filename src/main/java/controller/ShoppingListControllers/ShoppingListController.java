package controller.ShoppingListControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import controller.ResourcesAccess.Images;
import controller.ResourcesAccess.Resources;
import model.Dao.FundamentalClasses.ShoppingListDao;
import controller.BaseControllers.WithCrossController;
import model.FundamentalClasses.ShoppingList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Class controlling all the methods on the FXML view of the shopping list window
 */
public class ShoppingListController extends WithCrossController {
    private static final int LISTLABELWIDTH = 334;
    private Image editImage;
    private Image archiviseImage;
    private List<ShoppingList> shoppingLists = new ArrayList<>();
    private final List<ShoppingList> searchBarResults = new ArrayList<>();

    /**
     * Shopping list search field
     */
    @FXML
    private TextField shoppingListSearchField;
    /**
     * Shopping lists list view
     */
    @FXML
    private ListView<HBox> shoppingListsListView;
    /**
     * Vegeterian filter checkBox
     */
    @FXML
    private CheckBox vegetarianFilterCheckBox;
    /**
     * Fish filter checkBox
     */
    @FXML
    private CheckBox fishFilterCheckBox;

    /**
     * Method creating the new list insertion window
     * @throws IOException exception thrown by never existing file
     */
    public void addList() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.ADDLISTMENU.toString()));
        Parent parent = loader.load();
        NewShoppingListController newShoppingListController = loader.getController();
        newShoppingListController.setUser(user);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Shopping List");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        loadShoppingLists();
    }

    /**
     * Method setting up the archive button and its actions
     * @param archivise archivise button
     * @param  list shoppinglist
     * @param listHBox Hbox where the list is to be removed
     */
    private void setArchiviseButton(Button archivise, ShoppingList list, HBox listHBox) {
        archivise.setOnAction(e -> {
            try {
                list.setArchived(true);
            } catch (SQLException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
            shoppingListsListView.getItems().remove(listHBox);
        });
    }

    /**
     * Method srtting up the delete button and its actions
     * @param delete delete button
     * @param  list shoppinglist
     * @param listHBox Hbox where the list is to be removed
     */
    private void setDeleteButton(Button delete, ShoppingList list, HBox listHBox) {
        delete.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Shopping List");
            alert.setHeaderText("Are you sure you want to delete that shopping list?");
            alert.setContentText("It will never be accessible anymore.\nYou can still send it to archives.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    ShoppingListDao.getInstance().delete(list);
                } catch (SQLException throwables) {
                    new ExceptionAlertController().showExceptionWindow(throwables);
                }
                shoppingListsListView.getItems().remove(listHBox);
            }
        });
    }

    /**
     * Method setting up the Edit button and its actions
     * @param edit edit button
     * @param  list shoppinglist
     */
    private void setEditButton(Button edit, ShoppingList list) {
        edit.setOnAction(e -> {
            try {
                goToShoppingList(e, list);
            } catch (IOException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
        });
    }

    /**
     * Method creating the HBox of a list content
     * @param delete delete button of list
     * @param archivise archivise button of list
     * @param edit edit button of list
     * @param list the list from which data is retrieved
     * @param listHBox the HBox where the widgets are inserted
     */
    private void setActionButtons(Button delete, Button archivise, Button edit, ShoppingList list, HBox listHBox) {
        setDeleteButton(delete, list, listHBox);
        setArchiviseButton(archivise, list, listHBox);
        setEditButton(edit, list);
    }

    /**
     * Method setting the images of the controller
     */
    public void setImages() {
        String pathToEdit = Images.EDIT.toString();
        String pathToArchive = Images.SAVE.toString();
        try (FileInputStream editInput = new FileInputStream(pathToEdit);
             FileInputStream archiviseInput = new FileInputStream(pathToArchive)) {
            editImage = new Image(editInput);
            archiviseImage = new Image(archiviseInput);
        } catch (IOException ignored) {}
    }

    /**
     * Shopping list loader
     */
    public void loadShoppingLists() {
        shoppingListsListView.getItems().clear();
        shoppingLists = ShoppingListDao.getInstance().getAllUserShoppingLists(user.getID());
        setImages();
        searchShoppingList();
    }

    /**
     * Method setting up the image
     * @param image imageto be set up
     */
    private ImageView setupImage(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(gethBoxHeight()); imageView.setFitHeight(gethBoxHeight());
        return imageView;
    }

    /**
     * Method adding shopping lists to the list view
     * @param listsToAdd list to be added to the ListView
     */
    public void addListsToListView(List<ShoppingList> listsToAdd) {
        int listLabelWidth = LISTLABELWIDTH;
        for (ShoppingList list : listsToAdd) {
            Label name = new Label(list.getName());
            Label date = new Label(list.getDate());
            name.setPrefSize(listLabelWidth, gethBoxHeight());
            date.setPrefSize(listLabelWidth, gethBoxHeight());
            Button delete = setupButton(setupImage(getDeleteImage()));
            Button edit = setupButton(setupImage(editImage));
            Button archivise = setupButton(setupImage(archiviseImage));
            HBox listHBox = new HBox(name, date, edit, delete, archivise);
            setActionButtons(delete, archivise, edit, list, listHBox);
            shoppingListsListView.getItems().add(listHBox);
        }
    }

    /**
     * Method searching through the shopping lists depending on search bar
     */
    public void searchShoppingList() {
        shoppingListsListView.getItems().clear();
        searchBarResults.clear();
        boolean hasToBeVegetarian = vegetarianFilterCheckBox.isSelected();
        boolean hasToBeFish = fishFilterCheckBox.isSelected();
        for (ShoppingList list : shoppingLists) {
            if (!list.getName().toLowerCase(Locale.ROOT).contains(shoppingListSearchField.getText().toLowerCase(Locale.ROOT))) {continue;}
            if (hasToBeVegetarian && !list.checkVegetarian()) {continue;}
            if (hasToBeFish && !list.checkFish()) {continue;}
            searchBarResults.add(list);
        }
        addListsToListView(searchBarResults);
    }

    /**
     * Method going to the shopping list content
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @param selectedShoppingList shopping list to be opened
     * @throws IOException exception thrown by never existing file
     */
    public void goToShoppingList(ActionEvent actionEvent, ShoppingList selectedShoppingList) throws IOException {
        FXMLLoader loader = setRoot(Resources.LISTMENU.toString());
        ShoppingListIngredientsController shoppingListIngredientsController = loader.getController();
        shoppingListIngredientsController.loadUser(user);
        shoppingListIngredientsController.loadShoppingList(selectedShoppingList);
        shoppingListIngredientsController.loadSideBar(loader);
        setStage(actionEvent);
    }
}
