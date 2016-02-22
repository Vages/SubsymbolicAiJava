package project2.integerSpecializations;


import project2.AdultSelection;
import project2.EvolutionWorld;
import project2.Individual;
import project2.SelectionStrategy;
import project2.integerSpecializations.evaluators.PhenotypeEvaluator;

import java.util.ArrayList;

public class IntegerEvolutionWorld extends EvolutionWorld {
    private ArrayList<int[]> matingGenotypeList;
    private IntegerVectorCrossBreeder crossBreeder;
    private PhenotypeEvaluator evaluator;
    private IntegerMutator mutator;
    private int stringLength;

    public IntegerEvolutionWorld(AdultSelection adultSelection, SelectionStrategy matingStrategy,
                                 int childPoolSize, int adultPoolSize, int numberOfGenerations,
                                 int tournamentSize, double tournamentE, String logFileName,
                                 int maxInt, double mutateThreshold, int numberOfMutations, double crossingRate,
                                 int stringLength, PhenotypeEvaluator evaluator) {
        super(adultSelection, matingStrategy, childPoolSize, adultPoolSize, numberOfGenerations, tournamentSize, tournamentE, logFileName);
        mutator = new IntegerMutator(maxInt, mutateThreshold, numberOfMutations);
        crossBreeder = new IntegerVectorCrossBreeder(crossingRate);
        this.evaluator = evaluator;
        this.stringLength = stringLength;
    }

    @Override
    public void generateRandomChildren() {
        for (int i = 0; i < childPoolSize; i++) {
            int[] genotype = new int[this.stringLength];

            for (int j = 0; j < this.stringLength; j++){
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
