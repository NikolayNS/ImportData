package org.importdata;

import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Main builder
 */

public class ImportDataBuilder {

    private static final Logger LOGGER = Logger.getLogger(ImportDataBuilder.class);
    private static final String PROPERTIES = "src/main/resources/config.properties";

    private static String firstFileDir;
    private static String secondFileDir;
    private static String patternFileDir;

    private String dbDriver = "org.postgresql.Driver";
    private String dbAddress = "jdbc:postgresql://127.0.0.1:5432/Java";
    private String dbUser = "postgres";
    private String dbPassword = "1596321";
    private int N = 1000000;

    public ImportDataBuilder() {
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbAddress() {
        return dbAddress;
    }

    public void setDbAddress(String dbAddress) {
        this.dbAddress = dbAddress;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() { return dbPassword; }

    public void setDbPassword(String dbPassword) { this.dbPassword = dbPassword; }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public static void main(String[] args) {
        LOGGER.info("Старт работы приложения");
        readPropertry();
        ResultSet resultSet = createSQLdata();
        createXMLdata(resultSet);
        LOGGER.info("Завершение работы приложения");
    }

    private static ResultSet createSQLdata() {
        ImportDataBuilder dataBuilder = new ImportDataBuilder();
        QueryEditor queryEditor = new QueryEditor(dataBuilder.dbDriver,
                dataBuilder.dbAddress,
                dataBuilder.dbUser,
                dataBuilder.dbPassword);
        LOGGER.info("Проверяем наличе данных в таблице");
        if (queryEditor.checkTable("SELECT * FROM test")) {
            LOGGER.info("Очищаем таблицу");
            queryEditor.truncateTable("test");
        }
        LOGGER.info("Генерируем текст запроса для вставки данных в БД");
        StringBuilder partRequest = new StringBuilder();
        for (int i = 1; i <= dataBuilder.N; i++) {
            partRequest.append("(").append(i).append("), ");
        }
        partRequest.delete(partRequest.length() - 2, partRequest.length());
        LOGGER.info("Вставляем данные в БД");
        queryEditor.updateTable("INSERT INTO test (field) VALUES " + partRequest);

        return queryEditor.selectTable("SELECT field FROM test");
    }

    private static void createXMLdata(ResultSet resultSet) {
        LOGGER.info("Создаем первый файл на основании данных из БД");
        XMLFactory xmlFactory = new XMLFactory(firstFileDir);
        xmlFactory.createFile(resultSet);
        LOGGER.info("Создаем файл шаблон, для XLST преобразования");
        XLSTFactory xlstFactory = new XLSTFactory(patternFileDir);
        xlstFactory.createFile();
        LOGGER.info("Преобразуем данные из первого файла");
        xmlFactory.updateFile(secondFileDir, patternFileDir);
        LOGGER.info("Парсим второй файл с данными ");
        Parser parser = new Parser(secondFileDir);
        ArrayList<String> arrayValueField = null;
        arrayValueField = parser.getValueField("entry", "field");

        LOGGER.info("Сумма всех значений FIELD: " + sumFields(arrayValueField));
    }

    private static void readPropertry() {
        try (FileReader reader = new FileReader(PROPERTIES)) {
            Properties properties = new Properties();
            properties.load(reader);

            firstFileDir = properties.getProperty("io.firstFileDir");
            secondFileDir = properties.getProperty("io.secondFileDir");
            patternFileDir = properties.getProperty("io.patternFileDir");
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private static long sumFields(ArrayList<String> arrayValue) {
        long sum = 0;
        if (arrayValue != null) {
            for (String value : arrayValue) {
                sum += Integer.parseInt(value);
            }
        }
        return sum;
    }
}
