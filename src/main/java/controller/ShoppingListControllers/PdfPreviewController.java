package controller.ShoppingListControllers;

import controller.BaseControllers.WithCrossController;
import controller.ExceptionControllers.ExceptionAlertController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ProductPerList;
import model.Dao.FundamentalClasses.ProductsListDao;
import model.Dao.FundamentalClasses.ShoppingListDao;
import model.FundamentalClasses.ShoppingList;
import model.FundamentalClasses.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller pdf preview
 */
public class PdfPreviewController extends WithCrossController {
    private static final int LISTLABELWITH = 436;
    private static final int LISTQUANTITYFIELDWITH = 140;
    private static final int ACTIONBUTTONWIDTH = 245;
    private Image plusImage;
    private Image minusImage;
    private String lastQuantityInput;
    @FXML
    private TextField shoppingListNameTextField;
    @FXML
    private ListView<HBox> ingredientsListView;
    @FXML
    private Button returnButton;
    private ShoppingList shoppingList;

    /**
     * Method going back to the shopping lists window
     * @throws  IOException exception thrown by never existing file
     */
    public void back() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Method going back to the shopping lists window
     * @throws IOException thrown whenever could not go back to the previous fxml file
     */
    public void confirm() throws IOException {
        back();
    }

    /**
     * Method setting the shopping list name TextField callbacks
     */
    public void setShoppingListNameActions() {
        shoppingListNameTextField.setOnMouseClicked(e -> shoppingListNameTextField.positionCaret(shoppingListNameTextField.getText().length()));
        shoppingListNameTextField.setOnKeyReleased(e -> {
            try {
                shoppingList.setName(shoppingListNameTextField.getText());
            } catch (SQLException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
            shoppingListNameTextField.positionCaret(shoppingListNameTextField.getText().length());
        });
    }

    /**
     * Method loading the shopping list to the ListView
     * @param selectedShoppingList shoppinglist to load to the ListView
     */
    public void loadShoppingList(ShoppingList selectedShoppingList) {
        shoppingList = selectedShoppingList;
        setImages();
        shoppingListNameTextField.setText(shoppingList.getName());
        setShoppingListNameActions();
        addProductsToListView(shoppingList);
    }

    /**
     * Method setting the images to the Window
     */
    public void setImages() {
        String pathToPlus = "img/Plus.png";
        String pathToMinus = "img/Minus.png";
        try (FileInputStream plusInput = new FileInputStream(pathToPlus);
             FileInputStream minusInput = new FileInputStream(pathToMinus)) {
            shoppingListNameTextField.setText("No Shopping List Name Integrated In The Database Yet");
            plusImage = new Image(plusInput);
            minusImage = new Image(minusInput);
        } catch (IOException ignored) {}
    }

    /**
     * method setting the action of the minus button
     * @param minus the button.
     * @param quantityTextField the quantity asked by the user
     * @param product the product that need to be changed
     */
    private void setMinusButton(Button minus, TextField quantityTextField, Product product) {
        minus.setOnAction(e -> {
            product.decreaseQuantity();
            ProductsListDao.getInstance().update(new ProductPerList(product.getID(), product.getQuantity(), shoppingList.getID()));
            quantityTextField.setText(product.getQuantity() + product.getUnits());
        });
    }

    /**
     * method setting the action of the plus button
     * @param plus the button.
     * @param quantityTextField the quantity asked by the user
     * @param product the product that need to be changed
     */
    private void setPlusButton(Button plus, TextField quantityTextField, Product product) {
        plus.setOnAction(e -> {
            product.increaseQuantity();
            ProductsListDao.getInstance().update(new ProductPerList(product.getID(), product.getQuantity(), shoppingList.getID()));
            quantityTextField.setText(product.getQuantity() + product.getUnits());
        });
    }

    /**
     * method setting the action of the minus button
     * @param delete the button.
     * @param listHBox the ingredients list
     * @param product the product that need to be deleted
     */
    private void setDeleteButton(Button delete, HBox listHBox, Product product) {
        delete.setOnAction(e -> {
            ProductsListDao.getInstance().delete(new ProductPerList(product.getID(), product.getQuantity(), shoppingList.getID()));
            ingredientsListView.getItems().remove(listHBox);
        });
    }

    /**
     * method used to set a certain quantity in the texfield
     * @param quantityTextField the texfield that need to be changed
     * @param product the product whose quantity is changed
     */
    private void setQuantityTextField(TextField quantityTextField, Product product) {
        quantityTextField.setOnMouseEntered(e -> {
            quantityTextField.setText(String.valueOf(product.getQuantity()));
            quantityTextField.positionCaret(quantityTextField.getText().length());
            quantityTextField.setEditable(true);
        });
        quantityTextField.setOnKeyPressed(e -> lastQuantityInput = quantityTextField.getText());
        quantityTextField.setOnKeyReleased(e -> {
            try {
                int newQuantity = Integer.parseInt(quantityTextField.getText());
                ProductsListDao.getInstance().update(new ProductPerList(product.getID(), product.getQuantity(), shoppingList.getID()));
                product.setQuantity(newQuantity);
            } catch (Exception throwables) {
                quantityTextField.setText(lastQuantityInput);}
            quantityTextField.positionCaret(quantityTextField.getText().length());
        });
        quantityTextField.setOnMouseExited(e -> {
            quantityTextField.setText(quantityTextField.getText() + product.getUnits());
            quantityTextField.positionCaret(quantityTextField.getText().length());
            quantityTextField.setEditable(false);
        });
    }

    /**
     * Method creating the HBox of the product
     * @param minus minus button of a product
     * @param plus plus button of a product
     * @param quantityTextField quantity TextField of a product
     * @param delete delete button of a product
     * @param product product from which data is retrieved
     * @param listHBox HBox to which the content is added
     */
    private void setActionButtons(Button minus, Button plus, TextField quantityTextField, Button delete, Product product, HBox listHBox) {
        setMinusButton(minus, quantityTextField, product);
        setPlusButton(plus, quantityTextField, product);
        setQuantityTextField(quantityTextField, product);
        setDeleteButton(delete, listHBox, product);
    }

    /**
     * Method used to set up the Image View
     * @param image the image used
     * @return the correctly set up view
     */
    public ImageView setupImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(gethBoxHeight()); imageView.setFitHeight(gethBoxHeight());
        return imageView;
    }

