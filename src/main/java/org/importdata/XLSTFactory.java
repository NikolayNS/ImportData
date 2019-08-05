package org.importdata;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Реализация класса XLST шаблонов
 */

public class XLSTFactory {

    private static final Logger LOGGER = Logger.getLogger(XLSTFactory.class);

    private String fileDirectory;

    public XLSTFactory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public void createFile() {

        String pattern = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
                "  <xsl:template match=\"entries\">\n" +
                "    <entries>\n" +
                "      <xsl:apply-templates/>\n" +
                "    </entries>\n" +
                "  </xsl:template>\n" +
                "\n" +
                "  <xsl:template match=\"entry\">\n" +
                "    <entry>\n" +
                "      <xsl:for-each select=\"*\">\n" +
                "        <xsl:attribute name=\"{name()}\">\n" +
                "          <xsl:value-of select=\"text()\"/>\n" +
                "        </xsl:attribute>\n" +
                "      </xsl:for-each>\n" +
                "    </entry>\n" +
                "  </xsl:template>\n" +
                "</xsl:stylesheet>";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileDirectory))){
            writer.write(pattern);
        } catch (IOException e) {
            LOGGER.error(e + "Ошибка создания файла XLST");
        }
    }
}
