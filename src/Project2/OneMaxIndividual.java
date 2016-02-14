package Project2;

import java.util.Arrays;

public class OneMaxIndividual extends Individual{
    private int[] genotype;

    public OneMaxIndividual(int[] genotype) {
        this.genotype = genotype;
    }

    @Override
    public int[] getPhenotype() {
        int [] a = new int[genotype.length];
        System.arraycopy(genotype, 0, a, 0, genotype.length);
        return a;
    }

    public int[] getGenotype() {
        return this.getPhenotype();
    }

    @Override
    public String toString() {
        return Arrays.toString(this.genotype);
    }
}
