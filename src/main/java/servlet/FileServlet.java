package servlet;

import manager.FileManager;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

public class FileServlet {
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
        FileServlet.configureApiPath();
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
                FileManager.storeResponseToFile(sb);
            }
        } else {
            System.err.println("Error in sending a GET request");
        }
    }



}