    /**
     * Method adding all the products to the ListView
     * @param shoppingListToAdd the ShoppingList added to the ListView
     */
    public void addProductsToListView(ShoppingList shoppingListToAdd) {
        ingredientsListView.getItems().clear();
        shoppingList = ShoppingListDao.getInstance().get(shoppingListToAdd.getID());
        for (Product product : shoppingListToAdd.getProducts()) {
            Label name = new Label(product.getName());
            name.setPrefSize(LISTLABELWITH, gethBoxHeight());
            TextField quantityTextField = new TextField();
            quantityTextField.setText(product.getQuantity() + product.getUnits());
            quantityTextField.setPrefSize(LISTQUANTITYFIELDWITH, gethBoxHeight());
            Button minus = setupButton(setupImageView(minusImage), gethBoxHeight());
            Button plus = setupButton(setupImageView(plusImage), gethBoxHeight());
            Button delete = setupButton(setupImageView(getDeleteImage()), ACTIONBUTTONWIDTH);
            HBox listHBox = new HBox(name, minus, quantityTextField, plus, delete);
            setActionButtons(minus, plus, quantityTextField, delete, product, listHBox);
            ingredientsListView.getItems().add(listHBox);
        }
    }

    /**
     * Method setting a button of a ListView
     * @param buttonImgView ImageView of the button
     * @param imgSize size of the button
     * @return button after setting
     */
    private Button setupButton(ImageView buttonImgView, int imgSize) {
        Button button = new Button();
        button.setPrefSize(imgSize, gethBoxHeight());
        button.setGraphic(buttonImgView);
        button.setStyle("-fx-background-color : transparent");

        return button;
    }

    /**
     * Method to get User
     * @return user of the controller
     */
    public User getUser() {
        return user;
    }
}

