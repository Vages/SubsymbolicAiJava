package project4;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class ContinuousTimeRecurrentNeuralNetwork {
    protected int[] topology;
    protected INDArray[] activations;
    protected INDArray[] transitionWeights;
    protected INDArray[] currentLayerWeights;
    protected INDArray[] biasWeights;
    protected INDArray[] gains;
    protected INDArray[] timeConstants;
    protected INDArray biasNode;

    protected ContinuousTimeRecurrentNeuralNetwork(int[] topology, NeuralNetworkGene[] genes) {
        this.topology = topology;
        this.activations = new INDArray[topology.length];

        // All of these arrays will have an empty first index. This will make the code for them simpler.
        this.transitionWeights = new INDArray[topology.length];
        this.currentLayerWeights = new INDArray[topology.length];
        this.biasWeights = new INDArray[topology.length];
        this.gains = new INDArray[topology.length];
        this.timeConstants = new INDArray[topology.length];

        this.biasNode = Nd4j.ones(1);

        int noOfGenesAdded = 0;

        this.activations[0] = Nd4j.zeros(topology[0]); // Add activations for first layer

        for (int i = 1; i < topology.length; i++) {
            // Activations, 1*t_n
            int neuronsInThisLayer = topology[i];
            this.activations[i] = Nd4j.zeros(neuronsInThisLayer);

            // Transition weights, t_(n-1)*t_n
            int neuronsInPreviousLayer = topology[i-1];
            int noOfTransitionWeights = neuronsInPreviousLayer*neuronsInThisLayer;

            double[] thisLayersTransitionWeights = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, noOfTransitionWeights);
            this.transitionWeights[i] = Nd4j.create(thisLayersTransitionWeights, new int[]{neuronsInPreviousLayer, neuronsInThisLayer});
            noOfGenesAdded += noOfTransitionWeights;

            // Current layer weights, t_n**2
            int noOfCurrentLayerWeights = neuronsInThisLayer*neuronsInThisLayer;
            double[] thisLayersCurrentLayerWeights = convertGeneRangeToDoublesArray(genes, noOfGenesAdded, noOfCurrentLayerWeights);
            this.currentLayerWeights[i] = Nd4j.create(thisLayersCurrentLayerWeights, new int[]{neuronsInThisLayer, neuronsInThisLayer});
            noOfGenesAdded += noOfCurrentLayerWeights;

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
