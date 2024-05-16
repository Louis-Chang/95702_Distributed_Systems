import neuronObj.NeuronLayer;

import java.util.*;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/22/2024
 */
public class NeuralNetwork {
    // The learning rate is chosen experimentally. Typically, it is set between 0 and 1.
    private double LEARNING_RATE = 0.5;
    // This truth table example will have two inputs.
    private int numInputs;

    // This neural network will be built from two layers of neurons.
    private static NeuronLayer hiddenLayer;
    private static NeuronLayer outputLayer;

    // The neural network is constructed by specifying the number of inputs, the number of neurons in the hidden layer,
    // the number of neurons in the output layer, the hidden layer weights, the hidden layer bias,
    // the output layer weights and output layer bias.
    public NeuralNetwork(int numInputs, int numHidden, int numOutputs, List<Double> hiddenLayerWeights, Double hiddenLayerBias,
                               List<Double> outputLayerWeights, Double outputLayerBias) {
        // How many inputs to this neural network
        this.numInputs = numInputs;

        // Create two layers, one hidden layer and one output layer.
        hiddenLayer = new NeuronLayer(numHidden, hiddenLayerBias);
        outputLayer = new NeuronLayer(numOutputs, outputLayerBias);

        initWeightsFromInputsToHiddenLayerNeurons(hiddenLayerWeights);

        initWeightsFromHiddenLayerNeuronsToOutputLayerNeurons(outputLayerWeights);
    }
    // The hidden layer neurons have weights that are assigned here. If the actual weights are not
    // provided, random weights are generated.
    public void initWeightsFromInputsToHiddenLayerNeurons(List<Double> hiddenLayerWeights) {

        int weightNum = 0;
        for (int h = 0; h < hiddenLayer.neurons.size(); h++) {
            for (int i = 0; i < numInputs; i++) {
                if (hiddenLayerWeights == null) {
                    hiddenLayer.neurons.get(h).weights.add((new Random()).nextDouble());
                } else {
                    hiddenLayer.neurons.get(h).weights.add(hiddenLayerWeights.get(weightNum));
                }
                weightNum = weightNum + 1;
            }
        }
    }

    // The output layer neurons have weights that are assigned here. If the actual weights are not
    // provided, random weights are generated.
    public void initWeightsFromHiddenLayerNeuronsToOutputLayerNeurons(List<Double> outputLayerWeights) {
        int weightNum = 0;
        for (int o = 0; o < outputLayer.neurons.size(); o++) {
            for (int h = 0; h < hiddenLayer.neurons.size(); h++) {
                if (outputLayerWeights == null) {
                    outputLayer.neurons.get(o).weights.add((new Random()).nextDouble());
                } else {
                    outputLayer.neurons.get(o).weights.add(outputLayerWeights.get(weightNum));
                }
                weightNum = weightNum + 1;
            }
        }
    }

    // Display a NeuralNetwork object by calling the toString on each layer.
    public String toString() {
        String s = "";
        s = s + "-----\n";
        s = s + "* Inputs: " + numInputs + "\n";
        s = s + "-----\n";

        s = s + "Hidden Layer\n";
        s = s + hiddenLayer.toString();
        s = s + "----";
        s = s + "* Output layer\n";
        s = s + outputLayer.toString();
        s = s + "-----";
        return s;
    }

    // Feed the inputs provided into the network and get outputs.
    // The inputs are provided to the hidden layer. The hidden layer's outputs
    // are provided as inputs the output layer. The outputs of the output layer
    // are returned to the caller as a list of outputs. That number of outputs may be one.
    // The feedForward method is called on each layer.
    public List<Double> feedForward(List<Double> inputs) {

        List<Double> hiddenLayerOutputs = hiddenLayer.feedForward(inputs);
        return outputLayer.feedForward(hiddenLayerOutputs);
    }

    // Training means to feed the data forward - forward propagation. Compare the result with the target(s), and
    // use backpropagation to update the weights. See the blog post to review the math.
    public void train(List<Double> trainingInputs, List<Double> trainingOutputs) {

        // Update state of neural network and ignore the return value
        feedForward(trainingInputs);
        // Perform backpropagation
        List<Double> pdErrorsWRTOutputNeuronTotalNetInput =
                new ArrayList<Double>(Collections.nCopies(outputLayer.neurons.size(), 0.0));
        for (int o = 0; o < outputLayer.neurons.size(); o++) {
            pdErrorsWRTOutputNeuronTotalNetInput.set(o, outputLayer.neurons.get(o).calculatePDErrorWRTTotalNetInput(trainingOutputs.get(o)));
        }
        List<Double> pdErrorsWRTHiddenNeuronTotalNetInput =
                new ArrayList<>(Collections.nCopies(hiddenLayer.neurons.size(), 0.0));
        for (int h = 0; h < hiddenLayer.neurons.size(); h++) {
            double dErrorWRTHiddenNeuronOutput = 0;
            for (int o = 0; o < outputLayer.neurons.size(); o++) {
                dErrorWRTHiddenNeuronOutput +=
                        pdErrorsWRTOutputNeuronTotalNetInput.get(o) * outputLayer.neurons.get(o).weights.get(h);
                pdErrorsWRTHiddenNeuronTotalNetInput.set(h, dErrorWRTHiddenNeuronOutput *
                        hiddenLayer.neurons.get(h).calculatePDTotalNetInputWRTInput());
            }
        }
        for (int o = 0; o < outputLayer.neurons.size(); o++) {
            for (int wHo = 0; wHo < outputLayer.neurons.get(o).weights.size(); wHo++) {
                double pdErrorWRTWeight =
                        pdErrorsWRTOutputNeuronTotalNetInput.get(o) *
                                outputLayer.neurons.get(o).calculatePDTotalNetInputWRTWeight(wHo);
                outputLayer.neurons.get(o).weights.set(wHo, outputLayer.neurons.get(o).weights.get(wHo) - LEARNING_RATE * pdErrorWRTWeight);
            }
        }
        for (int h = 0; h < hiddenLayer.neurons.size(); h++) {
            for (int wIh = 0; wIh < hiddenLayer.neurons.get(h).weights.size(); wIh++) {
                double pdErrorWRTWeight =
                        pdErrorsWRTHiddenNeuronTotalNetInput.get(h) *
                                hiddenLayer.neurons.get(h).calculatePDTotalNetInputWRTWeight(wIh);
                hiddenLayer.neurons.get(h).weights.set(wIh, hiddenLayer.neurons.get(h).weights.get(wIh) - LEARNING_RATE * pdErrorWRTWeight);
            }
        }
    }

    // Perform a feed forward for each training row and total the error.
    public double calculateTotalError(List<Double[][]> trainingSets) {

        double totalError = 0.0;

        for (int t = 0; t < trainingSets.size(); t++) {
            List<Double> trainingInputs = Arrays.asList(trainingSets.get(t)[0]);
            List<Double> trainingOutputs = Arrays.asList(trainingSets.get(t)[1]);
            feedForward(trainingInputs);
            for (int o = 0; o < trainingOutputs.size(); o++) {
                totalError += outputLayer.neurons.get(o).calculate_error(trainingOutputs.get(o));
            }
        }
        return totalError;
    }
}
