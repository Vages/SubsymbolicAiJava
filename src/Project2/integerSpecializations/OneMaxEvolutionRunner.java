package project2.integerSpecializations;

import project2.AdultSelection;
import project2.SelectionStrategy;
import project2.integerSpecializations.evaluators.OneMaxEvaluator;
import project2.integerSpecializations.evaluators.PhenotypeEvaluator;

public class OneMaxEvolutionRunner {
    public static void main(String[] args) {
        AdultSelection adultSelection;

        switch (args[0].charAt(0)) {
            case 'f':
                adultSelection = AdultSelection.FULL_GENERATIONAL_REPLACEMENT;
                break;
            case 'o':
                adultSelection = AdultSelection.OVER_PRODUCTION;
                break;
            case 'g':
                adultSelection = AdultSelection.GENERATIONAL_MIXING;
                break;
            default:
                adultSelection = AdultSelection.FULL_GENERATIONAL_REPLACEMENT;
                break;
        }

        SelectionStrategy selectionStrategy;

        switch (args[1].charAt(0)) {
            case 'f':
                selectionStrategy = SelectionStrategy.FITNESS_PROPORTIONATE;
                break;
            case 's':
                selectionStrategy = SelectionStrategy.SIGMA_SCALING;
                break;
            case 't':
                selectionStrategy = SelectionStrategy.TOURNAMENT_SELECTION;
                break;
            case 'b':
                selectionStrategy = SelectionStrategy.BOLTZMANN_SCALING;
                break;
            default:
                selectionStrategy = SelectionStrategy.FITNESS_PROPORTIONATE;
                break;
        }

        int childPoolSize = Integer.parseInt(args[2]);
        int adultPoolSize = Integer.parseInt(args[3]);
        int generations = Integer.parseInt(args[4]);
        int tournamentSize = Integer.parseInt(args[5]);
        double tournamentE = Double.parseDouble(args[6]);
        String logFileName = args[7];
        int maxInt = 1;
        double mutateThreshold = Double.parseDouble(args[8]);
        int numberOfMutations = Integer.parseInt(args[9]);
        double crossingRate = Double.parseDouble(args[10]);

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
        omew.runAllGenerations();
    }
}
