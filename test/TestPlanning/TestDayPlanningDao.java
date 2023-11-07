package TestPlanning;

import controller.ExceptionControllers.ExceptionAlertController;


import model.Dao.FundamentalClasses.UserDao;
import model.Dao.Planning.DayPlanningDao;
import model.FundamentalClasses.User;
import model.Planning.DayPlanning;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDayPlanningDao {
    private static final double defaultLatitude = 50.850346;
    private static final double defaultLongitude = 4.351721;
    private final UserDao userDao = UserDao.getInstance();
    private final User userInstance = new User(-1,"Unit","passwordtest","Test",
            "unittest","unittest@mail.com",false, defaultLatitude, defaultLongitude);
    private final DayPlanningDao dayPlanningDao = DayPlanningDao.getInstance();

    private static final int MOINSDEUX = -2;
    private static final int MOINSTROIS = -3;
    private static final int TROIS = 3;
    private static final int QUATRE = 4;
    private static final int CINQ = 5;

    private DayPlanning planningToGet = new DayPlanning(-1, "randomDate1", -1);
    private static DayPlanning planningToDelete = new DayPlanning(MOINSDEUX, "randomDate2", -1);
    private DayPlanning planningToCreate = new DayPlanning(MOINSTROIS,"randomDate3",-1);

    /**
     *  Method init
     */
    @BeforeAll
    public void init() throws SQLException {
        userDao.create("Unit","passwordtest","Test",
                "unittest","unittest@mail.com",false, defaultLatitude, defaultLongitude);
        dayPlanningDao.create(planningToGet);
        dayPlanningDao.create(planningToDelete);
        String query1 = String.format("SELECT * FROM day_planning WHERE user = %d AND date = '%s'", planningToGet.getUserID(), planningToGet.getDate());
        try (ResultSet queryResult = dayPlanningDao.realizeExecuteQuery(query1)) {
            queryResult.next();
            planningToGet = new DayPlanning(queryResult.getInt("id"),
                    queryResult.getString("date"),
                    queryResult.getInt("user"));
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        String query2 = String.format("SELECT * FROM day_planning WHERE user = %d AND date = '%s'", planningToDelete.getUserID(), planningToDelete.getDate());
        try (ResultSet queryResult = dayPlanningDao.realizeExecuteQuery(query2)) {
            queryResult.next();
            planningToDelete = new DayPlanning(queryResult.getInt("id"),
                    queryResult.getString("date"),
                    queryResult.getInt("user"));
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    @Test
    @Order(1)
    public void getByIdDayPlanningTest() throws SQLException {
       DayPlanning dayPlanningReturned = dayPlanningDao.get(planningToGet.getID());
       assertEquals(planningToGet.getID(),dayPlanningReturned.getID());
       assertEquals(planningToGet.getUserID(),dayPlanningReturned.getUserID());
       assertEquals(planningToGet.getDate(),dayPlanningReturned.getDate());
    }

    @Test
    @Order(2)
    public void getByUserAndDateTest() throws SQLException {
        DayPlanning dayPlanningReturned = dayPlanningDao.get(planningToGet.getUserID(), planningToGet.getDate());
        assertEquals(planningToGet.getID(),dayPlanningReturned.getID());
        assertEquals(planningToGet.getUserID(),dayPlanningReturned.getUserID());
        assertEquals(planningToGet.getDate(),dayPlanningReturned.getDate());
    }

    @Test
    @Order(TROIS)
    public void updateTest() throws SQLException {
        DayPlanning dayPlanningUpdated = new DayPlanning(planningToGet.getID(),"updatedDate",planningToGet.getUserID());
        dayPlanningDao.update(dayPlanningUpdated);
        DayPlanning dayPlanningReturned = dayPlanningDao.get(planningToGet.getID());
        assertEquals(dayPlanningUpdated.getDate(),dayPlanningReturned.getDate());
    }

    @Test
    @Order(QUATRE)
    public void createTest() throws SQLException {
        dayPlanningDao.create(planningToCreate);
        String query = String.format("SELECT * FROM day_planning WHERE user = %d AND date = '%s'", planningToCreate.getUserID(), planningToCreate.getDate());
        try (ResultSet queryResult = dayPlanningDao.realizeExecuteQuery(query)) {
            queryResult.next();
            planningToCreate = new DayPlanning(queryResult.getInt("id"),
                    queryResult.getString("date"),
                    queryResult.getInt("user"));
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        DayPlanning dayPlanningReturned = dayPlanningDao.get(planningToCreate.getUserID(), planningToCreate.getDate());
        assertEquals(planningToCreate.getUserID(), dayPlanningReturned.getUserID());
        assertEquals(planningToCreate.getDate(), dayPlanningReturned.getDate());
    }

    @Test
    @Order(CINQ)
    public void deleteTest() throws SQLException {
        dayPlanningDao.delete(planningToDelete);
        String query = String.format("SELECT * FROM day_planning WHERE user = %d AND date = '%s'", planningToDelete.getUserID(), planningToDelete.getDate());
        try (ResultSet queryResult = dayPlanningDao.realizeExecuteQuery(query)) {
            assertFalse(queryResult.next());
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**
     * Method test to cleanUpDB
     */
    @AfterAll
    public void cleanUpDB() throws SQLException {
        dayPlanningDao.delete(planningToCreate);
        dayPlanningDao.delete(planningToGet);
        userDao.delete(userInstance);
    }
}
