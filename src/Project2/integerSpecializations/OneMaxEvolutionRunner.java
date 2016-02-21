package project2.integerSpecializations;

import project2.SelectionStrategy;

public class OneMaxEvolutionRunner {
    public static void main(String[] args) {
        int stringlength = 40;
        int individuals = 70;
        int generations = 100;
        int[] idealPhenotype = new int[stringlength];

        for (int i = 0; i < stringlength; i++){
            idealPhenotype[i] = 1;
        }

        IntegerEvolutionWorld omew = new IntegerEvolutionWorld(SelectionStrategy.FITNESS_PROPORTIONATE, individuals, generations, stringlength, idealPhenotype);
        omew.runAllGenerations();
    }
}
