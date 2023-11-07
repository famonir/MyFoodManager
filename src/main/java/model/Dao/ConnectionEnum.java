package model.Dao;

import controller.ExceptionControllers.ExceptionAlertController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

enum ConnectionEnum {
    INSTANCE;
    private Connection connection;

    ConnectionEnum() {
        try {
            String url = "jdbc:sqlite:../src/database/AppDB.db";
            try {
                connection = DriverManager.getConnection(url);
            } catch (Exception exception) {
                String urlForTests = "jdbc:sqlite:./src/database/AppDB.db";
                connection = DriverManager.getConnection(urlForTests);
            }
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

     public Connection getConnection() {
        return connection;
     }
}
