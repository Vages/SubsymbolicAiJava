package Project2;

import java.util.ArrayList;
import java.util.Map;

public abstract class EvolutionWorld {
    protected ArrayList<Individual>
            undevelopedChildren,
            developedChildren;
    protected Map<Individual, Double> fitnessTestedChildren, fitnessTestedAdults;

    public void oneRoundOfEvolution(){
        developChildren();
        testFitness();
        selectAdults();
        ageBasedFiltering();
        parentSelection();
        genotypeCopying();
        reproduction();
    }

    protected void developChildren() {
        for (Individual c: undevelopedChildren) {
            c.develop();
            developedChildren.add(c);
        }
    };

    protected abstract void testFitness();

    protected abstract void selectAdults();

    protected abstract void ageBasedFiltering();

    protected abstract void parentSelection();

    protected abstract void genotypeCopying();

    protected abstract void reproduction();
}
