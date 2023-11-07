package TestDao;

import controller.ExceptionControllers.ExceptionAlertController;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ProductPerList;
import model.Dao.FundamentalClasses.ProductsListDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestProductsListDao {
    private final ProductsListDao prodListDao = ProductsListDao.getInstance();
    private final Connection conn = prodListDao.getConnection();

    private final ProductPerList prodListToCreate = new ProductPerList(3,12,6);
    private final ProductPerList prodListToUpdate = new ProductPerList(2,666,5);
    private final ProductPerList prodListToDelete = new ProductPerList(60,666,6);


    /**
     * init before all
     */
    @BeforeAll
    public void initBeforeAll() {
        String query = "INSERT INTO products_list (product, quantity, list) VALUES ( 60, 666, 6); ";
        this.realizeUpdateQuery(query);
    }

    @Test
    public void getByShoppingListTest(){
        List<Product> products = prodListDao.getByShoppingList(5);
        assertEquals(1, products.size());
        assertEquals("Milk", products.get(0).getName());

    }

    @Test
    public void createTest(){
        prodListDao.create(prodListToCreate);
        String query = "SELECT * FROM products_list WHERE product = 3 AND quantity = 12 AND list = 6;";
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(3, queryResult.getInt("product"));
            assertEquals(12, queryResult.getInt("quantity"));
            assertEquals(6, queryResult.getInt("list"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void updateTest(){
        prodListDao.update(prodListToUpdate);
        String query = "SELECT * FROM products_list WHERE product = 2 AND quantity = 666 AND list = 5;";
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(2, queryResult.getInt("product"));
            assertEquals(666, queryResult.getInt("quantity"));
            assertEquals(5, queryResult.getInt("list"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void deleteTest(){
        prodListDao.delete(prodListToDelete);
        String query = "SELECT * FROM products_list WHERE product = 60 AND quantity = 666 AND list = 6;";
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertFalse(queryResult.next());

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

    }

    @Test
    void getAllTest() {
        assertNull(prodListDao.getAll());
    }

    @Test
    void getByIDTest() throws SQLException {
        assertNull(prodListDao.get(0));
    }

    @Test
    void getByNameTest() throws SQLException {
        assertNull(prodListDao.get("Name"));
    }

    /**
     * test clean up DB
     */
    @AfterAll
    public void cleanUpDB() {
        String deleteCreateTestQuery = "DELETE FROM products_list WHERE product = 3 AND quantity = 12 AND list = 6;";
        prodListDao.update(new ProductPerList(2,25,5));

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteCreateTestQuery);

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**
     * reaalize update query
     * @param query update/delete/insert query to be realized
     */
    public void realizeUpdateQuery(String query){
        try {
            conn.createStatement().executeUpdate(query);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }
}
