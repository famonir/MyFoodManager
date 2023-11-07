package TestExport;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.FundamentalClasses.ProductDao;
import model.Dao.FundamentalClasses.ProductsListDao;
import model.Dao.FundamentalClasses.ShoppingListDao;
import model.Dao.FundamentalClasses.UserDao;
import model.Exporter.Exporter;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ShoppingList;
import model.FundamentalClasses.User;
import org.junit.jupiter.api.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Exporter test
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestExporter {
    double defaultLatitude = 50.850346;
    double defaultLongitude = 4.351721;
    final private UserDao userDao = UserDao.getInstance();
    final private Connection conn = userDao.getConnection();
    final private ProductDao productDao = ProductDao.getInstance();
    final private ShoppingListDao shoppingListDao = ShoppingListDao.getInstance();
    final private ProductsListDao productsListDao = ProductsListDao.getInstance();
    final private User userInstance = new User(-1,"Unit","passwordtest","Test",
            "unittest","unittest@mail.com",false, defaultLatitude, defaultLongitude);
    final private Product product1 = new Product("Product1","Basic",1,"grammes");
    final private Product product2 = new Product("Product2","Meat",2,"kg");
    final private Product product3 = new Product("Product3","Sauce",3,"L");
    ShoppingList shoppingListInstance;


    @BeforeAll
    public void dbInit() throws SQLException {
        int idUser = 0;
        int idShoppingList = 0;
        userDao.create("Unit","passwordtest","Test",
                "unittest","unittest@mail.com",false, defaultLatitude, defaultLongitude);
        productDao.create(product1);
        productDao.create(product2);
        productDao.create(product3);
        String query1 = String.format("SELECT * FROM user WHERE username = '%s';", userInstance.getUsername());

        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query1)) {
            idUser = queryResult.getInt("id");

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        shoppingListInstance = new ShoppingList(-1,"testList", idUser,"randomdate",false);
        shoppingListDao.create(shoppingListInstance);

        String query2 = String.format("SELECT * FROM shopping_list WHERE name = '%s' AND date = '%s' AND user = %d;",
                                        shoppingListInstance.getName(), shoppingListInstance.getDate(), shoppingListInstance.getUserID());

        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query2)) {
            idShoppingList = queryResult.getInt("id");

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        shoppingListInstance = new ShoppingList(idShoppingList,"testList", idUser,"randomdate",false);
        shoppingListInstance.addProduct(product1);
        shoppingListInstance.addProduct(product2);
        shoppingListInstance.addProduct(product3);

    }

    @Test
    public void exportSetUpTest() throws Exception {
        File testFile = new File("test.txt");
        if (!testFile.createNewFile()) {
            throw new Exception("File not " + testFile.getName() + " not created.");
        }
        Exporter testExporter = new Exporter(shoppingListInstance, testFile);
        testExporter.exportSetUp();
        BufferedReader brTest = new BufferedReader(new FileReader(testFile));
        String actualText = brTest .readLine();
        String expectedText = "   testList Shopping List ";
        brTest.close();
        if (!testFile.delete()) {
            throw new Exception("File not " + testFile.getName() + " not erased.");
        }
        assertEquals(expectedText,actualText);

    }

    @AfterAll
    public void dbCleanUp() throws SQLException {
        String query = String.format(Locale.US,"DELETE FROM products_list WHERE list = %d", shoppingListInstance.getID());
        productsListDao.realizeUpdateQuery(query);
        shoppingListDao.delete(shoppingListInstance);
        productDao.delete(product1);
        productDao.delete(product2);
        productDao.delete(product3);
        userDao.delete(userInstance);

    }
}
