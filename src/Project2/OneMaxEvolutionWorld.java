package Project2;


import java.util.ArrayList;
import java.util.TreeMap;

public class OneMaxEvolutionWorld extends EvolutionWorld {

    private RandomCollection<Individual> parentRouletteWheel;
    private RandomCollection<int[]> matingRouletteWheel;
    private int numberOfMates;
    private ArrayList<Individual> matingList;
    private IntVectorCrossBreeder crossBreeder = new IntVectorCrossBreeder();
    private PhenotypeEvaluator evaluator;

    public OneMaxEvolutionWorld(int stringLength, int initialChildren, int[] idealPhenotype) {
        undevelopedChildren = new ArrayList<>();
        developedChildren = new ArrayList<>();

        evaluator = new OneMaxEvaluator(idealPhenotype);

        fitnessTestedChildren = new TreeMap<>(new PhenotypeComparator(evaluator));
        fitnessTestedAdults = new TreeMap<>(new PhenotypeComparator(evaluator));
        parentRouletteWheel = new RandomCollection<>();
        matingRouletteWheel = new RandomCollection<>();
        matingList = new ArrayList<>();
        numberOfMates = initialChildren/2;

        // Adding all children
        for (int i = 0; i < initialChildren; i++) {
            int[] genotype = new int[stringLength];

            for (int j = 0; j < stringLength; j++){
                genotype[j] = (int) (Math.random()*2);
            }

            this.undevelopedChildren.add(new OneMaxIndividual(genotype));
        }
    }

    public static void main(String[] args) {
        int stringlength = 20;
        int generations = 100;
        int[] idealPhenotype = new int[stringlength];

        for (int i = 0; i < stringlength; i++){
            idealPhenotype[i] = 1;
        }

        OneMaxEvolutionWorld omew = new OneMaxEvolutionWorld(stringlength, generations, idealPhenotype);

        for (int i = 0; i<1000; i++){
            omew.oneRoundOfEvolution();
        }

        System.out.println("Hello!");
    }

    @Override
    protected void testFitness() {
        for (Individual c: developedChildren) {
            fitnessTestedChildren.put(c, evaluator.evaluate(c.getPhenotype()));
        }
    }

    @Override
    protected void selectAdults() {
        for (Individual c: fitnessTestedChildren.keySet()){
            fitnessTestedAdults.put(c, evaluator.evaluate(c.getPhenotype()));
        }

    }

    @Override
    protected void ageBasedFiltering() {

    }

    @Override
    protected void parentSelection() {
        parentRouletteWheel = new RandomCollection<>();
        for (Individual a: fitnessTestedAdults.keySet()) {
            parentRouletteWheel.add(fitnessTestedAdults.get(a), a);
        }

        matingList = new ArrayList<>();
        for (int i = 0; i < numberOfMates; i++) {
            Individual next = parentRouletteWheel.next();
            matingList.add(next);
        }
    }

    @Override
    protected void genotypeCopying() {
        matingRouletteWheel = new RandomCollection<>();
        for (Individual i: matingList) {
            matingRouletteWheel.add(fitnessTestedAdults.get(i), i.getGenotype());
        }
    }

    @Override
    protected void reproduction() {
        int number_of_reproductions = numberOfMates;
        undevelopedChildren = new ArrayList<>();
        developedChildren = new ArrayList<>();
        for (int i = 0; i < number_of_reproductions; i++){
            int[][] childPair = crossBreeder.crossBreed(matingRouletteWheel.next(), matingRouletteWheel.next());
            undevelopedChildren.add(new OneMaxIndividual(OneMaxMutator.mutate(childPair[0])));
            undevelopedChildren.add(new OneMaxIndividual(OneMaxMutator.mutate(childPair[1])));
        }

    }
}
