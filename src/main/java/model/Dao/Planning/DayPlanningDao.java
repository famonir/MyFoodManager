package model.Dao.Planning;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Dao;
import model.Planning.DayPlanning;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Day planning DAO class
 */
public final class DayPlanningDao extends Dao<DayPlanning> {
    private static final String IDCOLUMN = "id";
    private static final String DAYCOLUMN = "date";
    private static final String USERCOLUMN = "user";

    private static DayPlanningDao singletonInstance = null;

    private DayPlanningDao() {}

    /**
     * Singleton instance getter
     * @return instance
     */
    public static DayPlanningDao getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new DayPlanningDao();
        }

        return singletonInstance;
    }

    /**Mandatory override
     * @return null
     */
    @Override
    public List<DayPlanning> getAll(){ return null;}

    /**Method getting a DayPlanning object from the database
     * @param idDayPlanning id of the day planning
     * @return dayPlanning associated to the id
     * @throws SQLException database exception
     */
    @Override
    public DayPlanning get(int idDayPlanning) throws SQLException {
        DayPlanning dayPlanning = null;
        String query = String.format("SELECT * FROM day_planning WHERE id = %d", idDayPlanning);

        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            dayPlanning = new DayPlanning(queryResult.getInt(IDCOLUMN),
                                          queryResult.getString(DAYCOLUMN),
                                          queryResult.getInt(USERCOLUMN));
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return dayPlanning;
    }

    /**
     * Mandatory override
     */
    @Override
    public DayPlanning get(String date) throws SQLException {return null;}

    /**
     * Method adding a dayPlanning to the database
     * @param dayPlanning the dayPlanning to be added
     * @throws SQLException database exception
     */
    @Override
    public void create(DayPlanning dayPlanning) throws SQLException {
        String query = String.format("INSERT INTO day_planning (date, user) VALUES ('%s', %d);", dayPlanning.getDate(),
                        dayPlanning.getUserID());
        realizeUpdateQuery(query);
    }

    /**
     * Method updating a dayPlanning in the database
     * @param dayPlanning the dayPlanning to be updated
     * @throws SQLException database exception
     */
    @Override
    public void update(DayPlanning dayPlanning) throws SQLException {
        String query = String.format("UPDATE day_planning SET date = '%s', user = %d WHERE id = %d", dayPlanning.getDate(),
                        dayPlanning.getUserID(), dayPlanning.getID());
        this.realizeUpdateQuery(query);
    }

    /**
     * Method deleting a dayPlanning from the database
     * @param dayPlanning the dayPlanning to be deleted
     * @throws SQLException database exception
     */
    @Override
    public void delete(DayPlanning dayPlanning) throws SQLException {
        RecipesToDayDao.getInstance().deleteUsersDay(dayPlanning.getID());
        String query = String.format("DELETE FROM day_planning WHERE id = %d;", dayPlanning.getID());
        this.realizeUpdateQuery(query);
    }

    /** Day planning getter based on user and date
     * @param userID user ID
     * @param date date to be fetched
     * @return DayPlanning object
     * @throws SQLException database exception
     */
    public DayPlanning get(int userID, String date) throws SQLException {
        DayPlanning dayPlanning;
        String query = String.format("SELECT * FROM day_planning WHERE user = %d AND date = '%s'", userID, date);
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            dayPlanning = new DayPlanning(
                    queryResult.getInt(IDCOLUMN),
                    queryResult.getString(DAYCOLUMN),
                    queryResult.getInt(USERCOLUMN)
            );
        } catch (SQLException ignored) {
            String newQuery = String.format("INSERT INTO day_planning (date, user) VALUES ('%s', %d);", date, userID);
            realizeUpdateQuery(newQuery);
            try (ResultSet queryResult = realizeExecuteQuery(query)) {
                queryResult.next();
                dayPlanning = new DayPlanning(
                        queryResult.getInt(IDCOLUMN),
                        queryResult.getString(DAYCOLUMN),
                        queryResult.getInt(USERCOLUMN)
                );
            }
        }

        return dayPlanning;
    }
}
