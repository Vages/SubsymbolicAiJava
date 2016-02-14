package Project2;


import java.util.ArrayList;
import java.util.TreeMap;

public class OneMaxEvolutionWorld extends EvolutionWorld {

    public OneMaxEvolutionWorld(int stringLength, int initialChildren) {
        undevelopedChildren = new ArrayList<>();
        developedChildren = new ArrayList<>();
        fitnessTestedAdults = new TreeMap<>();
        IndividualEvaluator evaluator = new OneMaxEvaluator();



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
        OneMaxEvolutionWorld omew = new OneMaxEvolutionWorld(20, 20);

        System.out.println("Hello!");
    }

    @Override
    protected void testFitness() {

    }

    @Override
    protected void selectAdults() {

    }

    @Override
    protected void ageBasedFiltering() {

    }

    @Override
    protected void parentSelection() {

    }

    @Override
    protected void genotypeCopying() {

    }

    @Override
    protected void reproduction() {

    }
}
