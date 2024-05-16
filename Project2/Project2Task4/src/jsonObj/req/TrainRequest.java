package jsonObj.req;

/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */
// This class is used to create a request object for the neural network
public class TrainRequest extends NeuralNetworkRequest {
    private int iterations;

    public TrainRequest(int iterations) {
        this.request = TRAIN;
        this.iterations = iterations;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}
