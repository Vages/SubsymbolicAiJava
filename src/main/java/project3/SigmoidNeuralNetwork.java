package project3;

import static project3.MatrixAndVectorOperations.*;

import java.util.Arrays;

public class SigmoidNeuralNetwork {
    protected double[][]biasWeights;
    protected int[] topology;
    protected double[][] activations;
    protected double[][][] weights;
    protected boolean biasOn;

    protected SigmoidNeuralNetwork(int[] topology, double[] weightDoubles, boolean biasOn) {
        this.topology = topology;
        this.activations = new double[topology.length][];
        this.weights = new double[topology.length-1][][];
        this.biasWeights = new double[topology.length-1][];
        this.biasOn = biasOn;

        int noOfWeightsAddedSoFar = 0;

        for (int i = 0; i < topology.length-1; i++) {
            int activationsInThisLayer = topology[i];
            int activationsInNextLayer = topology[i+1];
            int weightsToBeGotten = activationsInThisLayer*activationsInNextLayer;

            this.activations[i] = new double[activationsInThisLayer];

            double[] thisLayersWeights = Arrays.copyOfRange(weightDoubles, noOfWeightsAddedSoFar, noOfWeightsAddedSoFar+weightsToBeGotten);
            this.weights[i] = createTwoDimensionalMatrix(thisLayersWeights, activationsInThisLayer, activationsInNextLayer);
            noOfWeightsAddedSoFar += weightsToBeGotten;

            this.biasWeights[i] = Arrays.copyOfRange(weightDoubles, noOfWeightsAddedSoFar, noOfWeightsAddedSoFar+weightsToBeGotten);
            noOfWeightsAddedSoFar += activationsInNextLayer;
        }

        // Add output layer without bias node
        activations[topology.length-1] = new double[topology[topology.length-1]];
    }

    public void propagate() {
        for (int i = 0; i < this.weights.length; i++) {
            double[] currentLayer = this.activations[i];
            double[][] transitionWeights = this.weights[i];
            double[] biasWeights = this.biasWeights[i];

            double[] newActivation = multiplyVectorByMatrix(currentLayer, transitionWeights);
            if (biasOn) {
                newActivation = addArrays(newActivation, biasWeights);
            }

            this.activations[i+1] = applySigmoid(newActivation);
        }
    }

    public void setInput(int index, double value){
        this.activations[0][index] = value;
    }

    public double[] getOutputs() {
        return this.activations[activations.length-1];
    }
}
