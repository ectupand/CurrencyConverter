package servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.ConvertManager;
import manager.FileManager;

import java.io.IOException;

@WebServlet("/converter")
public class ConverterServlet extends HttpServlet {
    private FileManager fileManager;
    private ConvertManager convertManager;
    private String filePath;

    @Override
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        //CurrencyRepository currencyRepository = new CurrencyRepository();
        //List<CurrencyModel> currencyList = currencyRepository.retrieveCurrencies();
        //ServletContext context = config.getServletContext();
        //context.setAttribute("currencyList", currencyList);
        this.fileManager = new FileManager();
        this.filePath = getServletContext().getRealPath("") + "/" + "response.xml";

        try {
            this.fileManager.parseXML(this.filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getServletContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currenciesList", this.fileManager.getCurrenciesModelsList());
        request.setAttribute("fromCurrency", "RUB");
        request.setAttribute("fromCurrencyMuch", 1);
        request.setAttribute("toCurrency", "RUB");
        request.getRequestDispatcher("/WEB-INF/converter.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fromCurrency = request.getParameter("fromCurrencyDropdown").substring(0,3);
        String toCurrency = request.getParameter("toCurrencyDropdown").substring(0,3);
        Float fromCurrencyMuch = Float.valueOf(request.getParameter("fromCurrencyName"));

        this.convertManager = new ConvertManager();
        if (this.convertManager.getExchangeRate(fromCurrency, fromCurrencyMuch, toCurrency) == null){
            this.filePath = getServletContext().getRealPath("") + "/" + "response.xml";
            try {
                this.fileManager.parseXML(this.filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String currencyTotal = this.convertManager.getExchangeRate(fromCurrency, fromCurrencyMuch, toCurrency);

        request.setAttribute("toCurrency", toCurrency);
        request.setAttribute("fromCurrency", fromCurrency);
        request.setAttribute("currenciesList", this.fileManager.getCurrenciesModelsList());
        request.setAttribute("fromCurrencyMuch", fromCurrencyMuch);
        request.setAttribute("currencyTotal", currencyTotal);
        request.getRequestDispatcher("/WEB-INF/converter.jsp").forward(request, response);
    }


}
