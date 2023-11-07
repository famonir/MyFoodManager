package TestDao;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.RecipeDao.RecipeListDao;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ProductPerList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRecipeListDao {
    private final RecipeListDao recipeListDao = RecipeListDao.getInstance();
    private final Connection conn = recipeListDao.getConnection();

    private final ProductPerList recipeListToDelete = new ProductPerList(60,666,21);


    @BeforeAll
    public void initBeforeAll() {
        String query = "INSERT INTO recipe_list (product, quantity, recipe) VALUES ( 60, 666, 21); ";
        this.realizeUpdateQuery(query);

    }

    @Test
    public void getProductsTest(){
        List<Product> productsFromMethod = recipeListDao.getProducts(23); // Recipe: Omelet
        assertEquals(6, productsFromMethod.size());
        assertEquals("Eggs",productsFromMethod.get(0).getName());
        assertEquals("Olive Oil",productsFromMethod.get(1).getName());
        assertEquals("Pork",productsFromMethod.get(2).getName());
        assertEquals("Rice",productsFromMethod.get(3).getName());
        assertEquals("Salt",productsFromMethod.get(4).getName());
        assertEquals("Tomato",productsFromMethod.get(5).getName());

            }

    @Test
    public void createTest(){
        recipeListDao.create(new ProductPerList(1,100,23));
        String query = "SELECT * FROM recipe_list WHERE product = 1 AND quantity = 100 AND recipe = 23;";
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(1, queryResult.getInt("product"));
            assertEquals(100, queryResult.getInt("quantity"));
            assertEquals(23, queryResult.getInt("recipe"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

    }

    @Test
    public void updateTest(){
        recipeListDao.update(new ProductPerList(9,123,21));
        String query = "SELECT * FROM recipe_list WHERE product = 9 AND quantity = 123 AND recipe = 21;";
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(9, queryResult.getInt("product"));
            assertEquals(123, queryResult.getInt("quantity"));
            assertEquals(21, queryResult.getInt("recipe"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

    }

    @Test
    public void deleteTest(){
        recipeListDao.delete(recipeListToDelete);
        String query = "SELECT * FROM recipe_list WHERE product = 60 AND quantity = 666 AND recipe = 21;";
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
             assertFalse(queryResult.next());

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    void getAllTest() {
        assertNull(recipeListDao.getAll());
    }

    @Test
    void getByIDTest() throws SQLException {
        assertNull(recipeListDao.get(0));
    }

    @Test
    void getByNameTest() throws SQLException {
        assertNull(recipeListDao.get("name"));
    }

    @AfterAll
    public void cleanUpDB() {
        String deleteCreateTestQuery = "DELETE FROM recipe_list WHERE product = 1 AND quantity = 100 AND recipe = 23 ;";
        recipeListDao.update(new ProductPerList(9,10,21));

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteCreateTestQuery);

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    public void realizeUpdateQuery(String query){
        try {
            conn.createStatement().executeUpdate(query);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }


}
