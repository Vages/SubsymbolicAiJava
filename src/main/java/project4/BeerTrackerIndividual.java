package project4;

import project2.Individual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class BeerTrackerIndividual extends Individual<NeuralNetworkGene> {
    private final TrackerAction[] availableActions;
    private NeuralNetworkGene[] genotype;
    private int[] topology;
    private ContinuousTimeRecurrentNeuralNetwork network;
    private int lastRewardVersionNumber = -1;
    private double lastAssessedFitness = 0;
    private BeerTrackerEvolutionWorld world;

    public BeerTrackerIndividual(NeuralNetworkGene[] genotype, int[] topology, TrackerAction[] availableActions, BeerTrackerEvolutionWorld world) {
        this.genotype = genotype;
        this.topology = topology;
        this.availableActions = availableActions;
        this.world = world;
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
        return Arrays.copyOf(genotype, genotype.length);
    }

    @Override
    public double getFitness() {
        if (world.getRewardVersion() <= this.lastRewardVersionNumber) {
            return this.lastAssessedFitness;
        }

        Map<GameEvent, Double> rewards = world.getRewards();

        BeerTrackerGame game = new BeerTrackerGame();

        double fitness = 0;
        while (true) {
            GameEvent resultOfMove = doOneMoveInGame(game);
            if (resultOfMove == GameEvent.GAME_OVER)
                break;
            fitness += rewards.get(resultOfMove);
        }

        this.lastRewardVersionNumber = world.getRewardVersion();
        this.lastAssessedFitness = fitness;
        return fitness;
    }

    @Override
    public void develop() {
        this.network = new ContinuousTimeRecurrentNeuralNetwork(topology, genotype);
    }

    public GameEvent doOneMoveInGame(BeerTrackerGame g) {
        boolean[] sensings = g.getShadowSensings();

        for (int i = 0; i < sensings.length; i++) {
            double activation = 0;
            if (sensings[i]) activation = 1;
            network.setInputActivation(i, activation);
        }

        network.propagate();
        int mostActiveNode = network.getMostActiveOutputNode();
        double activation = network.getOutputActivation(mostActiveNode);
        int magnitude;
        double threshold = 0.2;
        if (activation < threshold) {
            magnitude = 0;
        } else {
            double stepSize = (1-threshold) / 4;
            magnitude = 1 + (int) ((activation-threshold)/stepSize);
            if (magnitude > 4){
                magnitude = 4;
            }
        }

        TrackerAction move = availableActions[mostActiveNode];
        return g.performAction(move, magnitude);
    }
}
