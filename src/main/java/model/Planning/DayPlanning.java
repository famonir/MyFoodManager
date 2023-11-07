package model.Planning;

import model.FundamentalClasses.RecipeToDayPlanning;
import model.Dao.Planning.RecipesToDayDao;
import model.FundamentalClasses.Recipe;

import java.sql.SQLException;
import java.util.List;

/**
 * Class of DayPlanning (model)
 */
public class DayPlanning {
    private final int id;
    private final String date;
    private final int userID;
    private final List<Recipe> plannedRecipes;
    private final RecipesToDayDao recipesToDayDao = RecipesToDayDao.getInstance();

    /**
     * Constructor DayPlaning
     * @param idToSet id of the dayPlanning
     * @param dateToSet date of the dayPlanning
     * @param userIDToSet user ID of the dayPlanning
     */
    public DayPlanning(int idToSet, String dateToSet, int userIDToSet) {
        this.id = idToSet;
        this.date = dateToSet;
        this.userID = userIDToSet;
        this.plannedRecipes = recipesToDayDao.getDayRecipes(idToSet);
    }

    /**
     * ID getter
     * @return id int
     */
    public int getID() {return id;}

    /**
     * Method to get date
     * @return date
     */
    public String getDate() {return date;}

    /**
     * Method to get userId
     * @return userId
     */
    public int getUserID() {return userID;}

    /**
     * Method adding a recipe to a day
     * @param recipe recipe to be added to the day
     * @param dayID ID of the day for which the recipe is to be added
     * @throws SQLException database exception
     */
    public void addRecipe(Recipe recipe, int dayID) throws SQLException {
        this.plannedRecipes.add(recipe);
        recipesToDayDao.create(new RecipeToDayPlanning(dayID, recipe.getID(), recipe.getPeopleCount(), recipe.getPeopleCount()));
    }

    /**
     * Method removing a recipe from a day
     * @param recipe recipe to be delete
     * @param dayID id of the day
     * @throws SQLException database exception
     */
    public void removeRecipe(Recipe recipe, int dayID) throws SQLException {
        this.plannedRecipes.remove(recipe);
        recipesToDayDao.delete(new RecipeToDayPlanning(dayID, recipe.getID(), recipe.getPeopleCount(), recipe.getPeopleCount()));
    }

    /**
     * Recipes getter
     * @return List of recipes
     */
    public List<Recipe> getRecipes() {return plannedRecipes;}

    /**
     * Method to clean all recipes
     */
    public void cleanAllRecipes() {
        recipesToDayDao.deleteUsersDay(id);
    }
}
