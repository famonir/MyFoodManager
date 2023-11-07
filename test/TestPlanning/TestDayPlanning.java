package TestPlanning;

import model.Dao.Planning.RecipesToDayDao;
import model.FundamentalClasses.Recipe;
import model.Planning.DayPlanning;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**DayPlanning class test
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class TestDayPlanning {

    private static final int testID = 0;
    private static final String testDate = "01/01/2022";
    private static final int testUserID = 0;

    private final RecipesToDayDao recipesToDayDao = RecipesToDayDao.getInstance();
    private final DayPlanning testDayPlanning = new DayPlanning(testID, testDate, testUserID);
    Recipe testRecipe = new Recipe(0, "testRecipe for planning", testUserID, 2, "testType", "testDescription", false);

    private final List<Recipe> testPlannedRecipes = recipesToDayDao.getDayRecipes(testID);


    @Test
    @Order(1)
    void getIDTest() {
        assertEquals(testID, testDayPlanning.getID());
    }

    @Test
    @Order(2)
    void getDateTest() {
        assertEquals(testDate, testDayPlanning.getDate());
    }

    @Test
    @Order(3)
    void getUserIDTest() {
        assertEquals(testUserID, testDayPlanning.getUserID());
    }

    @Test
    @Order(4)
    void getRecipesTest() {
        assertEquals(testPlannedRecipes, testDayPlanning.getRecipes());
    }

    @Test
    @Order(5)
    void addRecipeTest() throws SQLException {
        testPlannedRecipes.add(testRecipe);
        testDayPlanning.addRecipe(testRecipe, testID);
        assertEquals(testPlannedRecipes, testDayPlanning.getRecipes());
    }

    @Test
    @Order(6)
    void removeRecipeTest() throws SQLException {
        testPlannedRecipes.remove(testRecipe);
        testDayPlanning.removeRecipe(testRecipe, testID);
        assertEquals(testPlannedRecipes, testDayPlanning.getRecipes());
    }

    @Test
    @Order(7)
    void cleanAllRecipesTest() {
        // method calls another method tested in TestRecipesToDayDao
    }
}
