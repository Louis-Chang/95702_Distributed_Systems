package jsonObj.res;

/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */
public class ErrorResponse extends NeuralNetworkResponse {
    public ErrorResponse() {
        this.response = NeuralNetworkResponse.ERROR;
        this.status = STATUS_ERROR;
    }
}
