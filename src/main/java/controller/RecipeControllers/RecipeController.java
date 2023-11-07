package controller.RecipeControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import controller.ResourcesAccess.Images;
import controller.ResourcesAccess.Resources;
import model.Dao.RecipeDao.RecipeDao;
import model.Dao.RecipeDao.RecipeListDao;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ProductPerList;
import model.FundamentalClasses.Recipe;
import model.JSONImport.JSONRecipeReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * Class controlling all the methods on the FXML view of the recipes list window
 */
public class RecipeController extends RecipeDataController {
    private Image editImage;
    private List<Recipe> recipes;
    private final List<Recipe> searchBarResult = new ArrayList<>();
    @FXML
    private TextField recipeSearchField;
    @FXML
    private ListView<HBox> recipesListView;
    @FXML
    private CheckBox vegetarianFilterCheckBox;
    @FXML
    private CheckBox fishFilterCheckBox;

    private static final int LIGHTINGVALUE = 45;
    private static final int LISTVIEWLABELSWIDTH = 325;
    private static final double COLORV1 = 0.88;
    private static final double COLORV2 = 0.89;

    /**
     * Method loading the recipes and the window contents
     */
    public void loadRecipes() {
        recipesListView.getItems().clear();
        recipes = RecipeDao.getInstance().getAllUserRecipes(user.getID());
        setImages();
        setAdditionalImages();
        searchRecipe();
    }

    /**
     * Method generating a people count label
     * @param peopleImgView ImageView of the people icon
     * @return the generated label
     */
    public Label setupPeopleLabel(ImageView peopleImgView) {
        Label label = new Label();
        label.setPrefSize(gethBoxHeight(), gethBoxHeight());
        label.setGraphic(peopleImgView);
        label.setStyle("-fx-background-color : transparent");
        return label;
    }

