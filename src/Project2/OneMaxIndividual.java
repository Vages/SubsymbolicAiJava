package Project2;

import java.util.Arrays;

public class OneMaxIndividual extends Individual{
    private int[] genotype;

    public OneMaxIndividual(int[] genotype) {
        this.genotype = genotype;
    }

    @Override
    public int[] getPhenotype() {
        return this.getGenotype();
    }

    @Override
    public void develop() {

    }

    public int[] getGenotype() {
        int [] a = new int[genotype.length];
        System.arraycopy(genotype, 0, a, 0, genotype.length);
        return a;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.genotype);
    }


}
