package project2.integerSpecializations.evaluators;

import project2.helperStructures.Tuple;

import java.util.HashSet;

public class SurprisingSequenceEvaluator implements IntegerPhenotypeEvaluator {
    private boolean globallySurprising;

    public SurprisingSequenceEvaluator(boolean globallySurprising) {
        this.globallySurprising = globallySurprising;
    }

    @Override
    public double evaluate(Integer[] phenotype) {
        double errors = 0;

        int maxDist = (globallySurprising) ? phenotype.length-1 : 1;

        HashSet<Tuple> alreadySeen = new HashSet<>();

        for (int i = 1; i <= maxDist; i++) {
            alreadySeen.clear();

            for (int j = 0; j < phenotype.length-i; j++) {
                int a = phenotype[j];
                int b = phenotype[j+i];
                Tuple sequence = new Tuple(a, b);
                if (alreadySeen.contains(sequence)) {
                    errors++;
                }
                alreadySeen.add(sequence);
            }
        }

        return 1.0/(1.0+errors);
    }
}
