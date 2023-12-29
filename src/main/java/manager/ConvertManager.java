package manager;

import repository.CurrencyRepository;

import java.io.IOException;


public class ConvertManager {
    private CurrencyRepository currencyRepository = new CurrencyRepository();

    public String getExchangeRate(String fromCurrency, Float fromCurrencyMuch, String toCurrency) {
        if (currencyRepository.isUpdated()) {

            if (fromCurrency.equals("RUB")) {
                Float value = currencyRepository.getCurrencyExchangeRate(toCurrency);
                return String.format("%.2f", fromCurrencyMuch / value);
            } else if (toCurrency.equals("RUB")){
                Float value = currencyRepository.getCurrencyExchangeRate(fromCurrency);
                return String.format("%.2f", fromCurrencyMuch*value);
            }
            return String.format("%.2f",
                    currencyRepository.getCurrencyExchangeRate(toCurrency)
                            * fromCurrencyMuch
                            / currencyRepository.getCurrencyExchangeRate(fromCurrency)
            );
        }
        return null;
    }
}
