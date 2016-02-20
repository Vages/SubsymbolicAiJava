package Project2;

import java.util.ArrayList;

public abstract class EvolutionWorld {
    protected ArrayList<Individual> children, adults;

    public EvolutionWorld() {
        children = new ArrayList<>();
        adults = new ArrayList<>();
    }

    public void oneRoundOfEvolution(){
        developChildren();
        assessFitness();
        selectAdults();
        ageBasedFiltering();
        parentSelection();
        genotypeCopying();
        reproduction();
    }

    protected void developChildren() {
        for (Individual c: children) {
            c.develop();
        }
    }

    protected void assessFitness() {
        for (Individual c: children) {
            c.getFitness();
        }
    }

    protected void selectAdults() {
        for (Individual c: children){
            adults.add(c);
        }
    }

    protected void ageBasedFiltering(){

    }

    protected abstract void parentSelection();

    protected abstract void genotypeCopying();

    protected abstract void reproduction();
}
