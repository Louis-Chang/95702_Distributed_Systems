package jsonObj.req;

/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */
public class GetCurrentRangeRequest extends NeuralNetworkRequest {
    public static final String GET_CURRENT_RANGE = "getCurrentRange";

    public GetCurrentRangeRequest() {
        this.request = GET_CURRENT_RANGE;
    }
}
