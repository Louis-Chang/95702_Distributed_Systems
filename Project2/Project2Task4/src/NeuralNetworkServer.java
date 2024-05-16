import com.google.gson.Gson;
import jsonObj.req.NeuralNetworkRequest;
import jsonObj.req.SetCurrentRangeRequest;
import jsonObj.req.TestRequest;
import jsonObj.req.TrainRequest;
import jsonObj.res.*;
import neuronObj.NeuronLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/22/2024
 */
public class NeuralNetworkServer {

    public static void main(String args[]) {

        // Create an initial truth table with all 0's in the range.
        List<Double[][]> userTrainingSets = new ArrayList<>(Arrays.asList(
                new Double[][]{{0.0, 0.0}, {0.0}},
                new Double[][]{{0.0, 1.0}, {0.0}},
                new Double[][]{{1.0, 0.0}, {0.0}},
                new Double[][]{{1.0, 1.0}, {0.0}}
        ));
        //     Create a neural network suitable for working with truth tables.
        //     There will be two inputs, 5 hidden neurons, and 1 output. All weights and biases will be random.
        //     This is the initial neural network on start up.
        NeuralNetwork neuralNetwork = new NeuralNetwork(2, 5, 1, null, null, null, null);

        Random rand = new Random();
        int random_choice;

        // Hold a list of doubles for input for the neural network to train on.
        // In this example, if we want to train the neural network to learn the XOR,
        // the list would have two doubles, say 0 1 or 1 0 or 1 1.
        List<Double> userTrainingInputs;

        // Hold a list of double for the output of training. For example, XOR would produce 1 double as output.
        List<Double> userTrainingOutputs;

        Gson gson = new Gson();
        DatagramSocket aSocket = null;
        byte[] buffer = new byte[1000];
        try {
            System.out.println("The Neural Network server is running.");
            aSocket = new DatagramSocket(6789);
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            while(true) {
                aSocket.receive(request);
                // copy the request data into a new byte array with correct length
                byte[] requestData = new byte[request.getLength()];
                System.arraycopy(request.getData(), 0, requestData, 0, request.getLength());
                String requestString = new String(request.getData(), 0, request.getLength());
                System.out.println("Request: " + requestString);

                // Process the request
                NeuralNetworkResponse response;
                if (requestString.contains(NeuralNetworkRequest.GET_CURRENT_RANGE)) {   // Get the current range
                    double[][] responseData = new double[4][3];
                    for (int r = 0; r < 4; r++) {
                        responseData[r][0] = userTrainingSets.get(r)[0][0];
                        responseData[r][1] = userTrainingSets.get(r)[0][1];
                        responseData[r][2] = userTrainingSets.get(r)[1][0];
                    }
                    response = new GetCurrentRangeResponse(responseData);
                } else if (requestString.contains(NeuralNetworkRequest.SET_CURRENT_RANGE)) {    // Set the current range
                    SetCurrentRangeRequest req = gson.fromJson(requestString, SetCurrentRangeRequest.class);
                    userTrainingSets = new ArrayList<>(Arrays.asList(
                            new Double[][]{{0.0, 0.0}, {req.getVal1()}},
                            new Double[][]{{0.0, 1.0}, {req.getVal2()}},
                            new Double[][]{{1.0, 0.0}, {req.getVal3()}},
                            new Double[][]{{1.0, 1.0}, {req.getVal4()}}
                    ));
                    // Build a new neural network with new random weights.
                    neuralNetwork = new NeuralNetwork(2, 5, 1, null, null, null, null);
                    response = new SetCurrentRangeResponse();
                } else if (requestString.contains(NeuralNetworkRequest.TRAIN)) {    // Train the neural network
                    TrainRequest req = gson.fromJson(requestString, TrainRequest.class);
                    int n = req.getIterations();
                    for (int i = 0; i < n; i++) {
                        random_choice = rand.nextInt(4);
                        // Get the two inputs
                        userTrainingInputs = Arrays.asList(userTrainingSets.get(random_choice)[0]);
                        // Get the one output
                        userTrainingOutputs = Arrays.asList(userTrainingSets.get(random_choice)[1]);
                        // Show that row to the neural network
                        neuralNetwork.train(userTrainingInputs, userTrainingOutputs);
                    }
                    // Show error as we train
                    double error = neuralNetwork.calculateTotalError(userTrainingSets);
                    response = new TrainResponse(n, error);
                } else if (requestString.contains(NeuralNetworkRequest.TEST)) { // Test the neural network
                    TestRequest req = gson.fromJson(requestString, TestRequest.class);
                    double input0 = req.getVal1();
                    double input1 = req.getVal2();
                    List<Double> testUserInputs = new ArrayList<>(Arrays.asList(input0, input1));
                    List<Double> userOutput = neuralNetwork.feedForward(testUserInputs);
                    response = new TestResponse(userOutput.get(0));
                } else {    // Error
                    response = new ErrorResponse();
                }

                // reply to client
                String resJsonStr = gson.toJson(response);
                System.out.println("Response: " + resJsonStr);
                byte[] replyData = resJsonStr.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                DatagramPacket reply = new DatagramPacket(replyData, replyData.length, request.getAddress(), request.getPort());
                aSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(aSocket != null) {
                System.out.println("UDP Server side quitting");
                aSocket.close();
            }
        }
    }


}
