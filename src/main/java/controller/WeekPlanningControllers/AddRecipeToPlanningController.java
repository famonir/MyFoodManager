package controller.WeekPlanningControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Planning.DayPlanning;
import model.FundamentalClasses.Recipe;
import model.FundamentalClasses.User;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**Controller of the window responsible of adding a recipe to a day
 */
public class AddRecipeToPlanningController {
    /**search bar text field from the FXML file
     *
     */
    @FXML
    public TextField searchBarTextField;
    /**Guests count text field from the FXML file
     *
     */
    @FXML
    public TextField guestsCountTextField;
    /**recipes list view from the FXML file
     *
     */
    @FXML
    public ListView<HBox> recipesListView;
    User user;
    DayPlanning dayPlanning;
    List<Recipe> userRecipes;
    String dishTypeToFind = null;

    /**
     * Method used to load the user related to the planning
     * @param userToSet user to be loaded
     */
    public void loadUser(User userToSet) {
        this.user = userToSet;
        userRecipes = user.getUsersRecipes();
    }

    /**
     * Method loading the day planning
     * @param dayPlanningToSet day planning to be set
     */
    public void loadDayPlanning(DayPlanning dayPlanningToSet) {
        this.dayPlanning = dayPlanningToSet;
        searchRecipes();
    }

    /**
     * Method used to search recipes and to add them to the weekly planning
     */
    public void searchRecipes() {
        recipesListView.getItems().clear();
        int nameLabelWidth = 500;
        int dishTypeWidth = 100;
        int hBoxHeight = 30;
        for (Recipe recipe : userRecipes) {
            if (!recipe.getName().toLowerCase(Locale.ROOT).contains((searchBarTextField.getText().toLowerCase(Locale.ROOT)))) {
                continue;
            }
            if (dishTypeToFind != null && !Objects.equals(recipe.getDishType(), dishTypeToFind)) {
                    continue;
            }
            Label nameLabel = new Label(recipe.getName());
            nameLabel.setPrefSize(nameLabelWidth, hBoxHeight);
            Label dishTypeLabel = new Label(recipe.getDishType());
            dishTypeLabel.setPrefSize(dishTypeWidth, hBoxHeight);
            HBox hBoxToAdd = new HBox(nameLabel, dishTypeLabel);
            recipesListView.getItems().add(hBoxToAdd);
        }
    }

    /**
     * Method loading the dish type
     * @param newDishType dish type to load
     */
    public void loadDishType(String newDishType) {this.dishTypeToFind = newDishType;}

    /**
     * Method closing the window after confirm button is hit
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void confirmRecipe(ActionEvent actionEvent) {
        int recipeIndex = recipesListView.getSelectionModel().getSelectedIndex();
        if (recipeIndex < 0) {
            return;
        }
        Recipe selectedRecipe = userRecipes.get(recipeIndex);
        if (Objects.equals(guestsCountTextField.getText(), "")) {
            return;
        }
        try {
            int guestsCount = Integer.parseInt(guestsCountTextField.getText());
            selectedRecipe.setPeopleCountPlanningInit(guestsCount);
            dayPlanning.addRecipe(selectedRecipe, dayPlanning.getID());
        } catch (Exception throwables) {return;}
        closeStage(actionEvent);
    }

    /**
     * Method closing the stage when cancel button is hit
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     */
    public void cancel(ActionEvent actionEvent) {closeStage(actionEvent);}

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
