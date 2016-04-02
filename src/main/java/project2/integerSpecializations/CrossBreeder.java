package project2.integerSpecializations;

import java.lang.reflect.Array;
import java.util.Random;

public class CrossBreeder<T> {
    Random randomNumberGenerator;
    double crossingRate;
    Class<T> clazz;

    public CrossBreeder(Class<T> clazz, double crossingRate) {
        this.clazz = clazz;
        this.crossingRate = crossingRate;
        randomNumberGenerator = new Random();
    }

    public CrossBreeder(Class<T> clazz, double crossingRate, Random randomNumberGenerator) {
        this.clazz = clazz;
        this.crossingRate = crossingRate;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public T[][] crossBreed(T[] genotypeA, T[] genotypeB){
        int genotypeLength = genotypeA.length;

        @SuppressWarnings("unchecked")
        T[][] returnArray = (T[][]) Array.newInstance(clazz, 2, genotypeLength);


        if (Math.random() > crossingRate){
            returnArray[0] = genotypeA;
            returnArray[1] = genotypeB;
            return returnArray;
        }

        int crossingPoint = (int) (randomNumberGenerator.nextDouble()*genotypeLength);


        T[] childAGenotype = (T[]) Array.newInstance(clazz, genotypeLength);
        T[] childBGenotype = (T[]) Array.newInstance(clazz, genotypeLength);

        for (int i = 0; i < genotypeLength; i++){
            if (i < crossingPoint){
                childAGenotype[i] = genotypeA[i];
                childBGenotype[i] = genotypeB[i];
            } else {
                childBGenotype[i] = genotypeA[i];
                childAGenotype[i] = genotypeB[i];
            }
        }

        returnArray[0] = childAGenotype;
        returnArray[1] = childBGenotype;

        return returnArray;
    }
}
