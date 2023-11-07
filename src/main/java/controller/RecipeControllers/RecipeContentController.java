package controller.RecipeControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import controller.ResourcesAccess.Images;
import controller.ResourcesAccess.Resources;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ProductPerList;
import model.FundamentalClasses.Recipe;
import model.Dao.RecipeDao.RecipeListDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**Class controlling all the methods on the FXML view of the recipe content window
 *
 */
public class RecipeContentController extends RecipeDataController {
    private Recipe recipe;
    private Image plusImage;
    private Image minusImage;
    private String lastQuantityInput;
    @FXML
    private TextArea instructionsTextArea;
    @FXML
    private ListView<HBox> ingredientsListView;
    @FXML
    private CheckBox vegetarianCheckBox;
    @FXML
    private CheckBox fishCheckBox;
    @FXML
    private TextField dishTypeTextField;
    @FXML
    private TextField recipeNameTextField;
    @FXML
    private Label peopleCountLabel;
    @FXML
    private Button decreasePeopleButton;
    @FXML
    private Button increasePeopleButton;
    @FXML
    private ImageView peopleIcon;
    @FXML
    private Button favouriteButton;

    private static final int SQUAREBUTTONSSIZE = 42;
    private static final int LISTVIEWLABELSWIDTH = 80;
    private static final int LIGHTINGVALUE = 45;
    private static final double COLORV1 = 0.88;
    private static final double COLORV2 = 0.89;

    /**Method saving all the instructions of a recipe
     * @throws SQLException database exception
     */
    public void saveInstructions() throws SQLException {
        String newInstruction = instructionsTextArea.getText();
        for (int instructionsCharIndex = 0; instructionsCharIndex < newInstruction.length(); ++instructionsCharIndex) {
            if (String.valueOf(newInstruction.charAt(instructionsCharIndex)).equals("'")) {
                String preComa = newInstruction.substring(0, instructionsCharIndex);
                String postComa = newInstruction.substring(instructionsCharIndex);
                newInstruction = preComa + "'" + postComa;
                ++instructionsCharIndex;
            }

        }
        recipe.setDescription(newInstruction);
    }

    /**Method going back from the window to the recipes list
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException exception thrown by never existing file
     */
    public void back(ActionEvent actionEvent) throws IOException {
        onRecipesPressed(actionEvent);
    }

    /**Method loading the recipe and the window content based on it
     * @param selectedRecipe Recipe chosen to be opened
     */
    public void loadRecipe(Recipe selectedRecipe) {
        recipe = selectedRecipe;
        vegetarianCheckBox.setDisable(true);
        fishCheckBox.setDisable(true);
        recipeNameTextField.setText(recipe.getName());
        peopleCountLabel.setText(String.valueOf(recipe.getPeopleCount()));
        instructionsTextArea.setText(recipe.getDescription());
        dishTypeTextField.setText(recipe.getDishType());
        setRecipeNameActions();
        setImages();
        setAdditionalImages();
        addProductsToListView(recipe);
    }

    /**
     * Method setting the + and - image into place
     * @param buttonSize size of the buttons
     */
    private void setPlusMinusImages(int buttonSize) {
        String pathToPlus = Images.PLUS.toString();
        String pathToMinus = Images.MINUS.toString();
        try (FileInputStream plusInput = new FileInputStream(pathToPlus);
             FileInputStream minusInput = new FileInputStream(pathToMinus)) {
            plusImage = new Image(plusInput);
            minusImage = new Image(minusInput);
            ImageView minusImgView = new ImageView(minusImage);
            ImageView plusImgView = new ImageView(plusImage);
            minusImgView.setFitHeight(buttonSize);
            minusImgView.setFitWidth(buttonSize);
            plusImgView.setFitHeight(buttonSize);
            plusImgView.setFitWidth(buttonSize);
            decreasePeopleButton.setGraphic(minusImgView);
            increasePeopleButton.setGraphic(plusImgView);
        } catch (IOException ignored) {}
    }

    /**
     * Method loading all the addition images of the window
     */
    private void setAdditionalImages() {
        int squareButtonsSize = SQUAREBUTTONSSIZE;
        setPlusMinusImages(squareButtonsSize);
        ImageView favouriteImgView = new ImageView(getFavouriteImage());
        favouriteImgView.setFitWidth(squareButtonsSize);
        favouriteImgView.setFitHeight(squareButtonsSize);
        favouriteButton.setGraphic(favouriteImgView);
        setFavourite(recipe.getFavourite());
        peopleIcon.setImage(getPeopleImage());
    }

