package db;

import utils.ConfigReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
        private static final String URL = ConfigReader.getProperty("db.url");
        private static final String USER = ConfigReader.getProperty("db.username");
        private static final String PASSWORD = ConfigReader.getProperty("db.password");

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
}
