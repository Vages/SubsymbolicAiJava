package project2.evaluators;

public class LolzEvaluator implements PhenotypeEvaluator {
    private int zCap;

    public LolzEvaluator(int zCap) {
        this.zCap = zCap;
    }

    public double evaluate(int[] phenotype) {
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
