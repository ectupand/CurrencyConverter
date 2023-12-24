package manager;

import entity.Currency;
import models.CurrencyModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import repository.CurrencyRepository;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class FileManager {
    private List<Currency> currenciesList = new ArrayList<>();
    private List<CurrencyModel> currenciesModelsList = new ArrayList<>();
    private File file;

    private void addToCurrenciesList(Currency currency) {
        currenciesList.add(currency);
    }

    private void addToCurrenciesModelsList(CurrencyModel currency) {
        currenciesModelsList.add(currency);
    }

    public List<Currency> getCurrenciesList() {
        return currenciesList;
    }

    public List<CurrencyModel> getCurrenciesModelsList() {
        return currenciesModelsList;
    }

    private URL apiPath = null;
    private HttpsURLConnection connection = null;

    private void configureApiPath() throws IOException, URISyntaxException {
        Properties prop = new Properties();
        try (InputStream in = getClass().getResourceAsStream("/app.config");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            prop.load(reader);
        }
        this.apiPath = URI.create(prop.getProperty("STORE.RU.CBR.PATH")).toURL();
    }


    private void connect() throws IOException {
        try {
            configureApiPath();
        }catch (URISyntaxException e){
            System.err.println(e);
        }
        this.connection = (HttpsURLConnection) apiPath.openConnection();
        this.connection.setRequestMethod("GET");

        System.out.println("Response CODE: " + this.connection.getResponseCode() + " " + this.connection.getResponseMessage());

    }

    private void disconnect(){
        this.connection.disconnect();
        this.connection = null;
    }

    private void getXML(String filePath) throws IOException {
        connect();
        if (this.connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(this.connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
                storeResponseToFile(sb, filePath);
            }
        } else {
            System.err.println("Error in sending a GET request");
        }

        disconnect();
    }

    private void storeResponseToFile(StringBuilder response, String filePath) {
        try {
            this.file = new File(filePath);
            FileWriter fstream = new FileWriter(this.file, true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(String.valueOf(response));
            out.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void parseXML(String filePath) throws IOException {
        getXML(filePath);

        Document doc;
        try {
            doc = buildDocument(filePath);
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
        new CurrencyRepository().updateCurrenciesTable(getCurrenciesList());
        this.file.delete();
    }

    private Document buildDocument(String filePath) throws Exception {
        File file = new File(filePath);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder().parse(file);
    }


}
