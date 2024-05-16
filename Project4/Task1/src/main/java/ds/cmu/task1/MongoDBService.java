package ds.cmu.task1;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBService {
    private MongoDatabase database;

    public MongoDBService(String connectionString) {
        try {
            MongoClient mongoClient = MongoClients.create(connectionString);
            this.database = mongoClient.getDatabase("PokerGame");
            System.out.println("Connected to MongoDB.");
        } catch (Exception e) {
            System.err.println("An error occurred while connecting to MongoDB");
            e.printStackTrace();
        }
    }

    public void insertRequestDetails(String userAgent, String uuid, long timestamp, String method, String requestDetails, String responseDetails, String ipAddress) {
        MongoCollection<Document> collection = database.getCollection("pokerOperations");
        Document doc = new Document("userAgent", userAgent)
                .append("uuid", uuid)
                .append("timestamp", timestamp)
                .append("method", method)
                .append("requestDetails", requestDetails)
                .append("responseDetails", responseDetails)
                .append("ipAddress", ipAddress);
        collection.insertOne(doc);
        System.out.println("Request details inserted.");
    }

    public void insertCardDrawAction(Card card) {
        MongoCollection<Document> collection = database.getCollection("cardDraws");
        Document doc = new Document("code", card.getCode())
                .append("value", card.getValue())
                .append("suit", card.getSuit());
        collection.insertOne(doc);
    }
}
