package Project2.evaluators;

public class OneMaxEvaluator implements PhenotypeEvaluator {
    private int[] idealPhenotype;

    public OneMaxEvaluator(int[] idealPhenotype) {
        this.idealPhenotype = new int[idealPhenotype.length];
        System.arraycopy(idealPhenotype, 0, this.idealPhenotype, 0, idealPhenotype.length);
    }

    public double evaluate(int[] phenotype) {
        double errors = 0;

        for (int i = 0; i<phenotype.length; i++){
            if (phenotype[i] != this.idealPhenotype[i]) {
                errors++;
            }
        }

        return 1.0/(1.0+errors);
    }
}
