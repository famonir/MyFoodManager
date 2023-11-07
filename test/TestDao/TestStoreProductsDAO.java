package TestDao;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Stores.StoreDao;
import model.Dao.Stores.StoreProductsDAO;
import model.FundamentalClasses.Product;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestStoreProductsDAO {
    private final StoreDao storeDao = StoreDao.getInstance();
    private final StoreProductsDAO storeProductsDAO = StoreProductsDAO.getInstance();
    private int storeId;
    private final String nameStore = "StoreTest";
    private Product product1 = new Product(1, "Chicken","Meat",-1,"g");
    private final Product product2 = new Product(2, "Milk","Dairy",-2,"mL");
    private final Product productCreateTest = new Product(1, "Chicken","Meat",-1,"g");

    @BeforeAll
    public void initBeforeAll(){
        String query = String.format("""
                INSERT INTO stores (name)
                VALUES ('%s');
                """, nameStore);
        storeDao.realizeUpdateQuery(query);
        storeId = storeDao.getIdFromName(nameStore);
        product1.setPrice((float) 5.3);
        product1.setBrand("brand1");
        product2.setPrice((float) 2.3);
        product2.setBrand("brand2");
        String query1 = String.format(
                Locale.US, "INSERT INTO storeProducts (storeId, productId, price, unit, brand) VALUES ( %d, %d, %f, '%s', '%s'); ",
                storeId, product1.getID(), product1.getPrice(), product1.getUnits(), product1.getBrand()
        );

        String query2 = String.format(
                Locale.US, "INSERT INTO storeProducts (storeId, productId, price, unit, brand) VALUES ( %d, %d, %f, '%s', '%s'); ",
                storeId, product2.getID(), product2.getPrice(), product2.getUnits(), product2.getBrand()
        );
        storeProductsDAO.realizeUpdateQuery(query1);
        storeProductsDAO.realizeUpdateQuery(query2);
    }

    @Test
    @Order(1)
    public void getAllProductsTest(){
        List<Product> productList = storeProductsDAO.getAllProducts(nameStore);
        assertEquals(2, productList.size());
    }

    @Test
    @Order(2)
    public void createTest(){
        productCreateTest.setBrand("Test");
        storeProductsDAO.create(productCreateTest, storeId);
        List<Product> productList = storeProductsDAO.getAllProducts(nameStore);
        assertEquals(3, productList.size());
    }

    @Test
    @Order(3)
    public void updateTest(){
        int oldid = product1.getID();
        int newid = 0;
        String query1 = String.format(Locale.US, "SELECT * FROM storeProducts WHERE storeId = %d AND productId = %d AND price = %f AND brand = '%s';",
                storeId, product1.getID(), product1.getPrice(), product1.getBrand());

        try (ResultSet queryResult = storeProductsDAO.realizeExecuteQuery(query1)) {
            newid = queryResult.getInt("id");
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        product1 = new Product(newid, "Chicken","Meat",-1,"g");
        product1.setBrand("updatedBrand");
        product1.setPrice((float) 6.6);
        product1.setQuantity(200);
        storeProductsDAO.update(product1);
        String query2 = String.format("SELECT * FROM storeProducts WHERE Id = %d;", product1.getID());
        product1 = new Product(oldid, "Chicken","Meat",-1,"g");
        product1.setBrand("updatedBrand");
        product1.setPrice((float) 6.6);
        product1.setQuantity(200);
        try (ResultSet queryResult = storeProductsDAO.realizeExecuteQuery(query2)) {
            assertTrue(queryResult.next());
            assertEquals("updatedBrand", queryResult.getString("brand"));
            assertEquals(6.6, queryResult.getDouble("price"));
            assertEquals(200, queryResult.getInt("quantity"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    @Order(4)
    public void deleteTest(){
        storeProductsDAO.delete(product1, storeId);
        List<Product> productList = storeProductsDAO.getAllProducts(nameStore);
        assertEquals(2, productList.size());
    }

    @AfterAll
    public void cleanUpDB() throws SQLException {
        storeProductsDAO.delete(product2, storeId);
        storeProductsDAO.delete(productCreateTest, storeId);
        storeDao.delete(nameStore);
    }


}
