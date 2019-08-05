package org.importdata;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Реализация класса подключения к БД
 */

public class Connector {

    private static final Logger LOGGER = Logger.getLogger(Connector.class);

    public Connector() {
    }

    public Connection getDBConnection(String dbDriver, String dbAddress, String dbUser, String dbPassword) {
        Connection dbConnection = null;
        try {
            Class.forName(dbDriver);
            dbConnection = DriverManager.getConnection(dbAddress, dbUser, dbPassword);
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error(e + "Ошибка подключения к СУБД");
        }
        return dbConnection;
    }
}
