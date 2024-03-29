package project3;

import org.apache.commons.lang3.ArrayUtils;
import project2.Individual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class FlatlandIndividual extends Individual<Double> {
    private Double[] genotype;
    private int[] topology;
    private FlatlandNeuralNetwork network;
    private int lastScenariosVersionAssessed = -1;
    private double fitness;
    private FlatlandEvolutionWorld world;
    private static FlatlandGui gui;

    /*static {
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(FlatlandGui.class);
            }
        }.start();
        gui = FlatlandGui.waitForStartUpTest();
        gui.printSomething();
    }*/

    public FlatlandIndividual(Double[] genotype, int[] topology, FlatlandEvolutionWorld world) {
        this.genotype = genotype;
        this.topology = topology;
        this.world = world;
    }

    @Override
    public Double[] getGenotype() {
        return Arrays.copyOf(genotype, genotype.length);
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    public double assessFitnessForAllWorldScenarios(boolean guiOn) {
        ArrayList<Board> boards = world.getScenarios();

        double fitnessSum = 0;
        for (Board b: boards) {
            b.reset();
            fitnessSum += assessFitnessForBoard(b, guiOn);
        }

        return fitnessSum;
    }

    private double assessFitnessForBoard(Board b, boolean guiOn){
        double sum = 0;
        for (int i = 0; i < 60; i++) {
            sum += doOneMoveOnBoard(b);
            if (guiOn) {
                gui.drawBoard(b);
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return sum;
    }

    public double doOneMoveOnBoard(Board b) {
        boolean[] food = b.sense(CellType.FOOD);
        boolean[] poison = b.sense(CellType.POISON);

        MoveDirection nextMove = network.getNextMove(food, poison);

        CellType contents = b.move(nextMove);

        if (contents == CellType.FOOD) {
            return world.getFoodReward();
        } else if (contents == CellType.POISON){
            return world.getPoisonReward();
        } else return 0;
    }

    @Override
    public void develop() {
        if (this.network == null) {
            this.network = new FlatlandNeuralNetwork(topology, ArrayUtils.toPrimitive(genotype));
        }
    }

    @Override
    public void assessFitness() {
        if (this.lastScenariosVersionAssessed != world.getScenariosVersion()) {
            double f = assessFitnessForAllWorldScenarios(false);
            this.fitness = f > 0 ? f : 0; // Cannot be smaller than 0, to avoid errors.
            this.lastScenariosVersionAssessed = world.getScenariosVersion();
        }
    }

    public static Double[] generateRandomGenotype(int[] topology){
        int numberOfWeightsToBeGenerated = 0;

        for (int i = 0; i < topology.length-1; i++) {
            numberOfWeightsToBeGenerated += topology[i]*topology[i+1]; // For transition weights
            numberOfWeightsToBeGenerated += topology[i+1]; // for bias weights
        }

        Double[] weights = new Double[numberOfWeightsToBeGenerated];

        for (int i = 0; i < weights.length; i++){
            weights[i] = Math.random()*2-1;
        }

        return weights;
    }
}
