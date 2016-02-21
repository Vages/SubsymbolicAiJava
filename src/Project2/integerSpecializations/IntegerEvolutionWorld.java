package project2.integerSpecializations;


import project2.AdultSelection;
import project2.EvolutionWorld;
import project2.Individual;
import project2.SelectionStrategy;
import project2.integerSpecializations.evaluators.OneMaxEvaluator;
import project2.integerSpecializations.evaluators.PhenotypeEvaluator;

import java.util.ArrayList;

public class IntegerEvolutionWorld extends EvolutionWorld {
    private ArrayList<int[]> matingGenotypeList;
    private IntegerVectorCrossBreeder crossBreeder;
    private PhenotypeEvaluator evaluator;
    private IntegerMutator mutator;

    public IntegerEvolutionWorld(SelectionStrategy matingStrategy, int childPoolSize, int numberOfGenerations, int stringLength, int[] idealPhenotype) {
        super(AdultSelection.FULL_GENERATIONAL_REPLACEMENT, matingStrategy, childPoolSize, 70, numberOfGenerations, 5, 0.1, "test");
        evaluator = new OneMaxEvaluator(idealPhenotype);
        mutator = new IntegerMutator(1, 0.1, 1);
        crossBreeder = new IntegerVectorCrossBreeder(0.2);

        for (int i = 0; i < childPoolSize; i++) {
            int[] genotype = new int[stringLength];

            for (int j = 0; j < stringLength; j++){
                genotype[j] = (int) (Math.random()*2);
            }

            this.children.add(new IntegerIndividual(genotype, evaluator));
        }
    }

    @Override
    protected void genotypeCopying() {
        matingGenotypeList = new ArrayList<>();
        for (Individual i: matingIndividualList) {
            matingGenotypeList.add(i.getGenotype());
        }
    }

    @Override
    protected void reproduction() {
        children.clear();
        for (int i = 0; i < matingGenotypeList.size(); i = i + 2){
            int[][] childPair = crossBreeder.crossBreed(matingGenotypeList.get(i), matingGenotypeList.get(i+1));
            children.add(new IntegerIndividual(mutator.mutate(childPair[0]), evaluator));
            children.add(new IntegerIndividual(mutator.mutate(childPair[1]), evaluator));
        }

    }
}
