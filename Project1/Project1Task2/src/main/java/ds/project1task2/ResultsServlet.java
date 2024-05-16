package ds.project1task2;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/05/2024
 */
@WebServlet(name = "ResultsServlet", urlPatterns = {"/getResults"})
public class ResultsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current session
        ServletContext context = getServletContext();
        // Retrieve the answers map from the session
        Map<String, Integer> results = (Map<String, Integer>) context.getAttribute("results");
        request.setAttribute("results", results);
        // Clear the stored results so that a new question can be posed
        context.removeAttribute("results");

        // Forward to the results JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/results.jsp");
        dispatcher.forward(request, response);
    }

}
