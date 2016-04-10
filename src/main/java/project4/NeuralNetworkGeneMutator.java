package project4;

public class NeuralNetworkGeneMutator {
    private final int numberOfMutations;
    private final double mutateThreshold;

    public NeuralNetworkGeneMutator(double mutateThreshold, int numberOfMutations) {
        this.mutateThreshold = mutateThreshold;
        this.numberOfMutations = numberOfMutations;
    }

    public NeuralNetworkGene[] mutate(NeuralNetworkGene[] neuralNetworkGenes) {
        if (Math.random() > mutateThreshold) {
            return neuralNetworkGenes;
        }

        for (int i = 0; i < numberOfMutations; i++) {
            int position = (int) (Math.random()*neuralNetworkGenes.length);
            neuralNetworkGenes[i].mutate();
        }

        return neuralNetworkGenes;
    }
}
