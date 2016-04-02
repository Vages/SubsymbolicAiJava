package project2.integerSpecializations;

import project2.Individual;
import project2.integerSpecializations.evaluators.IntegerPhenotypeEvaluator;

import java.util.Arrays;

/**
 *
 */
public class IntegerIndividual extends Individual<Integer, Integer[]> {
    private Double fitness;
    private IntegerPhenotypeEvaluator evaluator;


    public IntegerIndividual(Integer[] genotype, IntegerPhenotypeEvaluator evaluator) {
        this.genotype = genotype;
        this.evaluator = evaluator;
    }

    @Override
    public Integer[] getPhenotype() {
        return this.getGenotype();
    }

    @Override
    public void develop() {

    }

    public Integer[] getGenotype() {
        Integer [] a = new Integer[genotype.length];
        System.arraycopy(genotype, 0, a, 0, genotype.length);
        return a;
    }

    public double getFitness() {
        if (this.fitness == null) {
            this.fitness = this.evaluator.evaluate(this.getPhenotype());
        }

        return this.fitness;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.genotype);
    }

}
