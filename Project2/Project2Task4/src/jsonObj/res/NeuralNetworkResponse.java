package jsonObj.res;

/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */

// This class is used to create a response object for the neural network
public abstract class NeuralNetworkResponse {
    public static final String GET_CURRENT_RANGE = "getCurrentRange";
    public static final String SET_CURRENT_RANGE = "setCurrentRange";
    public static final String TRAIN = "train";
    public static final String TEST = "test";
    public static final String ERROR = "error";

    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";

    String response;
    String status;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