    /**
     * Method preparing the favourite image
     * @param imageView ImageView of the favourite image
     */
    private void setFavourite(ImageView imageView) {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1);
        lighting.setSpecularConstant(0);
        lighting.setSpecularExponent(0);
        lighting.setSurfaceScale(0);
        lighting.setLight(new Light.Distant(LIGHTINGVALUE, LIGHTINGVALUE, new Color(COLORV1, COLORV2, 0, 1)));
        imageView.setEffect(lighting);
        imageView.setStyle("background-color : transparent");
    }

    /**
     * Method creating the ImageView for the list button
     * @param image image of the button
     */
    private ImageView createListButtonImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(gethBoxHeight());
        imageView.setFitHeight(gethBoxHeight());
        return imageView;
    }

    /**
     * Method adding recipes to the ListView
     * @param recipesToSet a list of all the recipes to add to the ListView
     */
    private void addRecipesToListView(List<Recipe> recipesToSet) {
        int listViewLabelsWidth = LISTVIEWLABELSWIDTH;
        for (Recipe recipe : recipesToSet) {
            Label name = new Label(recipe.getName());
            Label dishType = new Label(recipe.getDishType());
            Label peopleCount = new Label(String.valueOf(recipe.getPeopleCount()));
            name.setPrefSize(listViewLabelsWidth, gethBoxHeight());
            dishType.setPrefSize(listViewLabelsWidth, gethBoxHeight());
            peopleCount.setPrefSize(gethBoxHeight(), gethBoxHeight());
            peopleCount.setAlignment(Pos.CENTER);
            ImageView favouriteImgView = createListButtonImageView(getFavouriteImage());
            if (recipe.getFavourite()) {
                setFavourite(favouriteImgView);
            }
            Button delete = setupButton(createListButtonImageView(getDeleteImage()));
            Button edit = setupButton(createListButtonImageView(editImage));
            Label peopleImg = setupPeopleLabel(createListButtonImageView(getPeopleImage()));
            HBox listHBox = new HBox(name, dishType, favouriteImgView, peopleImg, peopleCount, edit, delete);
            listHBox.setAlignment(Pos.CENTER);
            setActionButtons(delete, edit, recipe, listHBox);
            recipesListView.getItems().add(listHBox);
        }
    }

    /**
     * Method preparing the HBox of a recipe
     * @param delete delete button of a product
     * @param edit edit button of a product
     * @param recipe recipe from which the data is retrieved
     * @param listHBox the HBox for all the widgets
     */
    public void setActionButtons(Button delete, Button edit, Recipe recipe, HBox listHBox) {
        delete.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Shopping List");
            alert.setHeaderText("Are you sure you want to delete that shopping list?");
            alert.setContentText("It will never be accessible anymore.\n");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    RecipeDao.getInstance().delete(recipe);
                } catch (SQLException throwables) {
                    new ExceptionAlertController().showExceptionWindow(throwables);
                }
                recipesListView.getItems().remove(listHBox);
            }
        });
        edit.setOnAction(e -> {
            try {
                goToRecipe(e, recipe);
            } catch (IOException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
        });
    }

    /**
     * Method generating the Image of the additional content
     */
    private void setAdditionalImages() {
        String pathToEdit = Images.EDIT.toString();
        try (FileInputStream editInput = new FileInputStream(pathToEdit)) {
            editImage = new Image(editInput);
        } catch (IOException ignored) {}
    }

    /**
     * Method searching through recipes based on search bar
     */
    public void searchRecipe() {
        recipesListView.getItems().clear();
        searchBarResult.clear();
        boolean hasToBeVegetarian = vegetarianFilterCheckBox.isSelected();
        boolean hasToBeFish = fishFilterCheckBox.isSelected();
        for (Recipe recipe : recipes) {
            if (!recipe.getName().toLowerCase(Locale.ROOT).contains(recipeSearchField.getText().toLowerCase(Locale.ROOT))) {continue;}
            if (hasToBeVegetarian && !recipe.checkVegetarian()) {continue;}
            if (hasToBeFish && !recipe.checkFish()) {continue;}
            if (!hasToBeFish && recipe.checkFish()) {continue;}
            searchBarResult.add(recipe);
        }
        addRecipesToListView(searchBarResult);
    }

    /**
     * Method opening a window to create a recipe
     * @throws IOException exception thrown by never existing file
     */
    public void addRecipe() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.ADDRECIPE.toString()));
        Parent parent = loader.load();
        NewRecipeController newRecipeController = loader.getController();
        newRecipeController.setUser(user);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Recipe");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        loadRecipes();
    }

    /**
     * Method switching to the recipe window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @param selectedRecipe Recipe the user wants to open
     * @throws IOException exception thrown by never existing file
     */
    public void goToRecipe(ActionEvent actionEvent, Recipe selectedRecipe) throws IOException {
        FXMLLoader loader = setRoot(Resources.RECINGRMENU.toString());
        RecipeContentController recipeContentController = loader.getController();
        recipeContentController.loadUser(user);
        recipeContentController.loadRecipe(selectedRecipe);
        recipeContentController.loadSideBar(loader);
        setStage(actionEvent);
    }

    /**
     * Method triggering the JSON recipe insertion
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void importJsonRecipe(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        JSONRecipeReader recipeImporter = new JSONRecipeReader(user.getID());
            try {
                File file = fileChooser.showOpenDialog(this.getStage(actionEvent));
                if (file != null) {
                    String filename = file.getAbsolutePath();
                    Recipe recipe = recipeImporter.importRecipeBasicInfos(filename);
                    List<Product> products = recipeImporter.importRecipeProducts(filename);
                    RecipeDao.getInstance().create(recipe);
                    Recipe retrievedRecipe = RecipeDao.getInstance().get(recipe.getName());
                    for (Product prod : products) {
                        RecipeListDao.getInstance().create(new ProductPerList(prod.getID(), prod.getQuantity(), retrievedRecipe.getID()));
                    }
                }
            } catch (Exception throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
        loadRecipes();
    }
}
