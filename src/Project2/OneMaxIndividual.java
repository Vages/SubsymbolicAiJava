package Project2;

import java.util.Arrays;

public class OneMaxIndividual extends Individual{
    private final int[] genotype;
    private Double fitness;
    private PhenotypeEvaluator evaluator;


    public OneMaxIndividual(int[] genotype, PhenotypeEvaluator evaluator) {
        this.genotype = genotype;
        this.evaluator = evaluator;
    }

    @Override
    public int[] getPhenotype() {
        return this.getGenotype();
    }

    @Override
    public void develop() {

    }

    public int[] getGenotype() {
        int [] a = new int[genotype.length];
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
