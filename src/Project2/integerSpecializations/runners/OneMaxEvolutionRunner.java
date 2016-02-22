package project2.integerSpecializations.runners;

import project2.AdultSelection;
import project2.SelectionStrategy;
import project2.integerSpecializations.IntegerEvolutionWorld;
import project2.integerSpecializations.evaluators.OneMaxEvaluator;
import project2.integerSpecializations.evaluators.PhenotypeEvaluator;

public class OneMaxEvolutionRunner {
    public static void main(String[] args) {
        AdultSelection adultSelection = AdultSelection.getStrategyFromChar(args[0].charAt(0));
        SelectionStrategy selectionStrategy = SelectionStrategy.getStrategyFromChar(args[1].charAt(0));

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

        int stringlength;
        int[] idealPhenotype;

        switch (args[11].charAt(0)) {
            case 's':
                String userDefinedPhenotype = args[12];

                stringlength = userDefinedPhenotype.length();
                idealPhenotype = new int[stringlength];
                for (int i = 0; i < stringlength; i++) {
                    idealPhenotype[i] = Integer.parseInt(String.valueOf(userDefinedPhenotype.charAt(i)));
                }
                break;
            default:
                stringlength = Integer.parseInt(args[12]);
                idealPhenotype = new int[stringlength];

                for (int i = 0; i < stringlength; i++){
                    idealPhenotype[i] = 1;
                }
                break;
        }

        PhenotypeEvaluator evaluator = new OneMaxEvaluator(idealPhenotype);

        IntegerEvolutionWorld omew = new IntegerEvolutionWorld(adultSelection, selectionStrategy, childPoolSize,
                adultPoolSize, generations, tournamentSize, tournamentE, logFileName, maxInt, mutateThreshold,
                numberOfMutations, crossingRate, stringlength, evaluator);
        omew.runAllEpochs();
    }
}
