package TestDao;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.FundamentalClasses.UserDao;
import model.FundamentalClasses.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;



@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestUserDao {

    private static final double DEFAULT_LATITUDE = 50.850346;       // Latitude and longitude of Brussels by default
    private static final double DEFAULT_LONGITUDE = 4.351721;
    private final UserDao userDao = UserDao.getInstance();
    private final Connection conn = userDao.getConnection();

    private final User userToCreate = new User(0, "createtestname", "createtestpassword",
            "createtestsurname", "createtestusername", "createtest@createtest.com", true, DEFAULT_LATITUDE, DEFAULT_LONGITUDE);

    private final User userToGet = new User(-1, "gettestname", "gettestpassword",
            "gettestsurname", "gettestusername", "gettest@gettest.com", false, DEFAULT_LATITUDE, DEFAULT_LONGITUDE);

    private final User userToDelete = new User(-2, "deletetestname", "deletetestpassword",
            "deletetestsurname", "deletetestusername", "deletetest@deletetest.com", true, DEFAULT_LATITUDE, DEFAULT_LONGITUDE);

    private User userToUpdate = new User(-3, "updatetestname", "updatetestpassword",
            "updatetestsurname", "updatetestusername", "updatetest@updatetest.com", true, DEFAULT_LATITUDE, DEFAULT_LONGITUDE);

    /**
     * Before tests init
     */
    @BeforeAll
    public void initBeforeAll() {
        String queryGetTest             = String.format(" INSERT INTO user (name, password, surname, username, email, vegetarian, latitude, longitude) VALUES" +
                " ( '%s', '%s', '%s', '%s', '%s', %d, '%f', '%f'); ", userToGet.getName(),userDao.encrypt(userToGet.getPassword()),userToGet.getSurname(),
                userToGet.getUsername(), userToGet.getEmail(), userToGet.getVegetarian() ? 1 : 0, DEFAULT_LATITUDE, DEFAULT_LONGITUDE);

        String queryDeleteTest             = String.format(" INSERT INTO user (name, password, surname, username, email, vegetarian, latitude, longitude) VALUES" +
                        " ( '%s', '%s', '%s', '%s', '%s', %d, '%f', '%f'); ", userToDelete.getName(),userDao.encrypt(userToDelete.getPassword()),userToDelete.getSurname(),
                userToDelete.getUsername(), userToDelete.getEmail(), userToDelete.getVegetarian() ? 1 : 0, DEFAULT_LATITUDE, DEFAULT_LONGITUDE);

        String queryUpdateTest             = String.format(" INSERT INTO user (name, password, surname, username, email, vegetarian, latitude, longitude) VALUES" +
                        " ( '%s', '%s', '%s', '%s', '%s', %d, '%f', '%f'); ", userToUpdate.getName(),userDao.encrypt(userToUpdate.getPassword()),userToUpdate.getSurname(),
                userToUpdate.getUsername(), userToUpdate.getEmail(), userToUpdate.getVegetarian() ? 1 : 0, DEFAULT_LATITUDE, DEFAULT_LONGITUDE);

        this.realizeUpdateQuery(queryGetTest);
        this.realizeUpdateQuery(queryDeleteTest);
        this.realizeUpdateQuery(queryUpdateTest);
    }

    @Test
    public void encryptTest(){
        assertEquals("8bb6118f8fd6935ad0876a3be34a717d32708ffd", userDao.encrypt("testpassword"));
    }

    @Test
    public void createTest() throws SQLException {
        userDao.create(userToCreate.getName(),userToCreate.getPassword(),userToCreate.getSurname(),
                userToCreate.getUsername(), userToCreate.getEmail(), userToCreate.getVegetarian(), DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        String query = String.format("SELECT * FROM user WHERE username = '%s';", userToCreate.getUsername());

        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(userToCreate.getName(), queryResult.getString("name"));
            assertEquals(userDao.encrypt(userToCreate.getPassword()), queryResult.getString("password"));
            assertEquals(userToCreate.getSurname(), queryResult.getString("surname"));
            assertEquals(userToCreate.getUsername(), queryResult.getString("username"));
            assertEquals(userToCreate.getEmail(), queryResult.getString("email"));
            assertEquals(userToCreate.getVegetarian() ? 1 : 0, queryResult.getInt("vegetarian"));
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void getByIdTest(){
        // we need the id because the method get() work with the user's id
        // we are forced to get the id from the database because we created the test user before putting it in the database
        // and create a new user because we can't set the id of a user instance
        int idUserToGet = 0;
        String query1 = String.format("SELECT * FROM user WHERE username = '%s';", userToGet.getUsername());
        try (ResultSet queryResult1 = realizeExecuteQuery(query1)) {
            assertTrue(queryResult1.next());
            idUserToGet = queryResult1.getInt("id");
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        User userGot = userDao.get(idUserToGet);
        String query2 = String.format("SELECT * FROM user WHERE id = %d;", userGot.getID());
        try (ResultSet queryResult2 = realizeExecuteQuery(query2)) {
            assertTrue(queryResult2.next());
            assertEquals(userGot.getName(), queryResult2.getString("name"));
            assertEquals(userGot.getPassword(), queryResult2.getString("password"));
            assertEquals(userGot.getSurname(), queryResult2.getString("surname"));
            assertEquals(userGot.getUsername(), queryResult2.getString("username"));
            assertEquals(userGot.getEmail(), queryResult2.getString("email"));
            assertEquals(userGot.getVegetarian() ? 1 : 0, queryResult2.getInt("vegetarian"));
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void deleteTest(){
        userDao.delete(userToDelete);
        String query = String.format("SELECT * FROM user WHERE username = '%s';", userToDelete.getUsername());

        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            assertFalse(queryResult.next());
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void updateTest() {
        // we need the id because the method updated() work with the user's id
        // we are forced to get the id from the database because we created the test user before putting it in the database
        // and create a new user because we can't set the id of a user instance
        int idUser = 0;
        String query1 = String.format("SELECT * FROM user WHERE username = '%s';", userToUpdate.getUsername());
        try (ResultSet queryResult1 = realizeExecuteQuery(query1)) {
            assertTrue(queryResult1.next());
            idUser = queryResult1.getInt("id");
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        userToUpdate = new User(idUser, "updatedtestname", userDao.encrypt("updatedtestpassword"),
                "updatedtestsurname", "updatedtestusername", "updatedtest@updatedtest.com", true, DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        userDao.update(userToUpdate);
        String query = String.format("SELECT * FROM user WHERE username = '%s';", userToUpdate.getUsername());

        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            assertTrue(queryResult.next());
            assertEquals(userToUpdate.getName(), queryResult.getString("name"));
            assertEquals(userToUpdate.getPassword(), queryResult.getString("password"));
            assertEquals(userToUpdate.getSurname(), queryResult.getString("surname"));
            assertEquals(userToUpdate.getUsername(), queryResult.getString("username"));
            assertEquals(userToUpdate.getEmail(), queryResult.getString("email"));
            assertEquals(userToUpdate.getVegetarian() ? 1 : 0, queryResult.getInt("vegetarian"));
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    public void loginTest(){
        User userLogin = userDao.login(userToGet.getUsername(), userToGet.getPassword());
        assertEquals(userToGet.getName(), userLogin.getName());
        assertEquals(userToGet.getSurname(), userLogin.getSurname());
        assertEquals(userToGet.getUsername(), userLogin.getUsername());
        assertEquals(userDao.encrypt(userToGet.getPassword()), userLogin.getPassword());
        assertEquals(userToGet.getEmail(), userLogin.getEmail());
        assertEquals(userToGet.getVegetarian(), userLogin.getVegetarian());
    }

    @Test
    void getAllTest() {
        assertNull(userDao.getAll());
    }

    /**
     * After all cleanup
     */
    @AfterAll
    public void cleanUpDB() {
        String deleteCreateTestQuery = String.format("DELETE FROM user WHERE username = '%s'", userToCreate.getUsername());
        String deleteGetTestQuery = String.format("DELETE FROM user WHERE username = '%s'", userToGet.getUsername());
        String deleteUpdateTestQuery = String.format("DELETE FROM user WHERE username = '%s'", userToUpdate.getUsername());

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteCreateTestQuery);
            stmt.executeUpdate(deleteGetTestQuery);
            stmt.executeUpdate(deleteUpdateTestQuery);

        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**
     * realize execute query
     * @param query update/delete/insert query
     * @return result set of the query
     */
    public ResultSet realizeExecuteQuery(String query) {
        ResultSet queryResult = null;

        try {
            queryResult = conn.createStatement().executeQuery(query);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return queryResult;
    }

    public void realizeUpdateQuery(String query){
        try {
            conn.createStatement().executeUpdate(query);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

}
