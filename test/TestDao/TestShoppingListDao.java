package TestDao;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.FundamentalClasses.ShoppingListDao;
import model.FundamentalClasses.ShoppingList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestShoppingListDao {
    private final ShoppingListDao shoppingListDao = ShoppingListDao.getInstance();
    private final Connection conn = shoppingListDao.getConnection();
    private final ShoppingList shoppingListToCreate = new ShoppingList(0 ,"testNameToCreate",0,"31 March 2022",false);
    private final ShoppingList shoppingListToUpdate = new ShoppingList(7 ,"testNameToUpdate",0,"66 March 666",true);
    private final ShoppingList shoppingListToDelete = new ShoppingList(0 ,"shoppingListToDelete",6,"01 avril 1994",true);

    /**
     * init before all
     */
    @BeforeAll
    public void initBeforeAll() {
        String query = "INSERT INTO shopping_list (name, date, user, archived) VALUES ('shoppingListToDelete', '01 avril 1994', 6, 1); ";
        this.realizeUpdateQuery(query);
    }

    @Test
    public void getAllUserShoppingListsTest(){
        List<ShoppingList> shoppingListsFromMethod = shoppingListDao.getAllUserShoppingLists(28);
        String query = "SELECT * FROM shopping_list WHERE user = 28 AND archived = 0;";
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            for (ShoppingList element : shoppingListsFromMethod){
                assertTrue(queryResult.next());
                assertEquals(queryResult.getString("name"), element.getName());
                assertEquals(queryResult.getInt("user"), element.getUserID());
                assertEquals(queryResult.getString("date"), element.getDate());
                assertEquals(queryResult.getInt("archived"), element.getArchived() ? 1 : 0);
            }

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void getByNameTest(){
        ShoppingList shoppingList = shoppingListDao.get("Shopping List", 28);
        String query = String.format("SELECT * FROM shopping_list WHERE name = '%s' AND user = %d;", shoppingList.getName(), shoppingList.getUserID());
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(shoppingList.getName(), queryResult.getString("name"));
            assertEquals(shoppingList.getUserID(), queryResult.getInt("user"));
            assertEquals(shoppingList.getDate(), queryResult.getString("date"));
            assertEquals(shoppingList.getArchived() ? 1 : 0, queryResult.getInt("archived"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void getByIdTest(){
        ShoppingList shoppingList = shoppingListDao.get(2);
        String query = "SELECT * FROM shopping_list WHERE id = 2";
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(shoppingList.getID(), queryResult.getInt("id"));
            assertEquals(shoppingList.getName(), queryResult.getString("name"));
            assertEquals(shoppingList.getUserID(), queryResult.getInt("user"));
            assertEquals(shoppingList.getDate(), queryResult.getString("date"));
            assertEquals(shoppingList.getArchived() ? 1 : 0, queryResult.getInt("archived"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void createTest() throws SQLException {
        shoppingListDao.create(shoppingListToCreate);
        String query = String.format("SELECT * FROM shopping_list WHERE name = '%s';", shoppingListToCreate.getName());
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(shoppingListToCreate.getName(), queryResult.getString("name"));
            assertEquals(shoppingListToCreate.getUserID(), queryResult.getInt("user"));
            assertEquals(shoppingListToCreate.getDate(), queryResult.getString("date"));
            assertEquals(shoppingListToCreate.getArchived() ? 1 : 0, queryResult.getInt("archived"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

    }

    @Test
    public void updateTest() throws SQLException {
        shoppingListDao.update(shoppingListToUpdate);
        String query = String.format("SELECT * FROM shopping_list WHERE id = '%s';", shoppingListToUpdate.getID());
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(shoppingListToUpdate.getName(), queryResult.getString("name"));
            assertEquals(shoppingListToUpdate.getUserID(), queryResult.getInt("user"));
            assertEquals(shoppingListToUpdate.getDate(), queryResult.getString("date"));
            assertEquals(shoppingListToUpdate.getArchived() ? 1 : 0, queryResult.getInt("archived"));

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void deleteTest() throws SQLException {
        int idList = 0;
        String query = String.format("SELECT * FROM shopping_list WHERE name = '%s';", shoppingListToDelete.getName());
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            idList = queryResult.getInt("id");
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        shoppingListDao.delete(new ShoppingList(idList ,"shoppingListToDelete",6,"01 avril 1994",true));
        String query2 = String.format("SELECT * FROM shopping_list WHERE name = '%s';", shoppingListToDelete.getName());
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query2)) {
            assertFalse(queryResult.next());
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

    }

    @Test
    public void getUserArchivesTest(){
        List<ShoppingList> shoppingListsFromMethod = shoppingListDao.getUserArchives(28);
        String query = "SELECT * FROM shopping_list WHERE user = 28 AND archived = 1;";
        try (Statement stmt        = conn.createStatement();
             ResultSet queryResult = stmt.executeQuery(query)) {
            for (ShoppingList element : shoppingListsFromMethod){
                assertTrue(queryResult.next());
                assertEquals(queryResult.getString("name"), element.getName());
                assertEquals(queryResult.getInt("user"), element.getUserID());
                assertEquals(queryResult.getString("date"), element.getDate());
                assertEquals(queryResult.getInt("archived"), element.getArchived() ? 1 : 0);
            }

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    void getAllTest() {
        assertNull(shoppingListDao.getAll());
    }

    /**
     * Test to clean up DB
     * @throws SQLException database exception
     */
    @AfterAll
    public void cleanUpDB() throws SQLException {
        String deleteCreateTestQuery = String.format("DELETE FROM shopping_list WHERE name = '%s' ;", shoppingListToCreate.getName());
        shoppingListDao.update(new ShoppingList(7 ,"test",30,"31 March 2022",false));

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteCreateTestQuery);

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**
     * realize update query
     * @param query update/insert/delete query
     */
    public void realizeUpdateQuery(String query){
        try {
            conn.createStatement().executeUpdate(query);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }
}