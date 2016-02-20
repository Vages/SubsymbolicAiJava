package Project2;

public abstract class Individual {
    public abstract int[] getGenotype();

    public abstract int[] getPhenotype();

    public abstract double getFitness();

    public abstract void develop();
}
