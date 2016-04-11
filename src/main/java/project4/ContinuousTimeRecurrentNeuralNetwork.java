package project4;

import static project3.MatrixAndVectorOperations.*;

public class ContinuousTimeRecurrentNeuralNetwork {
    protected int[] topology;

    protected double[][] internalActivations;
    protected double[][] outputActivations;

    protected double[][][] transitionWeights;
    protected double[][][] internalLayerWeights;
    protected double[][] biasWeights;
    protected double[][] gains;
    protected double[][] timeConstants;

    protected double[] biasNode;

    protected ContinuousTimeRecurrentNeuralNetwork(int[] topology, NeuralNetworkGene[] genes) {
        this.topology = topology;
        this.outputActivations = new double[topology.length][];
        this.internalActivations = new double[topology.length][];

        // All of these arrays will have an empty first index. This will make the code for them simpler.
        this.transitionWeights = new double[topology.length][][];
        this.internalLayerWeights = new double[topology.length][][];
        this.biasWeights = new double[topology.length][];
        this.gains = new double[topology.length][];
        this.timeConstants = new double[topology.length][];

        this.biasNode = new double[]{1.0};

        int noOfGenesAdded = 0;

        this.outputActivations[0] = new double[topology[0]]; // Add outputActivations for first layer

        for (int i = 1; i < topology.length; i++) {
            // Set internal and output activations
            int neuronsInThisLayer = topology[i];
            this.internalActivations[i] = new double[neuronsInThisLayer];
            this.outputActivations[i] = new double[neuronsInThisLayer];

            // Transition weights, t_(n-1)*t_n
            int neuronsInPreviousLayer = topology[i - 1];
            int noOfTransitionWeights = neuronsInPreviousLayer * neuronsInThisLayer;

            double[] thisLayersTransitionWeights = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, noOfTransitionWeights);
            this.transitionWeights[i] = createTwoDimensionalMatrix(thisLayersTransitionWeights, neuronsInPreviousLayer, neuronsInThisLayer);
            noOfGenesAdded += noOfTransitionWeights;

            // Current layer weights, t_n**2
            int noOfInternalLayerWeights = neuronsInThisLayer * neuronsInThisLayer;
            double[] thisLayersCurrentLayerWeights = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, noOfInternalLayerWeights);
            this.internalLayerWeights[i] = createTwoDimensionalMatrix(thisLayersCurrentLayerWeights, neuronsInThisLayer, neuronsInThisLayer);
            noOfGenesAdded += noOfInternalLayerWeights;

            // Biases, 1*t_n
            this.biasWeights[i] = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, neuronsInThisLayer);
            noOfGenesAdded += neuronsInThisLayer;

            // Gains, 1*t_n
            this.gains[i] = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, neuronsInThisLayer);
            noOfGenesAdded += neuronsInThisLayer;

            // Time constants, 1*t_n
            this.timeConstants[i] = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, neuronsInThisLayer);
            noOfGenesAdded += neuronsInThisLayer;
        }

    }

    /**
     * Propagates the information in the input nodes through the network.
     */
    public void propagate() {
        for (int i = 1; i < this.outputActivations.length; i++) {
            // Get internal and external activations
            double[] previousLayerOutputActivations = this.outputActivations[i - 1];
            double[] thisLayerOutputActivations = this.outputActivations[i];
            double[] lastTimeStepInternalActivations = this.internalActivations[i];

            // Get all weights related to this layer
            double[][] previousLayerTransitionWeights = this.transitionWeights[i];
            double[][] thisInternalWeights = this.internalLayerWeights[i];
            double[] thisLayerBiases = this.biasWeights[i];
            double[] thisLayerGain = this.gains[i];
            double[] thisLayerTimeConstants = this.timeConstants[i];

            // Calculate contributions from other nodes (including bias)
            double[] contributionFromPreviousLayer = multiplyVectorByMatrix(previousLayerOutputActivations, previousLayerTransitionWeights);
            double[] contributionFromCurrentLayer = multiplyVectorByMatrix(thisLayerOutputActivations, thisInternalWeights);

            double[] sumOfContributions = addArrays(contributionFromPreviousLayer, contributionFromCurrentLayer);
            sumOfContributions = addArrays(sumOfContributions, thisLayerBiases);

            // Update the internal activation
            double[] differenceFromLastStep = subtractArrays(sumOfContributions, lastTimeStepInternalActivations);
            double[] derivative = divideArrays(differenceFromLastStep, thisLayerTimeConstants);
            double[] newInternalActivation = addArrays(lastTimeStepInternalActivations, derivative);
            this.internalActivations[i] = newInternalActivation;

            // Set output activation
            double[] gainedInternalActivation = multiplyArrays(newInternalActivation, thisLayerGain);
            this.outputActivations[i] = applySigmoid(gainedInternalActivation);
        }
    }

    /**
     * Converts a certain number of genes to an array of doubles, starting with the start index.
     *
     * @param genes        a gene array to copy from
     * @param start        start index, inclusive
     * @param noOfElements number of elements to be gotten
     * @return array of gene values
     */
    private double[] convertGeneRangeToDoublesArray(NeuralNetworkGene[] genes, int start, int noOfElements) {
        double[] values = new double[noOfElements];

        for (int i = 0; i < noOfElements; i++) {
            values[i] = genes[start + i].getValue();
        }

        return values;
    }

    public void setInputActivation(int node, double value) {
        this.outputActivations[0][node] = value;
    }

    @SuppressWarnings("Duplicates")
    public int getMostActiveOutputNode() {
        double[] outputLayer = outputActivations[outputActivations.length - 1];

        int maxIndex = -1;
        double maxOutput = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < outputLayer.length; i++) {
            double output_i = outputLayer[i];

            if (output_i > maxOutput) {
                maxIndex = i;
                maxOutput = output_i;
            }
        }

        return maxIndex;
    }

    public double getOutputActivation(int node) {
        double[] outputLayer = outputActivations[outputActivations.length - 1];
        return outputLayer[node];
    }


}
