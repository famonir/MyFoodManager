package TestDao;

import controller.ExceptionControllers.ExceptionAlertController;
import model.FundamentalClasses.ProductType;
import model.Dao.FundamentalClasses.ProductTypeDao;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**Product type DAO test class
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestProductTypeDao {
    private final ProductTypeDao prodTypeDao = ProductTypeDao.getInstance();
    private final Connection conn = prodTypeDao.getConnection();

    private static final String PRODUCTTYPENAMETOCREATE = "typetocreate";
    private static final String PRODUCTTYPENAMETODELETE = "typetodelete";
    private static final String PRODUCTTYPENAMETOUPDATE = "typetoupdate";
    private static final String PRODUCTTYPENAMEUPDATED = "typeupdated";


    /**Before tests init
     *
     */
    @BeforeAll
    public void initBeforeAll() {

        String queryInsertTypeDel    = String.format(" INSERT INTO product_types (name) VALUES ('%s');", PRODUCTTYPENAMETODELETE);
        String queryInsertTypeUP    = String.format(" INSERT INTO product_types (name) VALUES ('%s');", PRODUCTTYPENAMETOUPDATE);
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(queryInsertTypeDel);
            stmt.executeUpdate(queryInsertTypeUP);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**Test creator
     *
     */
    @Test
    public void createTest(){
        ProductType prodTypeToCreate = new ProductType(PRODUCTTYPENAMETOCREATE);
        prodTypeDao.create(prodTypeToCreate);
        String query = String.format("SELECT * FROM product_types WHERE name = '%s';", PRODUCTTYPENAMETOCREATE);


        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(queryResult.getString("name"), PRODUCTTYPENAMETOCREATE);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

    }

    @Test
    public void updateTest() {
        prodTypeDao.update(PRODUCTTYPENAMEUPDATED,PRODUCTTYPENAMETOUPDATE);
        String query = String.format("SELECT * FROM product_types WHERE name = '%s';", PRODUCTTYPENAMEUPDATED);

        try (Statement stmt = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(queryResult.getString("name"), PRODUCTTYPENAMEUPDATED);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

    }

    @Test
    public void deleteTest() {
        ProductType prodTypeToDelete = new ProductType(PRODUCTTYPENAMETODELETE);
        prodTypeDao.delete(prodTypeToDelete);
        String query = String.format("SELECT * FROM product_types WHERE name = '%s';", PRODUCTTYPENAMETODELETE);

        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertFalse(queryResult.next());
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

    }

    @Test
    void getAllTest() {
        assertNull(prodTypeDao.getAll());
    }

    @Test
    void getByIDTest() throws SQLException {
        assertNull(prodTypeDao.get(0));
    }

    @Test
    void getByNameTest() throws SQLException {
        assertNull(prodTypeDao.get("name"));
    }



    /**After all cleanup
     *
     */
    @AfterAll
    public void cleanUpDB() {
        String deleteCreateQuery = String.format("DELETE FROM product_types WHERE name = '%s'", PRODUCTTYPENAMETOCREATE);
        String deleteUpdateQuery = String.format("DELETE FROM product_types WHERE name = '%s'", PRODUCTTYPENAMEUPDATED);

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteCreateQuery);
            stmt.executeUpdate(deleteUpdateQuery);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

    }

}
