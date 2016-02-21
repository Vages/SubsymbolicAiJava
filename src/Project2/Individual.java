package project2;

public abstract class Individual implements Comparable<Individual> {
    public abstract int[] getGenotype();

    public abstract int[] getPhenotype();

    public abstract double getFitness();

    public abstract void develop();

    @Override
    public int compareTo(Individual o) {
        double otherObjectFitness = o.getFitness();
        return (int) Math.signum(this.getFitness()-otherObjectFitness);
    }
}
