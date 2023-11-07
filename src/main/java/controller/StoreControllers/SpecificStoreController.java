package controller.StoreControllers;

import controller.BaseControllers.MainAppController;
import controller.MapControllers.Store;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Dao.Stores.StoreProductsDAO;
import model.FundamentalClasses.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller responsible for the store that is opened
 */
public class SpecificStoreController extends MainAppController {
    @FXML
    private Label shoppingListPriceLabel;
    @FXML
    private ListView<HBox> productsListView;
    @FXML
    private TextField searchBar;
    @FXML
    private Label storeNameLabel;
    private List<Product> allStoreProducts = new ArrayList<>();
    private final List<Product> productsToBeDisplayed = new ArrayList<>();
    private Store store;

    /**
     * Method loading the product list from the DB
     * @param storetoSet store refered to
     */
    public void loadProductListView (Store storetoSet) {
        this.store = storetoSet;
        setStoreNameLabel(storetoSet.getStoreName());
        if (storetoSet.getStorePriceForSL() == 0f) {
            getProducts();
        }
    }

    /**Method setting the price if it has a right value
     * @param shoppingListName the shopping list that is used
     */
    public void setShoppingListPriceLabel(String shoppingListName) {
        if (store.getStorePriceForSL() > 0.f) {
            shoppingListPriceLabel.setText(String.format("Price for %s : %s€", shoppingListName, store.getStorePriceForSL()));
            setListView();
        }
    }

    private void setListView() {
        List<Product> products = store.getProductsForSL();
        if (products != null) {
            addProductsFromSL(products);
        }
    }

    /**
     * Method getting the product list from the database
     */
    private void getProducts() {
        allStoreProducts.clear();
        StoreProductsDAO storeProductsDAO = StoreProductsDAO.getInstance();
        List<Product> products = storeProductsDAO.getAllProducts(storeNameLabel.getText());
        allStoreProducts = products;
        addProductsFromArray(products);
    }

    private void addProductsFromSL(List<Product> productsToBeDisplayed) {
        productsListView.getItems().clear();
        int squareWidth = 30;
        int nameWidth = 150;
        for (Product product : productsToBeDisplayed) {
            Label nameLabel = new Label(product.getName());
            nameLabel.setPrefSize(nameWidth, squareWidth);
            Label brandLabel = new Label(product.getBrand());
            brandLabel.setPrefSize(nameWidth, squareWidth);
            Label quantityLabel = new Label(product.getQuantity() + " x " + product.getPrice() + "€");
            quantityLabel.setPrefSize(nameWidth, squareWidth);
            Label priceLabel = new Label(( ((float) product.getQuantity()) * product.getPrice()) + "€");
            priceLabel.setPrefSize(nameWidth, squareWidth);
            HBox productHBox = new HBox(nameLabel, brandLabel, quantityLabel, priceLabel);
            productHBox.setAlignment(Pos.CENTER);
            productsListView.getItems().add(productHBox);
        }

    }

    /**
     * Method adding the products from the database
     * @param productsToBeDisplayed  products fetched from the db to be shown in the window
     */
    private void addProductsFromArray(List<Product> productsToBeDisplayed) {
        productsListView.getItems().clear();
        int nameWidth = 200;
        addProducts(nameWidth, productsListView, productsToBeDisplayed);
    }

    /**
     * Method setting the name of the store
     * @param storeName new name of the store
     */
    private void setStoreNameLabel(String storeName) {this.storeNameLabel.setText(storeName);}


    /**
     * Method dynamically searching the products in the product list
     */
    public void searchForProducts() {
        productsToBeDisplayed.clear();
        for (Product product : allStoreProducts) {
            if (!product.getName().toLowerCase(Locale.ROOT).contains(searchBar.getText().toLowerCase(Locale.ROOT))) {continue;}
            productsToBeDisplayed.add(product);
        }
        addProductsFromArray(productsToBeDisplayed);
    }
}
