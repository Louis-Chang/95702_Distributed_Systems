package ds.cmu.task1;

import com.mongodb.client.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.Document;
import java.io.IOException;
import org.bson.conversions.Bson;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Projections.*;

@WebServlet(urlPatterns = {"/dashboard"}, loadOnStartup = 1)
public class DashboardServlet extends HttpServlet {
    private final String connectionString = "mongodb+srv://hungyic:s17krRBnBmShobWE@cluster0.czpxrau.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (MongoClient client = MongoClients.create(connectionString)) {
            MongoDatabase database = client.getDatabase("PokerGame");
            MongoCollection<Document> collection = database.getCollection("cardDraws");

            List<Bson> pipeline = Arrays.asList(
                    project(fields(
                            include("suit", "code"),
                            computed("value", new Document("$toInt", "$value")) // 将value字段从String转换为Integer
                    )),
                    group(null, avg("averageValue", "$value"))
            );

            AggregateIterable<Document> result = collection.aggregate(pipeline);
            Document avgDoc = result.first();

            if (avgDoc != null) {
                Double avgValue = avgDoc.getDouble("averageValue");
                // 将平均值传递给请求，以便可以在JSP等前端展示
                request.setAttribute("averageValue", avgValue);
            } else {
                // 如果没有结果，可能需要处理这种情况
                request.setAttribute("averageValue", "No data");
            }

            List<Document> valueDistribution = collection.aggregate(
                    Arrays.asList(
                            group("$value", sum("count", 1))
                    )
            ).into(new java.util.ArrayList<>());

            // 查询卡牌花色的分布
            List<Document> suitDistribution = collection.aggregate(
                    Arrays.asList(
                            group("$suit", sum("count", 1))
                    )
            ).into(new java.util.ArrayList<>());

            MongoCollection<Document> operationsCollection = database.getCollection("pokerOperations");
            List<Document> cardOperations = operationsCollection.find().into(new ArrayList<>());

            request.setAttribute("valueDistribution", valueDistribution);
            request.setAttribute("suitDistribution", suitDistribution);
            request.setAttribute("cardOperations", cardOperations);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("Database connection error");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}
