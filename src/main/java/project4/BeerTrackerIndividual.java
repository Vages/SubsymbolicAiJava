package project4;

import project2.Individual;

import java.util.ArrayList;

public class BeerTrackerIndividual extends Individual<NeuralNetworkGene> {
    private NeuralNetworkGene[] genotype;
    private int[] topology;
    private ContinuousTimeRecurrentNeuralNetwork network;

    public BeerTrackerIndividual(NeuralNetworkGene[] genotype, int[] topology) {
        this.genotype = genotype;
        this.topology = topology;
    }

    /**
     * Generates a random genotype for an indidual of this class given the typology
     *
     * @param topology Topology of form {int, …}
     * @return random genotype
     */
    public static NeuralNetworkGene[] generateRandomGenotype(int[] topology) {
        ArrayList<NeuralNetworkGene> genes = new ArrayList<>();

        for (int i = 1; i < topology.length; i++) {
            int neuronsInThisLayer = topology[i];
            int neuronsInPreviousLayer = topology[i - 1];
            addGenesForLayer(genes, neuronsInThisLayer, neuronsInPreviousLayer);
        }

        return genes.toArray(new NeuralNetworkGene[genes.size()]);
    }

    /**
     * Helper method for generating a random genotype
     *
     * @param genes                  destination for the randomly generated genes
     * @param neuronsInThisLayer     number of neurons in this layer
     * @param neuronsInPreviousLayer number of neurons in previous layer
     */
    private static void addGenesForLayer(ArrayList<NeuralNetworkGene> genes, int neuronsInThisLayer, int neuronsInPreviousLayer) {
        int normalWeightsForThisLayer = neuronsInThisLayer * (neuronsInThisLayer + neuronsInPreviousLayer);

        // Normal weights
        for (int j = 0; j < normalWeightsForThisLayer; j++) {
            NeuralNetworkGene gene = new NeuralNetworkGene(0, -5, 5);
            gene.mutate();
            genes.add(gene);
        }

        // Biases
        for (int j = 0; j < neuronsInThisLayer; j++) {
            NeuralNetworkGene gene = new NeuralNetworkGene(0, -10, 0);
            gene.mutate();
            genes.add(gene);
        }

        // Gains
        for (int j = 0; j < neuronsInThisLayer; j++) {
            NeuralNetworkGene gene = new NeuralNetworkGene(0, 1, 5);
            gene.mutate();
            genes.add(gene);
        }

        // Time constants
        for (int j = 0; j < neuronsInThisLayer; j++) {
            NeuralNetworkGene gene = new NeuralNetworkGene(0, 1, 2);
            gene.mutate();
            genes.add(gene);
        }
    }

    @Override
    public NeuralNetworkGene[] getGenotype() {
        return genotype;
    }

    @Override
    public double getFitness() {
        return 0;
    }

    @Override
    public void develop() {
        this.network = new ContinuousTimeRecurrentNeuralNetwork(topology, genotype);
    }
}
