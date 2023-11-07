package controller.WeekPlanningControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import controller.ResourcesAccess.Resources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Planning.DayPlanning;
import model.Planning.FoodPlanningGenerator;
import model.FundamentalClasses.Recipe;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller responsible for the window of the autogeneration of a week
 */
public class AutoGenerateWeekController extends WeekPlanController {
    private static final int RIGHTAMOUNTS = 7;
    @FXML
    private TextField vegetarianBreakfastTextField;
    @FXML
    private TextField vegetarianLunchTextField;
    @FXML
    private TextField vegetarianSupperTextField;
    @FXML
    private TextField fishBreakfastTextField;
    @FXML
    private TextField fishLunchTextField;
    @FXML
    private TextField fishSupperTextField;

    /**
     * Method converting the text from the texting to an integer and returning it
     * @param textField textfield
     * @return the converted integer
     */
    private int checkCountTextFieldInteger(TextField textField) {return Integer.parseInt(textField.getText());}

    /**
     * Method checking if the amount of each type of dish selected is respected
     * @param vegBreakfasts number of vegeterian breakfasts
     * @param vegLunches number of vegeterian lunches
     * @param vegSuppers number of vegeterian suppers
     * @param fishBreakfasts number of fish breakfasts
     * @param fishLunches number of fish lunches
     * @param fishSuppers number of fish suppers
     */
    private boolean checkRightAmounts(int vegBreakfasts, int vegLunches, int vegSuppers, int fishBreakfasts, int fishLunches, int fishSuppers) {
        if (vegBreakfasts + fishBreakfasts > RIGHTAMOUNTS) {return false;}
        if (vegLunches + fishLunches > RIGHTAMOUNTS) {return false;}
        return vegSuppers + fishSuppers <= RIGHTAMOUNTS;
    }

    /**
     * Method used to add the recipe to a given day on the dayplanning
     * @param dayPlanning dayplanning where the recipe will be added
     * @param dishTypeToReplace dishtype to load
     */
    private void addRecipeToDay(DayPlanning dayPlanning, String dishTypeToReplace) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.NEWRECIPETOPLAN.toString()));
        try {
            Parent parent = loader.load();
            AddRecipeToPlanningController addRecipeToPlanningController = loader.getController();
            addRecipeToPlanningController.loadUser(user);
            addRecipeToPlanningController.loadDishType(dishTypeToReplace);
            addRecipeToPlanningController.loadDayPlanning(dayPlanning);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Recipe : " + dayPlanning.getDate());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            setListViews();
        } catch (IOException | SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**
     * Method to set the actions for the delete button
     * @param delete delete button
     * @param recipe recipe to be deleted from the dayplanning
     * @param dayPlanning dayplanning to be modified
     * @param listView listView
     * @param finalVBox Vbox to remove from the listView
     */
    @Override
    protected void setDeleteButtonCallback(Button delete, Recipe recipe, DayPlanning dayPlanning, ListView<VBox> listView, VBox finalVBox) {
        delete.setOnAction(e -> {
            try {
                dayPlanning.removeRecipe(recipe, dayPlanning.getID());
                listView.getItems().remove(finalVBox);
                addRecipeToDay(dayPlanning, recipe.getDishType());
            } catch (SQLException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
        });
    }

    /**
     * Method to automatically generate weeks
     * @throws SQLException sql exception which might occur from the setListViews() method
     */
    public void generate() throws SQLException {
        eraseAllRecipes();
        setListViews();
        try {
            int vegBreakfasts = checkCountTextFieldInteger(vegetarianBreakfastTextField);
            int vegLunches = checkCountTextFieldInteger(vegetarianLunchTextField);
            int vegSuppers = checkCountTextFieldInteger(vegetarianSupperTextField);
            int fishBreakfasts = checkCountTextFieldInteger(fishBreakfastTextField);
            int fishLunches = checkCountTextFieldInteger(fishLunchTextField);
            int fishSuppers = checkCountTextFieldInteger(fishSupperTextField);
            if (checkRightAmounts(vegBreakfasts, vegLunches, vegSuppers, fishBreakfasts, fishLunches, fishSuppers)) {
                new FoodPlanningGenerator(getPlannedDays(), vegBreakfasts, vegLunches, vegSuppers, fishBreakfasts, fishLunches, fishSuppers, user);
                setListViews();
            }
        } catch (Exception throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**
     * Method to back
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws SQLException sql exception which might occur from the onPlanningPressed() method
     * @throws IOException io exception which might occur from the onPlanningPressed() method
     */
    public void back(ActionEvent actionEvent) throws SQLException, IOException {
        onPlanningPressed(actionEvent);
    }

    /**
     * Method erasing all the recipes
     */
    public void eraseAllRecipes() {
        for (DayPlanning dayPlanning : getPlannedDays()) {
            dayPlanning.cleanAllRecipes();
        }
    }
}
