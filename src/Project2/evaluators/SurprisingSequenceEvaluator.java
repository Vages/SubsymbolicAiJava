package Project2.evaluators;

import Project2.Tuple;

import java.util.HashSet;

public class SurprisingSequenceEvaluator implements PhenotypeEvaluator {
    private boolean globallySurprising;

    public SurprisingSequenceEvaluator(boolean globallySurprising) {
        this.globallySurprising = globallySurprising;
    }

    @Override
    public double evaluate(int[] phenotype) {
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
