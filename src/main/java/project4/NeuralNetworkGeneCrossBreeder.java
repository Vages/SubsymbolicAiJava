package project4;

public class NeuralNetworkGeneCrossBreeder {
    double crossingRate;

    public NeuralNetworkGeneCrossBreeder(double crossingRate) {
        this.crossingRate = crossingRate;
    }

    public NeuralNetworkGene[][] crossBreed(NeuralNetworkGene[] genotypeA, NeuralNetworkGene[] genotypeB) {
        int genotypeLength = genotypeA.length;

        NeuralNetworkGene[] childAGenotype = new NeuralNetworkGene[genotypeLength];
        NeuralNetworkGene[] childBGenotype = new NeuralNetworkGene[genotypeLength];

        if (Math.random() < crossingRate) {
            for (int i = 0; i < genotypeLength; i++) {
                childAGenotype[i] = genotypeA[i].copy();
                childBGenotype[i] = genotypeB[i].copy();

            }

            return new NeuralNetworkGene[][]{childAGenotype, childBGenotype};
        }

        int crossingPoint = (int) (Math.random()*genotypeLength);

        for (int i = 0; i < genotypeLength; i++) {
            if (i < crossingPoint) {
                childAGenotype[i] = genotypeA[i].copy();
                childBGenotype[i] = genotypeB[i].copy();
            } else {
                childAGenotype[i] = genotypeB[i].copy();
                childBGenotype[i] = genotypeA[i].copy();
            }
        }

        return new NeuralNetworkGene[][]{childAGenotype, childBGenotype};
    }
}
