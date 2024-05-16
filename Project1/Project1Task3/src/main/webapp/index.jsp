<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Know Your State</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <!-- Ensure proper rendering and touch zooming for mobile devices -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style>
            body {
                background-color: #f8f9fa;
                margin-top: 20px;
            }
            .custom-header {
                background-color: #007bff;
                color: #fff;
                padding: 10px 0;
                text-align: center;
                margin-bottom: 30px;
            }
            .custom-header h2 {
                margin: 0;
            }
            .card {
                box-shadow: 0 0.25rem 0.75rem rgba(0, 0, 0, .05);
            }
            .btn-primary {
                background-color: #007bff;
                border-color: #007bff;
            }
            .btn-primary:hover {
                background-color: #0056b3;
                border-color: #004085;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="custom-header">
                <h2>Know Your State</h2>
            </div>
            <!-- Start of form -->
            <form action="states" method="get" class="mb-4">
                <!-- Textfield for user's name -->
                <div class="form-group">
                    <label for="userName"><span class="text-danger">*</span>Your Name: </label>
                    <input type="text" class="form-control" id="userName" name="userName" placeholder="Enter your name" required>
                </div>

                <div class="form-group">
                    <label for="stateSelect">Select your state:</label>
                    <select class="form-control" id="stateSelect" name="state">
                        <c:forEach items="${states}" var="state">
                            <option <c:if test="${param.selectedState == state}">selected="selected"</c:if>>${state}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Radio buttons for user choice -->
                <fieldset class="form-group">
                    <div class="row">
                        <legend class="col-form-label col-sm-2 pt-0">Preferences:</legend>
                        <div class="col-sm-10">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="displayOption" id="stateBird" value="bird" checked>
                                <label class="form-check-label" for="stateBird">
                                    State Bird
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="displayOption" id="stateFlower" value="flower">
                                <label class="form-check-label" for="stateFlower">
                                    State Flower
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="displayOption" id="both" value="both">
                                <label class="form-check-label" for="both">
                                    Both
                                </label>
                            </div>
                        </div>
                    </div>
                </fieldset>

                <!-- submit button -->
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
            <!-- End of form -->
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    </body>
</html>
