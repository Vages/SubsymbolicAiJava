package project2.integerSpecializations;


import project2.AdultSelection;
import project2.EvolutionWorld;
import project2.Individual;
import project2.SelectionStrategy;
import project2.integerSpecializations.evaluators.PhenotypeEvaluator;

import java.util.ArrayList;

public class IntegerEvolutionWorld extends EvolutionWorld {
    private ArrayList<Integer[]> matingGenotypeList;
    private IntegerCrossBreeder crossBreeder;
    private PhenotypeEvaluator evaluator;
    private IntegerMutator mutator;
    private int stringLength;
    private int maxInt;

    public IntegerEvolutionWorld(AdultSelection adultSelection, SelectionStrategy matingStrategy,
                                 int childPoolSize, int adultPoolSize, int numberOfGenerations,
                                 int tournamentSize, double tournamentE, String logFileName,
                                 int maxInt, double mutateThreshold, int numberOfMutations, double crossingRate,
                                 int stringLength, PhenotypeEvaluator evaluator) {
        super(adultSelection, matingStrategy, childPoolSize, adultPoolSize, numberOfGenerations, 1, tournamentSize, tournamentE, logFileName);
        mutator = new IntegerMutator(maxInt, mutateThreshold, numberOfMutations);
        crossBreeder = new IntegerCrossBreeder(crossingRate);
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
