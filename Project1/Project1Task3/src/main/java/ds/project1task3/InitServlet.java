package ds.project1task3;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/05/2024
 */
// The first loaded servlet
@WebServlet(urlPatterns = {"/init"}, loadOnStartup = 1)
public class InitServlet extends HttpServlet {


    /**
     * Read the data of each state and parse it from JSON to list, and then send out via request
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONParser parser = new JSONParser();
        List<String> stateList = new ArrayList<>();
        try {
            // get the JSON file and parse it
            String path = getServletContext().getRealPath("resource/states.json");
            Object obj = parser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray states = (JSONArray) jsonObject.get("states");

            // Add the parsed data to list
            for (Object state : states) {
                stateList.add((String) state);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // put the state list into request and send to index.jsp
        request.setAttribute("states", stateList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}
