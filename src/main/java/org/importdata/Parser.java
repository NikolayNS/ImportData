package org.importdata;

import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Реализация парсинга заданного файла
 */

public class Parser {

    private static final Logger LOGGER = Logger.getLogger(Parser.class);

    private String parseFile;

    public Parser(String parseFile) {
        this.parseFile = parseFile;
    }

    public ArrayList<String> getValueField(String entry, String field) {
        ArrayList<String> arrayValueField= new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(parseFile));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.error(e + "Ошибка парсинга файла XML");
        }
        if (document != null) {
            NodeList entriesElement = document.getElementsByTagName(entry);
            for (int i = 0; i < entriesElement.getLength(); i++) {
                NamedNodeMap attributes = entriesElement.item(i).getAttributes();
                arrayValueField.add(attributes.getNamedItem(field).getNodeValue());
            }
        }
        return arrayValueField;
    }
}
