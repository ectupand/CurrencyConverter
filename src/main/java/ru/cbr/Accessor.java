package ru.cbr;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;

public class Accessor {
    private static URL apiPath = null;
    private static HttpsURLConnection connection = null;

    private static void configureApiPath() throws MalformedURLException {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("app.config")) {
            prop.load(fis);
        }catch (Exception e){
            System.err.println(e);
        }
        apiPath = URI.create(prop.getProperty("STORE.RU.CBR.PATH")).toURL();
    }

    public static void connect() throws IOException {
        ru.cbr.Accessor.configureApiPath();
        connection = (HttpsURLConnection) apiPath.openConnection();
        connection.setRequestMethod("GET");

        System.out.println("Response CODE: " + connection.getResponseCode() + " " + connection.getResponseMessage());

    }

    public static void disconnect(){
        connection.disconnect();
        connection = null;
    }

    public static void getResponse() throws IOException {
        if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
                storeResponseToFile(sb);
            }
        } else {
            System.err.println("Error in sending a GET request");
        }
    }

    private static void storeResponseToFile(StringBuilder response){
        try{
            FileWriter fstream = new FileWriter( "response.xml", StandardCharsets.UTF_8);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(String.valueOf(response));
            out.close();
        }catch (Exception e){
            System.err.println(e);
        }

    }

}
