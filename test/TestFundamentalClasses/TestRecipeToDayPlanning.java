package TestFundamentalClasses;

import model.FundamentalClasses.RecipeToDayPlanning;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**Product class test
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRecipeToDayPlanning {
    RecipeToDayPlanning testInstance = new RecipeToDayPlanning(-1,-2,-3, -4);

    @Test
    public void getDayIDTest(){
        assertEquals(-1, testInstance.getDayID());
    }

    @Test
    public void getRecipeIDTest(){
        assertEquals(-2, testInstance.getRecipeID());
    }

    @Test
    public void getGuestsCountTest(){
        assertEquals(-3, testInstance.getGuestsCount());
    }

    @Test
    public void getOldGuestsCountTest(){
        assertEquals(-4, testInstance.getOldGuestsCount());
    }


}
