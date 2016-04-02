package project3;

import org.apache.commons.lang3.ArrayUtils;
import project2.Individual;

public class FlatLandIndividual extends Individual<Double, FlatlandNeuralNetwork> {
    private Double[] genotype;
    private int[] topology;
    private FlatlandNeuralNetwork phenotype;
    private double fitness;

    public FlatLandIndividual(Double[] genotype, int[] topology) {
        this.genotype = genotype;
        this.topology = topology;
    }

    @Override
    public Double[] getGenotype() {
        return genotype;
    }

    @Override
    public FlatlandNeuralNetwork getPhenotype() {
        return phenotype;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public void develop() {
        this.phenotype = new FlatlandNeuralNetwork(topology, ArrayUtils.toPrimitive(genotype));
    }
}
