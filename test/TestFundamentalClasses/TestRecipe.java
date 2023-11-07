package TestFundamentalClasses;

import model.FundamentalClasses.Product;
import model.FundamentalClasses.Recipe;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**Product class test
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRecipe {
    private final Recipe testInstance = new Recipe(46,"Tomato Halloumi Pasta", 30, 8,
                                            "Lunch", "default desc", false);

    private final Product product = new Product(1,"Chicken","Meat", 30,"g");

    @Test
    public void setNameTest() throws SQLException {
        testInstance.setName("testname");
        assertEquals("testname", testInstance.getName());
        testInstance.setName("Tomato Halloumi Pasta");
    }

    @Test
    public void setFavouriteTest() throws SQLException {
        testInstance.setFavourite(true);
        assertTrue(testInstance.getFavourite());
        testInstance.setFavourite(false);
    }

    @Test
    public void getFavouriteTest(){
        assertFalse(testInstance.getFavourite());
    }

    @Test
    public void setPeopleCountTest() throws SQLException {
        testInstance.setPeopleCount(123);
        assertEquals(123, testInstance.getPeopleCount());
        testInstance.setPeopleCount(6);
    }

    @Test
    public void setPeopleCountNoDBUpdateTest(){

    }

    @Test
    public void setPeopleCountPlanningInitTest(){

    }

    @Test
    public void  getPeopleCountTest(){
        assertEquals(8, testInstance.getPeopleCount());
    }

    @Test
    public void increasePeopleCountTest() throws SQLException {
        testInstance.increasePeopleCount();
        assertEquals(9, testInstance.getPeopleCount());
        testInstance.decreasePeopleCount();
    }

    @Test
    public void decreasePeopleCountTest() throws SQLException {
        testInstance.decreasePeopleCount();
        assertEquals(7, testInstance.getPeopleCount());
        testInstance.increasePeopleCount();
    }

    @Test
    public void setDishTypeTest() throws SQLException {
        testInstance.setDishType("testtype");
        assertEquals("testtype", testInstance.getDishType());
        testInstance.setDishType("Lunch");
    }

    @Test
    public void setDescriptionTest() throws SQLException {
        testInstance.setDescription("test description");
        assertEquals("test description", testInstance.getDescription());
        testInstance.setDescription("default desc");
    }

    @Test
    public void getDescriptionTest(){
        assertEquals("default desc", testInstance.getDescription());
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
    public void setProductTest(){
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        testInstance.setProduct(productList);
        assertEquals(productList, testInstance.getProducts());
        testInstance.removeProduct(product);
    }

    @Test
    public void removeProductTest(){
        testInstance.addProduct(product);
        testInstance.removeProduct(product);
        assertTrue(testInstance.getProducts().isEmpty());
    }

    @Test
    public void equalsTest(){
        assertEquals(testInstance, testInstance);
    }

    @Test
    public void hashCodeTest(){
        assertEquals(1684690954, testInstance.hashCode());
    }
}
