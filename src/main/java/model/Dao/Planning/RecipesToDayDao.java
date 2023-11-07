package model.Dao.Planning;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Dao;
import model.FundamentalClasses.RecipeToDayPlanning;
import model.Dao.RecipeDao.RecipeDao;
import model.FundamentalClasses.Recipe;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Recipes to day dao class
 */
public final class RecipesToDayDao extends Dao<RecipeToDayPlanning> {
    private static final String DAYCOLUMN = "day";
    private static final String RECIPECOLUMN = "recipe";
    private static final String GUESTSCOLUMN = "guests_count";
    private static RecipesToDayDao singletonInstance = null;
    private final RecipeDao recipeDao = RecipeDao.getInstance();

    private RecipesToDayDao() {}

    /**
     * Singleton instance getter
     * @return instance
     */
    public static RecipesToDayDao getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new RecipesToDayDao();
        }

        return singletonInstance;
    }

    @Override
    public List<RecipeToDayPlanning> getAll() {return null;}

    /**
     * get a RecipeToDayPlanning
     * @param idRecipesToDay id of the recipe as an int
     * @return recipeToDayPlanning
     */
    @Override
    public RecipeToDayPlanning get(int idRecipesToDay) throws SQLException {
        RecipeToDayPlanning recipeToDayPlanning = null;
        String query = String.format("SELECT * FROM recipes_to_day WHERE id = %d", idRecipesToDay);
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            recipeToDayPlanning = new RecipeToDayPlanning(queryResult.getInt(DAYCOLUMN),
                                                          queryResult.getInt(RECIPECOLUMN),
                                                          queryResult.getInt(GUESTSCOLUMN),
                                                          queryResult.getInt(GUESTSCOLUMN));
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return recipeToDayPlanning;
    }

    @Override
    public RecipeToDayPlanning get(String name) throws SQLException {return null;}

    /**
     * create a RecipesToDayDao
     * @param recipeToDayPlanning recipe to day planning object
     * @throws SQLException sql exception which could occur from the query
     */
    @Override
    public void create(RecipeToDayPlanning recipeToDayPlanning) throws SQLException {
        String query = String.format("INSERT INTO recipes_to_day (day, recipe, guests_count) VALUES (%d, %d, %d);",
                recipeToDayPlanning.getDayID(), recipeToDayPlanning.getRecipeID(), recipeToDayPlanning.getGuestsCount());
        realizeUpdateQuery(query);
    }

    /**
     * update a RecipesToDayDao
     * @param recipeToDayPlanning recipe to day planning object
     * @throws SQLException sql exception which could occur from the query
     */
    @Override
    public void update(RecipeToDayPlanning recipeToDayPlanning) throws SQLException {
        String query = String.format("UPDATE recipes_to_day SET day = %d, recipe = %d, guests_count = %d WHERE day = %d" +
                " AND recipe = %d AND guests_count = %d",
                recipeToDayPlanning.getDayID(), recipeToDayPlanning.getRecipeID(), recipeToDayPlanning.getGuestsCount(),
                recipeToDayPlanning.getDayID(), recipeToDayPlanning.getRecipeID(), recipeToDayPlanning.getOldGuestsCount());
        realizeUpdateQuery(query);
    }

    /**
     * delete a RecipesToDayDao
     * @param recipeToDayPlanning recipe to day planning object
     * @throws SQLException sql exception which could occur from the query
     */
    @Override
    public void delete(RecipeToDayPlanning recipeToDayPlanning) throws SQLException {
        String query = String.format("DELETE FROM recipes_to_day WHERE day = %d AND recipe = %d AND guests_count = %d",
                recipeToDayPlanning.getDayID(), recipeToDayPlanning.getRecipeID(), recipeToDayPlanning.getOldGuestsCount());
        realizeUpdateQuery(query);
    }

    /**
     * get a day recipes by idDay
     * @param idDay id of the day as an it
     * @return recipes of the day
     */
    public List<Recipe> getDayRecipes(int idDay) {
        List<Recipe> recipes = new ArrayList<>();
        String query = String.format("SELECT * FROM recipes_to_day WHERE day = %d", idDay);

        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            while(queryResult.next()) {
                Recipe recipeAppend = recipeDao.get(queryResult.getInt(RECIPECOLUMN));
                recipeAppend.setPeopleCountPlanningInit(queryResult.getInt(GUESTSCOLUMN));
                recipes.add(recipeAppend);
            }

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return recipes;
    }

    /**Method deleting all recipes from a day
     * @param dayID the ID of the day in the database
     */
    public void deleteUsersDay(int dayID) {
        String query = String.format("DELETE FROM recipes_to_day WHERE day = %d", dayID);
        this.realizeUpdateQuery(query);
    }
}
