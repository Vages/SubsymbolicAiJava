package Project2;

public class OneMaxEvaluator implements IndividualEvaluator {

    @Override
    public double evaluate(Individual a) {
        String p = a.getPhenotype();
        double errors = 0;

        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) != '1') {
                errors++;
            }
        }

        return 1/(1+errors);
    }
}
