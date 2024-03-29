package project2;

import project2.tools.GenerateCsv;
import project2.tools.ScalingTools;

import java.util.ArrayList;
import java.util.Collections;

public abstract class EvolutionWorld<G> {
    protected ArrayList<G[]> matingGenotypeList;
    protected ArrayList<Individual> children, adults;
    protected AdultSelection adultSelection;
    protected MatingSelection matingSelection;
    protected ArrayList<Individual> matingIndividualList;
    protected ArrayList<ArrayList<String>> statisticsLog;
    protected int childPoolSize;
    protected int adultPoolSize;
    protected int numberOfGenerations;
    protected int epochs;
    protected int currentEpoch;
    protected int currentGeneration;
    protected String logFileName;

    // Fields related to tournament selection
    protected int tournamentSize;
    protected double tournamentE;

    // Arrays to contain statistics about runs
    protected double[] bestFitnessSums;
    protected double[] averageFitnessSums;
    protected double[] standardDeviationSums;
    
    public EvolutionWorld(AdultSelection adultSelection, MatingSelection matingSelection,
                          int childPoolSize, int adultPoolSize, int numberOfGenerations, int epochs,
                          int tournamentSize, double tournamentE, String logFileName) {

        this(adultSelection, matingSelection, childPoolSize, adultPoolSize, numberOfGenerations, logFileName);

        children = new ArrayList<>();
        adults = new ArrayList<>();
        matingIndividualList = new ArrayList<>();

        this.epochs = epochs;
        this.tournamentSize = tournamentSize;
        this.tournamentE = tournamentE;
    }

    public EvolutionWorld(AdultSelection adultSelection, MatingSelection matingSelection,
                          int childPoolSize, int adultPoolSize, int numberOfGenerations, String logFileName) {
        children = new ArrayList<>();
        adults = new ArrayList<>();
        matingIndividualList = new ArrayList<>();

        this.adultSelection = adultSelection;
        this.matingSelection = matingSelection;
        this.childPoolSize = childPoolSize;
        this.adultPoolSize = adultPoolSize;
        this.numberOfGenerations = numberOfGenerations;
        this.epochs = 1;
        this.tournamentSize = 5;
        this.tournamentE = 0.1;
        this.logFileName = logFileName;

        bestFitnessSums = new double[numberOfGenerations];
        averageFitnessSums= new double[numberOfGenerations];
        standardDeviationSums = new double[numberOfGenerations];

        statisticsLog = new ArrayList<>();
    }

    public void oneRoundOfEvolution(){
        developChildren();
        assessFitness();
        selectAdults();
        ageBasedFiltering();
        findGenerationStatistics();
        parentSelection();
        genotypeCopying();
        reproduction();
    }

    public void runAllGenerations() {
        statisticsLog.clear();
        children.clear();
        adults.clear();
        ArrayList<String> descriptiveLine = new ArrayList<>();
        descriptiveLine.add("Generation");
        descriptiveLine.add("Best fitness");
        descriptiveLine.add("Average fitness");
        descriptiveLine.add("Standard deviation");
        descriptiveLine.add("Best phenotype");
        statisticsLog.add(descriptiveLine);

        generateRandomChildren();
        for (currentGeneration = 0; currentGeneration < this.numberOfGenerations; currentGeneration++) {
            this.oneRoundOfEvolution();
        }

        GenerateCsv.generateCsvFile("./out/logs/SubsymbolicAiJava/"+this.logFileName+"-"+Integer.toString(currentEpoch)+".csv", statisticsLog);
    }

    public void runAllEpochs() {
        for (currentEpoch = 0; currentEpoch < epochs; currentEpoch++){
            runAllGenerations();
        }
        statisticsLog.clear();
        ArrayList<String> descriptiveLine = new ArrayList<>();
        descriptiveLine.add("Generation");
        descriptiveLine.add("Best fitness");
        descriptiveLine.add("Average fitness");
        descriptiveLine.add("Standard deviation");
        statisticsLog.add(descriptiveLine);

        for (int i = 0; i<numberOfGenerations; i++) {
            ArrayList<String> line = new ArrayList<>();
            line.add(Integer.toString(i));
            line.add(Double.toString(bestFitnessSums[i]/epochs));
            line.add(Double.toString(averageFitnessSums[i]/epochs));
            line.add(Double.toString(standardDeviationSums[i]/epochs));
            statisticsLog.add(line);
        }

        GenerateCsv.generateCsvFile("./out/logs/SubsymbolicAiJava/"+this.logFileName+"-averages.csv", statisticsLog);
    }

