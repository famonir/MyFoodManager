package controller.StoreControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Dao.Stores.StoreProductsDAO;
import model.FundamentalClasses.Product;

import java.util.Objects;

/**
 * Controller responsible for editing a product of a store
 */
public class EditProductController {
    /** Price text field of the fxml file
     *
     */
    @FXML
    private TextField priceTextField;
    /** Brand text field of the fxml file
     *
     */
    @FXML
    private TextField brandTextField;
    /** Quantity text field of the fxml file
     *
     */
    @FXML
    private TextField quantityTextField;
    /** Store label of the fxml file
     *
     */
    @FXML
    private Label storeLabel;
    /** Product label of the fxml file
     *
     */
    @FXML
    private Label productLabel;
    Product productToEdit;

    /**
     * method handling the enter action
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void enterPressed(ActionEvent actionEvent) {
        confirm(actionEvent);
    }

    /**
     * method handling the confirmation when enter is pressed, Product is edited in database
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void confirm(ActionEvent actionEvent) {
        float price;
        int quantity;
        if (Objects.equals(priceTextField.getText(), "")) {return;}
        if (Objects.equals(brandTextField.getText(), "")) {return;}
        if (Objects.equals(quantityTextField.getText(), "")) {return;}
        try {
            price = Float.parseFloat(priceTextField.getText());
            quantity = Integer.parseInt(quantityTextField.getText());
            productToEdit.setPrice(price);
            productToEdit.setBrand(brandTextField.getText());
            productToEdit.setQuantity(quantity);
            StoreProductsDAO.getInstance().update(productToEdit);
            closeStage(actionEvent);
        } catch (Exception throwables) {
            wrongInputsPopup();
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**
     * Creates a pop up window to warn the user that he has not selected a product
     */
    private void wrongInputsPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error while modifying product");
        alert.setHeaderText("Please check your inputs format.");
        alert.setContentText("""
                Make sure that the price is composed of numbers separated by a decimal point.
                Make sure that the brand name isn't empty.
                Make sure that the quantity is an integer.
                                
                """);
        alert.showAndWait();
    }

    /**
     * method handling the press of the back button
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void back(ActionEvent actionEvent) {
        closeStage(actionEvent);
    }

    /**
     * method loading the content of the page to be shown when product edit is clicked
     * @param product product to be edited
     * @param storeName name of the store
     */
    public void loadContent(Product product, String storeName) {
        productToEdit = product;
        storeLabel.setText(storeName);
        productLabel.setText(product.getName());
        priceTextField.setText(String.valueOf(product.getPrice()));
        brandTextField.setText(product.getBrand());
        quantityTextField.setText(String.valueOf(product.getQuantity()));
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
