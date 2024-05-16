package jsonObj.res;

/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */

// This class is used to create a response object for the neural network
public class TrainResponse extends NeuralNetworkResponse {
    private int steps;
    private double error;

    public TrainResponse(int steps, double error) {
        this.response = NeuralNetworkResponse.TRAIN;
        this.status = STATUS_OK;
        this.steps = steps;
        this.error = error;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }
}
