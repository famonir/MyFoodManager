package TestDao;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.RecipeDao.RecipeDao;
import model.FundamentalClasses.Recipe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRecipeDao {

    private final RecipeDao testRecipeDao = RecipeDao.getInstance();
    private final Connection conn = testRecipeDao.getConnection();


    private static final String defaultRecipeName = "Test Recipe";
    private static final int defaultRecipeID = 0;
    private static final int defaultUserID = 0;
    private static final int defaultPeopleCount = 4;
    private static final String defaultDescription = "Test description for a test recipe";
    private static final String defaultType = "Breakfast";
    private static final boolean defaultFavourite = false;

    Recipe expectedRecipe = new Recipe(defaultRecipeID, "Test Recipe", defaultUserID, defaultPeopleCount, defaultType, defaultDescription, defaultFavourite);


    @BeforeAll
    public void initBeforeAll() {
        String defaultQuery = String.format(
                "INSERT INTO recipe (description, name, dish_type, user, number_people, favourite) VALUES ( '%s', '%s', '%s', %d, %d, %d); ",
                defaultDescription, defaultRecipeName , defaultType, defaultUserID, defaultPeopleCount, defaultFavourite ? 1 : 0);

        String recipeDeleteQuery = String.format(
                "INSERT INTO recipe (description, name, dish_type, user, number_people, favourite) VALUES ( '%s', '%s', '%s', %d, %d, %d); ",
                defaultDescription, "Recipe to delete" , defaultType, defaultUserID, defaultPeopleCount, defaultFavourite ? 1 : 0);

        String recipeUpdateQuery = String.format(
                "INSERT INTO recipe (description, name, dish_type, user, number_people, favourite) VALUES ( '%s', '%s', '%s', %d, %d, %d); ",
                defaultDescription, "Recipe to update" , defaultType, defaultUserID, defaultPeopleCount, defaultFavourite ? 1 : 0);

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(defaultQuery);
            stmt.executeUpdate(recipeDeleteQuery);
            stmt.executeUpdate(recipeUpdateQuery);

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }


    @Test
    public void getAllTest() {
        assertNull(testRecipeDao.getAll());
    }


    @Test
    public void getAllByUserIdTest() {
        List<Recipe> allRecipiesFromUserID = testRecipeDao.getAllUserRecipes(defaultUserID);
        String querySelectAll         = String.format("SELECT * FROM recipe WHERE user = '%d';", defaultUserID);
        List<Recipe> allRecipesByQuery;
        try (ResultSet queryResult = testRecipeDao.realizeExecuteQuery(querySelectAll)) {
            allRecipesByQuery = new ArrayList<>();

            while(queryResult.next()) {
                allRecipesByQuery.add(new Recipe(queryResult.getInt("id"), queryResult.getString("name"), queryResult.getInt("user"), queryResult.getInt("number_people"), queryResult.getString("dish_type"), queryResult.getString("description"), queryResult.getBoolean("favourite")));
            }
            assertEquals(allRecipiesFromUserID, allRecipesByQuery);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }


    @Test
    public void getByRecipeIdTest() throws SQLException {

        // find the ID of the test recipe by using verified method get(String recipeName)
        Recipe defaultRecipe = testRecipeDao.get(defaultRecipeName);
        int recipeID = defaultRecipe.getID();

        // Use this ID to retrieve the recipe
        Recipe retrievedRecipe = testRecipeDao.get(recipeID);

        assertEquals(expectedRecipe, retrievedRecipe);
    }


    @Test
    public void getByNameTest() {
        Recipe retrievedRecipe = testRecipeDao.get(defaultRecipeName);
        assertEquals(expectedRecipe, retrievedRecipe);
    }



    @Test
    public void createTest() {
        Recipe testRecipe = new Recipe(defaultRecipeID, "Test Create Recipe", defaultUserID, defaultPeopleCount, "Breakfast", defaultDescription, defaultFavourite);
        testRecipeDao.create(testRecipe);
        String query = String.format("SELECT * FROM recipe WHERE name = '%s';", "Test Create Recipe");

        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals("Test Create Recipe", queryResult.getString("name"));
            assertEquals(defaultDescription, queryResult.getString("description"));
            assertEquals(defaultType, queryResult.getString("dish_type"));
            assertEquals(defaultUserID, queryResult.getInt("user"));
            assertEquals(defaultFavourite ? 1 : 0, queryResult.getInt("favourite"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }


    @Test
    public void updateTest() throws SQLException {

        Recipe recipeToUpdate = testRecipeDao.get("Recipe to update");
        recipeToUpdate.setDescription("New descritpion - changed in update method");
        recipeToUpdate.setFavourite(true);
        recipeToUpdate.setDishType("Lunch");

        testRecipeDao.update(recipeToUpdate);
        Recipe updatedRecipe = testRecipeDao.get("Recipe to update");
        assertEquals(recipeToUpdate, updatedRecipe);
    }


    @Test
    public void deleteTest() throws SQLException {

        testRecipeDao.delete(testRecipeDao.get("Recipe to delete"));
        String query = String.format("SELECT * FROM recipe WHERE name = '%s';", "Recipe to delete");

        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertFalse(queryResult.next());
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

    }


    @AfterAll
    public void cleanUpDB() {
        String deleteBeforeAllQuery = String.format("DELETE FROM recipe WHERE name = '%s'", defaultRecipeName);
        String deleteCreateQuery = String.format("DELETE FROM recipe WHERE name = '%s'", "Test Create Recipe");
        String deleteDeleteQuery = String.format("DELETE FROM recipe WHERE name = '%s'", "Recipe to delete");
        String deleteUpdateQuery = String.format("DELETE FROM recipe WHERE name = '%s'", "Recipe to update");


        executeQuery(deleteBeforeAllQuery, deleteCreateQuery, deleteDeleteQuery, deleteUpdateQuery, conn);
    }

    static void executeQuery(String deleteBeforeAllQuery, String deleteCreateQuery, String deleteDeleteQuery, String deleteUpdateQuery, Connection conn) {
        execUpdate(deleteBeforeAllQuery, deleteCreateQuery, deleteDeleteQuery, deleteUpdateQuery, conn);
    }

    public static void execUpdate(String deleteBeforeAllQuery, String deleteCreateQuery, String deleteDeleteQuery, String deleteUpdateQuery, Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteBeforeAllQuery);
            stmt.executeUpdate(deleteCreateQuery);
            stmt.executeUpdate(deleteDeleteQuery);
            stmt.executeUpdate(deleteUpdateQuery);

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }
}
