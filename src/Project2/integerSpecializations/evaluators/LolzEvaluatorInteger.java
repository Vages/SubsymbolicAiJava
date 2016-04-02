package project2.integerSpecializations.evaluators;

public class LolzEvaluatorInteger implements IntegerPhenotypeEvaluator {
    private int zCap;

    public LolzEvaluatorInteger(int zCap) {
        this.zCap = zCap;
    }

    public double evaluate(Integer[] phenotype) {
        int firstDigit = phenotype[0];
        int stopIndex;

        if (firstDigit == 1) {
            stopIndex = phenotype.length;
        } else {
            stopIndex = (zCap < phenotype.length) ? zCap : phenotype.length;
        }

        int i;
        for (i = 1; i < stopIndex; i++) {
            if (phenotype[i] != firstDigit) {
                break;
            }
        }

        return i;
    }
}
