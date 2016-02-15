package Project2;

import java.util.Comparator;

public class OneMaxComparator implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        return (int) Math.signum(OneMaxEvaluator.evaluate(o1)-OneMaxEvaluator.evaluate(o2));
    }
}