    /**
     * Method setting the product image in place
     * @param image of the product
     */
    private ImageView setProductImageButtons(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(gethBoxHeight()); imageView.setFitHeight(gethBoxHeight());
        return imageView;
    }

    /**Method adding all the recipe's ingredients to the ListView
     * @param recipeToAdd Recipe where all the content is retrieved
     */
    public void addProductsToListView(Recipe recipeToAdd) {
        ingredientsListView.getItems().clear();
        resetCheckBoxes();
        int listViewLabelsWidth = LISTVIEWLABELSWIDTH;
        for (model.FundamentalClasses.Product product : recipeToAdd.getProducts()) {
            Label name = new Label(product.getName());
            name.setPrefSize(listViewLabelsWidth, gethBoxHeight());
            TextField quantityTextField = new TextField();
            quantityTextField.setText(product.getQuantity() + product.getUnits());
            quantityTextField.setPrefSize(listViewLabelsWidth, gethBoxHeight());
            Button minus = setupButton(setProductImageButtons(minusImage));
            Button plus = setupButton(setProductImageButtons(plusImage));
            Button delete = setupButton(setProductImageButtons(getDeleteImage()));
            HBox listHBox = new HBox(name, minus, quantityTextField, plus, delete);
            setActionButtons(minus, plus, quantityTextField, delete, product, listHBox);
            ingredientsListView.getItems().add(listHBox);
        }
    }

