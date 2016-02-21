package project2.integerSpecializations;

import java.util.Random;

public class IntegerVectorCrossBreeder {
    Random randomNumberGenerator;
    double crossingRate = 0.2;

    public IntegerVectorCrossBreeder() {
        randomNumberGenerator = new Random();
    }

    public IntegerVectorCrossBreeder(Random randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public IntegerVectorCrossBreeder(double crossingRate) {
        this.crossingRate = crossingRate;
        randomNumberGenerator = new Random();
    }

    public int[][] crossBreed(int[] genotypeA, int[] genotypeB){
        int genotypeLength = genotypeA.length;

        if (Math.random() > crossingRate)
            return new int[][] {genotypeA, genotypeB};

        int crossingPoint = (int) (randomNumberGenerator.nextDouble()*genotypeLength);

        int[] childAGenotype = new int[genotypeLength];
        int[] childBGenotype = new int[genotypeLength];

        for (int i = 0; i < genotypeLength; i++){
            if (i < crossingPoint){
                childAGenotype[i] = genotypeA[i];
                childBGenotype[i] = genotypeB[i];
            } else {
                childBGenotype[i] = genotypeA[i];
                childAGenotype[i] = genotypeB[i];
            }
        }

        return new int[][] {childAGenotype, childBGenotype};
    }
}
