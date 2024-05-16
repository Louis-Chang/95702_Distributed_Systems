package jsonObj.res;

/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */
// This class is used to create a response object for the neural network
public class SetCurrentRangeResponse extends NeuralNetworkResponse {
    public SetCurrentRangeResponse() {
        this.response = NeuralNetworkResponse.SET_CURRENT_RANGE;
        this.status = STATUS_OK;
    }
}
