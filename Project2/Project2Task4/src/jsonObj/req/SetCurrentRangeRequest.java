package jsonObj.req;


/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */

// This class is used to create a request object for the neural network
public class SetCurrentRangeRequest extends NeuralNetworkRequest {
    private double val1;
    private double val2;
    private double val3;
    private double val4;

    public SetCurrentRangeRequest(double val1, double val2, double val3, double val4) {
        this.request = SET_CURRENT_RANGE;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.val4 = val4;
    }

    public double getVal1() {
        return val1;
    }

    public void setVal1(double val1) {
        this.val1 = val1;
    }

    public double getVal2() {
        return val2;
    }

    public void setVal2(double val2) {
        this.val2 = val2;
    }

    public double getVal3() {
        return val3;
    }

    public void setVal3(double val3) {
        this.val3 = val3;
    }

    public double getVal4() {
        return val4;
    }

    public void setVal4(double val4) {
        this.val4 = val4;
    }
}
