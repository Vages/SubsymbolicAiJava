package project2.integerSpecializations.runners;

import project2.AdultSelection;
import project2.MatingSelection;
import project2.integerSpecializations.IntegerEvolutionWorld;
import project2.integerSpecializations.evaluators.LolzEvaluator;
import project2.integerSpecializations.evaluators.IntegerPhenotypeEvaluator;

public class LolzRunner {
    public static void main(String[] args) {
        AdultSelection adultSelection = AdultSelection.getStrategyFromChar(args[0].charAt(0));
        MatingSelection matingSelection = MatingSelection.getStrategyFromChar(args[1].charAt(0));

        int childPoolSize = Integer.parseInt(args[2]);
        int adultPoolSize = Integer.parseInt(args[3]);
        int generations = Integer.parseInt(args[4]);
        int tournamentSize = Integer.parseInt(args[5]);
        double tournamentE = Double.parseDouble(args[6]);
        String logFileName = args[7];
        double mutateThreshold = Double.parseDouble(args[8]);
        int numberOfMutations = Integer.parseInt(args[9]);
        double crossingRate = Double.parseDouble(args[10]);

        int maxInt = 1;
        int stringlength = Integer.parseInt(args[11]);
        int zCap = Integer.parseInt(args[12]);

        IntegerPhenotypeEvaluator evaluator = new LolzEvaluator(zCap);

        IntegerEvolutionWorld omew = new IntegerEvolutionWorld(adultSelection, matingSelection, childPoolSize,
                adultPoolSize, generations, tournamentSize, tournamentE, logFileName, maxInt, mutateThreshold,
                numberOfMutations, crossingRate, stringlength, evaluator);
        omew.runAllEpochs();

    }
}
