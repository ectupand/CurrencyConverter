package repository;

import entity.Currency;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class CurrencyRepository {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        }
    }

    private Connection getConnection() throws SQLException {
        /*Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("app.config");
        prop.load(fis);
        InputStream is = CurrencyRepository.class.getResourceAsStream("/resources/app.config");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/" + reader.read("DATABASE.NAME".toCharArray()),
                String.valueOf(reader.read("DATABASE.LOGIN".toCharArray())),
                String.valueOf(reader.read("DATABASE.PASSWORD".toCharArray()))
        );
       */
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "0000");
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

    private void createCurrenciesTable(Connection connection) {
        String sql = "CREATE TABLE currencies(" +
                "name VARCHAR(128), " +
                "char_code VARCHAR(3), " +
                "value FLOAT(50), " +
                "valute_id VARCHAR(7), " +
                "updated_at DATE)";
        try {
            connection.prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void dropCurrenciesTableIfExists(Connection connection) {
        String sql = "DROP TABLE IF EXISTS currencies";
        try {
            connection.prepareStatement(sql).executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCurrenciesTable(List<Currency> currencies) {
        Connection connection = null;

        try {
            String sql = "INSERT INTO currencies(name, char_code, value, valute_id, updated_at) VALUES (?, ?, ?, ?, ?)";
            connection = getConnection();
            dropCurrenciesTableIfExists(connection);
            createCurrenciesTable(connection);
            for (Currency currency : currencies) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, currency.getName());
                statement.setString(2, currency.getCharCode());
                statement.setFloat(3, currency.getValue());
                statement.setString(4, currency.getValuteID());
                statement.setDate(5, Date.valueOf(currency.getUpdatedAt()));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
    }

    public Float getCurrencyExchangeRate(String name){
        String sql = "SELECT * FROM currencies " +
                "WHERE char_code LIKE ?";
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getFloat("value");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
        return null;
    }


/*
    public List<CurrencyModel> retrieveCurrencies() {
        List<CurrencyModel> currencies= new ArrayList<>();

        String sql = "SELECT * from currencies";
        Connection connection = null;

        try{
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                CurrencyModel currencyModel = new CurrencyModel();
                currencyModel.setName(resultSet.getString("name"));
                currencyModel.setCharCode(resultSet.getString("char_code"));
                currencyModel.setValue(resultSet.getFloat("value"));
                currencies.add(currencyModel);
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
        return currencies;
    }*/

    public boolean isUpdated(){
        LocalDate today = LocalDate.now();

        String sql = "SELECT updated_at FROM currencies LIMIT 1";
        Connection connection = null;
        try{
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Date updated_at = resultSet.getDate("updated_at");
                return !today.isAfter(updated_at.toLocalDate());
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            closeConnection(connection);
        }
    }

}
