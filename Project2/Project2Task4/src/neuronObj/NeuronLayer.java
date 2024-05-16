package neuronObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: Louis Chang
 * Last Modified: 02/22/2024
 */
// The Neuron layer represents a collection of neurons.
// All neurons in the same layer have the same bias.
// We include in each layer the number of neurons and the list of neurons.
public class NeuronLayer {
    private double bias;
    private int numNeurons;

    public List<Neuron> neurons;

    // Construct by specifying the number of neurons and the bias that applies to all the neurons in this layer.
    // If the bias is not provided, choose a random bias.
    // Create neurons for this layer and set the bias in each neuron.
    public NeuronLayer(int numNeurons, Double bias) {
        if(bias == null) {

            this.bias = new Random().nextDouble();
        }
        else {
            this.bias = bias;
        }
        this.numNeurons = numNeurons;
        this.neurons  = new ArrayList<Neuron>();
        for(int i = 0; i < numNeurons; i++) {
            this.neurons.add(new Neuron(this.bias));
        }
    }
    // Display the neuron layer by displaying each neuron.
    public String toString() {
        String s = "";
        s = s + "Neurons: " + neurons.size() + "\n";
        for(int n = 0; n < neurons.size(); n++) {
            s = s + "Neuron " + n + "\n";
            for (int w = 0; w < neurons.get(n).weights.size(); w++) {
                s = s + "\tWeight: " + neurons.get(n).weights.get(w) + "\n";
            }
            s = s + "\tBias " + bias + "\n";
        }

        return s;
    }

    // Feed the input data into the neural network and produce some output in the output layer.
    // Return a list of outputs. There may be a single output in the output list.
    public List<Double> feedForward(List<Double> inputs) {

        List<Double> outputs = new ArrayList<Double>();

        for(Neuron neuron : neurons ) {

            outputs.add(neuron.calculateOutput(inputs));
        }

        return outputs;
    }
    // Return a list of outputs from this layer.
    // We do this by gathering the output of each neuron in the layer.
    // This is returned as a list of Doubles.
    // This is not used in this program.
    List<Double> getOutputs() {
        List<Double> outputs = new ArrayList<Double>();
        for(Neuron neuron : neurons ) {
            outputs.add(neuron.output);
        }
        return outputs;
    }
}
