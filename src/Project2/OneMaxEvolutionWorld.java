package Project2;


import java.util.ArrayList;

public class OneMaxEvolutionWorld extends EvolutionWorld {
    private ArrayList<int[]> matingGenotypeList;
    private IntVectorCrossBreeder crossBreeder = new IntVectorCrossBreeder();
    private PhenotypeEvaluator evaluator;
    private IntegerMutator mutator;

    public OneMaxEvolutionWorld(SelectionStrategy ms, int initialChildren, int numberOfGenerations, int stringLength, int[] idealPhenotype) {
        super(ms, initialChildren, numberOfGenerations);
        evaluator = new OneMaxEvaluator(idealPhenotype);
        mutator = new IntegerMutator(1, 0.3, 1);

        for (int i = 0; i < initialChildren; i++) {
            int[] genotype = new int[stringLength];

            for (int j = 0; j < stringLength; j++){
                genotype[j] = (int) (Math.random()*2);
            }

            this.children.add(new OneMaxIndividual(genotype, evaluator));
        }
    }

    public static void main(String[] args) {
        int stringlength = 40;
        int individuals = 100;
        int generations = 100;
        int[] idealPhenotype = new int[stringlength];

        for (int i = 0; i < stringlength; i++){
            idealPhenotype[i] = 1;
        }

        OneMaxEvolutionWorld omew = new OneMaxEvolutionWorld(SelectionStrategy.FITNESS_PROPORTIONATE, individuals, generations, stringlength, idealPhenotype);
        omew.runAllGenerations();

        System.out.println("Hello!");
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
            children.add(new OneMaxIndividual(mutator.mutate(childPair[0]), evaluator));
            children.add(new OneMaxIndividual(mutator.mutate(childPair[1]), evaluator));
        }

    }
}
