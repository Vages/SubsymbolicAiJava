package project2.integerSpecializations;


import project2.EvolutionWorld;
import project2.Individual;
import project2.SelectionStrategy;
import project2.integerSpecializations.evaluators.OneMaxEvaluator;
import project2.integerSpecializations.evaluators.PhenotypeEvaluator;

import java.util.ArrayList;

public class OneMaxEvolutionWorld extends EvolutionWorld {
    private ArrayList<int[]> matingGenotypeList;
    private IntegerVectorCrossBreeder crossBreeder;
    private PhenotypeEvaluator evaluator;
    private IntegerMutator mutator;

    public OneMaxEvolutionWorld(SelectionStrategy ms, int initialChildren, int numberOfGenerations, int stringLength, int[] idealPhenotype) {
        super(ms, initialChildren, numberOfGenerations);
        evaluator = new OneMaxEvaluator(idealPhenotype);
        mutator = new IntegerMutator(1, 0.1, 1);
        crossBreeder = new IntegerVectorCrossBreeder(0.2);

        for (int i = 0; i < initialChildren; i++) {
            int[] genotype = new int[stringLength];

            for (int j = 0; j < stringLength; j++){
                genotype[j] = (int) (Math.random()*2);
            }

            this.children.add(new IntegerIndividual(genotype, evaluator));
        }
    }

    public static void main(String[] args) {
        int stringlength = 40;
        int individuals = 70;
        int generations = 100;
        int[] idealPhenotype = new int[stringlength];

        for (int i = 0; i < stringlength; i++){
            idealPhenotype[i] = 1;
        }

        OneMaxEvolutionWorld omew = new OneMaxEvolutionWorld(SelectionStrategy.FITNESS_PROPORTIONATE, individuals, generations, stringlength, idealPhenotype);
        omew.runAllGenerations();
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
