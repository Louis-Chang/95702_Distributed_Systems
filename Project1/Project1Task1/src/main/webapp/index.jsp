<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Compute Hashes</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <meta name="viewport" content="width=device-width, initial-scale=1">
    </head>
    <body>
        <div class="container mt-3">
            <h2>Compute Hash Form</h2>
            <form action="ComputeHashes" method="get">
                <div class="row">
                    <div class="col-12">
                        <div class="form-group mb-3">
                            <label for="textData">Text Data:</label>
                            <input type="text" class="form-control" id="textData" name="textData" required>
                        </div>
                    </div>
                    <div class="col-12">
                        <p>Select a Hash Function:</p>
                        <div class="form-check mb-3">
                            <input type="radio" class="form-check-input" id="hashFunction1" name="hashFunction" value="MD5" checked>
                            <label class="form-check-label" for="hashFunction1">MD5</label>
                        </div>
                        <div class="form-check mb-3">
                            <input type="radio" class="form-check-input" id="hashFunction2" name="hashFunction" value="SHA-256">
                            <label class="form-check-label" for="hashFunction2">SHA-256</label>
                        </div>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
