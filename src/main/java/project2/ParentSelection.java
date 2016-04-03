package project2;

import project2.helperStructures.RandomCollection;
import project2.tools.ScalingTools;

import java.util.ArrayList;
import java.util.Collections;

public class ParentSelection {

    public static ArrayList<Individual> fitnessProportionateParentSelection(ArrayList<Individual> adults, int childPoolSize) {
        ArrayList<Individual> matingIndividualList = new ArrayList<>();

        RandomCollection<Individual> parentRouletteWheel = new RandomCollection<>();
        for (Individual a: adults) {
            parentRouletteWheel.add(a.getFitness(), a);
        }

        for (int i = 0; i < childPoolSize; i++) {
            Individual next = parentRouletteWheel.next();
            matingIndividualList.add(next);
        }

        return matingIndividualList;
    }

    public static ArrayList<Individual> sigmaScaledParentSelection(ArrayList<Individual> adults, int childPoolSize) {
        ArrayList<Individual> matingIndividualList = new ArrayList<>();

        double[] fitnessValues = new double[adults.size()];
        for (int i = 0; i < adults.size(); i++) {
            fitnessValues[i] = adults.get(i).getFitness();
        }

        double average = ScalingTools.average(fitnessValues);
        double standardDeviation = ScalingTools.standardDeviation(fitnessValues, average);

        double[] scaledFitnessValues = new double[adults.size()];

        for (int i = 0; i < adults.size(); i++) {
            scaledFitnessValues[i] = ScalingTools.sigmaScale(adults.get(i).getFitness(), average, standardDeviation);
        }

        RandomCollection<Individual> parentRouletteWheel = new RandomCollection<>();
        for (int i = 0; i < adults.size(); i++) {
            parentRouletteWheel.add(scaledFitnessValues[i], adults.get(i));
        }

        for (int i = 0; i < childPoolSize; i++) {
            Individual next = parentRouletteWheel.next();
            matingIndividualList.add(next);
        }

        return matingIndividualList;
    }

    public static ArrayList<Individual> tournamentParentSelection(ArrayList<Individual> adults, int childPoolSize, int tournamentSize, double tournamentE) {
        ArrayList<Individual> matingIndividualList = new ArrayList<>();

        ArrayList<Individual> tournament;
        for (int i = 0; i < childPoolSize; i++) {
            tournament = new ArrayList<>();
            Collections.shuffle(adults);

            for (int j = 0; j < tournamentSize; j++) {
                tournament.add(adults.get(j));
            }

            if (Math.random() > tournamentE) {
                double maxFitness = 0;
                Individual maxIndividual = null;

                for (Individual a: tournament) {
                    double f = a.getFitness();
                    if (f > maxFitness) {
                        maxFitness = f;
                        maxIndividual = a;
                    }
                }

                matingIndividualList.add(maxIndividual);
            } else {
                matingIndividualList.add(tournament.get(0)); // List is already randomized, so this is a random choice
            }
        }

        return matingIndividualList;
    }

    public static ArrayList<Individual> boltzmannParentSelection(ArrayList<Individual> adults, int childPoolSize, double t) {
        ArrayList<Individual> matingIndividualList = new ArrayList<>();

        double[] fitnessValues = new double[adults.size()];
        for (int i = 0; i < adults.size(); i++) {
            fitnessValues[i] = adults.get(i).getFitness();
        }

        double average = ScalingTools.averageBoltzmannExponent(fitnessValues, t);

        double[] scaledFitnessValues = new double[adults.size()];

        for (int i = 0; i < adults.size(); i++) {
            scaledFitnessValues[i] = ScalingTools.boltzmannFitness(fitnessValues[i], t, average);
        }

        RandomCollection<Individual> parentRouletteWheel = new RandomCollection<>();
        for (int i = 0; i < adults.size(); i++) {
            parentRouletteWheel.add(scaledFitnessValues[i], adults.get(i));
        }

        for (int i = 0; i < childPoolSize; i++) {
            Individual next = parentRouletteWheel.next();
            matingIndividualList.add(next);
        }

        return matingIndividualList;
    }
}
