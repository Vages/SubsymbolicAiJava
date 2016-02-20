package Project2;

import java.util.ArrayList;

public abstract class EvolutionWorld {
    protected ArrayList<Individual> children, adults;
    protected MatingSelection matingSelection;
    protected RandomCollection<Individual> parentRouletteWheel;
    protected ArrayList<Individual> matingIndividualList;
    protected int numberOfNewChildren;

    public EvolutionWorld(MatingSelection matingSelection, int numberOfNewChildren) {
        children = new ArrayList<>();
        adults = new ArrayList<>();
        this.matingSelection = matingSelection;
        this.numberOfNewChildren = numberOfNewChildren;
    }

    public void oneRoundOfEvolution(){
        developChildren();
        assessFitness();
        selectAdults();
        ageBasedFiltering();
        switch (this.matingSelection) {
            case FITNESS_PROPORTIONATE:
                fitnessProportionateParentSelection();
                break;
            case SIGMA_SCALING:
                sigmaScaledParentSelection();
                break;
        }
        genotypeCopying();
        reproduction();
    }

    protected void developChildren() {
        for (Individual c: children) {
            c.develop();
        }
    }

    protected void assessFitness() {
        for (Individual c: children) {
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

        matingIndividualList = new ArrayList<>();
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

        matingIndividualList = new ArrayList<>();
        for (int i = 0; i < numberOfNewChildren; i++) {
            Individual next = parentRouletteWheel.next();
            matingIndividualList.add(next);
        }
    }

    protected abstract void genotypeCopying();

    protected abstract void reproduction();
}
