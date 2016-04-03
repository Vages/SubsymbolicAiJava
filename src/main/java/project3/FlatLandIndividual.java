package project3;

import org.apache.commons.lang3.ArrayUtils;
import project2.Individual;

import java.util.ArrayList;

public class FlatLandIndividual extends Individual<Double> {
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
    public double getFitness() {
        return fitness;
    }

    @Override
    public void develop() {
        this.phenotype = new SigmoidNeuralNetwork(topology, ArrayUtils.toPrimitive(genotype));
    }

    public static Double[] generateRandomGenotype(int[] topology){
        ArrayList<Double> weights = new ArrayList<>();

        for (int i = 0; i < topology.length-1; i++) {
            int nodes_in_this_layer = topology[i]+1;
            int nodes_in_next_layer = topology[i+1]+1;
            if (i == topology.length -2) {
                nodes_in_next_layer--;
            }

            int weightsToBeGenerated = nodes_in_this_layer*nodes_in_next_layer;

            for (int j = 0; j < weightsToBeGenerated; j++) {
                weights.add(Math.random()*2-1); // Random number in range -1 to 1
            }
        }

        return weights.stream().toArray(Double[]::new);
    }
}
