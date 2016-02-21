package Project2;

import java.util.ArrayList;
import java.util.Collections;

public abstract class EvolutionWorld {
    protected ArrayList<Individual> children, adults;
    protected MatingSelection matingSelection;
    protected RandomCollection<Individual> parentRouletteWheel;
    protected ArrayList<Individual> matingIndividualList;
    protected int numberOfNewChildren;
    protected int numberOfGenerations;
    protected int currentGeneration;

    // Fields related to tournament selection
    protected int tournamentSize = 5;
    protected double tournamentE = 0.1;

    public EvolutionWorld(MatingSelection matingSelection, int numberOfNewChildren, int generations) {
        children = new ArrayList<>();
        adults = new ArrayList<>();
        this.matingSelection = matingSelection;
        this.numberOfNewChildren = numberOfNewChildren;
        this.numberOfGenerations = generations;
    }

    public void oneRoundOfEvolution(){
        developChildren();
        assessFitness();
        selectAdults();
        ageBasedFiltering();
        matingIndividualList = new ArrayList<>();
        switch (this.matingSelection) {
            case FITNESS_PROPORTIONATE:
                fitnessProportionateParentSelection();
                break;
            case SIGMA_SCALING:
                sigmaScaledParentSelection();
                break;
            case TOURNAMENT_SELECTION:
                tournamentParentSelection();
                break;
            case BOLTZMANN_SCALING:
                boltzmannParentSelection();
                break;
        }
        genotypeCopying();
        reproduction();
    }

    public void runAllGenerations() {
        for (currentGeneration = 0; currentGeneration < this.numberOfGenerations; currentGeneration++) {
            this.oneRoundOfEvolution();
        }
    }

    protected void developChildren() {
        for (Individual c: children) {
            c.develop();
        }
    }

    protected void assessFitness() {
        for (Individual c: children) {
            c.getFitness();
            System.out.println(c.getFitness());
        }
    }

    protected void selectAdults() {
        for (Individual c: children){
            adults.add(c);
        }
    }

    protected void ageBasedFiltering(){

    }

    protected void fitnessProportionateParentSelection() {
        parentRouletteWheel = new RandomCollection<>();
        for (Individual a: adults) {
            parentRouletteWheel.add(a.getFitness(), a);
        }

        for (int i = 0; i < numberOfNewChildren; i++) {
            Individual next = parentRouletteWheel.next();
            matingIndividualList.add(next);
        }
    }

    protected void sigmaScaledParentSelection() {
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

        parentRouletteWheel = new RandomCollection<>();
        for (int i = 0; i < adults.size(); i++) {
            parentRouletteWheel.add(scaledFitnessValues[i], adults.get(i));
        }

        for (int i = 0; i < numberOfNewChildren; i++) {
            Individual next = parentRouletteWheel.next();
            matingIndividualList.add(next);
        }
    }

    protected void tournamentParentSelection() {
        ArrayList<Individual> tournament;
        for (int i = 0; i < this.numberOfNewChildren; i++) {
            tournament = new ArrayList<>();
            Collections.shuffle(adults);

            for (int j = 0; j < this.tournamentSize; j++) {
                tournament.add(adults.get(j));
            }

            if (Math.random() > this.tournamentE) {
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
    }

    protected void boltzmannParentSelection() {
        double t = getTemperature();

        double[] fitnessValues = new double[adults.size()];
        for (int i = 0; i < adults.size(); i++) {
            fitnessValues[i] = adults.get(i).getFitness();
        }

        double average = ScalingTools.averageBoltzmannExponent(fitnessValues, t);

        double[] scaledFitnessValues = new double[adults.size()];

        for (int i = 0; i < adults.size(); i++) {
            scaledFitnessValues[i] = ScalingTools.boltzmannFitness(fitnessValues[i], t, average);
        }

        parentRouletteWheel = new RandomCollection<>();
        for (int i = 0; i < adults.size(); i++) {
            parentRouletteWheel.add(scaledFitnessValues[i], adults.get(i));
        }

        for (int i = 0; i < numberOfNewChildren; i++) {
            Individual next = parentRouletteWheel.next();
            matingIndividualList.add(next);
        }
    }

    private double getTemperature() {
        return 0.1*((double) (numberOfGenerations-currentGeneration))/numberOfGenerations+0.1;
    }

    protected abstract void genotypeCopying();

    protected abstract void reproduction();
}
