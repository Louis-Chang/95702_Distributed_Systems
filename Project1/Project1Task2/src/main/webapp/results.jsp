<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Distributed Systems Class Clicker - Results</title>
        <!-- Include Bootstrap for styling -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <!-- Ensure proper rendering and touch zooming for all devices -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style>
            .result-item {
                font-size: larger; /* Make text larger for better readability on small screens */
            }
            .back-button {
                margin-top: 20px; /* Add some space above the back button */
            }
        </style>
    </head>
    <body>
        <div class="container mt-3">
            <h2>Distributed Systems Class Clicker - Results</h2>

            <!-- Check if the results object is not empty -->
            <% if (request.getAttribute("results") != null) { %>
            <p>The results from the survey are as follows:</p>
            <ul class="list-unstyled">
                <!-- Iterate over the results map entries -->
                <c:forEach items="${requestScope.results}" var="entry">
                    <li class="result-item">${entry.key}: ${entry.value}</li>
                </c:forEach>
            </ul>
            <% } else { %>
            <div class="alert alert-secondary" role="alert">
                There are currently no results.
            </div>
            <% } %>

            <a href="index.jsp" class="btn btn-primary back-button">Back to Questions</a>
        </div>
    </body>
</html>