package cmu.ds.project3;

import com.google.gson.Gson;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 03/17/2024
 */
public class ClientTCP {
    private static final Gson gson = new Gson();
    private static final int PORT = 7778;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            String userInput;
            while (true) {
                System.out.println("\n0. View basic blockchain status.");
                System.out.println("1. Add a transaction to the blockchain.");
                System.out.println("2. Verify the blockchain.");
                System.out.println("3. View the blockchain.");
                System.out.println("4. Corrupt the chain.");
                System.out.println("5. Hide the corruption by repairing the chain.");
                System.out.println("6. Exit");

                System.out.print("Enter your choice: ");
                userInput = scanner.nextLine();

                if ("6".equals(userInput)) {
                    System.out.println("Exiting...");
                    break;
                }

                // Create request message based on user input
                RequestMessage request;
                switch (userInput) {
                    case "0":
                        request = new RequestMessage(RequestMessage.VIEW_BLOCKCHAIN_STATUS);
                        break;
                    case "1":
                        System.out.print("Enter difficulty > ");
                        int difficulty = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter transaction: ");
                        String transaction = scanner.nextLine();
                        request = new RequestMessage(RequestMessage.ADD_TRANSACTION, transaction, difficulty);
                        break;
                    case "2":
                        request = new RequestMessage(RequestMessage.VERIFY_BLOCKCHAIN);
                        break;
                    case "3":
                        request = new RequestMessage(RequestMessage.VIEW_BLOCKCHAIN);
                        break;
                    case "4":
                        System.out.print("Enter block ID of block to corrupt: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter new data for block " + id + ": ");
                        String newData = scanner.nextLine();
                        request = new RequestMessage(RequestMessage.CORRUPT_BLOCKCHAIN, id, newData);
                        break;
                    case "5":
                        request = new RequestMessage(RequestMessage.REPAIR_CHAIN);
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        continue;
                }

                String jsonRequest = gson.toJson(request);
                out.println(jsonRequest);

                String jsonResponse = in.readLine();
                ResponseMessage response = gson.fromJson(jsonResponse, ResponseMessage.class);
                System.out.println("Server response: " + response.getMessage());
                for (Map.Entry<String, String> entry : response.getData().entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
