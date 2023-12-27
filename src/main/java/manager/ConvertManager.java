package manager;

import repository.CurrencyRepository;

import java.io.IOException;


public class ConvertManager {
    private CurrencyRepository currencyRepository = new CurrencyRepository();

    public String getExchangeRate(String fromCurrency, Float fromCurrencyMuch, String toCurrency) throws IOException {
        if (currencyRepository.isUpdated()) {
            fromCurrency = fromCurrency.substring(0, 3);
            toCurrency = toCurrency.substring(0, 3);

            if (fromCurrency.equals("RUB")) {
                Float value = currencyRepository.getCurrencyExchangeRate(toCurrency);
                return String.format("%.2f", fromCurrencyMuch / value);
            }
            return String.format("%.2f",
                    currencyRepository.getCurrencyExchangeRate(toCurrency)
                            / currencyRepository.getCurrencyExchangeRate(fromCurrency)
            );
        }
        return null;
    }
}
