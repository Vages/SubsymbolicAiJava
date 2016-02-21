package project2.tools;

public class ScalingTools {

    public static double sigmaScale(double fitness, double averageFitness, double standardDeviation){
        return (1 + (fitness-averageFitness)/(2*standardDeviation));
    }

    public static double average(double[] values) {
        double sum = 0;

        for (double v: values) {
            sum += v;
        }

        return sum/values.length;
    }

    public static double standardDeviation(double[] values, double average) {
        double sum = 0;

        for (double v: values) {
            sum = sum + Math.pow((v-average), 2);
        }

        double variance = sum/(values.length-1); // Because the average is estimated, one must be subtracted

        return Math.sqrt(variance);
    }

    public static double boltzmannExponent(double fitness, double temp) {
        return Math.pow(Math.E, fitness/temp);
    }

    public static double boltzmannFitness(double fitness, double temp, double average) {
        return boltzmannExponent(fitness, temp)/average;
    }

    public static double averageBoltzmannExponent(double[] values, double temp) {
        double sum = 0;

        for (double v: values)
            sum += boltzmannExponent(v, temp);

        return sum/values.length;
    }
}
