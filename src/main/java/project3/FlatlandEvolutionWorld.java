package project3;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import project2.AdultSelection;
import project2.CrossBreeder;
import project2.EvolutionWorld;
import project2.MatingSelection;

public class FlatlandEvolutionWorld extends EvolutionWorld<Double> {
    private final CrossBreeder<Double> crossBreeder;
    private int[] topology;
    private DoubleMutator doubleMutator;

    public FlatlandEvolutionWorld(AdultSelection adultSelection, MatingSelection matingSelection, int childPoolSize,
                                  int adultPoolSize, int numberOfGenerations, int epochs, int tournamentSize,
                                  double tournamentE, String logFileName, int[] topology, double crossingRate,
                                  double mutateThreshold, int numberOfMutations, double minValue, double maxValue) {
        super(adultSelection, matingSelection, childPoolSize, adultPoolSize, numberOfGenerations, epochs,
                tournamentSize, tournamentE, logFileName);
        this.topology = topology;
        this.crossBreeder = new CrossBreeder<>(Double.class, crossingRate);
        this.doubleMutator = new DoubleMutator(mutateThreshold, numberOfMutations, minValue, maxValue);
    }

    @Override
    protected void generateRandomChildren() {
        for (int i = 0; i < childPoolSize; i++) {
            Double[] genotype = FlatlandIndividual.generateRandomGenotype(topology);
            this.children.add(new FlatlandIndividual(genotype, topology));
        }
    }

    @Override
    protected void reproduction() {
        children.clear();

        for (int i = 0; i < matingGenotypeList.size(); i = i + 2) {
            Double[][] childPair = crossBreeder.crossBreed(matingGenotypeList.get(i), matingGenotypeList.get(i+1));
            children.add(new FlatlandIndividual(doubleMutator.mutate(childPair[0]), topology));
            children.add(new FlatlandIndividual(doubleMutator.mutate(childPair[1]), topology));
        }

    }
    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("prog")
                .description("Flatland Evolution World");

        parser.addArgument("-g", "--generations")
                .metavar("N")
                .setDefault(50)
                .type(Integer.class)
                .help("number of generations used for evolution");

        parser.addArgument("-c", "--children")
                .metavar("N")
                .setDefault(100)
                .type(Integer.class)
                .help("number of individuals in the child pool");

        parser.addArgument("-a", "--adults")
                .metavar("N")
                .setDefault(200)
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
                    mutation_range[1]
            );

            System.out.println("Hello");

        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
    }
}
