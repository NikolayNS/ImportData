package org.importdata;

import org.apache.log4j.Logger;

import java.sql.*;

/**
 * Реализация класса редактора запросов к СУБД
 */

public class QueryEditor {

    private static final Logger LOGGER = Logger.getLogger(QueryEditor.class);

    private String dbDriver;
    private String dbAddress;
    private String dbUser;
    private String dbPassword;

    private Connector connector = new Connector();

    public QueryEditor(String dbDriver, String dbAddress, String dbUser, String dbPassword) {
        this.dbDriver = dbDriver;
        this.dbAddress = dbAddress;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public ResultSet selectTable(String request) {
        ResultSet resultSet = null;
        try (
                Connection connection = connector.getDBConnection(dbDriver, dbAddress, dbUser, dbPassword)
        ) {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.error(e + "Ошибка выполнения запроса к БД");
        }

        //На версиях до 1.7 закрывать сессии нужно так
        /*try{
            LOGGER.info("Выполнение запроса");
            Connection connection = connector.getDBConnection(dbDriver, dbAddress, dbUser, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            ResultSet resultSet = preparedStatement.executeQuery();
            LOGGER.info("Запрос выполнен успешно");
        } catch (SQLException e) {
            LOGGER.error(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                LOGGER.error(e);
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                LOGGER.error(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error(e);
            }
        }*/
        return resultSet;
    }

    public void truncateTable (String tableName){
        try (
                Connection connection = connector.getDBConnection(dbDriver, dbAddress, dbUser, dbPassword);
                PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE " + tableName)
        ) {
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error(e + "Ошибка выполнения запроса к БД");
        }
    }

    public void updateTable (String request){
        try (
                Connection connection = connector.getDBConnection(dbDriver, dbAddress, dbUser, dbPassword);
                PreparedStatement preparedStatement = connection.prepareStatement(request)
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e + "Ошибка Выполнения запроса к БД");
        }
    }

    public boolean checkTable (String request){
        boolean isRetrieved = false;
        try (
                Connection connection = connector.getDBConnection(dbDriver, dbAddress, dbUser, dbPassword);
                PreparedStatement preparedStatement = connection.prepareStatement(request)
        ) {
            isRetrieved = preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error(e + "Ошибка Выполнения запроса к БД");
        }
        return isRetrieved;
    }
}

