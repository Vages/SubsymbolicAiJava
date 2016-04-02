package project3;

import project2.AdultSelection;
import project2.EvolutionWorld;
import project2.SelectionStrategy;

import java.util.ArrayList;

public class FlatlandEvolutionWorld extends EvolutionWorld {
    private ArrayList<Double> matingGenotypeList;

    public FlatlandEvolutionWorld(AdultSelection adultSelection, SelectionStrategy matingSelection, int childPoolSize, int adultPoolSize, int numberOfGenerations, int epochs, int tournamentSize, double tournamentE, String logFileName) {
        super(adultSelection, matingSelection, childPoolSize, adultPoolSize, numberOfGenerations, epochs, tournamentSize, tournamentE, logFileName);
    }

    @Override
    protected void generateRandomChildren() {

    }

    @Override
    protected void genotypeCopying() {

    }

    @Override
    protected void reproduction() {

    }
}
