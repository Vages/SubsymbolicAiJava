package Project2;


import java.util.ArrayList;

public class OneMaxEvolutionWorld extends EvolutionWorld {

    private RandomCollection<Individual> parentRouletteWheel;
    private ArrayList<int[]> matingGenotypeList;
    private int numberOfNewChildren;
    private ArrayList<Individual> matingIndividualList;
    private IntVectorCrossBreeder crossBreeder = new IntVectorCrossBreeder();
    private PhenotypeEvaluator evaluator;
    private IntegerMutator mutator;

    public OneMaxEvolutionWorld(int stringLength, int initialChildren, int[] idealPhenotype) {
        super();
        numberOfNewChildren = initialChildren;

        evaluator = new OneMaxEvaluator(idealPhenotype);
        mutator = new IntegerMutator(1, 0.3, 1);

        // Adding all children
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

        OneMaxEvolutionWorld omew = new OneMaxEvolutionWorld(stringlength, individuals, idealPhenotype);

        for (int i = 0; i<generations; i++){
            omew.oneRoundOfEvolution();
        }

        System.out.println("Hello!");
    }

    @Override
    protected void parentSelection() {
        // Roulette wheel implementation
        parentRouletteWheel = new RandomCollection<>();
        for (Individual a: adults) {
            parentRouletteWheel.add(a.getFitness(), a);
        }

        matingIndividualList = new ArrayList<>();
        for (int i = 0; i < numberOfNewChildren; i++) {
            Individual next = parentRouletteWheel.next();
            matingIndividualList.add(next);
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
        children = new ArrayList<>();
        adults = new ArrayList<>();
        for (int i = 0; i < numberOfNewChildren; i = i + 2){
            int[][] childPair = crossBreeder.crossBreed(matingGenotypeList.get(i), matingGenotypeList.get(i+1));
            children.add(new OneMaxIndividual(mutator.mutate(childPair[0]), evaluator));
            children.add(new OneMaxIndividual(mutator.mutate(childPair[1]), evaluator));
        }

    }
}
