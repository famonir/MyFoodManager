package model.Dao.RecipeDao;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Dao;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ProductPerList;
import model.FundamentalClasses.Recipe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Recipe DAO class
 */
public final class RecipeDao extends Dao<Recipe> {

    private static RecipeDao singletonInstance = null;

    private RecipeDao() {}

    /**
     * Singleton instance getter
     * @return instance
     */
    public static RecipeDao getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new RecipeDao();
        }

        return singletonInstance;
    }

    @Override
    public List<Recipe> getAll() {
        return null;
    }

    /**
     * Method extracting all the recipes of a user
     * @param userID the ID of the user in the database
     * @return an array containing all the Recipe objects constructed with the help of the database
     */
    public List<Recipe> getAllUserRecipes(int userID) {
        List<Recipe> recipes = new ArrayList<>();
        String query         = String.format("SELECT * FROM recipe WHERE user = %d", userID);

        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            while (queryResult.next()) {
                Recipe nextRecipe = new Recipe(queryResult.getInt("id"), queryResult.getString("name"),
                        queryResult.getInt("user"),queryResult.getInt("number_people"),
                        queryResult.getString("dish_type"), queryResult.getString("description"),
                        queryResult.getInt("favourite") == 1);
                recipes.add(nextRecipe);
            }
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return recipes;
    }

    /**
     * Recipe getter from database
     * @param id id of recipe
     * @return Recipe
     * @throws SQLException database exception
     */
    @Override
    public Recipe get(int id) throws SQLException{
        String query = String.format("SELECT * FROM recipe WHERE id = %d;", id);

        return getRecipeQuery(query);
    }

    /**
     * Recipe getter from database
     * @param nameRecipe recipe name to be found
     * @return Recipe object associated to the given name
     */
    @Override
    public Recipe get(String nameRecipe) {
        String query = String.format("SELECT * FROM recipe WHERE name = '%s';", nameRecipe);

        return getRecipeQuery(query);
    }

    /**
     * Method to avoid code duplication
     * @param query query to be executed
     * @return Recipe that has been found, null otherwise
     */
    private Recipe getRecipeQuery(String query) {
        Recipe recipeOutput = null;
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            recipeOutput          = new Recipe(queryResult.getInt("id"), queryResult.getString("name"),
                    queryResult.getInt("user"),queryResult.getInt("number_people"),
                    queryResult.getString("dish_type"), queryResult.getString("description"),
                    queryResult.getInt("favourite") == 1);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return recipeOutput;
    }

    /**
     * Method adding a recipe, the products are stored in another table
     * @param recipe recipe object to be added in the database
     */
    @Override
    public void create(Recipe recipe) {
        String query = String.format(
                " INSERT INTO recipe (description, name, dish_type, user, number_people, favourite) " +
                        "VALUES ( '%s', '%s', '%s', %d, %d, %d); ",
                recipe.getDescription(), recipe.getName(), recipe.getDishType(), recipe.getUserID(), recipe.getPeopleCount(),
                recipe.getFavourite() ? 1 : 0
        );

        realizeUpdateQuery(query);
    }

    /**
     * Change a recipe
     * @param recipe recipe object to be updated
     */
    @Override
    public void update(Recipe recipe) throws SQLException {
        String query = String.format(
                "UPDATE recipe SET dish_type = '%s',name = '%s', user = %d, number_people = %d, description = '%s', favourite = %d WHERE id = %d",
                recipe.getDishType(), recipe.getName(), recipe.getUserID(), recipe.getPeopleCount(), recipe.getDescription(),
                recipe.getFavourite() ? 1 : 0, recipe.getID()
        );

        this.realizeUpdateQuery(query);
    }

    /**
     * Delete a recipe
     * @param recipe recipe object to be deleted
     */
    @Override
    public void delete(Recipe recipe) throws SQLException {
        for (Product product : recipe.getProducts()) {
            RecipeListDao.getInstance().delete(new ProductPerList(product.getID(), product.getQuantity(), recipe.getID()));
        }
        String query = String.format("DELETE FROM recipe WHERE name = '%s';", recipe.getName());

        this.realizeUpdateQuery(query);
    }
}