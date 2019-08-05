package org.importdata;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 */

public class ConnectSQL{

    private static final Logger LOGGER = Logger.getLogger(ConnectSQL.class);

    public ConnectSQL() {
    }

    protected Connection getDBConnection(String dbDriver, String dbAddress, String dbUser, String dbPassword) {
        Connection dbConnection = null;
        try {
            LOGGER.info("Установка соединения с СУБД");
            Class.forName(dbDriver);
            dbConnection = DriverManager.getConnection(dbAddress, dbUser, dbPassword);
            LOGGER.info("Подключение успешно");
        } catch (ClassNotFoundException e) {
            LOGGER.error(e);
        } catch (SQLException e) {
            LOGGER.error(e);
        }
        return dbConnection;
    }
}
