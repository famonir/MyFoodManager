package TestFundamentalClasses;

import model.FundamentalClasses.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**Product class test
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestUser {

    private static final double defaultLatitude = 50.850346;       // Latitude and longitude of Brussels by default
    private static final double defaultLongitude = 4.351721;

    @Test
    public void setSurnameTest(){
        User testInstance = new User(-1,"testname","testpassword","testsurname",
                        "testusername","testemail",true, defaultLatitude, defaultLongitude);
        testInstance.setSurname("surnameChanged");
        assertEquals("surnameChanged", testInstance.getSurname());
    }

    @Test
    public void getSurnameTest(){
        User testInstance = new User(-1,"testname","testpassword","testsurname",
                "testusername","testemail",true, defaultLatitude, defaultLongitude);
        assertEquals("testsurname", testInstance.getSurname());
    }

    @Test
    public void setUsernameTest(){
        User testInstance = new User(-1,"testname","testpassword","testsurname",
                "testusername","testemail",true, defaultLatitude, defaultLongitude);
        testInstance.setUsername("usernameChanged");
        assertEquals("usernameChanged", testInstance.getUsername());
    }

    @Test
    public void getUsernameTest(){
        User testInstance = new User(-1,"testname","testpassword","testsurname",
                "testusername","testemail",true, defaultLatitude, defaultLongitude);
        assertEquals("testusername", testInstance.getUsername());
    }

    @Test
    public void getEmailTest(){
        User testInstance = new User(-1,"testname","testpassword","testsurname",
                "testusername","testemail",true, defaultLatitude, defaultLongitude);
        assertEquals("testemail", testInstance.getEmail());
    }

    @Test
    public void getVegetarianTest(){
        User testInstance = new User(-1,"testname","testpassword","testsurname",
                "testusername","testemail",true, defaultLatitude, defaultLongitude);
        assertTrue(testInstance.getVegetarian());
    }

    @Test
    public void getPasswordTest(){
        User testInstance = new User(-1,"testname","testpassword","testsurname",
                "testusername","testemail",true, defaultLatitude, defaultLongitude);
        assertEquals("testpassword", testInstance.getPassword());
    }

    @Test
    public void getUsersRecipesTest(){
        // method that calls a DAO method that we already tested
    }

    @Test
    public void getUsersShoppingListsTest(){
        // method that calls a DAO method that we already tested
    }

    @Test
    public void createRecipeTest(){
        // method that calls a DAO method that we already tested
    }

}
