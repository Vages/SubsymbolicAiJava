package project2.integerSpecializations;

import java.util.Random;

public class IntegerCrossBreeder {
    Random randomNumberGenerator;
    double crossingRate = 0.2;

    public IntegerCrossBreeder() {
        randomNumberGenerator = new Random();
    }

    public IntegerCrossBreeder(Random randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public IntegerCrossBreeder(double crossingRate) {
        this.crossingRate = crossingRate;
        randomNumberGenerator = new Random();
    }

    public Integer[][] crossBreed(Integer[] genotypeA, Integer[] genotypeB){
        int genotypeLength = genotypeA.length;

        if (Math.random() > crossingRate)
            return new Integer[][] {genotypeA, genotypeB};

        int crossingPoint = (int) (randomNumberGenerator.nextDouble()*genotypeLength);

        Integer[] childAGenotype = new Integer[genotypeLength];
        Integer[] childBGenotype = new Integer[genotypeLength];

        for (int i = 0; i < genotypeLength; i++){
            if (i < crossingPoint){
                childAGenotype[i] = genotypeA[i];
                childBGenotype[i] = genotypeB[i];
            } else {
                childBGenotype[i] = genotypeA[i];
                childAGenotype[i] = genotypeB[i];
            }
        }

        return new Integer[][] {childAGenotype, childBGenotype};
    }
}
