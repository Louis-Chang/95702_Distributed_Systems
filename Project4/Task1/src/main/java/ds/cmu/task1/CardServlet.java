package ds.cmu.task1;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet(name = "CardServlet", urlPatterns = {"/card/*"})
public class CardServlet extends HttpServlet {

    private MongoDBService mongoDBService;
    private final String connectionString = "mongodb+srv://hungyic:s17krRBnBmShobWE@cluster0.czpxrau.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    private final ConcurrentHashMap<String, String> deviceDeckMap = new ConcurrentHashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        mongoDBService = new MongoDBService(connectionString);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestInfoWrapper requestWrapper = new RequestInfoWrapper(request);
        String userAgent = requestWrapper.getUserAgent();
        long timestamp = requestWrapper.getTimestamp();
        String ipAddress = request.getRemoteAddr();
        String uuid = request.getParameter("uuid");
        if (uuid == null || uuid.isEmpty()) {
            response.getWriter().write("{\"error\":\"UUID is required.\"}");
            mongoDBService.insertRequestDetails(userAgent, null, timestamp, "Create", null, null, ipAddress);
            return;
        }

        HttpClient client = HttpClient.newHttpClient();
        // 更新API URL以創建並洗牌一副新牌
        HttpRequest apiRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1"))
                .build();

        try {
            Gson gson = new Gson();
            HttpResponse<String> apiResponse = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = gson.fromJson(apiResponse.body(), JsonObject.class);

            if (jsonResponse.get("success").getAsBoolean()) {
                String deckId = jsonResponse.get("deck_id").getAsString();
                deviceDeckMap.put(uuid, deckId);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(apiResponse.body()); // 將API的響應原封不動地傳送回去
            } else {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"message\":\"Failed to create a new deck\"}");
            }
            mongoDBService.insertRequestDetails(userAgent, uuid, timestamp, "Create", null, jsonResponse.toString(), ipAddress);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            response.getWriter().write("{\"message\":\"Request interrupted\"}");
        } catch (Exception e) {
            response.getWriter().write("{\"message\":\"Error occurred: " + e.getMessage() + "\"}");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestInfoWrapper requestWrapper = new RequestInfoWrapper(request);
        String userAgent = requestWrapper.getUserAgent();
        long timestamp = requestWrapper.getTimestamp();
        String ipAddress = request.getRemoteAddr();
        String uuid = extractUuidFromRequestBody(request);
        if (uuid == null || uuid.isEmpty() || !deviceDeckMap.containsKey(uuid)) {
            response.getWriter().write("{\"error\":\"Valid UUID is required to draw a card.\"}");
            mongoDBService.insertRequestDetails(userAgent, uuid, timestamp, "Draw", null, null, ipAddress);
            return;
        }

        // Step 1: Draw a card from the deck
        String deckId = deviceDeckMap.get(uuid);
        String drawCardUrl = "https://deckofcardsapi.com/api/deck/" + deckId + "/draw/?count=2";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest drawRequest = HttpRequest.newBuilder()
                .uri(URI.create(drawCardUrl))
                .build();

        try {
            HttpResponse<String> drawResponse = client.send(drawRequest, HttpResponse.BodyHandlers.ofString());

            // Check if the draw was successful before attempting to shuffle
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(drawResponse.body(), JsonObject.class);
            mongoDBService.insertRequestDetails(userAgent, uuid, timestamp, "Draw", null, jsonResponse.toString(), ipAddress);
            if (!jsonResponse.get("success").getAsBoolean()) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"message\":\"Failed to draw a card\"}");
                return;
            }
            JsonArray cardsArray = jsonResponse.getAsJsonArray("cards");
            for (JsonElement cardElement : cardsArray) {
                JsonObject cardObject = cardElement.getAsJsonObject();
                String code = cardObject.get("code").getAsString();
                String value = transformValue(cardObject.get("value").getAsString());
                String suit = cardObject.get("suit").getAsString();

                Card card = new Card(code, value, suit);
                mongoDBService.insertCardDrawAction(card);
            }


            // Step 2: Shuffle the deck
            String shuffleUrl = "https://deckofcardsapi.com/api/deck/" + deckId + "/shuffle/";
            HttpRequest shuffleRequest = HttpRequest.newBuilder()
                    .uri(URI.create(shuffleUrl))
                    .build();

            HttpResponse<String> shuffleResponse = client.send(shuffleRequest, HttpResponse.BodyHandlers.ofString());
            mongoDBService.insertRequestDetails(userAgent, uuid, timestamp, "Shuffle", shuffleRequest.toString(), shuffleResponse.body(), ipAddress);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(drawResponse.body()); // Returns the drawn card response
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            response.getWriter().write("{\"message\":\"Request interrupted\"}");
        } catch (Exception e) {
            response.getWriter().write("{\"message\":\"Error occurred: " + e.getMessage() + "\"}");
        }
    }
    private String extractUuidFromRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(requestBody.toString(), JsonObject.class);
        return jsonObject.has("uuid") ? jsonObject.get("uuid").getAsString() : null;
    }
    private String transformValue(String value) {
        switch (value) {
            case "ACE":
                return "1";
            case "KING":
                return "13";
            case "QUEEN":
                return "12";
            case "JACK":
                return "11";
            default:
                return value;
        }
    }
}
