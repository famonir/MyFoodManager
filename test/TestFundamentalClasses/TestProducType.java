package TestFundamentalClasses;

import model.FundamentalClasses.ProductType;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**Product class test
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestProducType {

    @Test
    public void getTest(){
        ProductType productTypeTest = new ProductType("productTypeTest");
        assertEquals("productTypeTest", productTypeTest.getName());
    }

    @Test
    public void setTest() throws SQLException {
        ProductType productTypeTest = new ProductType("productTypeTest");
        productTypeTest.setName("changedName");
        assertEquals("changedName", productTypeTest.getName());
    }
}
