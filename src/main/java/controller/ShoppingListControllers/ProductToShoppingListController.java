package controller.ShoppingListControllers;

import model.Dao.FundamentalClasses.ProductDao;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ShoppingList;
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

/**
 * Class controlling all the methods on the FXML view of the product insertion window
 */
public class ProductToShoppingListController {
    private List<Product> allProducts = new ArrayList<>();
    private ShoppingList shoppingList;
    @FXML
    private ListView<String> productsListView;
    @FXML
    private TextField productSearchBar;
    @FXML
    private TextField quantityTextField;

    /**
     * Method searching for products based on research and already present products
     */
    public void searchProducts() {
        productsListView.getItems().clear();
        for (Product product : allProducts) {
            boolean alreadyPresent = false;
            for (Product alreadyAddedProduct : shoppingList.getProducts()) {
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

    /**
     * Method confirming the product insertion and closing the window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void confirmAddProduct(ActionEvent actionEvent) {
        String productName = productsListView.getSelectionModel().getSelectedItem();
        if (productName == null) {
            return;
        }
        if (Objects.equals(quantityTextField.getText(), "")) {
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityTextField.getText());
            Product newProduct = ProductDao.getInstance().get(productName);
            newProduct.setQuantity(quantity);
            shoppingList.addProduct(newProduct);

        } catch (Exception throwables) {return;}
        closeStage(actionEvent);
    }

    /**
     * Method closing the window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void cancelAddProduct(ActionEvent actionEvent) {
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
     * Method closing the window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void closeStage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        stage.close();
    }

    /**
     * Method loading the products of the database
     */
    public void loadProducts() {
        productsListView.getItems().clear();
        allProducts = ProductDao.getInstance().getAll();
        searchProducts();
    }

    /**
     * Method loading the shopping list to the controller
     * @param shoppingListToSet shopping list used to check the already present products
     */
    public void loadShoppingList(ShoppingList shoppingListToSet) {this.shoppingList = shoppingListToSet;}
}
