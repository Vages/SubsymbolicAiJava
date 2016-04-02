package project2.integerSpecializations.evaluators;

public class OneMaxEvaluatorInteger implements IntegerPhenotypeEvaluator {
    private int[] idealPhenotype;

    public OneMaxEvaluatorInteger(int[] idealPhenotype) {
        this.idealPhenotype = new int[idealPhenotype.length];
        System.arraycopy(idealPhenotype, 0, this.idealPhenotype, 0, idealPhenotype.length);
    }

    public double evaluate(Integer[] phenotype) {
        double errors = 0;

        for (int i = 0; i<phenotype.length; i++){
            if (phenotype[i] != this.idealPhenotype[i]) {
                errors++;
            }
        }

        return 1.0/(1.0+errors);
    }
}