    /**
     * Method setting all the recipeNameTextField and dishTypeTextField callback methods
     */
    public void setRecipeNameActions() {
        recipeNameTextField.setOnMouseClicked(e -> recipeNameTextField.positionCaret(recipeNameTextField.getText().length()));
        recipeNameTextField.setOnKeyReleased(e -> {
            try {
                recipe.setName(recipeNameTextField.getText());
            } catch (SQLException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
            recipeNameTextField.positionCaret(recipeNameTextField.getText().length());
        });
        dishTypeTextField.setOnKeyReleased(e -> {
            try {
                recipe.setDishType(dishTypeTextField.getText());
            } catch (SQLException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
            dishTypeTextField.positionCaret(dishTypeTextField.getText().length());
        });

    }

    /**
     * Method setting the callback for the - button
     * @param minus minus button
     * @param quantityTextField Textfield for the quantity
     * @param product product associated with the quantity
     */
    private void setMinusButtonCallbacks(Button minus, TextField quantityTextField, Product product) {
        minus.setOnAction(e -> {
            product.decreaseQuantity();
            RecipeListDao.getInstance().update(new ProductPerList(product.getID(), product.getQuantity(), recipe.getID()));
            quantityTextField.setText(product.getQuantity() + product.getUnits());
        });
    }

    /**
     * Method setting the callback for the + button
     * @param plus plus button
     * @param quantityTextField Textfield for the quantity
     * @param product product associated with the quantity
     */
    private void setPlusButtonCallbacks(Button plus, TextField quantityTextField, Product product) {
        plus.setOnAction(e -> {
            product.increaseQuantity();
            RecipeListDao.getInstance().update(new ProductPerList(product.getID(), product.getQuantity(), recipe.getID()));
            quantityTextField.setText(product.getQuantity() + product.getUnits());
        });
    }

    /**
     * Method setting the callback for the delete button
     * @param delete button
     * @param listHBox HBox containing the product
     * @param product to be deleted
     */
    private void setDeleteButtonCallbacks(Button delete, HBox listHBox, Product product) {
        delete.setOnAction(e -> {
            recipe.removeProduct(product);
            ingredientsListView.getItems().remove(listHBox);
            resetCheckBoxes();
        });
    }

    /**
     * Method setting the callback for the onMouse movements quantity textfield
     * @param quantityTextField Textfield for the quantity
     * @param product product associated with the quantity
     */
    private void setQuantityButtonMouseCallbacks(TextField quantityTextField, Product product) {
        quantityTextField.setOnMouseEntered(e -> {
            quantityTextField.setText(String.valueOf(product.getQuantity()));
            quantityTextField.positionCaret(quantityTextField.getText().length());
            quantityTextField.setEditable(true);
        });
        quantityTextField.setOnMouseExited(e -> {
            quantityTextField.setText(quantityTextField.getText() + product.getUnits());
            quantityTextField.positionCaret(quantityTextField.getText().length());
            quantityTextField.setEditable(false);
        });
    }

    /**
     * Method setting the callback for the onKeyPressed quantity textfield
     * @param quantityTextField Textfield for the quantity
     * @param product product associated with the quantity
     */
    private void setQuantityButtonKeyCallbacks(TextField quantityTextField, Product product) {
        quantityTextField.setOnKeyPressed(e -> lastQuantityInput = quantityTextField.getText());

        quantityTextField.setOnKeyReleased(e -> {
            try {
                int newQuantity = Integer.parseInt(quantityTextField.getText());
                RecipeListDao.getInstance().update(new ProductPerList(product.getID(), product.getQuantity(), recipe.getID()));
                product.setQuantity(newQuantity);
                for (model.FundamentalClasses.Product prod : recipe.getProducts()) {
                    if (Objects.equals(prod.getName(), product.getName())) {
                        prod.setQuantity(product.getQuantity());
                    }
                }
            } catch (Exception throwables) {
                quantityTextField.setText(lastQuantityInput);}
            quantityTextField.positionCaret(quantityTextField.getText().length());
        });
    }

    /**
     * Method setting the callback for the quantity textfield
     * @param quantityTextField Textfield for the quantity
     * @param product product associated with the quantity
     */
    private void setQuantityButtonCallbacks(TextField quantityTextField, Product product) {
        setQuantityButtonMouseCallbacks(quantityTextField, product);
        setQuantityButtonKeyCallbacks(quantityTextField, product);
    }

    /**
     * Method loading the HBox callbacks
     * @param minus minus button of a product
     * @param plus plus button of a product
     * @param quantityTextField quantity TextField of a product
     * @param delete delete button of a product
     * @param product product from which all the data is retrieved
     * @param listHBox HBox where all the widgets will be put in
     */
    public void setActionButtons(Button minus, Button plus, TextField quantityTextField, Button delete, Product product, HBox listHBox) {
        setMinusButtonCallbacks(minus, quantityTextField, product);
        setPlusButtonCallbacks(plus, quantityTextField, product);
        setDeleteButtonCallbacks(delete, listHBox, product);
        setQuantityButtonCallbacks(quantityTextField, product);

    }

    /**
     * Method decreasing the people count
     * @throws SQLException database exception
     */
    public void decreasePeopleCount() throws SQLException {
        recipe.decreasePeopleCount();
        peopleCountLabel.setText(String.valueOf(recipe.getPeopleCount()));
        addProductsToListView(recipe);
    }

    /**
     * Method increasing the people count
     * @throws SQLException database exception
     */
    public void increasePeopleCount() throws SQLException {
        recipe.increasePeopleCount();
        peopleCountLabel.setText(String.valueOf(recipe.getPeopleCount()));
        addProductsToListView(recipe);
    }

    /**
     * Method calling the add ingredient window
     * @throws IOException exception thrown by never existing file
     */
    public void addIngredient() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource((Resources.ADDINGREDIENTMENU.toString())));
        Parent parent = loader.load();
        ProductToRecipeController productToRecipeController = loader.getController();
        productToRecipeController.loadShoppingList(recipe);
        productToRecipeController.loadProducts();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Product");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        addProductsToListView(recipe);
    }

    /**
     * Method rechecking the filters
     */
    private void resetCheckBoxes() {
        vegetarianCheckBox.setSelected(recipe.checkVegetarian());
        fishCheckBox.setSelected(recipe.checkFish());
    }

    /**
     * Method setting the favourite button lighting
     * @param favourite boolean checking if the recipe is a favourite one
     */
    private void setFavourite(boolean favourite) {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1);
        lighting.setSpecularConstant(0);
        lighting.setSpecularExponent(0);
        lighting.setSurfaceScale(0);
        if (favourite) {
            lighting.setLight(new Light.Distant(LIGHTINGVALUE, LIGHTINGVALUE, new Color(COLORV1, COLORV2, 0, 1)));
        }
        else {
            lighting.setLight(new Light.Distant(LIGHTINGVALUE, LIGHTINGVALUE, Color.WHITE));
        }
        favouriteButton.getGraphic().setEffect(lighting);
        favouriteButton.getGraphic().setStyle("background-color : transparent");
    }

    /**
     * Method switching the favourite value
     * @throws SQLException database exception
     */
    public void changeFavourite() throws SQLException {
        recipe.setFavourite(!recipe.getFavourite());
        setFavourite(recipe.getFavourite());
    }
}
