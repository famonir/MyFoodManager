package controller.RecipeControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.Recipe;
import model.Dao.FundamentalClasses.ProductDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**Class controlling all the methods on the FXML view of the new product in recipe window
 *
 */
public class ProductToRecipeController extends ExceptionAlertController {
    private List<model.FundamentalClasses.Product> allProducts = new ArrayList<>();
    private Recipe recipe;
    @FXML
    private ListView<String> productsListView;
    @FXML
    private TextField productSearchBar;
    @FXML
    private TextField quantityTextField;

    /**Method searching for all the products the user hasn't added yet
     * and for the products associated to his research
     *
     */
    public void searchProducts() {
        productsListView.getItems().clear();
        for (Product product : allProducts) {
            boolean alreadyPresent = false;
            for (model.FundamentalClasses.Product alreadyAddedProduct : recipe.getProducts()) {
                if (Objects.equals(alreadyAddedProduct.getName(), product.getName())) {
                    alreadyPresent = true;
                    break;
                }
            }
            if (!product.getName().toLowerCase(Locale.ROOT).contains(productSearchBar.getText().toLowerCase(Locale.ROOT))) {
                continue;
            }
            if (alreadyPresent) {
                continue;
            }
            productsListView.getItems().add(product.getName());
        }
    }

    /**Method confirming the product insertion and closing the window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void confirmAddProduct(ActionEvent actionEvent) {
        String productName = productsListView.getSelectionModel().getSelectedItem();
        if (productName == null) {return;}
        if (Objects.equals(quantityTextField.getText(), "")) {return;}
        try {
            int quantity = Integer.parseInt(quantityTextField.getText());
            model.FundamentalClasses.Product productToAdd = ProductDao.getInstance().get(productName);
            productToAdd.setQuantity(quantity);
            recipe.addProduct(productToAdd);

        } catch (Exception throwables) {
            showExceptionWindow(throwables);
            return;
        }
        closeStage(actionEvent);
    }

    /**Method called when the insertion is canceled
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void cancelAddProduct(ActionEvent actionEvent) {
        closeStage(actionEvent);
    }

    /**Stage getter
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @return Stage (window)
     */
    public Stage getStage(ActionEvent actionEvent) {
        Node source = (Node)  actionEvent.getSource();
        return (Stage) source.getScene().getWindow();
    }

    /**Method closing the stage
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void closeStage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        stage.close();
    }

    /**Method loading the products to the controller
     *
     */
    public void loadProducts() {
        productsListView.getItems().clear();
        allProducts = ProductDao.getInstance().getAll();
        searchProducts();
    }

    /**Method loading the recipe to the class
     * @param oldRecipe recipe loader to check if products are in the recipe already
     */
    public void loadShoppingList(Recipe oldRecipe) {this.recipe = oldRecipe;}
}
