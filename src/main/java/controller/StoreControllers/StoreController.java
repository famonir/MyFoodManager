package controller.StoreControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import controller.ResourcesAccess.Images;
import controller.BaseControllers.MainAppController;
import controller.ResourcesAccess.Resources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Dao.Stores.StoreDao;
import model.Dao.Stores.StoreProductsDAO;
import model.FundamentalClasses.Product;
import model.JSONImport.JSONStoreProductsReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller for the Stores
 */
public class StoreController extends MainAppController {
    /**
     * Button to import products
     */
    public Button importProductsButton;
    /**
     * Textfield for the product name search bar
     */
    @FXML
    private TextField productNameSearchBar;
    /**
     * Hbox listview for the stores products
     */
    @FXML
    private ListView<HBox> storesProductsListView;
    /**
     * Textfield for the store name search bar
     */
    @FXML
    private TextField storeNameSearchBar;
    /**
     * Label listView for the stores name
     */
    @FXML
    private ListView<Label> storesNamesListView;
    private Image editImage;
    private Image deleteImage;
    private List<String> allStoresNames;
    private final List<Product> productsSearchResult = new ArrayList<>();
    private final List<String> storesSearchResult = new ArrayList<>();
    private List<Product> storeProducts;

    /**
     * Method calling the methods used to set up the Store View
     */
    public void setStoresView() {
        setImages();
        setStoresNamesListView();
    }

    /**
     * Method setting the different images needed in the store view
     */
    public void setImages() {
        try (FileInputStream editInput = new FileInputStream(Images.EDIT.toString());
             FileInputStream deleteInput = new FileInputStream(Images.DELETE.toString())) {
            editImage = new Image(editInput);
            deleteImage = new Image(deleteInput);
        } catch (IOException ignored) {}
    }

    /**
     * Method getting the stores from the DB to later set up their names on the View
     */
    public void setStoresNamesListView() {
        StoreDao storeDao = StoreDao.getInstance();
        List<String> stores = storeDao.getAll();
        allStoresNames = stores;
        addStoresToListView(stores);
    }

    /**
     * Method setting the store names gotten from the DB or from the search result on the Store View
     * @param stores list of the store names
     */
    private void addStoresToListView(List<String> stores) {
        int labelWidth = 255;
        int squareWidth = 30;
        storesNamesListView.getItems().clear();
        for (String storeName : stores) {
            Label name = new Label(storeName);
            name.setPrefSize(labelWidth, squareWidth);
            storesNamesListView.getItems().add(name);
        }
    }

    /**
     * Method setting up the Square Button
     * @param buttonImgView ImageView of the Square Button
     * @param width desired width of the button
     */
    private Button setupSquareButton(ImageView buttonImgView, int width) {
        Button button = new Button();
        button.setPrefSize(width, width);
        button.setGraphic(buttonImgView);
        button.setStyle("-fx-background-color : transparent");
        return button;
    }

    /**
     * Method setting up the ImageView of the Square Button
     * @param width desired width of the button
     * @param img image to be set up into and Image View
     */
    private ImageView createSquareButtonImageView(int width, Image img) {
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(width);
        imageView.setFitWidth(width);
        return imageView;
    }

    /**
     * Method used to search among the store products
     */
    public void searchProducts() {
        productsSearchResult.clear();
        for (Product product : storeProducts) {
            if (product.getName().toLowerCase(Locale.ROOT).contains(productNameSearchBar.getText().toLowerCase(Locale.ROOT))) {
                productsSearchResult.add(product);
            }
        }
        int nameWidth = 140;
        addStoreProducts(nameWidth, productsSearchResult);
    }

    /**
     * Method used to search amongst the store list
     */
    public void searchStores() {
        storesSearchResult.clear();
        for (String storeName : allStoresNames) {
            if (!storeName.toLowerCase(Locale.ROOT).contains(storeNameSearchBar.getText().toLowerCase(Locale.ROOT))) {continue;}
            storesSearchResult.add(storeName);
        }
        addStoresToListView(storesSearchResult);
    }

