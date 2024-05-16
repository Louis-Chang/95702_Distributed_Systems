package jsonObj.req;

/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */

// This class is used to create a request object for the neural network
public abstract class NeuralNetworkRequest {
    public static final String GET_CURRENT_RANGE = "getCurrentRange";
    public static final String SET_CURRENT_RANGE = "setCurrentRange";
    public static final String TRAIN = "train";
    public static final String TEST = "test";

    String request;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}

