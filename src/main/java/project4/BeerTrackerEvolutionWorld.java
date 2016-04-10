package project4;

import project2.AdultSelection;
import project2.EvolutionWorld;
import project2.MatingSelection;
import project3.FlatlandIndividual;

public class BeerTrackerEvolutionWorld extends EvolutionWorld<NeuralNetworkGene> {
    private final NeuralNetworkGeneMutator mutator;
    private final NeuralNetworkGeneCrossBreeder crossBreeder;
    private final int[] topology;

    public BeerTrackerEvolutionWorld(AdultSelection adultSelection, MatingSelection matingSelection, int childPoolSize,
                                     int adultPoolSize, int numberOfGenerations, String logFileName,
                                     double crossingRate, double mutateThreshold, int numberOfMutations) {
        super(adultSelection, matingSelection, childPoolSize, adultPoolSize, numberOfGenerations, logFileName);
        this.crossBreeder = new NeuralNetworkGeneCrossBreeder(crossingRate);
        this.mutator = new NeuralNetworkGeneMutator(mutateThreshold, numberOfMutations);
        topology = new int[]{5, 2, 2};
    }

    @Override
    protected void generateRandomChildren() {

        for (int i = 0; i < childPoolSize; i++) {
            NeuralNetworkGene[] genotype = BeerTrackerIndividual.generateRandomGenotype(topology);
            children.add(new BeerTrackerIndividual(genotype, topology));
        }
    }

    @Override
    protected void reproduction() {
        children.clear();

        for (int i = 0; i < matingGenotypeList.size(); i = i + 2) {
            NeuralNetworkGene[][] childPair = crossBreeder.crossBreed(matingGenotypeList.get(i), matingGenotypeList.get(i+1));
            children.add(new BeerTrackerIndividual(mutator.mutate(childPair[0]), topology));
            children.add(new BeerTrackerIndividual(mutator.mutate(childPair[1]), topology));
        }

    }
}
