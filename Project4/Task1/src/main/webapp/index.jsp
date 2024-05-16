<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Dashboard</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    </head>
    <body>
        <div class="container">
            <h2 class="mt-5">Card Statistics</h2>
            <p>Average Card Value: ${averageValue}</p>

            <p>Value Distribution:</p>
            <ul class="list-group">
                <c:forEach var="doc" items="${valueDistribution}">
                    <li class="list-group-item">${doc.get("_id")}: ${doc.getInteger("count")}</li>
                </c:forEach>
            </ul>

            <p>Suit Distribution:</p>
            <ul class="list-group">
                <c:forEach var="doc" items="${suitDistribution}">
                    <li class="list-group-item">${doc.get("_id")}: ${doc.getInteger("count")}</li>
                </c:forEach>
            </ul>

            <h2 class="mt-5">Card Operations</h2>
            <table class="table" id="logTable">
                <thead>
                    <tr>
                        <th>User Agent</th>
                        <th>UUID</th>
                        <th>Timestamp</th>
                        <th>Method</th>
                        <th>IP Address</th>
                        <th>Response Details</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="operation" items="${cardOperations}">
                        <tr>
                            <td>${operation.userAgent}</td>
                            <td>${operation.uuid}</td>
                            <td>${operation.timestamp}</td>
                            <td>${operation.method}</td>
                            <td>${operation.ipAddress}</td>
                            <td>${operation.responseDetails}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
