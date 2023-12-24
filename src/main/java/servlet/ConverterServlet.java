package servlet;

import jakarta.servlet.ServletConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.FileManager;
import models.CurrencyModel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;

@WebServlet("/converter")
public class ConverterServlet extends HttpServlet {
    private FileManager fileManager;

    @Override
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        /*
        CurrencyRepository currencyRepository = new CurrencyRepository();
        List<CurrencyModel> currencyList = currencyRepository.retrieveCurrencies();
        ServletContext context = config.getServletContext();
        context.setAttribute("currencyList", currencyList);
        */
        this.fileManager = new FileManager();
        String filePath = getServletContext().getRealPath("") + "/" + "response.xml";
        try {
            this.fileManager.parseXML(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getServletContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currenciesList", this.fileManager.getCurrenciesModelsList());
        request.getRequestDispatcher("/WEB-INF/converter.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "converter.jsp";

    }

    /*
    */



}
