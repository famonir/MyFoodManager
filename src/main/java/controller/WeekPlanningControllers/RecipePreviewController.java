package controller.WeekPlanningControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.Recipe;
import model.FundamentalClasses.User;

/**
 * Controller of the recipe preview window
 */
public class RecipePreviewController {
    User user;
    /**Recipe name label from the fxml file
     *
     */
    @FXML
    public Label recipeNameLabel;
    /**Ingredients list view from the fxml file
     *
     */
    @FXML
    public ListView<HBox> ingredientsListView;
    /**Description of the recipe text are from the fxml file
     *
     */
    @FXML
    public TextArea descriptionTextArea;
    /**Close button from the fxml file
     *
     */
    @FXML
    public Button closeButton;
    /**Recipe dish type of the recipe from the fxml file
     *
     */
    @FXML
    public Label recipeDishTypeLabel;
    /**Recipe's guests count from the fxml file
     *
     */
    @FXML
    public Label recipeGuestsCountLabel;

    /**
     * Method used to load the user related to the planning
     * @param userToSet user to be loaded
     */
    public void loadUser(User userToSet) {this.user=userToSet;}

    /**
     * Method Loading the Recipe
     * @param recipe recipe to be loaded
     * */
    public void loadRecipe(Recipe recipe) {
        int productNameWidth = 290;
        int quantityWidth = 75;
        int hBoxHeight = 30;
        recipeNameLabel.setText(recipe.getName());
        for (Product product : recipe.getProducts()) {
            Label nameLabel = new Label(product.getName());
            nameLabel.setPrefSize(productNameWidth, hBoxHeight);
            Label quantityLabel = new Label(product.getQuantity() + product.getUnits());
            quantityLabel.setPrefSize(quantityWidth, hBoxHeight);
            HBox productHBox = new HBox(nameLabel, quantityLabel);
            ingredientsListView.getItems().add(productHBox);
        }
        descriptionTextArea.setText(recipe.getDescription());
        descriptionTextArea.setEditable(false);
        recipeDishTypeLabel.setText(recipe.getDishType());
        recipeGuestsCountLabel.setText(recipe.getPeopleCount() + " Guests");
    }

    /**
     * Method closing the preview
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * */
    public void closePreview(ActionEvent actionEvent) {closeStage(actionEvent);}

    /**Stage getter
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @return Stage (window)
     */
    private Stage getStage(ActionEvent actionEvent) {
        Node source = (Node)  actionEvent.getSource();
        return (Stage) source.getScene().getWindow();
    }

    /**Method closing the window
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    private void closeStage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        stage.close();
    }
}
