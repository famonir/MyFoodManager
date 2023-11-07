package TestFundamentalClasses;

import model.FundamentalClasses.ProductPerList;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**Product class test
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestProductPerList {
    final private ProductPerList testProductPerList = new ProductPerList(-1,-2,-3);

    @Test
    public void getProductIDTest(){
        assertEquals(-1, testProductPerList.productID());
    }

    @Test
    public void getQuantityTest(){
        assertEquals(-2, testProductPerList.getQuantity());
    }

    @Test
    public void getListIDTest(){
        assertEquals(-3, testProductPerList.getListID());
    }
}
