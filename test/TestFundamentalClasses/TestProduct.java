package TestFundamentalClasses;

import model.FundamentalClasses.Product;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**Product class test
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestProduct {
    private static final String unitTestString = "unitTest";
    private static final String categoryTestString = "categoryTest";
    private static final String productTestString = "productTest";

    @Test
    public void hashCodeTest(){
        Product productTest = new Product(-1, productTestString, categoryTestString,-1, unitTestString);
        assertEquals(0, productTest.hashCode());

    }

    @Test
    public void setQuantityTest(){
        Product productTest = new Product(-1, productTestString, categoryTestString,-1, unitTestString);
        productTest.setQuantity(123);
        assertEquals(123, productTest.getQuantity());
    }

    @Test
    public void getQuantityTest(){
        Product productTest = new Product(-1,productTestString, categoryTestString,-1, unitTestString);
        assertEquals(-1, productTest.getQuantity());
    }

    @Test
    public void getCategoryTest(){
        Product productTest = new Product(-1,productTestString, categoryTestString,-1, unitTestString);
        assertEquals("categoryTest", productTest.getCategory());
    }

    @Test
    public void getUnitTest(){
        Product productTest = new Product(-1,productTestString, categoryTestString,-1, unitTestString);
        assertEquals("unitTest", productTest.getUnits());
    }

    @Test
    public void equalsTest(){
        Product productTest = new Product(-1,productTestString, categoryTestString,-1, unitTestString);
        assertEquals(productTest, productTest);

    }

    @Test
    public void increaseQuantityTest(){
        Product productTest = new Product(-1,productTestString, categoryTestString,5, unitTestString);
        productTest.increaseQuantity();
        assertEquals(6,productTest.getQuantity());
    }

    @Test
    public void decreaseQuantityTest(){
        Product productTest = new Product(-1,productTestString, categoryTestString,5, unitTestString);
        productTest.decreaseQuantity();
        assertEquals(4,productTest.getQuantity());
    }
}
