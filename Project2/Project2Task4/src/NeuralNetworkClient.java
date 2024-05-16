import com.google.gson.Gson;
import jsonObj.req.*;
import jsonObj.res.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/22/2024
 */
public class NeuralNetworkClient {
    final String host = "localhost";
    static int serverPort = 6789;
    static Scanner scanner = new Scanner(System.in);
    static Gson gson = new Gson();

    public static void main(String[] args) {
        NeuralNetworkClient client = new NeuralNetworkClient();
        Scanner scanner = new Scanner(System.in);
        System.out.println("The client is running.");
        System.out.println();

        while (true) {
            NeuralNetworkRequest req;

            // Display the menu and get the user's selection.
            int userSelection = client.menu();
            if (userSelection == 0) {   // Display the current truth table.
                req = new GetCurrentRangeRequest();
            } else if (userSelection == 1) {    // Provide four inputs for the range of the two input truth table and build a new neural network.
                System.out.println("Enter the four results of a 4 by 2 truth table. Each value should be 0 or 1.");
                double range1 = scanner.nextDouble();
                double range2 = scanner.nextDouble();
                double range3 = scanner.nextDouble();
                double range4 = scanner.nextDouble();
                req = new SetCurrentRangeRequest(range1, range2, range3, range4);
            } else if (userSelection == 2) {    // Perform a single training step.
                req = new TrainRequest(1);
            } else if (userSelection == 3) {    // Perform n training steps.
                System.out.println("Enter the number of training sets.");
                int steps = scanner.nextInt();
                req = new TrainRequest(steps);
            } else if (userSelection == 4) {    // Test with a pair of inputs.
                System.out.println("Enter a pair of doubles from a row of the truth table. These are domain values.");
                double input1 = scanner.nextDouble();
                double input2 = scanner.nextDouble();
                req = new TestRequest(input1, input2);
            } else if (userSelection == 5) {    // Exit program.
                System.out.println("Client side quitting.");
                break;
            } else {    // Invalid option.
                System.out.println("Invalid option.");
                continue;
            }

            // Send the request and process the response.
            String resStr = client.sendRequest(req);
            if (resStr.contains(NeuralNetworkResponse.STATUS_OK)) { // Determine the type of response and process it.
                if (resStr.contains(NeuralNetworkResponse.GET_CURRENT_RANGE)) { // Get the current range
                    GetCurrentRangeResponse rangeRes = gson.fromJson(resStr, GetCurrentRangeResponse.class);
                    double[][] userTrainingSets = rangeRes.getUserTrainingSets();
                    System.out.println("Working with the following truth table");
                    for (double[] userTrainingSet : userTrainingSets) {
                        System.out.println(Arrays.toString(userTrainingSet));
                    }
                } else if (resStr.contains(NeuralNetworkResponse.SET_CURRENT_RANGE)) {  // Set the current range
                    SetCurrentRangeResponse setRes = gson.fromJson(resStr, SetCurrentRangeResponse.class);
                    System.out.println("The range has been set.");
                } else if (resStr.contains(NeuralNetworkResponse.TRAIN)) {  // Train the neural network
                    TrainResponse trainRes = gson.fromJson(resStr, TrainResponse.class);
                    System.out.println("Training complete. Error: " + trainRes.getError());
                    System.out.println("After " + trainRes.getSteps() + " training steps, our error " + trainRes.getError());
                } else if (resStr.contains(NeuralNetworkResponse.TEST)) {   // Test the neural network
                    TestResponse testRes = gson.fromJson(resStr, TestResponse.class);
                    System.out.println("The range value is approximately " + testRes.getRange());
                }
            } else {
                System.out.println("Error!");
            }
        }
        scanner.close();
    }
    private int menu() {
        System.out.println("Using a neural network to learn a truth table.\nMain Menu");
        System.out.println("0. Display the current truth table.");
        System.out.println("1. Provide four inputs for the range of the two input truth table and build a new neural network. To test XOR, enter 0  1  1  0.");
        System.out.println("2. Perform a single training step.");
        System.out.println("3. Perform n training steps. 10000 is a typical value for n.");
        System.out.println("4. Test with a pair of inputs.");
        System.out.println("5. Exit program.");
        return scanner.nextInt();
    }
    private String sendRequest(NeuralNetworkRequest req) {
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            InetAddress aHost = InetAddress.getByName(host);
            String jsonStr = gson.toJson(req);
            byte[] sendData = jsonStr.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            DatagramPacket request = new DatagramPacket(sendData, sendData.length, aHost, serverPort);
            aSocket.send(request);

            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            return new String(reply.getData(), 0, reply.getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (aSocket != null) aSocket.close();
        }
    }

}
