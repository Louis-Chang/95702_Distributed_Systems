<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Distributed Systems Class Clicker</title>
        <!-- Include Bootstrap for styling -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <!-- Ensure proper rendering and touch zooming -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style>
            .alert {
                margin-top: 20px; /* Add some space above the alert for better visibility */
            }
            .form-check-label {
                margin-bottom: 10px; /* Increase space between radio buttons for easier touch interaction */
            }
            .btn {
                margin-top: 10px; /* Add some space above the button */
            }
        </style>
    </head>
    <body>
        <div class="container mt-3">
            <h2>Distributed Systems Class Clicker</h2>
            <% if(request.getAttribute("message") != null) { %>
            <div class="alert alert-success" role="alert">
                <%= request.getAttribute("message") %>
            </div>
            <% } %>

            <!-- send the option user chose to SubmitServlet -->
            <form action="Submit" method="post">
                <div class="form-group">
                    <label>Submit your answer to the current question:</label>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="option" id="optionA" value="A">
                        <label class="form-check-label" for="optionA">A</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="option" id="optionB" value="B">
                        <label class="form-check-label" for="optionB">B</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="option" id="optionC" value="C">
                        <label class="form-check-label" for="optionC">C</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="option" id="optionD" value="D">
                        <label class="form-check-label" for="optionD">D</label>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
                <!-- Button to direct to the results page -->
            </form>
            <!-- the button that can go to results.jsp to see results -->
            <a href="results" class="btn btn-info" style="margin-top: 20px;">View Results</a>
        </div>
    </body>
</html>
