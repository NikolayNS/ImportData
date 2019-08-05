package org.importdata;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Реализация класса XML файлов
 */

public class XMLFactory {

    private static final Logger LOGGER = Logger.getLogger(XMLFactory.class);

    private String fileDirectory;

    public XMLFactory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public void createFile(ResultSet resultSet) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("entries");
            doc.appendChild(rootElement);

            while (resultSet.next()){
                Element entry = doc.createElement("entry");
                rootElement.appendChild(entry);

                Element field = doc.createElement("field");
                field.setTextContent(resultSet.getString("field"));
                entry.appendChild(field);
            }

            File file = new File(fileDirectory);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(file));

        } catch (ParserConfigurationException | TransformerException | SQLException e) {
            LOGGER.error(e + "Ошибка создания файла XML");
        }
    }

    public void updateFile(String updateFileDir, String XLSTpattern) {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xsl = new StreamSource(XLSTpattern);

        try {
            Transformer transformer = factory.newTransformer(xsl);
            Source xml = new StreamSource(new File(fileDirectory));
            transformer.transform(xml, new StreamResult(updateFileDir));
        } catch (TransformerException e) {
            LOGGER.error(e + "Ошибка редактирования файла XML");
        }
    }
}
