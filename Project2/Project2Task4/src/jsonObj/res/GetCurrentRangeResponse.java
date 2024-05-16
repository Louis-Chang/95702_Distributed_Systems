package jsonObj.res;

/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */
// This class is used to create a response object for the neural network
public class GetCurrentRangeResponse extends NeuralNetworkResponse {
    private double[][] userTrainingSets;

    public GetCurrentRangeResponse(double[][] userTrainingSets) {
        this.response = NeuralNetworkResponse.GET_CURRENT_RANGE;
        this.status = STATUS_OK;
        this.userTrainingSets = userTrainingSets;
    }
    public double[][] getUserTrainingSets() {
        return userTrainingSets;
    }

    public void setUserTrainingSets(double[][] userTrainingSets) {
        this.userTrainingSets = userTrainingSets;
    }
}
