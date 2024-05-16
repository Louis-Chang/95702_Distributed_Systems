<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Know Your State</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <!-- Ensure proper rendering and touch zooming -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
    </head>
    <body>
        <nav class="navbar navbar-light bg-light">
            <span class="navbar-brand mb-0 h1">Know Your State</span>
        </nav>

        <!-- Displaying Hi, userName -->
        <div class="container-fluid mt-3">
            <h2>Hi, ${userName}</h2>
        </div>

        <!-- get infoMap from StatesServlet and show the data -->
        <div class="container-fluid mt-5"> <!-- Changed to container-fluid -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">${infoMap['StateID']} ${infoMap['Name']}</h3>
                    <h6 class="card-subtitle mb-2 text-muted">Population: ${infoMap['Population']}</h6>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-sm-12 col-md-8"> <!-- Adjusted for stacking on smaller screens -->
                            <p class="mt-4"><strong>State Bird: ${infoMap['birdName']}</strong></p>
                            <p class="card-text"><strong>Scientific Name:</strong> ${infoMap['birdSciName']}</p>
                            <p class="card-text"><strong>Year:</strong> ${infoMap['birdYear']}</p>
                        </div>
                        <div class="col-sm-12 col-md-4"> <!-- Adjusted for stacking on smaller screens -->
                            <img src="${infoMap['birdImgUrl']}" class="img-fluid" alt="State Bird"> <!-- Changed to img-fluid -->
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12 col-md-8"> <!-- Adjusted for stacking on smaller screens -->
                            <p class="mt-4"><strong>State Flower: ${infoMap['flowerName']}</strong></p>
                            <p class="card-text"><strong>Scientific Name:</strong> ${infoMap['flowerSciName']}</p>
                            <p class="card-text"><strong>Year:</strong> ${infoMap['flowerYear']}</p>
                        </div>
                        <div class="col-sm-12 col-md-4"> <!-- Adjusted for stacking on smaller screens -->
                            <img src="${infoMap['flowerImgUrl']}" class="img-fluid" alt="State Flower"> <!-- Changed to img-fluid -->
                            <!-- Add citation right below the image -->
                            <p class="text-secondary text-right"><small><strong>Credit:Mad Tinman at <a href="https://en.wikipedia.org/" rel="nofollow">https://en.wikipedia.org/</a></strong></small></p>
                        </div>
                    </div>
                </div>
                <div class="card-footer">
                    <a href="init" class="btn btn-primary mt-3">Go Back</a>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
