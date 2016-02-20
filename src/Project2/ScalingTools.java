package Project2;

public class ScalingTools {

    static double sigmaScale(double fitness, double averageFitness, double standardDeviation){
        return (1 + (fitness-averageFitness)/(2*standardDeviation));
    }

    static double average(double[] values) {
        double sum = 0;

        for (double v: values) {
            sum += v;
        }

        return sum/values.length;
    }

    static double standardDeviation(double[] values, double average) {
        double sum = 0;

        for (double v: values) {
            sum = sum + Math.pow((v-average), 2);
        }

        double variance = sum/(values.length-1); // Because the average is estimated, one must be subtracted

        return Math.sqrt(variance);
    }
}
