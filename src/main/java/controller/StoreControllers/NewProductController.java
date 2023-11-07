package controller.StoreControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Dao.FundamentalClasses.ProductDao;
import model.Dao.Stores.StoreDao;
import model.Dao.Stores.StoreProductsDAO;
import model.FundamentalClasses.Product;

import java.util.List;
import java.util.Objects;

/**
 * Controller responsible for the window creating a new product to a store
 */
public class NewProductController extends ExceptionAlertController {

    @FXML
    private TextField priceTextField;
    @FXML
    private ComboBox<String> productsComboBox;
    @FXML
    private TextField unitsTextField;
    @FXML
    private TextField brandTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private Label storeLabel;

    /**
     * method loading the products list for the new store
     * @param storeName name of the store
     */
    public void loadContent(String storeName) {
        storeLabel.setText(storeName);
        loadProducts();
    }

    /**
     * method loading the available products list
     */
    public void loadProducts() {
        ProductDao productDao = ProductDao.getInstance();
        productsComboBox.getItems().clear();
        List<Product> allProducts = productDao.getAll();
        for (Product product : allProducts) {
            productsComboBox.getItems().add(product.getName());
        }
    }

    /**
     * Method handling the press of the enter button
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void enterPressed(ActionEvent actionEvent) {
        confirm(actionEvent);
    }

    /**
     * Method handling the confirmation after the press of the enter button
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void confirm(ActionEvent actionEvent) {
        float price;
        int quantity;
        if (Objects.equals(priceTextField.getText(), "")) {return;}
        if (Objects.equals(unitsTextField.getText(), "")) {return;}
        if (Objects.equals(brandTextField.getText(), "")) {return;}
        if (Objects.equals(quantityTextField.getText(), "")) {return;}
        if (productsComboBox.getSelectionModel().getSelectedItem() == null || Objects.equals(productsComboBox.getSelectionModel().getSelectedItem(), "Product")) {
            popupNoProductSelected();
            return;}
        try {
            price = Float.parseFloat(priceTextField.getText());
            quantity = Integer.parseInt(quantityTextField.getText());
            Product productToAdd = new Product(0, productsComboBox.getSelectionModel().getSelectedItem(), "", quantity, unitsTextField.getText());
            productToAdd.setPrice(price);
            productToAdd.setBrand(brandTextField.getText());
            productToAdd.setID(ProductDao.getInstance().get(productsComboBox.getSelectionModel().getSelectedItem()).getID());
            int storeId = StoreDao.getInstance().getIdFromName(storeLabel.getText());
            StoreProductsDAO.getInstance().create(productToAdd, storeId);
            closeStage(actionEvent);
        } catch (Exception throwables) {
            popupWrongInputs();
        }
    }

    /**
     * Creates a pop up window to warn the user that he has not selected a product
     */
    private void popupNoProductSelected() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error while adding product");
        alert.setHeaderText("Please select the product to be added");
        alert.setContentText("Make sure that you have selected a product in the top right corner.\n");
        alert.showAndWait();
    }

    /**
     * Creates a pop up window to warn the user that his inputs are not correct
     */
    private void popupWrongInputs() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error while adding product");
        alert.setHeaderText("Please check your inputs format.");
        alert.setContentText("""
                Make sure that the price is composed of numbers separated by a decimal point.
                Make sure that units are only letters.
                Make sure that the brand name isn't empty.
                Make sure that the quantity is an integer.
                                
                """);
        alert.showAndWait();
    }

    /**
     * Method handling the press of the back button
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void back(ActionEvent actionEvent) {
        closeStage(actionEvent);
    }

    /**Method closing the window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void closeStage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        stage.close();
    }

    /**Stage getter
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @return stage (window) of actions
     */
    public Stage getStage(ActionEvent actionEvent) {
        Node source = (Node)  actionEvent.getSource();
        return (Stage) source.getScene().getWindow();
    }
}
