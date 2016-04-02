package project2.integerSpecializations;


import project2.*;
import project2.integerSpecializations.evaluators.IntegerPhenotypeEvaluator;

import java.util.ArrayList;

public class IntegerEvolutionWorld extends EvolutionWorld {
    private ArrayList<Integer[]> matingGenotypeList;
    private CrossBreeder<Integer> crossBreeder;
    private IntegerPhenotypeEvaluator evaluator;
    private IntegerMutator mutator;
    private int stringLength;
    private int maxInt;

    public IntegerEvolutionWorld(AdultSelection adultSelection, SelectionStrategy matingStrategy,
                                 int childPoolSize, int adultPoolSize, int numberOfGenerations,
                                 int tournamentSize, double tournamentE, String logFileName,
                                 int maxInt, double mutateThreshold, int numberOfMutations, double crossingRate,
                                 int stringLength, IntegerPhenotypeEvaluator evaluator) {
        super(adultSelection, matingStrategy, childPoolSize, adultPoolSize, numberOfGenerations, 1, tournamentSize, tournamentE, logFileName);
        mutator = new IntegerMutator(maxInt, mutateThreshold, numberOfMutations);
        crossBreeder = new CrossBreeder<Integer>(Integer.class, crossingRate);
        this.evaluator = evaluator;
        this.stringLength = stringLength;
        this.maxInt = maxInt;
    }

    @Override
    public void generateRandomChildren() {
        for (int i = 0; i < childPoolSize; i++) {
            Integer[] genotype = new Integer[this.stringLength];

            for (int j = 0; j < this.stringLength; j++){
                genotype[j] = (int) (Math.random()*(maxInt+1));
            }

            this.children.add(new IntegerIndividual(genotype, evaluator));
        }
    }
    @Override
    protected void genotypeCopying() {
        matingGenotypeList = new ArrayList<>();
        for (Individual i: matingIndividualList) {
            matingGenotypeList.add((Integer[]) i.getGenotype());
        }
    }

    @Override
    protected void reproduction() {
        children.clear();
        for (int i = 0; i < matingGenotypeList.size(); i = i + 2){
            Integer[][] childPair = crossBreeder.crossBreed(matingGenotypeList.get(i), matingGenotypeList.get(i+1));
            children.add(new IntegerIndividual(mutator.mutate(childPair[0]), evaluator));
            children.add(new IntegerIndividual(mutator.mutate(childPair[1]), evaluator));
        }

    }
}
