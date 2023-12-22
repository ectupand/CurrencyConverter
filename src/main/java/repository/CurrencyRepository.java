package repository;

import entity.Currency;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class CurrencyRepository {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        }
    }

    private Connection getConnection() throws SQLException, IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("app.config");
        prop.load(fis);

        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/" + prop.getProperty("DATABASE.NAME"),
                prop.getProperty("DATABASE.LOGIN"),
                prop.getProperty("DATABASE.PASSWORD")
        );
    }

    private void closeConnection(Connection connection) {
        if (connection == null)
            return;
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    private void createCurrenciesTable() {
        String sql = "CREATE TABLE currencies(" +
                "name VARCHAR(128), " +
                "char_code VARCHAR(3), " +
                "value FLOAT(50), " +
                "valute_id VARCHAR(7), " +
                "updated_at DATE)";

        Connection connection = null;
        try {
            connection = getConnection();
            connection.prepareStatement(sql).executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
    }

    private void dropCurrenciesTable() {
        String sql = "DROP TABLE currencies";
        Connection connection = null;
        try {
            connection = getConnection();
            connection.prepareStatement(sql).executeUpdate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException ignored){
        }
        finally {
            closeConnection(connection);
        }
    }

    public void updateCurrenciesTable(List<Currency> currencies) {
        dropCurrenciesTable();
        createCurrenciesTable();
        String sql = "INSERT INTO currencies(name, char_code, value, valute_id, updated_at) VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        try {
            connection = getConnection();
            for (Currency currency : currencies) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, currency.getName());
                statement.setString(2, currency.getCharCode());
                statement.setFloat(3, currency.getValue());
                statement.setString(4, currency.getValuteID());
                statement.setDate(5, Date.valueOf(currency.getUpdatedAt()));
                statement.executeUpdate();

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
    }

}
