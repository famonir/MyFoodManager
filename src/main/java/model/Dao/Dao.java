package model.Dao;

import controller.ExceptionControllers.ExceptionAlertController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Abstract class for all the DAO classes
 * @param <T> type of DAO content
 */
public abstract class Dao <T> {

    private final Connection connection = ConnectionEnum.INSTANCE.getConnection();

    /**Get all of the DAO table
     * @return List of T objects
     */
    public abstract List<T> getAll();

    /** getter by id
     * @param id id to get
     * @return T object
     * @throws SQLException database exception
     */
    public abstract T get(int id) throws SQLException;

    /** getter by string
     * @param name name to get
     * @return T object
     * @throws SQLException database exception
     */
    public abstract T get(String name) throws SQLException;

    /** object creator
     * @param obj object with the data
     * @throws SQLException database exception
     */
    public abstract void create(T obj) throws SQLException;

    /** object updater
     * @param obj object with the new data and the unchangeable id
     * @throws SQLException database exception
     */
    public abstract void update(T obj) throws SQLException;

    /** object eraser
     * @param obj object to be destroyed
     * @throws SQLException database exception
     */
    public abstract void delete(T obj) throws SQLException;

    /**
     * Method responsible for realizing the SELECT queries and for retrieving the data
     * @param query string with the exact query containing all the researched variables
     * @return a ResultSet, i.e. an object containing the result of the query
     * @see java.sql
     */
    public ResultSet realizeExecuteQuery(String query) {
        ResultSet queryResult = null;

        try {
            queryResult = connection.createStatement().executeQuery(query);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return queryResult;
    }

    /**
     * Method responsible for realizing the UPDATE, DELETE, INSERT queries
     * @param query string with the exact query containing all the researched variables
     * @see java.sql
     */
    public void realizeUpdateQuery(String query) {
        try {
            connection.createStatement().executeUpdate(query);
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**
     * Method closing the Database
     * Mandatory at the end of the program, to be used only once
     * @throws SQLException database exception
     */
    public void close() throws SQLException {
        connection.close();
    }

    /**
     * Connection getter
     * @return Connection
     */
    public Connection getConnection() {return connection;}
}
