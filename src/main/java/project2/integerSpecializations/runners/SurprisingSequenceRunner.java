package project2.integerSpecializations.runners;

import project2.AdultSelection;
import project2.SelectionStrategy;
import project2.integerSpecializations.IntegerEvolutionWorld;
import project2.integerSpecializations.evaluators.IntegerPhenotypeEvaluator;
import project2.integerSpecializations.evaluators.SurprisingSequenceEvaluator;

public class SurprisingSequenceRunner {
    public static void main(String[] args) {
        AdultSelection adultSelection = AdultSelection.getStrategyFromChar(args[0].charAt(0));
        SelectionStrategy selectionStrategy = SelectionStrategy.getStrategyFromChar(args[1].charAt(0));

        int childPoolSize = Integer.parseInt(args[2]);
        int adultPoolSize = Integer.parseInt(args[3]);
        int generations = Integer.parseInt(args[4]);
        int tournamentSize = Integer.parseInt(args[5]);
        double tournamentE = Double.parseDouble(args[6]);
        String logFileName = args[11]+"-s"+args[12]+"-l"+args[13]+"-c"+args[2]+"-a"+args[3]+"-m"+args[8]+"-cr"+args[10];
        double mutateThreshold = Double.parseDouble(args[8]);
        int numberOfMutations = Integer.parseInt(args[9]);
        double crossingRate = Double.parseDouble(args[10]);

        boolean globallySurprising;

        switch (args[11].charAt(0)){
            case 'g':
                globallySurprising = true;
                break;
            default:
                globallySurprising = false;
                break;
        }

        int maxInt = Integer.parseInt(args[12])-1;
        int stringlength = Integer.parseInt(args[13]);

        IntegerPhenotypeEvaluator evaluator = new SurprisingSequenceEvaluator(globallySurprising);

        IntegerEvolutionWorld omew = new IntegerEvolutionWorld(adultSelection, selectionStrategy, childPoolSize,
                adultPoolSize, generations, tournamentSize, tournamentE, logFileName, maxInt, mutateThreshold,
                numberOfMutations, crossingRate, stringlength, evaluator);
        omew.runAllEpochs();
    }
}
