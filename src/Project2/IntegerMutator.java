package project2;

import java.util.Random;

public class IntegerMutator {
    private double mutateThreshold;
    private int numberOfMutations, maxValue;
    private static Random random = new Random();

    public IntegerMutator(int maxValue, double mutateThreshold, int numberOfMutations) {
        this.maxValue = maxValue;
        this.mutateThreshold = mutateThreshold;
        this.numberOfMutations = numberOfMutations;
    }

    public int[] mutate(int[] genome){

        if (Math.random() > mutateThreshold)
            return genome;

        int[] copiedGenome = new int[genome.length];
        System.arraycopy(genome, 0, copiedGenome, 0, genome.length);

        for (int i = 0; i < numberOfMutations; i++){
            int position = (int) (Math.random()*genome.length);
            int ith_digit = genome[position];
            if (this.maxValue == 1){
                if (ith_digit == 0){
                    copiedGenome[position] = 1;
                } else {
                    copiedGenome[position] = 0;
                }
            }
            int new_digit = random.nextInt(maxValue+1);
            while (new_digit == ith_digit) new_digit = random.nextInt(maxValue+1);
            copiedGenome[position] = new_digit;
        }

        return copiedGenome;
    }
}
