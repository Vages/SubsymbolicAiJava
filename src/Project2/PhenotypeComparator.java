package Project2;

import java.util.Comparator;

public class PhenotypeComparator implements Comparator<Individual> {
    private PhenotypeEvaluator evaluator;

    public PhenotypeComparator(PhenotypeEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public int compare(Individual o1, Individual o2) {
        return (int) Math.signum(evaluator.evaluate(o1.getPhenotype())-evaluator.evaluate(o2.getPhenotype()));
    }
}
