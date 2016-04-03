package project3;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import project2.AdultSelection;
import project2.EvolutionWorld;
import project2.SelectionStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FlatlandEvolutionWorld extends EvolutionWorld<Double> {
    public FlatlandEvolutionWorld(AdultSelection adultSelection, SelectionStrategy matingSelection, int childPoolSize,
                                  int adultPoolSize, int numberOfGenerations, int epochs, int tournamentSize,
                                  double tournamentE, String logFileName) {
        super(adultSelection, matingSelection, childPoolSize, adultPoolSize, numberOfGenerations, epochs,
                tournamentSize, tournamentE, logFileName);
    }

    @Override
    protected void generateRandomChildren() {

    }

    @Override
    protected void reproduction() {

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
                .help("The directory and filename of the log file");


        parser.addArgument("-t", "--topology")
                .metavar("n")
                .setDefault(new int[]{6, 3})
                .nargs("*")
                .type(Integer.class);

        try {
            Namespace res = parser.parseArgs(args);

            FlatlandEvolutionWorld mew = new FlatlandEvolutionWorld(
                    AdultSelection.GENERATIONAL_MIXING,
                    SelectionStrategy.SIGMA_SCALING,
                    res.get("children"),
                    res.get("adults"),
                    res.get("generations"),
                    res.get("epochs"),
                    5,
                    0.1,
                    res.get("log_file")
            );

            System.out.println("Hello");

        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
    }
}
