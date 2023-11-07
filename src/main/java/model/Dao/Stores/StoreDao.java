package model.Dao.Stores;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao for the store
 */
public class StoreDao extends Dao<String> {

    private static StoreDao singletonInstance = null;

    private StoreDao() {}

    /**
     * Getter for the singleton Instance
     * @return singleton Instance
     */
    public static StoreDao getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new StoreDao();
        }
        return singletonInstance;
    }

    /**
     * Getter for the stores
     * @return all the stores
     */
    @Override
    public List<String> getAll() {
        List<String> stores = new ArrayList<>();
        String query = "SELECT * FROM stores ORDER BY name";
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            while (queryResult.next()) {
                stores.add(queryResult.getString("name"));
            }
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        return stores;
    }

    @Override
    public String get(int id) throws SQLException {
        return null;
    }

    @Override
    public String get(String name) throws SQLException {
        return null;
    }

    /**
     * Getter for the ID from the name passed as parameter
     * @param name  to get the id
     * @return ID related to the name
     */
    public int getIdFromName(String name) {
        int id = 0;
        String query = String.format("SELECT id FROM stores WHERE name = '%s'", name);
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            id = queryResult.getInt("id");
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        return id;
    }

    /**
     * puts a given string in the stores of the DB
     * @param name  string of object to be put in the DB
     */
    @Override
    public void create(String name) throws SQLException {
        String query = String.format("""
                INSERT INTO stores (name)
                VALUES ('%s');
                """, name);
        realizeUpdateQuery(query);
    }

    /**
     * updates a given string in the stores of the DB
     * @param obj  string of object to be updated in the DB
     */
    @Override
    public void update(String obj) throws SQLException {

    }

    /**
     * deletes a given string in the stores of the DB
     * @param nameStore string of object to be deleted in the DB
     */
    @Override
    public void delete(String nameStore) throws SQLException {
        String query = String.format("""
                DELETE FROM stores
                WHERE name = '%s';
                """, nameStore);
        realizeUpdateQuery(query);
    }
}
