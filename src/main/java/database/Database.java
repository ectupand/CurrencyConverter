package database;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static Connection connection = null;

    public static void connect(){
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("app.config")) {
            prop.load(fis);
        }catch (Exception e){
            System.err.println(e);
        }

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/"+prop.getProperty("DATABASE.NAME"),
                    prop.getProperty("DATABASE.LOGIN"),
                    prop.getProperty("DATABASE.PASSWORD")
            );
            if (connection!=null){
                System.out.println("DB connected");
            }else{
                System.out.println("Connection failed");
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void disconnect() throws SQLException {
        connection.close();
        connection = null;
    }

}
