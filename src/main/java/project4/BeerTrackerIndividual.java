package project4;

import project2.Individual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BeerTrackerIndividual extends Individual<NeuralNetworkGene> {
    private final TrackerAction[] availableActions;
    private final boolean noWrap;
    private NeuralNetworkGene[] genotype;
    private int[] topology;
    private ContinuousTimeRecurrentNeuralNetwork network;
    private int lastRewardVersionNumber = -1;
    private double lastAssessedFitness = 0;
    private BeerTrackerEvolutionWorld world;
    private boolean pullAllowed;

    public BeerTrackerIndividual(NeuralNetworkGene[] genotype, int[] topology, TrackerAction[] availableActions, BeerTrackerEvolutionWorld world, boolean noWrap, boolean pullAllowed) {
        this.genotype = genotype;
        this.topology = topology;
        this.availableActions = availableActions;
        this.world = world;
        this.noWrap = noWrap;
        this.pullAllowed = pullAllowed;
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
        return lastAssessedFitness;
    }

    @Override
    public void develop() {
        this.network = new ContinuousTimeRecurrentNeuralNetwork(topology, genotype);
    }

    @Override
    public void assessFitness() {
        if (this.lastRewardVersionNumber == world.getRewardVersion()) {
            return;
        }
        double fitness = 0;
        BeerTrackerGame game = new BeerTrackerGame(noWrap);

        Map<GameEvent, Double> rewards = world.getRewards();

        int numberOfSuccesses = 0;
        int numberOfErrors = 0;
        int numberOfTimesWithMove = 0;

        int zoneBuffer = 3;
        int timesInLeftZone = 0, timesInRightZone = 0;

        int numberOfTimesPullUsed = 0, numberOfSuccessfulPulls = 0;
        boolean waitingForPull = false;

        boolean lastVisitedRight = false;

        List<GameEvent> errors = Arrays.asList(GameEvent.PARTIALLY_CAPTURED_BIG, GameEvent.PARTIALLY_CAPTURED_SMALL, GameEvent.CAPTURED_BIG, GameEvent.AVOIDED_SMALL);
        List<GameEvent> successes = Arrays.asList(GameEvent.AVOIDED_BIG, GameEvent.CAPTURED_SMALL);

        while (true) {
            int positionBeforemove = game.getTrackerPosition();
            GameEvent resultOfMove = doOneMoveInGame(game);
            int positionAfterMove = game.getTrackerPosition();

            if (resultOfMove == GameEvent.GAME_OVER)
                break;

            if (waitingForPull) {
                waitingForPull = false;
                if (successes.contains(resultOfMove)) {
                    numberOfSuccessfulPulls++;
                }
            }

            if (resultOfMove == GameEvent.WAITING_FOR_PULL) {
                waitingForPull = true;
                numberOfTimesPullUsed++;
            }

            if (Math.abs(positionAfterMove -positionBeforemove) != 0)
                numberOfTimesWithMove++;
            if ((positionAfterMove <= zoneBuffer) && (positionBeforemove > zoneBuffer) && lastVisitedRight){
                timesInLeftZone++;
                lastVisitedRight = false;
            }
            if ((positionAfterMove >= 25-zoneBuffer) && (positionBeforemove < 25-zoneBuffer) && !lastVisitedRight){
                timesInRightZone++;
                lastVisitedRight = true;
            }
            if (successes.contains(resultOfMove)) {
                numberOfSuccesses++;
            } else if (errors.contains(resultOfMove)) {
                numberOfErrors++;
            }
        }

        fitness = 5*(double) numberOfSuccesses/(numberOfErrors+numberOfSuccesses);
        if (noWrap) {
            /*
            int numberOfMovesThreshold = 100;
            if (numberOfTimesWithMove > numberOfMovesThreshold)
                numberOfTimesWithMove = numberOfMovesThreshold;
            fitness += (double) numberOfTimesWithMove/600;
            */
            int timesCrossed = (timesInLeftZone < timesInRightZone) ? timesInLeftZone : timesInRightZone;
            if (timesCrossed > 10) timesCrossed = 10;
            fitness += (double) timesCrossed/10;
        }

        if (pullAllowed) {
            if (numberOfTimesPullUsed != 0) {
                fitness += 6*(double) numberOfSuccessfulPulls/numberOfTimesPullUsed;
            }
        }

        fitness *= 100;

        this.lastRewardVersionNumber = world.getRewardVersion();
        this.lastAssessedFitness = fitness;
    }

    public GameEvent doOneMoveInGame(BeerTrackerGame g) {
        boolean[] shadowSensings = g.getShadowSensings();

        for (int i = 0; i < shadowSensings.length; i++) {
            double activation = 0;
            if (shadowSensings[i]) activation = 1;
            network.setInputActivation(i, activation);
        }

        if (noWrap) {
            /*
            boolean[] edgeSensings = g.getEdgeTouchSensings();

            for (int i = 0; i < edgeSensings.length; i++) {
                double activation = 0;
                if (edgeSensings[i]) activation = 1;
                network.setInputActivation(shadowSensings.length + i, activation);
            }
            */

            double[] edgeProximities = g.getEdgeProximities();

            for (int i = 0; i < edgeProximities.length; i++) {
                network.setInputActivation(shadowSensings.length + i, edgeProximities[i]);
            }

            //network.setInputActivation(shadowSensings.length+edgeProximities.length, g.getOscillatingForce());


        }

        network.propagate();
        int mostActiveNode = network.getMostActiveOutputNode();
        double activation = network.getOutputActivation(mostActiveNode);
        int magnitude;
        double threshold = 0.4;
        if (activation < threshold) {
            magnitude = 0;
        } else {
            double stepSize = (1 - threshold) / 4;
            magnitude = 1 + (int) ((activation - threshold) / stepSize);
            if (magnitude > 4) {
                magnitude = 4;
            }
        }

        TrackerAction move = availableActions[mostActiveNode];
        return g.performAction(move, magnitude);
    }
}
