package project3;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.transforms.Sigmoid;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.util.Arrays;

public class SigmoidNeuralNetwork {
    private final INDArray[] biasWeights;
    protected int[] topology;
    protected INDArray[] activations;
    protected INDArray[] weights;
    protected INDArray biasNode;

    protected SigmoidNeuralNetwork(int[] topology, double[] weightDoubles, boolean biasOn) {
        this.topology = topology;
        this.activations = new INDArray[topology.length];
        this.weights = new INDArray[topology.length-1];
        this.biasWeights = new INDArray[topology.length-1];
        if (biasOn){
            this.biasNode = Nd4j.ones(1);
        } else {
            this.biasNode = Nd4j.zeros(1);
        }

        int noOfWeightsAddedSoFar = 0;

        for (int i = 0; i < topology.length-1; i++) {
            int activationsInThisLayer = topology[i];
            int activationsInNextLayer = topology[i+1];
            int weightsToBeGotten = activationsInThisLayer*activationsInNextLayer;

            this.activations[i] = Nd4j.zeros(activationsInThisLayer);

            double[] thisLayersWeights = Arrays.copyOfRange(weightDoubles, noOfWeightsAddedSoFar, noOfWeightsAddedSoFar+weightsToBeGotten);
            noOfWeightsAddedSoFar += weightsToBeGotten;
            this.weights[i] = Nd4j.create(thisLayersWeights, new int[]{activationsInThisLayer, activationsInNextLayer});

            double[] thisLayersBiasWeights = Arrays.copyOfRange(weightDoubles, noOfWeightsAddedSoFar, noOfWeightsAddedSoFar+weightsToBeGotten);
            noOfWeightsAddedSoFar += activationsInNextLayer;
            this.biasWeights[i] = Nd4j.create(thisLayersBiasWeights, new int[]{1, activationsInNextLayer});
        }

        // Add output layer without bias node
        activations[topology.length-1] = Nd4j.zeros(topology[topology.length-1]);
    }

    public void propagate() {
        for (int i = 0; i < this.weights.length; i++) {
            INDArray currentLayer = this.activations[i];
            INDArray transitionWeights = this.weights[i];
            INDArray biasWeights = this.biasWeights[i];

            INDArray activationFromCurrentLayer = currentLayer.mmul(transitionWeights);
            INDArray activationFromBiasNode = biasNode.mmul(biasWeights);
            INDArray unscaledActivation = activationFromCurrentLayer.add(activationFromBiasNode);

            this.activations[i+1] = Transforms.sigmoid(unscaledActivation);
        }
    }

    public void setInput(int index, double value){
        this.activations[0].putScalar(index, value);
    }

    public INDArray getOutputs() {
        return this.activations[activations.length-1];
    }
}