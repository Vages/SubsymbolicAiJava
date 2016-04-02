package project3;

import project2.Individual;

public class SimpleFlatLandIndividual extends Individual<Integer, FlatlandNeuralNetwork> {
    @Override
    public Integer[] getGenotype() {
        return new Integer[0];
    }

    @Override
    public FlatlandNeuralNetwork[] getPhenotype() {
        return new FlatlandNeuralNetwork[0];
    }

    @Override
    public double getFitness() {
        return 0;
    }

    @Override
    public void develop() {

    }
}
