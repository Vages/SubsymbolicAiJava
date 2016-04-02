package project2;

public abstract class Individual<G, P> implements Comparable<Individual> {
    protected G[] genotype;

    public abstract G[] getGenotype();

    public abstract P getPhenotype();

    public abstract double getFitness();

    public abstract void develop();

    @Override
    public int compareTo(Individual o) {
        double otherObjectFitness = o.getFitness();
        return (int) Math.signum(this.getFitness()-otherObjectFitness);
    }
}