package TestImport;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.FundamentalClasses.ProductDao;
import model.Dao.FundamentalClasses.UserDao;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.Recipe;
import model.FundamentalClasses.User;
import model.JSONImport.JSONRecipeReader;
import org.junit.jupiter.api.*;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestJSONRecipeReader {
    private static final double defaultLatitude = 50.850346;       // Latitude and longitude of Brussels by default
    private static final double defaultLongitude = 4.351721;
    final private UserDao userDao = UserDao.getInstance();
    final private ProductDao productDao = ProductDao.getInstance();
    final private JSONObject jsonObjectRecipe = new JSONObject();
    private final JSONRecipeReader jsonReader = new JSONRecipeReader(-1);
    final private User userInstance = new User(-1,"Unit","passwordtest","Test",
            "unittest","unittest@mail.com",false, defaultLatitude, defaultLongitude);
    final private Product product1 = new Product("Product1","Basic",1,"grammes");
    final private Product product2 = new Product("Product2","Meat",2,"kg");
    final private Product product3 = new Product("Product3","Sauce",3,"L");
    final private JSONObject parentProducts = new JSONObject();
    final private JSONObject childProducts = new JSONObject();

    @BeforeAll
    public void init() throws SQLException {
        userDao.create("Unit","passwordtest","Test",
                "unittest","unittest@mail.com",false, defaultLatitude, defaultLongitude);
        productDao.create(product1);
        productDao.create(product2);
        productDao.create(product3);
        jsonObjectRecipe.put("id", "1");
        jsonObjectRecipe.put("name", "testname");
        jsonObjectRecipe.put("peopleAmount", 123);
        jsonObjectRecipe.put("dishType", "testtype");
        jsonObjectRecipe.put("description", "test description");
        FileWriter fileWriter2;
        try (FileWriter filewriter1 = new FileWriter("jsontestrecipe.json")){
            filewriter1.write(jsonObjectRecipe.toString());
        } catch (IOException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        childProducts.put("Product1", 1);
        childProducts.put("Product2", 2);
        childProducts.put("Product3", 3);
        parentProducts.put("products", childProducts);
        try {
            fileWriter2 = new FileWriter("jsontestproducts.json");
            fileWriter2.write(parentProducts.toString());
            fileWriter2.close();
        } catch (IOException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void importRecipeBasicInfosTest(){
        Recipe recipeToTest = jsonReader.importRecipeBasicInfos("jsontestrecipe.json");
        assertEquals(0,recipeToTest.getID());
        assertEquals("testname", recipeToTest.getName());
        assertEquals(-1, recipeToTest.getUserID());
        assertEquals(123, recipeToTest.getPeopleCount());
        assertEquals("testtype", recipeToTest.getDishType());
        assertEquals("test description", recipeToTest.getDescription());

    }

    @Test
    public void importRecipeProductsTest() {
        List<Product> products = jsonReader.importRecipeProducts("jsontestproducts.json");
        assertEquals(3,products.size());
        assertEquals(new Product("Product3", 3), products.get(0));
        assertEquals(new Product("Product1", 1), products.get(1));
        assertEquals(new Product("Product2", 2), products.get(2));
    }

    @AfterAll
    public void cleanUp() throws Exception {
        File file1 = new File("jsontestrecipe.json");
        if (!file1.delete()) {
            throw new Exception(file1.getName() + " not erased.");
        }
        File file2 = new File("jsontestproducts.json");
        if (!file2.delete()) {
            throw new Exception(file2.getName() + " not erased.");
        }
        productDao.delete(product1);
        productDao.delete(product2);
        productDao.delete(product3);
        userDao.delete(userInstance);
    }
}