    /**
     * Method used to handle adding a new store (creates the scene where user will be able to set it up)
     * @throws IOException exception from loading error from the load() method
     */
    public void addNewStore() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.NEWSTORE.toString()));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Store");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        setStoresView();
        searchStores();
    }

    /**
     * Method handling the selection of the stores
     * @throws IOException exception from loading error
     */
    public void selectStore() throws IOException {
        Label selectedStore = storesNamesListView.getSelectionModel().getSelectedItem();
        if (selectedStore != null) {
            StoreProductsDAO storeProductsDAO = StoreProductsDAO.getInstance();
            storeProducts = storeProductsDAO.getAllProducts(selectedStore.getText());
            storesProductsListView.getItems().clear();
            int nameWidth = 140;
            addStoreProducts(nameWidth, storeProducts);
        }
    }

    /**
     * Method creating the label of the list view
     * @param text text to be put in the label
     * @param width width of the label
     * @param height  height of the label
     */
    private Label createListViewLabel(String text, int width, int height) {
        Label label = new Label(text);
        label.setPrefSize(width, height);
        return label;
    }

    /**
     * Method setting up window used to add products to a store
     * @param nameWidth width of the name of the store dynamically have a right size label
     */
    private void addStoreProducts(int nameWidth, List<Product> productsList) {
        storesProductsListView.getItems().clear();
        int squareSize = 30;
        for (Product product : productsList) {
            HBox productHBox = new HBox();
            Label nameLabel = createListViewLabel(product.getName(), nameWidth, squareSize);
            Label brandLabel = createListViewLabel(product.getBrand(), nameWidth, squareSize);
            String quantity = (product.getQuantity() == 1) ? "" : String.valueOf(product.getQuantity());
            Label priceLabel = createListViewLabel(product.getPrice() + "€/" + quantity + product.getUnits(), nameWidth, squareSize);
            Button editButton = setupSquareButton(createSquareButtonImageView(squareSize, editImage), squareSize);
            Button deleteButton = setupSquareButton(createSquareButtonImageView(squareSize, deleteImage), squareSize);
            setDeleteCallback(deleteButton, product, productHBox);
            setEditCallBack(editButton, product);

            productHBox.getChildren().addAll(brandLabel, nameLabel, priceLabel, editButton, deleteButton);
            storesProductsListView.getItems().add(productHBox);
        }
    }

    /**
     * Method setting up the deletion actions
     * @param deleteButton delete button to set the OnAction
     * @param productToDelete product to be deleted from the DB and from the store products
     * @param productHBox HBox to delete from the storesProductsListView list
     */
    private void setDeleteCallback(Button deleteButton, Product productToDelete, HBox productHBox) {
        deleteButton.setOnAction(actionEvent -> {
            StoreProductsDAO.getInstance().delete(productToDelete);
            storesProductsListView.getItems().remove(productHBox);
            storeProducts.remove(productToDelete);
        });
    }

    /**
     * Method setting up the edition actions
     * @param editButton edit button
     * @param productToEdit product to be edited
     */
    private void setEditCallBack(Button editButton, Product productToEdit) {
        editButton.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.EDITSTOREPRODUCT.toString()));
            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
            EditProductController editProductController = loader.getController();
            editProductController.loadContent(productToEdit, storesNamesListView.getSelectionModel().getSelectedItem().getText());
            assert parent != null;
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(String.format("Edit %s from %s.", productToEdit.getName(),
                    storesNamesListView.getSelectionModel().getSelectedItem().getText()));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            try {
                selectStore();
            } catch (IOException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
        });
    }


    /**
     * Method used to launch the scene to add a new product to a store after add product button is pressed
     * @throws IOException loading and search exception which might respectively occur from the load() and selectStore()
     * methods
     */
    public void addNewProduct() throws IOException {
        Label selectedStore = storesNamesListView.getSelectionModel().getSelectedItem();
        if (selectedStore != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.NEWSTOREPRODUCT.toString()));
            Parent parent = loader.load();
            NewProductController newProductController = loader.getController();
            newProductController.loadContent(selectedStore.getText());
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Product : " + selectedStore.getText());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            selectStore();
        }
    }

    /** Method used to import products for a store
     * @param actionEvent event that triggered the method
     */
    public void importProducts(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        JSONStoreProductsReader jsonStoreProductsReader = new JSONStoreProductsReader();
        try {
            File file = fileChooser.showOpenDialog(this.getStage(actionEvent));
            if (file != null) {
                String fileName = file.getAbsolutePath();
                jsonStoreProductsReader.fetchData(fileName);
            }
        } catch (Exception throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }
}
