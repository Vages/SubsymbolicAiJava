package project4;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

public class ContinuousTimeRecurrentNeuralNetwork {
    protected int[] topology;
    
    protected INDArray[] internalActivations;
    protected INDArray[] outputActivations;
    
    protected INDArray[] transitionWeights;
    protected INDArray[] internalLayerWeights;
    protected INDArray[] biasWeights;
    protected INDArray[] gains;
    protected INDArray[] timeConstants;
    
    protected INDArray biasNode;

    protected ContinuousTimeRecurrentNeuralNetwork(int[] topology, NeuralNetworkGene[] genes) {
        this.topology = topology;
        this.outputActivations = new INDArray[topology.length];
        this.internalActivations = new INDArray[topology.length];

        // All of these arrays will have an empty first index. This will make the code for them simpler.
        this.transitionWeights = new INDArray[topology.length];
        this.internalLayerWeights = new INDArray[topology.length];
        this.biasWeights = new INDArray[topology.length];
        this.gains = new INDArray[topology.length];
        this.timeConstants = new INDArray[topology.length];

        this.biasNode = Nd4j.ones(1);

        int noOfGenesAdded = 0;

        this.outputActivations[0] = Nd4j.zeros(topology[0]); // Add outputActivations for first layer

        for (int i = 1; i < topology.length; i++) {
            // Set internal and output activations, 1*t_n
            int neuronsInThisLayer = topology[i];
            this.internalActivations[i] = Nd4j.zeros(neuronsInThisLayer);
            this.outputActivations[i] = Nd4j.zeros(neuronsInThisLayer);
            

            // Transition weights, t_(n-1)*t_n
            int neuronsInPreviousLayer = topology[i-1];
            int noOfTransitionWeights = neuronsInPreviousLayer*neuronsInThisLayer;

            double[] thisLayersTransitionWeights = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, noOfTransitionWeights);
            this.transitionWeights[i] = Nd4j.create(thisLayersTransitionWeights, new int[]{neuronsInPreviousLayer, neuronsInThisLayer});
            noOfGenesAdded += noOfTransitionWeights;

            // Current layer weights, t_n**2
            int noOfInternalLayerWeights = neuronsInThisLayer*neuronsInThisLayer;
            double[] thisLayersCurrentLayerWeights = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, noOfInternalLayerWeights);
            this.internalLayerWeights[i] = Nd4j.create(thisLayersCurrentLayerWeights, new int[]{neuronsInThisLayer, neuronsInThisLayer});
            noOfGenesAdded += noOfInternalLayerWeights;

            // Biases, 1*t_n
            double[] thisLayersBiasWeights = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, neuronsInThisLayer);
            this.biasWeights[i] = Nd4j.create(thisLayersBiasWeights, new int[]{1, neuronsInThisLayer});
            noOfGenesAdded += neuronsInThisLayer;

            // Gains, 1*t_n
            double[] thisLayersGains = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, neuronsInThisLayer);
            this.gains[i] = Nd4j.create(thisLayersGains, new int[]{1, neuronsInThisLayer});
            noOfGenesAdded += neuronsInThisLayer;

            // Time constants, 1*t_n
            double[] thisLayersTimeConstants = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, neuronsInThisLayer);
            this.gains[i] = Nd4j.create(thisLayersTimeConstants, new int[]{1, neuronsInThisLayer});
            noOfGenesAdded += neuronsInThisLayer;
        }

    }

    /**
     * Propagates the information in the input nodes through the network.
     */
    public void propagate() {
        for (int i = 1; i < this.outputActivations.length; i++){
            // Get internal and external activations
            INDArray previousLayerOutputActivations = this.outputActivations[i-1];
            INDArray thisLayerOutputActivations = this.outputActivations[i];
            INDArray lastTimeStepInternalActivations = this.internalActivations[i];

            // Get all weights related to this layer
            INDArray previousLayerTransitionWeights = this.transitionWeights[i];
            INDArray thisInternalWeights = this.internalLayerWeights[i];
            INDArray thisLayerBiases = this.biasWeights[i];
            INDArray thisLayerGain = this.gains[i];
            INDArray thisLayerTimeConstants = this.timeConstants[i];

            // Calculate contributions from other nodes (including bias)
            INDArray contributionFromPreviousLayer = previousLayerOutputActivations.mmul(previousLayerTransitionWeights);
            INDArray contributionFromCurrentLayer = thisLayerOutputActivations.mmul(thisInternalWeights);
            INDArray contributionFromBias = biasNode.mmul(thisLayerBiases);

            INDArray sumOfContributions = contributionFromBias.add(contributionFromCurrentLayer).add(contributionFromPreviousLayer);

            // Update the internal activation
            INDArray differenceFromLastStep = sumOfContributions.sub(lastTimeStepInternalActivations);
            INDArray derivative = differenceFromLastStep.div(thisLayerTimeConstants);
            INDArray newInternalActivation = lastTimeStepInternalActivations.add(derivative);
            this.internalActivations[i] = newInternalActivation;

            // Set output activation
            INDArray gainedInternalActivations = newInternalActivation.mul(thisLayerGain);
            this.outputActivations[i] = Transforms.sigmoid(gainedInternalActivations);
        }
    }

    /**
     * Converts a certain number of genes to an array of doubles, starting with the start index.
     *
     * @param genes         a gene array to copy from
     * @param start         start index, inclusive
     * @param noOfElements  number of elements to be gotten
     * @return              array of gene values
     */
    private double[] convertGeneRangeToDoublesArray(NeuralNetworkGene[] genes, int start, int noOfElements) {
        int roof = start+noOfElements;
        double[] values = new double[roof-start];

        for (int i = start; i < roof; i++) {
            values[i] = genes[i].getValue();
        }

        return values;
    }

}
