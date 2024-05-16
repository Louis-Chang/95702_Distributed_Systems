package ds.project1task2;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/05/2024
 */
@WebServlet(name = "Submit", urlPatterns = {"/Submit"})
public class SubmitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to submission form JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }


    /**
     * get the option user chose from index.jsp, save it to the map, and then go back to index.jsp
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String option = request.getParameter("option");
        ServletContext context = getServletContext();
        if (option != null) {
            Map<String, Integer> results = (Map<String, Integer>) context.getAttribute("results");
            if (results == null) {
                results = new HashMap<>();
                context.setAttribute("results", results);
            }
            // if the option has existed, add the count to value
            results.merge(option, 1, Integer::sum);
            request.setAttribute("message", "Your option \"" + option + "\" has been registered. ");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

}
