package neuronObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */
// Each Neuron has a bias, a list of weights, and a list of inputs.
// Each neuron will produce a single real number as an output.
public class Neuron {
    private double bias;
    public List<Double> weights;
    public List<Double> inputs;
    double output;
    // Construct a neuron with a bias and reserve memory for its weights.
    public Neuron(double bias) {
        this.bias = bias;
        weights = new ArrayList<Double>();
    }
    //Calculate the output by using the inputs and weights already provided.
    //Squash the result so the output is between 0 and 1.
    public double calculateOutput(List<Double> inputs) {

        this.inputs = inputs;

        output = squash(calculateTotalNetInput());
        return output;
    }
    // Compute the total net input from the input, weights, and bias.
    public double calculateTotalNetInput() {

        double total = 0.0;
        for (int i = 0; i < inputs.size(); i++) {
            total += inputs.get(i) * weights.get(i);
        }
        return total + bias;
    }

    // This is the activation function, returning a value between 0 and 1.
    public double squash(double totalNetInput) {
        double v = 1.0 / (1.0 + Math.exp(-1.0 * totalNetInput));
        return v;
    }
    // Compute the partial derivative of the error with respect to the total net input.
    public Double calculatePDErrorWRTTotalNetInput(double targetOutput) {
        return calculatePDErrorWRTOutput(targetOutput) * calculatePDTotalNetInputWRTInput();
    }
    // Calculate error. How different are we from the target?
    public Double calculate_error(Double targetOutput) {
        double theError = 0.5 * Math.pow(targetOutput - output, 2.0);
        return theError;
    }
    // Compute the partial derivative of the error with respect to the output.
    public Double calculatePDErrorWRTOutput(double targetOutput) {
        return (-1) * ( targetOutput - output);
    }
    // Compute the partial derivative of the total net input with respect to the input.
    public Double  calculatePDTotalNetInputWRTInput() {
        return output * ( 1.0 - output);

    }
    // Calculate the partial derivative of the total net input with respect to the weight.
    public Double calculatePDTotalNetInputWRTWeight(int index) {
        return inputs.get(index);
    }
}