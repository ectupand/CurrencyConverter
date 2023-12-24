<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="ISO-8859-1">
    <title>Currency Converter</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="style.css">
</head>

<body>

<div class="container col-md-8 col-md-offset-3" style="overflow: auto">
    <form action="<%=request.getContextPath()%>/converter" method="post">
        <div>${requestScope.currenciesList[0].getCharCode()}</div>
        <select name="currencyDropdown" id="currencyDropdown" >
            <c:forEach items="${requestScope.currenciesList}" var="currency">
                <option value="${currency.getCharCode()}">${currency.getCharCode()}</option>
            </c:forEach>
        </select>


        <button type="submit" class="btn btn-secondary">Submit</button>
    </form>
</div>
</body>

</html>