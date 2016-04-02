package project3;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;

public class FlatlandNeuralNetwork {
    private int[] topology;
    private INDArray[] activations;
    private INDArray[] weights;

    public FlatlandNeuralNetwork(int[] topology, double[] weight) {
        this.topology = topology;
        this.activations = new INDArray[topology.length];
        this.weights = new INDArray[topology.length-1];

        int no_of_weights_added_so_far = 0;

        for (int i = 0; i < topology.length-1; i++) {
            int activations_in_this_layer = topology[i] + 1;
            int activations_in_next_layer = (i != topology.length-2) ? (topology[i + 1] + 1): topology[i+1];
            int weights_to_be_gotten = activations_in_this_layer*activations_in_next_layer;

            this.activations[i] = Nd4j.zeros(activations_in_this_layer); // Add 1 for a bias node in each layer

            double[] this_layers_weights = Arrays.copyOfRange(weight, no_of_weights_added_so_far, no_of_weights_added_so_far+weights_to_be_gotten);
            this.weights[i] = Nd4j.create(this_layers_weights, new int[]{activations_in_this_layer, activations_in_next_layer});
        }

        // Add output layer without bias node
        activations[topology.length-1] = Nd4j.zeros(topology[topology.length-1]);
    }

    public void propagate() {
        for (int i = 0; i < this.weights.length; i++) {
            INDArray current_layer = this.activations[i];
            INDArray transition_weights = this.weights[i];

            current_layer.putScalar(topology[i], 1);  // Set bias node to 1

            this.activations[i+1] = current_layer.mmul(transition_weights);
            INDArray next_layer = this.activations[i+1];

            for (int j = 0; j < next_layer.length(); j++){
                next_layer.putScalar(j, sigmoid(next_layer.getDouble(j)));
            }
        }
    }

    public void setInput(int index, double value){
        this.activations[0].putScalar(index, value);
    }

    public INDArray getOutputs() {
        return this.activations[activations.length-1];
    }

    private double sigmoid(double exponent) {
        return 1.0/(1.0+Math.pow(Math.E, -exponent));
    }
}
