package project4;

/**
 * Contains a gene value and its maximum and minimum mutation values.
 */
public class NeuralNetworkGene {
    private double value;
    private final double minMutationValue;
    private final double maxMutationValue;

    /**
     * @param value            Value of the gene
     * @param minMutationValue Minimum value it can be mutated to
     * @param maxMutationValue Maximum value it can be mutated to
     */
    public NeuralNetworkGene(double value, double minMutationValue, double maxMutationValue) {
        this.maxMutationValue = maxMutationValue;
        this.value = value;
        this.minMutationValue = minMutationValue;
    }

    public double getValue() {
        return value;
    }

    public double getMinMutationValue() {
        return minMutationValue;
    }

    public double getMaxMutationValue() {
        return maxMutationValue;
    }

    /**
     * Creates an exact copy of this object.
     *
     * @return An exact copy of this object.
     */
    public NeuralNetworkGene copy() {
        return new NeuralNetworkGene(value, minMutationValue, maxMutationValue);
    }

    /**
     * Mutates the given gene, i.e. changes the value to a random double in the range [minMutationValue, maxMutationValue])
     */
    public void mutate() {
        value = Math.random() * (maxMutationValue - minMutationValue) + minMutationValue;
    }

    @Override
    public String toString(){
        return Double.toString(value);
    }
}
