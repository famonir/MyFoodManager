package TestDao;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Planning.RecipesToDayDao;
import model.FundamentalClasses.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**RecipesToDayDao class test
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRecipesToDayDao {
    private final RecipesToDayDao recipesToDayDao = RecipesToDayDao.getInstance();
    private final Connection conn = recipesToDayDao.getConnection();
    private final RecipeToDayPlanning createTestInstance = new RecipeToDayPlanning(20,34,-30,-1);
    private final RecipeToDayPlanning beforeUpdateTestInstance = new RecipeToDayPlanning(9,58,-123,2);
    private final RecipeToDayPlanning updateTestInstance = new RecipeToDayPlanning(9,58,-66,-123);
    private final RecipeToDayPlanning getTestInstance = new RecipeToDayPlanning(21,34,-39,-1);
    private final RecipeToDayPlanning deleteUserDayTestInstance = new RecipeToDayPlanning(22,34,-35,-1);
    private final RecipeToDayPlanning deleteTestInstance = new RecipeToDayPlanning(22,34,-35,-1);
    private final RecipeToDayPlanning getByIdRecipeTestInstance = new RecipeToDayPlanning(2,34,2,-1);


    @BeforeAll
    public void TestInit() throws SQLException {
        recipesToDayDao.create(getTestInstance);
        recipesToDayDao.create(deleteUserDayTestInstance);
        recipesToDayDao.create(getByIdRecipeTestInstance);
        recipesToDayDao.create(beforeUpdateTestInstance);

    }

    @Test
    public void getAllTest(){
        assertNull(recipesToDayDao.getAll());}

    @Test
    public void getByIdRecipesToDayTest() throws SQLException {
        int idTest = 0;
        String query = String.format("SELECT * FROM recipes_to_day WHERE day = %d AND recipe = %d AND guests_count = %d;",
                getByIdRecipeTestInstance.getDayID(),getByIdRecipeTestInstance.getRecipeID(),getByIdRecipeTestInstance.getGuestsCount());
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            idTest = queryResult.getInt("id");

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        RecipeToDayPlanning testInstance = recipesToDayDao.get(idTest);
        assertEquals(2,testInstance.getDayID());
        assertEquals(34,testInstance.getRecipeID());
        assertEquals(2,testInstance.getGuestsCount());
    }

    @Test
    public void getByNameTest() throws SQLException {
        assertNull(recipesToDayDao.get("test"));}

    @Test
    public void createTest() throws SQLException {
        recipesToDayDao.create(createTestInstance);
        String query = String.format("SELECT * FROM recipes_to_day WHERE day = %d AND recipe = %d AND guests_count = %d;",
                createTestInstance.getDayID(),createTestInstance.getRecipeID(),createTestInstance.getGuestsCount());
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(20, queryResult.getInt("day"));
            assertEquals(34, queryResult.getInt("recipe"));
            assertEquals(-30, queryResult.getInt("guests_count"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void updateTest() throws SQLException {
        recipesToDayDao.update(updateTestInstance);
        String query = String.format("SELECT * FROM recipes_to_day WHERE day = %d AND recipe = %d AND guests_count = %d;",
                updateTestInstance.getDayID(),updateTestInstance.getRecipeID(),updateTestInstance.getGuestsCount());
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(9, queryResult.getInt("day"));
            assertEquals(58, queryResult.getInt("recipe"));
            assertEquals(-66, queryResult.getInt("guests_count"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void deleteTest() throws SQLException {
        recipesToDayDao.delete(deleteTestInstance);
        String query = String.format("SELECT * FROM recipes_to_day WHERE day = %d AND recipe = %d AND guests_count = %d;",
                deleteTestInstance.getDayID(),deleteTestInstance.getRecipeID(),deleteTestInstance.getGuestsCount());
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertFalse(queryResult.next());

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void getDayRecipesTest(){
        List<Recipe> testResult = recipesToDayDao.getDayRecipes(21);
        String description = """
                Slice the bacon slices in little cubes. Put them in a pan at medium\s
                high heat until they become crispy. Put aside
                At the same time, put some water to boil.
                Then prepare the sauce :\s
                 Put 3 Egg yolks and 3 whole eggs in a bowl, add half the pecorino,
                and stir until it makes little bubbles.
                When the pasta is ready, put two spoons of cooking water in the\s
                egg sauce and stir fast.
                Then put the pasta without the water in the pan with the bacon,
                stir, and when the pan is not too hot you can add the sauce.
                Stir, add pepper, the pecorino you had left and enjoy!""";
        Recipe recipeTest = new Recipe(34,"Pasta Carbonara",30,-39,
                "Supper", description , false);
        assertEquals(1,testResult.size());
        assertEquals(testResult.get(0), recipeTest);

    }

    @Test
    public void deleteUsersDayTest(){
        recipesToDayDao.deleteUsersDay(deleteUserDayTestInstance.getDayID());
        String query = String.format("SELECT * FROM recipes_to_day WHERE day = %d", deleteUserDayTestInstance.getDayID());
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertFalse(queryResult.next());

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @AfterAll
    public void cleanUpDB() throws SQLException {
        String deleteCreateTestQuery = String.format("DELETE FROM recipes_to_day WHERE day = %d AND recipe = %d AND guests_count = %d;",
                createTestInstance.getDayID(),createTestInstance.getRecipeID(),createTestInstance.getGuestsCount());

        String deleteGetTestQuery = String.format("DELETE FROM recipes_to_day WHERE day = %d AND recipe = %d AND guests_count = %d;",
                getTestInstance.getDayID(),getTestInstance.getRecipeID(),getTestInstance.getGuestsCount());

        String deleteGetByIdRecipeTestQuery = String.format("DELETE FROM recipes_to_day WHERE day = %d AND recipe = %d AND guests_count = %d;",
                getByIdRecipeTestInstance.getDayID(),getByIdRecipeTestInstance.getRecipeID(),getByIdRecipeTestInstance.getGuestsCount());

        String deleteUpdateTestQuery = String.format("DELETE FROM recipes_to_day WHERE day = %d AND recipe = %d AND guests_count = %d;",
                updateTestInstance.getDayID(),updateTestInstance.getRecipeID(),updateTestInstance.getGuestsCount());

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteCreateTestQuery);
            stmt.executeUpdate(deleteGetTestQuery);
            stmt.executeUpdate(deleteGetByIdRecipeTestQuery);
            stmt.executeUpdate(deleteUpdateTestQuery);

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }
}
