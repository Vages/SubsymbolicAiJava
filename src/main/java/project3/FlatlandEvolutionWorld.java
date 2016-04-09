package project3;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import project2.AdultSelection;
import project2.CrossBreeder;
import project2.EvolutionWorld;
import project2.MatingSelection;

import java.util.ArrayList;

public class FlatlandEvolutionWorld extends EvolutionWorld<Double> {
    private final CrossBreeder<Double> crossBreeder;
    private final ScenarioPolicy scenarioPolicy;
    private final int[] topology;
    private DoubleMutator doubleMutator;
    private double[] fpDistribution;
    private ArrayList<Board> scenarios;
    private int numberOfScenarios;
    private int scenariosVersion = 0;
    private double foodReward;
    private double poisonReward;

    public FlatlandEvolutionWorld(AdultSelection adultSelection, MatingSelection matingSelection, int childPoolSize,
                                  int adultPoolSize, int numberOfGenerations, int epochs, int tournamentSize,
                                  double tournamentE, String logFileName, int[] topology, double crossingRate,
                                  double mutateThreshold, int numberOfMutations, double minValue, double maxValue,
                                  double[] fpDistribution, double[] fpReward, int numberOfScenarios, ScenarioPolicy scenarioPolicy) {
        super(adultSelection, matingSelection, childPoolSize, adultPoolSize, numberOfGenerations, epochs,
                tournamentSize, tournamentE, logFileName);
        this.topology = topology;
        this.crossBreeder = new CrossBreeder<>(Double.class, crossingRate);
        this.doubleMutator = new DoubleMutator(mutateThreshold, numberOfMutations, minValue, maxValue);
        this.fpDistribution = fpDistribution;
        this.numberOfScenarios = numberOfScenarios;
        this.scenarioPolicy = scenarioPolicy;
        this.scenarios = new ArrayList<>();
        this.foodReward = fpReward[0];
        this.poisonReward = fpReward[1];
        generateNewScenarios();
    }

    public enum ScenarioPolicy {
        DYNAMIC, STATIC
    }

    private void generateNewScenarios(){
        scenarios.clear();
        for (int i = 0; i < numberOfScenarios; i++){
            scenarios.add(new Board(fpDistribution[0], fpDistribution[1], new int[]{0,0}));
        }
        scenariosVersion++;
    }

    @Override
    protected void generateRandomChildren() {
        for (int i = 0; i < childPoolSize; i++) {
            Double[] genotype = FlatlandIndividual.generateRandomGenotype(topology);
            this.children.add(new FlatlandIndividual(genotype, topology, this));
        }
    }

    @Override
    protected void reproduction() {
        children.clear();

        for (int i = 0; i < matingGenotypeList.size(); i = i + 2) {
            Double[][] childPair = crossBreeder.crossBreed(matingGenotypeList.get(i), matingGenotypeList.get(i+1));
            children.add(new FlatlandIndividual(doubleMutator.mutate(childPair[0]), topology, this));
            children.add(new FlatlandIndividual(doubleMutator.mutate(childPair[1]), topology, this));
        }

    }

    @Override
    public void oneRoundOfEvolution() {
        super.oneRoundOfEvolution();
        if (scenarioPolicy == ScenarioPolicy.DYNAMIC){
            generateNewScenarios();
            scenariosVersion ++;
        }
    }

    @Override
    public void runAllGenerations() {
        super.runAllGenerations();
        FlatlandIndividual bestIndividual = (FlatlandIndividual) adults.get(adults.size()-1);
        //bestIndividual.assessFitnessForAllWorldScenarios(true);
    }

    public FlatlandIndividual getBestIndividual() {
        return (FlatlandIndividual) adults.get(adults.size()-1);
    }

    public ArrayList<Board> getScenarios() {
        return scenarios;
    }

    public double getFoodReward() {
        return foodReward;
    }

    public double getPoisonReward() {
        return poisonReward;
    }

    public int getScenariosVersion() {
        return scenariosVersion;
    }

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("FlatlandEvolutionWorld")
                .description("Runs a flatland evolution simulation");

        parser.addArgument("-g", "--generations")
                .metavar("N")
                .setDefault(40)
                .type(Integer.class)
                .help("number of generations used for evolution");

        parser.addArgument("-c", "--children")
                .metavar("N")
                .setDefault(40)
                .type(Integer.class)
                .help("number of individuals in the child pool");

        parser.addArgument("-a", "--adults")
                .metavar("N")
                .setDefault(40)
                .type(Integer.class)
                .help("number of individuals in the adult pool");

        parser.addArgument("-e", "--epochs")
                .metavar("E")
                .setDefault(1)
                .type(Integer.class)
                .help("number of epochs (full run of N generations) to run for");

        parser.addArgument("-lf", "--log-file")
                .metavar("directory/filename")
                .setDefault("project3/log")
                .type(String.class)
                .help("directory and filename of the log file");

        parser.addArgument("-cr", "--crossing-rate")
                .type(Double.class)
                .setDefault(0.5)
                .help("probability of a genome crossing during mating");


        parser.addArgument("-t", "--topology")
                .metavar("n")
                .setDefault(new int[]{6, 3})
                .nargs("*")
                .type(Integer.class);

        parser.addArgument("-mt", "--mutate-threshold")
                .nargs(1)
                .type(Double.class)
                .setDefault(0.8);

        parser.addArgument("-nm", "--number-of-mutations")
                .nargs(1)
                .type(Integer.class)
                .setDefault(1);

        parser.addArgument("-mr", "--mutation-range")
                .nargs(2)
                .type(Double.class)
                .setDefault(new double[]{-1, 1});

        parser.addArgument("-fpd", "--food-poison-distribution")
                .nargs(2)
                .type(Double.class)
                .setDefault(new double[]{0.33, 0.33});

        parser.addArgument("-ns", "--number-of-scenarios")
                .nargs(1)
                .type(Integer.class)
                .setDefault(5);

        parser.addArgument("-dyn", "--dynamic")
                .setConst(ScenarioPolicy.DYNAMIC)
                .setDefault(ScenarioPolicy.STATIC);

        parser.addArgument("-fpr", "--food-poison-reward")
                .nargs(2)
                .type(Double.class)
                .setDefault(new double[]{1, -5});

        try {
            Namespace res = parser.parseArgs(args);

            double[] mutation_range = res.get("mutation_range");

            FlatlandEvolutionWorld mew = new FlatlandEvolutionWorld(
                    AdultSelection.GENERATIONAL_MIXING,
                    MatingSelection.SIGMA_SCALING,
                    res.get("children"),
                    res.get("adults"),
                    res.get("generations"),
                    res.get("epochs"),
                    5,
                    0.1,
                    res.get("log_file"),
                    res.get("topology"),
                    res.get("crossing_rate"),
                    res.get("mutate_threshold"),
                    res.get("number_of_mutations"),
                    mutation_range[0],
                    mutation_range[1],
                    res.get("food_poison_distribution"),
                    res.get("food_poison_reward"),
                    res.get("number_of_scenarios"),
                    res.get("dynamic")
            );

            mew.runAllEpochs();

        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
    }
}
