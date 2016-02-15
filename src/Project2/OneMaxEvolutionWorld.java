package Project2;


import java.util.ArrayList;
import java.util.TreeMap;

public class OneMaxEvolutionWorld extends EvolutionWorld {

    private RandomCollection<Individual> parentRouletteWheel;
    private RandomCollection<int[]> matingRouletteWheel;
    private int numberOfMates = 50;
    private ArrayList<Individual> matingList;
    private IntVectorCrossBreeder crossBreeder = new IntVectorCrossBreeder();

    public OneMaxEvolutionWorld(int stringLength, int initialChildren) {
        undevelopedChildren = new ArrayList<>();
        developedChildren = new ArrayList<>();
        fitnessTestedChildren = new TreeMap<>(new OneMaxComparator());
        fitnessTestedAdults = new TreeMap<>(new OneMaxComparator());
        parentRouletteWheel = new RandomCollection<>();
        matingRouletteWheel = new RandomCollection<>();
        matingList = new ArrayList<>();

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
        OneMaxEvolutionWorld omew = new OneMaxEvolutionWorld(40, 100);

        for (int i = 0; i<100; i++){
            omew.oneRoundOfEvolution();
        }

        System.out.println("Hello!");
    }

    @Override
    protected void testFitness() {
        for (Individual c: developedChildren) {
            fitnessTestedChildren.put(c, OneMaxEvaluator.evaluate(c));
        }
    }

    @Override
    protected void selectAdults() {
        for (Individual c: fitnessTestedChildren.keySet()){
            fitnessTestedAdults.put(c, OneMaxEvaluator.evaluate(c));
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
        int number_of_reproductions = numberOfMates/2;
        undevelopedChildren = new ArrayList<>();
        developedChildren = new ArrayList<>();
        for (int i = 0; i < number_of_reproductions; i++){
            int[][] childPair = crossBreeder.crossBreed(matingRouletteWheel.next(), matingRouletteWheel.next());
            undevelopedChildren.add(new OneMaxIndividual(childPair[0]));
            undevelopedChildren.add(new OneMaxIndividual(childPair[1]));
        }

    }
}
