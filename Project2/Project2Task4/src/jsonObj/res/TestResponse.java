package jsonObj.res;

/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */
// This class is used to create a response object for the neural network
public class TestResponse extends NeuralNetworkResponse {
    private double range;

    public TestResponse(double range) {
        this.range = range;
        this.response = NeuralNetworkResponse.TEST;
        this.status = STATUS_OK;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }
}
