package project3;

import org.apache.commons.lang3.ArrayUtils;
import project2.Individual;

public class FlatLandIndividual extends Individual<Double, SigmoidNeuralNetwork> {
    private Double[] genotype;
    private int[] topology;
    private SigmoidNeuralNetwork phenotype;
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
    public SigmoidNeuralNetwork getPhenotype() {
        return phenotype;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public void develop() {
        this.phenotype = new SigmoidNeuralNetwork(topology, ArrayUtils.toPrimitive(genotype));
    }
}
