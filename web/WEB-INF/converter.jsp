<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="ISO-8859-1">
    <title>Currency Converter</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="jquery.js"></script>
</head>

<body>

<div class="container col-md-8 col-md-offset-3" style="overflow: auto">
    <form action="<%=request.getContextPath()%>/" method="post">
            <select class="fromCurrency" name="fromCurrencyDropdown" id="fromCurrency">
                <option selected>${requestScope.fromCurrency} (current) </option>
                <c:forEach items="${requestScope.currenciesList}" var="currency">
                    <option value="${currency.getCharCode()}">${currency.getCharCode()}</option>
                </c:forEach>
            </select>
        <input class="fromCurrencyInput" type="text" id="fromCurrencyName" name="fromCurrencyName" value="1">

        <label for="fromCurrencyName">
            <select class="toCurrency" name="toCurrencyDropdown" id="toCurrency">
                <c:forEach items="${requestScope.currenciesList}" var="currency">
                    <option value="${currency.getCharCode()}">${currency.getCharCode()}</option>
                </c:forEach>
            </select>
        </label>
        <input disabled class="toCurrencyInput" type="text" id="toCurrencyName" name="toCurrencyName" value="${requestScope.currencyTotal}">

        <button type="submit" class="btn btn-secondary">Submit</button>
    </form>
</div>
</body>

</html>