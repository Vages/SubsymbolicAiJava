package Project2;

import java.util.Random;

public class IntVectorCrossBreeder {
    Random randomNumberGenerator;

    public IntVectorCrossBreeder() {
        randomNumberGenerator = new Random();
    }

    public IntVectorCrossBreeder(Random randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public int[][] crossBreed(Individual a, Individual b){
        int[] genotypeA = a.getGenotype();
        int[] genotypeB = b.getGenotype();

        int genotypeLength = genotypeA.length;

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
