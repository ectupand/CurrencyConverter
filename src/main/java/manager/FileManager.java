package manager;

import entity.Currency;
import models.CurrencyModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static List<Currency> currenciesList;
    private static List<CurrencyModel> currenciesModelsList;

    private static void addToCurrenciesList(Currency currency) {
        currenciesList.add(currency);
    }
    private static void addToCurrenciesModelsList(CurrencyModel currency) {
        currenciesModelsList.add(currency);
    }

    public static List<Currency> getCurrenciesList() {
        return currenciesList;
    }

    public static List<CurrencyModel> getCurrenciesModelsList() {
        return currenciesModelsList;
    }

    public static void storeResponseToFile(StringBuilder response){
        try{
            FileWriter fstream = new FileWriter( "response.xml", StandardCharsets.UTF_8);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(String.valueOf(response));
            out.close();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    private static void parseXML() {
        Document doc;
        try {
            doc = buildDocument();
        } catch (Exception e) {
            System.err.println();
            return;
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
            CurrencyModel currencyModel = new CurrencyModel(name, charCode, value);

            addToCurrenciesList(currency);
            addToCurrenciesModelsList(currencyModel);
        }
    }

    private static Document buildDocument() throws Exception {
        File file = new File("response.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder().parse(file);
    }
}
