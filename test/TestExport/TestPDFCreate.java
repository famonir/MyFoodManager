package TestExport;

import com.itextpdf.text.DocumentException;
import controller.ExceptionControllers.ExceptionAlertController;

import model.Dao.FundamentalClasses.ProductDao;
import model.Dao.FundamentalClasses.ProductsListDao;
import model.Dao.FundamentalClasses.ShoppingListDao;
import model.Dao.FundamentalClasses.UserDao;
import model.Exporter.PDFCreate;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ShoppingList;
import model.FundamentalClasses.User;
import org.junit.jupiter.api.*;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPDFCreate {
    double defaultLatitude = 50.850346;
    double defaultLongitude = 4.351721;
    final private File fileInstance = new File("randomfile.pdf");
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
    public void getFileTest(){
        ShoppingList shoppingListTest = new ShoppingList(-1,"randomname",-1,"randomdate",false);
        PDFCreate pdfCreate = new PDFCreate(shoppingListTest, fileInstance);
        assertEquals(fileInstance, pdfCreate.getFile());
    }

    @Test
    public void setUpPdfTest() throws DocumentException, IOException {
        PDFCreate pdfToSetup = new PDFCreate(shoppingListInstance, fileInstance);
        pdfToSetup.setUpPdf();
        assertTrue(fileInstance.exists());
    }

    @AfterAll
    public void dbCleanUp() throws Exception {
        String query = String.format(Locale.US,"DELETE FROM products_list WHERE list = %d", shoppingListInstance.getID());
        productsListDao.realizeUpdateQuery(query);
        shoppingListDao.delete(shoppingListInstance);
        productDao.delete(product1);
        productDao.delete(product2);
        productDao.delete(product3);
        userDao.delete(userInstance);
        if (!fileInstance.delete()) {
            throw new Exception("File not " + fileInstance.getName() + " not erased.");
        }

    }
}
