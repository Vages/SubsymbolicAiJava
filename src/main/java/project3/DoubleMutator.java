package project3;

import java.util.Arrays;

public class DoubleMutator {
    private double mutateThreshold;
    private int numberOfMutations;
    private double minValue, maxValue;

    public DoubleMutator(double mutateThreshold, int numberOfMutations, double minValue, double maxValue) {
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.mutateThreshold = mutateThreshold;
        this.numberOfMutations = numberOfMutations;
    }

    public Double[] mutate(Double[] genome) {

        if (Math.random() > mutateThreshold) {
            return genome;
        }

        Double[] copiedGenome = Arrays.copyOf(genome, genome.length);

        for (int i = 0; i < numberOfMutations; i++) {
            int position = (int) (Math.random()*genome.length);
            copiedGenome[position] = randomInRange(minValue, maxValue);
        }

        return copiedGenome;
    }

    public double randomInRange(double min, double max) {
        return (max-min)*Math.random()+min;
    }
}
