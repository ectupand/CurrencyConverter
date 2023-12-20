package store;

import models.Currency;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.spec.ECField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    public static void store() {

    }

    public static List<Currency> parseXML() {
        Document doc;
        try {
            doc = buildDocument();
        } catch (Exception e) {
            System.err.println();
            return null;
        }

        Node valCursNode = doc.getFirstChild();

        CharSequence uglyUpdatedAt = valCursNode.getAttributes().getNamedItem("Date").getNodeValue();
        LocalDate updatedAt = null;
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            updatedAt = LocalDate.parse(uglyUpdatedAt, pattern);
        } catch (DateTimeParseException e) {
            System.err.println(e);
        }

        List<Currency> currenciesList = new ArrayList<>();
        NodeList valCursChildren = valCursNode.getChildNodes();
        for (int i = 0; i < valCursChildren.getLength(); i++) {
            if (valCursChildren.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            String valuteID = valCursChildren.item(i).getAttributes().getNamedItem("ID").getNodeValue();

            NodeList valuteChildren = valCursChildren.item(i).getChildNodes();

            String name = "";
            String charCode = "";
            Float value = null;
            for (int j = 0; j < valuteChildren.getLength(); j++) {
                if (valCursChildren.item(j).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                switch (valuteChildren.item(j).getNodeName()) {
                    case "CharCode": {
                        charCode = valuteChildren.item(j).getTextContent();
                        break;
                    }
                    case "Name": {
                        name = valuteChildren.item(j).getTextContent();
                        break;
                    }
                    case "Value": {
                        value = Float.parseFloat(valuteChildren.item(j).getTextContent().replace(",", "."));
                        break;
                    }
                }
            }
            Currency currency = new Currency(name, charCode, value, valuteID, updatedAt);
            currenciesList.add(currency);
        }
        return currenciesList;
    }

    private static Document buildDocument() throws Exception {
        File file = new File("response.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder().parse(file);
    }


}
