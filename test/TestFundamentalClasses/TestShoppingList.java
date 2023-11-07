package TestFundamentalClasses;

import model.FundamentalClasses.Product;
import model.FundamentalClasses.ShoppingList;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestShoppingList {
    private final ShoppingList testInstance = new ShoppingList(1,"Bonjour",1, "28 March 2022", false);
    private static final Product product = new Product("testname", "test", 3, "testunit");

    @Test
    public void setNameTest() throws SQLException {
        testInstance.setName("nameChanged");
        assertEquals("nameChanged", testInstance.getName());
        testInstance.setName("Bonjour");

    }

    @Test
    public void setDateTest() throws SQLException {
        testInstance.setDate("dateChanged");
        assertEquals("dateChanged", testInstance.getDate());
        testInstance.setDate("28 March 2022");
    }

    @Test
    public void getDate(){
        assertEquals("28 March 2022", testInstance.getDate());
    }

    @Test
    public void setArchivedTest() throws SQLException {
        testInstance.setArchived(true);
        assertTrue(testInstance.getArchived());
        testInstance.setArchived(false);
    }

    @Test
    public void getArchivedTest(){
        assertFalse(testInstance.getArchived());
    }

    @Test
    public void addProductTest(){
        testInstance.addProduct(product);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        assertEquals(productList, testInstance.getProducts());
        testInstance.removeProduct(product);
    }

    @Test
    public void removeProductTest(){
        testInstance.addProduct(product);
        testInstance.removeProduct(product);
        assertTrue(testInstance.getProducts().isEmpty());
    }
}

