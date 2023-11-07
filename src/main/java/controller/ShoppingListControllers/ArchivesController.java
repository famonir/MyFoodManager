package controller.ShoppingListControllers;

import controller.BaseControllers.WithCrossController;
import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.FundamentalClasses.ShoppingListDao;
import model.FundamentalClasses.ShoppingList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class controlling all the methods on the FXML view of the archives window
 */
public class ArchivesController extends WithCrossController {
    private static final int LISTLABELWITH = 812;

    private List<ShoppingList> archives;
    private final List<ShoppingList> searchBarResults = new ArrayList<>();
    @FXML
    private TextField shoppingListSearchField;
    @FXML
    private ListView<HBox> shoppingListsListView;

    /**
     * Method searching for the archives based on search bar
     */
    public void searchShoppingList() {
        shoppingListsListView.getItems().clear();
        searchBarResults.clear();
        for (ShoppingList list : archives) {
            if (!list.getDate().contains(shoppingListSearchField.getText())) {
                continue;
            }
            searchBarResults.add(list);
        }
        addArchivesToListView(searchBarResults);
    }

    /**
     * Method loading all the user's arhives
     */
    public void loadArchives() {
        shoppingListsListView.getItems().clear();
        archives = ShoppingListDao.getInstance().getUserArchives(user.getID());
        addArchivesToListView(archives);
    }

    /**
     * Method adding all the lists in the ListView
     * @param listToAdd list of archived shopping list based on search bar
     */
    public void addArchivesToListView(List<ShoppingList> listToAdd) {
        for (ShoppingList list : listToAdd) {
            Label name = new Label(list.getDate());
            name.setPrefSize(LISTLABELWITH, gethBoxHeight());
            ImageView deleteImgView = new ImageView(getDeleteImage());
            deleteImgView.setFitHeight(gethBoxHeight()); deleteImgView.setFitWidth(gethBoxHeight());
            Button delete = setupButton(deleteImgView);
            HBox listHBox = new HBox(name, delete);
            delete.setOnAction(e -> {
                try {
                    ShoppingListDao.getInstance().delete(list);
                } catch (SQLException throwables) {
                    new ExceptionAlertController().showExceptionWindow(throwables);
                }
                shoppingListsListView.getItems().remove(listHBox);
            });
            shoppingListsListView.getItems().add(listHBox);
        }
    }
}
