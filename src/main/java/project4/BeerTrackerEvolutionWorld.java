package project4;

import project2.AdultSelection;
import project2.EvolutionWorld;
import project2.MatingSelection;

import java.util.HashMap;
import java.util.Map;

public class BeerTrackerEvolutionWorld extends EvolutionWorld<NeuralNetworkGene> {
    private final NeuralNetworkGeneMutator mutator;
    private final NeuralNetworkGeneCrossBreeder crossBreeder;
    private final int[] topology;
    private int rewardVersion = 0;
    private Map<GameEvent, Double> rewards;
    private boolean noWrap;

    public BeerTrackerEvolutionWorld(AdultSelection adultSelection, MatingSelection matingSelection, int childPoolSize,
                                     int adultPoolSize, int numberOfGenerations, String logFileName,
                                     double crossingRate, double mutateThreshold, int numberOfMutations, boolean noWrap) {
        super(adultSelection, matingSelection, childPoolSize, adultPoolSize, numberOfGenerations, logFileName);
        this.noWrap = noWrap;
        this.crossBreeder = new NeuralNetworkGeneCrossBreeder(crossingRate);
        this.mutator = new NeuralNetworkGeneMutator(mutateThreshold, numberOfMutations);

        int inputNodes = 5;
        if (noWrap) {
            inputNodes += 2; // Add two nodes for edge sensings
        }

        topology = new int[]{inputNodes, 30, 2};

        rewards = new HashMap<>();
        rewards.put(GameEvent.CAPTURED_SMALL, 50.0);
        rewards.put(GameEvent.PARTIALLY_CAPTURED_SMALL, 0.0);
        rewards.put(GameEvent.AVOIDED_SMALL, 0.0);
        rewards.put(GameEvent.CAPTURED_BIG, -100.0);
        rewards.put(GameEvent.PARTIALLY_CAPTURED_BIG, -100.0);
        rewards.put(GameEvent.AVOIDED_BIG, 0.0);
        rewards.put(GameEvent.NOTHING, 0.0);
        rewards.put(GameEvent.GAME_OVER, 0.0);
    }


    @Override
    protected void generateRandomChildren() {

        for (int i = 0; i < childPoolSize; i++) {
            NeuralNetworkGene[] genotype = BeerTrackerIndividual.generateRandomGenotype(topology);
            children.add(new BeerTrackerIndividual(genotype, topology, new TrackerAction[]{TrackerAction.MOVE_LEFT, TrackerAction.MOVE_RIGHT}, this, noWrap));
        }
    }

    @Override
    protected void reproduction() {
        children.clear();

        for (int i = 0; i < matingGenotypeList.size(); i = i + 2) {
            NeuralNetworkGene[][] childPair = crossBreeder.crossBreed(matingGenotypeList.get(i), matingGenotypeList.get(i+1));
            children.add(new BeerTrackerIndividual(mutator.mutate(childPair[0]), topology, new TrackerAction[]{TrackerAction.MOVE_LEFT, TrackerAction.MOVE_RIGHT}, this, noWrap));
            children.add(new BeerTrackerIndividual(mutator.mutate(childPair[1]), topology, new TrackerAction[]{TrackerAction.MOVE_LEFT, TrackerAction.MOVE_RIGHT}, this, noWrap));
        }

        rewardVersion++; // todo: make this a separate variable
    }

    public int getRewardVersion() {
        return rewardVersion;
    }

    public Map<GameEvent, Double> getRewards() {
        return rewards;
    }

    public static void main(String[] args) {
        BeerTrackerEvolutionWorld world = new BeerTrackerEvolutionWorld(
                AdultSelection.GENERATIONAL_MIXING,
                MatingSelection.SIGMA_SCALING,
                40,
                40,
                40,
                "project4/log",
                0.8,
                1,
                3, false);

        world.runAllEpochs();
    }

    public BeerTrackerIndividual getBestIndividual() {
        return (BeerTrackerIndividual) adults.get(adults.size() - 1);
    }
}
