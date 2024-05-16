package ds.project1task3;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/05/2024
 */
@WebServlet(name = "states", urlPatterns = {"/states"})
public class StatesServlet extends HttpServlet {
    final String apiUrl = "https://api.census.gov/data/2020/acs/acs5?get=NAME,B01001_001E&for=state:*";
    BirdPicModel birdPicModel;
    FlowerPicModel flowerPicModel;

    // init BirdPicModel
    @Override
    public void init() throws ServletException {
        birdPicModel = new BirdPicModel();
        flowerPicModel = new FlowerPicModel();
    }


    /**
     * Get the information from Census Bureau using API and state bird info from Wikipedia by screen scraping, and then send to stateInfoBird.jsp
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the userName from the dropdown
        String userName = request.getParameter("userName");

        // Retrieve the selected state from the dropdown
        String state = request.getParameter("state");

        // Retrieve the value of the selected radio button
        String displayOption = request.getParameter("displayOption");

        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(apiUrl).openConnection();
        httpURLConnection.setRequestMethod("GET");
        Gson gson = new Gson();
        InputStreamReader reader = new InputStreamReader(httpURLConnection.getInputStream());
        Type listType = new TypeToken<List<List<String>>>(){}.getType();
        List<List<String>> data = gson.fromJson(reader, listType);
        reader.close();

        Map<String, String> infoMap = new HashMap<>();
        for (int i = 1; i < data.size(); i++) {
            List<String> stateData = data.get(i);
            if (state.equals(stateData.get(0))) {
                infoMap.put("Name", stateData.get(0));
                infoMap.put("Population", stateData.get(1));
                infoMap.put("StateID", stateData.get(2));
                break;
            }
        }

        String nextJsp = "";
        if ("bird".equals(displayOption)) {
            // get data from birdPicModel, which scrape the data from Wikipedia, and save it into the map
            Map<String, String> birdInfoMap = birdPicModel.doSearch(infoMap.get("Name"));
            if (birdInfoMap != null) {
                infoMap.put("birdName", birdInfoMap.get("Name"));
                infoMap.put("birdSciName", birdInfoMap.get("sciName"));
                infoMap.put("birdImgUrl", birdInfoMap.get("imgUrl"));
                infoMap.put("birdYear", birdInfoMap.get("year"));
            }
            nextJsp = "/stateInfoBird.jsp";
        } else if ("flower".equals(displayOption)) {
            // get data from birdPicModel, which scrape the data from Wikipedia, and save it into the map
            Map<String, String> flowerInfoMap = flowerPicModel.doSearch(infoMap.get("Name"));
            if (flowerInfoMap != null) {
                infoMap.put("flowerName", flowerInfoMap.get("Name"));
                infoMap.put("flowerSciName", flowerInfoMap.get("sciName"));
                infoMap.put("flowerImgUrl", flowerInfoMap.get("imgUrl"));
                infoMap.put("flowerYear", flowerInfoMap.get("year"));
            }
            nextJsp = "/stateInfoFlower.jsp";
        } else {
            // get data from birdPicModel, which scrape the data from Wikipedia, and save it into the map
            Map<String, String> birdInfoMap = birdPicModel.doSearch(infoMap.get("Name"));
            if (birdInfoMap != null) {
                infoMap.put("birdName", birdInfoMap.get("Name"));
                infoMap.put("birdSciName", birdInfoMap.get("sciName"));
                infoMap.put("birdImgUrl", birdInfoMap.get("imgUrl"));
                infoMap.put("birdYear", birdInfoMap.get("year"));
            }

            // get data from birdPicModel, which scrape the data from Wikipedia, and save it into the map
            Map<String, String> flowerInfoMap = flowerPicModel.doSearch(infoMap.get("Name"));
            if (flowerInfoMap != null) {
                infoMap.put("flowerName", flowerInfoMap.get("Name"));
                infoMap.put("flowerSciName", flowerInfoMap.get("sciName"));
                infoMap.put("flowerImgUrl", flowerInfoMap.get("imgUrl"));
                infoMap.put("flowerYear", flowerInfoMap.get("year"));
            }
            nextJsp = "/stateInfoBoth.jsp";
        }

        // put the data to request and send it to stateInfo
        request.setAttribute("infoMap", infoMap);
        request.setAttribute("userName", userName);
        RequestDispatcher dispatcher = request.getRequestDispatcher(nextJsp);
        dispatcher.forward(request, response);
    }
}
