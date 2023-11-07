package model.Dao.Stores;

import controller.ExceptionControllers.ExceptionAlertController;
import controller.MapControllers.Store;
import model.Dao.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Dao for the stores location
 */
public class StoresLocationsDao extends Dao<Store> {
    private static StoresLocationsDao singletonInstance = null;
    private StoresLocationsDao() {}

    /**
     * Singleton Getter
     * @return singleton
     */
    public static StoresLocationsDao getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new StoresLocationsDao();
        }
        return singletonInstance;
    }

    /**
     * Getter for the stores and their locations
     * @return all the stores and their locations
     */
    @Override
    public List<Store> getAll() {
        List<Store> stores = new ArrayList<>();
        String query = """
                        SELECT storesLocations.latitude, storesLocations.longitude, stores.name
                        FROM storesLocations
                        INNER JOIN stores ON storesLocations.storeNameId = stores.id
                       """;
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            while (queryResult.next()) {
                stores.add(new Store(queryResult.getDouble("longitude"),
                                     queryResult.getDouble("latitude"),
                                     queryResult.getString("name")));
            }
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        return stores;
    }

    @Override
    public Store get(int id) throws SQLException {return null;}

    @Override
    public Store get(String name) throws SQLException {return null;}

    /**
     * puts a given store obj (containing name and location of a store) in the stores of the DB
     * @param obj store of object to be put in the DB
     */
    @Override
    public void create(Store obj) throws SQLException {
        String storeIdQuery = String.format("SELECT id FROM stores where name = '%s'", obj.getStoreName());
        int storeId;
        try (ResultSet storeIdResult = realizeExecuteQuery(storeIdQuery)) {
            storeIdResult.next();
            storeId = storeIdResult.getInt("id");
            String query = String.format("""
                INSERT INTO storesLocations(storeNameId, latitude, longitude)
                VALUES (%s, %s, %s)
                """, storeId, obj.getLat(), obj.getLng());
            realizeUpdateQuery(query);
        }

    }

    /**
     * updates a given store obj (containing name and location of a store) in the stores of the DB
     * @param obj store of object to be updated in the DB
     */
    @Override
    public void update(Store obj) throws SQLException {}

    /**
     * deletes a given store obj (containing name and location of a store) in the stores of the DB
     * @param obj store of object to be deleted in the DB
     */
    @Override
    public void delete(Store obj) throws SQLException {
        String query = String.format("DELETE FROM storesLocations WHERE latitude = %s AND longitude = %s", obj.getLat(), obj.getLng());
        this.realizeUpdateQuery(query);
    }
}
