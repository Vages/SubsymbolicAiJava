package Project2;

public class OneMaxEvaluator implements IndividualEvaluator {

    public static double evaluate(Individual a) {
        int[] p = a.getPhenotype();
        double errors = 0;

        for (int aP : p) {
            if (aP != 1) {
                errors++;
            }
        }

        return 1.0/(1.0+errors);
    }
}
