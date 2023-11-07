package TestDao;

import controller.ExceptionControllers.ExceptionAlertController;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ProductType;
import model.Dao.FundamentalClasses.ProductDao;
import model.Dao.FundamentalClasses.ProductTypeDao;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestProductDao {
    private final ProductTypeDao prodTypeDao = ProductTypeDao.getInstance();
    private final Connection conn = prodTypeDao.getConnection();
    private final ProductDao productDao = ProductDao.getInstance();

    private final ProductType prodTypeToCreate = new ProductType("test");
    private static final Product productToCreate = new Product("testcreatename", "test", 0, "testcreateunit");
    private static final Product productToUpdate = new Product("testtoupdatename", "test", 1, "testtoupdateunit");
    private Product productUpdated  = null;
    private static final Product productToDelete = new Product("testdeletename", "test", 3, "testdeleteunit");
    private static final Product productToSelect = new Product("testselectname", "test", 4, "testselectunit");


    @BeforeAll
    void initBeforeAll(){

       String queryInsertProdType      = String.format(" INSERT INTO product_types (name) VALUES ('%s');", prodTypeToCreate.getName()); // we need a product type for our tests with the products
       String queryInsertProdToDel    = String.format(" INSERT INTO product (name, type, unit) VALUES ('%s', '%s', '%s');",
                                                        productToDelete.getName(), productToDelete.getCategory(), productToDelete.getUnits());
       String queryInsertProdToUp     = String.format(" INSERT INTO product (name, type, unit) VALUES ('%s', '%s', '%s');",
                                                        productToUpdate.getName(), productToUpdate.getCategory(), productToUpdate.getUnits());
        String queryInsertProdToSelect     = String.format(" INSERT INTO product (name, type, unit) VALUES ('%s', '%s', '%s');",
                                                        productToSelect.getName(), productToSelect.getCategory(), productToSelect.getUnits());
        TestRecipeDao.execUpdate(queryInsertProdType, queryInsertProdToDel, queryInsertProdToUp, queryInsertProdToSelect, conn);

        String queryProductToUpdate  = String.format("SELECT * FROM product WHERE name = '%s';", productToUpdate.getName());
        try (Statement stmt = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(queryProductToUpdate)) {
             queryResult.next();
             productUpdated = new Product(queryResult.getInt("id"), "testupdatedname",     // we initialize here because we need the ID given automatically
                                            "test", 2, "testupdatedunit");             // by the database
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }


    }


    @Test
    void getAllTest() {
        List<Product> allProductsByMethod = productDao.getAll();
        String querySelectAll         = "SELECT * FROM product;";
        List<model.FundamentalClasses.Product> allProductsByQuery;
        try (ResultSet queryResult = productDao.realizeExecuteQuery(querySelectAll)) {
            allProductsByQuery = new ArrayList<>();

            while(queryResult.next()) {
                allProductsByQuery.add(new model.FundamentalClasses.Product(queryResult.getInt("id"), queryResult.getString("name"), queryResult.getString("type"),
                        0, queryResult.getString("unit")));
            }
            assertEquals(allProductsByMethod.size(),allProductsByQuery.size());
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }


    }

    @Test
    void getByIDTest() throws SQLException {
        Product productSelectedByMethod = ProductDao.getInstance().get(1);
        String query  = String.format("SELECT * FROM product WHERE id = %d;", 1);
        compareWithQuery(productSelectedByMethod, query);
    }

    @Test
    void getByNameTest() {
        Product productSelectedByMethod = ProductDao.getInstance().get(productToSelect.getName());
        String query  = String.format("SELECT * FROM product WHERE name = '%s';", productToSelect.getName());
        compareWithQuery(productSelectedByMethod, query);
    }

    private void compareWithQuery(Product productSelectedByMethod, String query) {
        try (Statement stmt = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(productSelectedByMethod.getID(),queryResult.getInt("id"));
            assertEquals(productSelectedByMethod.getName(),queryResult.getString("name"));
            assertEquals(productSelectedByMethod.getCategory(), queryResult.getString("type"));
            assertEquals(productSelectedByMethod.getUnits(), queryResult.getString("unit"));
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    void deleteTest() {
        ProductDao.getInstance().delete(productToDelete);
        String query = String.format("SELECT * FROM product WHERE name = '%s';", productToDelete.getName());

        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertFalse(queryResult.next());
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    void updateTest() {
        ProductDao.getInstance().update(productUpdated);
        compareProducts(productUpdated);
    }

    @Test
    void createTest() {
        ProductDao.getInstance().create(productToCreate);
        compareProducts(productToCreate);
    }

    // Extracted method to avoid code duplication
    private void compareProducts(Product productToCreate) {
        String query  = String.format("SELECT * FROM product WHERE name = '%s';", productToCreate.getName());
        try (Statement stmt = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(productToCreate.getName(), queryResult.getString("name"));
            assertEquals(productToCreate.getCategory(), queryResult.getString("type"));
            assertEquals(productToCreate.getUnits(), queryResult.getString("unit"));
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }


    @AfterAll
    public void cleanUpDB() {
        String deleteProdToCreateQuery = String.format("DELETE FROM product WHERE name = '%s'", productToCreate.getName());
        String deleteProdToSelectQuery = String.format("DELETE FROM product WHERE name = '%s'", productToSelect.getName());
        String deleteProdUpdatedQuery = String.format("DELETE FROM product WHERE name = '%s'", productUpdated.getName());
        String deleteProdTypeQuery = String.format("DELETE FROM product_types WHERE name = '%s'", prodTypeToCreate.getName());
        TestRecipeDao.execUpdate(deleteProdToCreateQuery, deleteProdToSelectQuery, deleteProdUpdatedQuery, deleteProdTypeQuery, conn);

    }

}