    protected abstract void generateRandomChildren();

    protected void developChildren() {
        for (Individual c: children) {
            c.develop();
        }
    }

    protected void assessFitness() {
        for (Individual c: children) {
            c.assessFitness();
        }
    }

    protected void selectAdults() {
        switch (this.adultSelection){
            case FULL_GENERATIONAL_REPLACEMENT:
                adults.clear();
                for (Individual c: children){
                    adults.add(c);
                }
                break;
            case OVER_PRODUCTION:
                adults.clear();
                Collections.sort(children);
                Collections.reverse(children);
                for (int i = 0; i < adultPoolSize; i++) {
                    adults.add(children.get(i));
                }
                break;
            case GENERATIONAL_MIXING:
                for (Individual a: adults) {
                    a.assessFitness();
                }
                for (Individual c: children){
                    adults.add(c);
                }
                Collections.sort(adults);
                Collections.reverse(adults);
                if (adults.size() > adultPoolSize)
                    adults.subList(adultPoolSize, adults.size()).clear();
                break;
        }

    }

    protected void ageBasedFiltering(){

    }

    protected void findGenerationStatistics() {
        ArrayList<String> statisticsLine = new ArrayList<>();

        Collections.sort(adults);
        Individual best = adults.get(adults.size()-1);

        double[] fitnessValues = new double[adults.size()];
        for (int i = 0; i < adults.size(); i++) {
            fitnessValues[i] = adults.get(i).getFitness();
        }

        double averageFitness = ScalingTools.average(fitnessValues);
        double standardDeviation = ScalingTools.standardDeviation(fitnessValues, averageFitness);
        double bestFitness = best.getFitness();
        String bestAsString = best.toString();

        System.out.println("Generation:         " + currentGeneration);
        System.out.println("Best fitness:       " + bestFitness);
        System.out.println("Average fitness:    " + averageFitness);
        System.out.println("Standard deviation: " + standardDeviation);
        System.out.println("Best phenotype:     " + bestAsString);
        System.out.println();

        statisticsLine.add(Integer.toString(currentGeneration));
        statisticsLine.add(Double.toString(bestFitness));
        statisticsLine.add(Double.toString(averageFitness));
        statisticsLine.add(Double.toString(standardDeviation));
        statisticsLine.add(bestAsString.replace(',', ';'));

        statisticsLog.add(statisticsLine);

        bestFitnessSums[currentGeneration] += bestFitness;
        averageFitnessSums[currentGeneration] += averageFitness;
        standardDeviationSums[currentGeneration] += standardDeviation;
    }

    private void parentSelection() {
        switch (this.matingSelection) {
            case FITNESS_PROPORTIONATE:
                matingIndividualList = ParentSelection.fitnessProportionateParentSelection(adults, childPoolSize);
                break;
            case SIGMA_SCALING:
                matingIndividualList = ParentSelection.sigmaScaledParentSelection(adults, childPoolSize);
                break;
            case TOURNAMENT_SELECTION:
                matingIndividualList = ParentSelection.tournamentParentSelection(adults, childPoolSize, tournamentSize, tournamentE);
                break;
            case BOLTZMANN_SCALING:
                matingIndividualList = ParentSelection.boltzmannParentSelection(adults, childPoolSize, getTemperature());
                break;
        }
    }

    private double getTemperature() {
        return 0.01*((double) (numberOfGenerations-currentGeneration))/numberOfGenerations+0.001;
    }

    protected void genotypeCopying() {
        matingGenotypeList = new ArrayList<>();
        for (Individual i: matingIndividualList) {
            matingGenotypeList.add((G[]) i.getGenotype());
        }
    }

    protected abstract void reproduction();
}
