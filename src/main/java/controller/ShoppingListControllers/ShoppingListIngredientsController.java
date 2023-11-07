package controller.ShoppingListControllers;

import com.itextpdf.text.DocumentException;
import controller.ExceptionControllers.ExceptionAlertController;
import controller.ResourcesAccess.Images;
import controller.ResourcesAccess.Resources;
import controller.BaseControllers.WithCrossController;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import model.Dao.FundamentalClasses.ProductsListDao;
import model.Dao.FundamentalClasses.ShoppingListDao;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ProductPerList;
import model.Exporter.Exporter;
import model.Exporter.PDFCreate;
import model.FundamentalClasses.ShoppingList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Class controlling all the methods on the FXML view of the shopping list content window
 */
public final class ShoppingListIngredientsController extends WithCrossController {
    private Image plusImage;
    private Image minusImage;
    private String lastQuantityInput;
    @FXML
    private TextField shoppingListNameTextField;
    @FXML
    private ListView<HBox> ingredientsListView;
    private ShoppingList shoppingList;

    /**
     * Method opening the window for the new product insertion
     * @throws IOException exception thrown by never existing file
     */
    public void addProduct() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.ADDPRODUCTMENU.toString()));
        Parent parent = loader.load();
        ProductToShoppingListController productToShoppingListController = loader.getController();
        productToShoppingListController.loadShoppingList(shoppingList);
        productToShoppingListController.loadProducts();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Product");
        stage.setScene(scene);
        stage.showAndWait();
        addProductsToListView(shoppingList);
    }

    /**
     * Method going back to the shopping lists window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void back(ActionEvent actionEvent) throws IOException {
        onShoppingListPressed(actionEvent);
    }

    /**
     * Method setting the shopping list name TextField callbacks
     */
    public void setShoppingListNameActions() {
        shoppingListNameTextField.setOnMouseClicked(
                e -> shoppingListNameTextField.positionCaret(shoppingListNameTextField.getText().length())
        );
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
        String pathToPlus = Images.PLUS.toString();
        String pathToMinus = Images.MINUS.toString();
        try (FileInputStream plusInput = new FileInputStream(pathToPlus);
             FileInputStream minusInput = new FileInputStream(pathToMinus)) {
            shoppingListNameTextField.setText("No Shopping List Name Integrated In The Database Yet");
            plusImage = new Image(plusInput);
            minusImage = new Image(minusInput);
        } catch (IOException ignored) {}
    }


    /**
     * Method setting the - button
     * @param minus minus button
     * @param quantityTextField Textfield for the quantity
     * @param product product associated with the quantity
     */
    private void setMinusButton(Button minus, TextField quantityTextField, Product product) {
        minus.setOnAction(e -> {
            product.decreaseQuantity();
            ProductsListDao.getInstance().update(new ProductPerList(product.getID(), product.getQuantity(), shoppingList.getID()));
            quantityTextField.setText(product.getQuantity() + product.getUnits());
        });
    }

    /**
     * Method setting the + button
     * @param plus plus button
     * @param quantityTextField Textfield for the quantity
     * @param product product associated with the quantity
     */
    private void setPlusButton(Button plus, TextField quantityTextField, Product product) {
        plus.setOnAction(e -> {
            product.increaseQuantity();
            ProductsListDao.getInstance().update(new ProductPerList(product.getID(), product.getQuantity(), shoppingList.getID()));
            quantityTextField.setText(product.getQuantity() + product.getUnits());
        });
    }

    /**
     * Method setting the - button
     * @param delete delete button
     * @param listHBox Hbox of the product to be deleted
     * @param product product associated with the quantity
     */
    private void setDeleteButton(Button delete, HBox listHBox, Product product) {
        delete.setOnAction(e -> {
            shoppingList.removeProduct(product);
            ingredientsListView.getItems().remove(listHBox);
        });
    }

    /**
     * Method setting up the quantity textfield, and the onKey & onMouse actions linked to it
     * @param quantityTextField Textfield for the quantity
     * @param product product associated with the quantity
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

    /**Method creating the HBox of the product
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
     * Method setting up the ImageView of the image received
     * @param image image to be set into and ImageView
     * @return the image view created from the image
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
        int listLabelWidth = 436;
        int listQuantityFieldWidth = 140;
        int actionButtonWidth = 245;
        ingredientsListView.getItems().clear();
        shoppingList = ShoppingListDao.getInstance().get(shoppingListToAdd.getID());
        for (Product product : shoppingListToAdd.getProducts()) {
            Label name = new Label(product.getName());
            name.setPrefSize(listLabelWidth, gethBoxHeight());
            TextField quantityTextField = new TextField();
            quantityTextField.setText(product.getQuantity() + product.getUnits());
            quantityTextField.setPrefSize(listQuantityFieldWidth, gethBoxHeight());
            Button minus = setupButton(setupImageView(minusImage), gethBoxHeight());
            Button plus = setupButton(setupImageView(plusImage), gethBoxHeight());
            Button delete = setupButton(setupImageView(getDeleteImage()), actionButtonWidth);
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
     * Method setting the callback for the quantity textfield
     * @throws IOException exception thrown by never existing file in loadShoppingList
     * @throws DocumentException exception thrown by pdf error in loadShoppingList
     */
    public void emailButton() throws IOException, DocumentException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.EMAILMENU.toString()));
        Parent parent = loader.load();
        ShoppingListEmailController shoppingListEmailController = loader.getController();
        shoppingListEmailController.loadShoppingList(shoppingList);
        loadShoppingList(shoppingList);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Product");
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Method setting up the export button
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void exportButton(ActionEvent actionEvent){
        ChoiceDialog<String> exportChoice = new ChoiceDialog<>("PDF", "PDF","ODT");
        exportChoice.setHeaderText("Export option");
        exportChoice.setContentText("Please select your export format");
        exportChoice.showAndWait().ifPresent( c ->{
            String choice = exportChoice.getSelectedItem();
            exportHandler(choice, actionEvent);
        });
    }

    /**
     * Method setting up the export button
     * @param choice Determines the choice of the user for the export, beit ODT or PDF
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void exportHandler(String choice, ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Shopping List");
        FileChooser.ExtensionFilter extFilter;
        if(choice.equals("ODT")) {
            extFilter = new FileChooser.ExtensionFilter("ODT files (*.odt)", "*.odt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(this.getStage(actionEvent));
            if (file != null) {
                String fileName = file.toString();
                if (!fileName.endsWith(".odt")) {
                    fileName += ".odt";
                }
                file = new File(fileName);
                exportShoppingListODT(file);
            }
        } else{
            extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(this.getStage(actionEvent));
            if (file != null) {
                String fileName = file.toString();
                if (!fileName.endsWith(".pdf")) {
                    fileName += ".pdf";
                }
                file = new File(fileName);
                exportShoppingListPDF(file);
            }
        }
    }

    /**
     * Method triggering the export of a shoppingList
     * @param file to write in
     */
    public void exportShoppingListODT(File file) {
        try {

            if (file != null) {
                Exporter exporter = new Exporter(shoppingList, file);
                exporter.exportSetUp();
            }
        } catch (Exception throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

    }

    /**
     * Method triggering the export of a shoppingList
     * @param file action throwing the button pressed, used to pass to the next window
     */
    public void exportShoppingListPDF(File file) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Shopping List");

        try {
            if (file != null) {
                PDFCreate pdfExporter = new PDFCreate(shoppingList, file);
                pdfExporter.setUpPdf();
            }
        } catch (Exception throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }
}
