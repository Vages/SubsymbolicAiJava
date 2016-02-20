package Project2;

public class ScalingTools {

    static double sigmaScale(double fitness, double averageFitness, double standardDeviation){
        return (1 + (fitness-averageFitness)/(2*standardDeviation));
    }
}
