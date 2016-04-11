package project3;

import org.nd4j.linalg.api.ndarray.INDArray;

public class FlatlandNeuralNetwork extends SigmoidNeuralNetwork{
    private MoveDirection[] dirs = {MoveDirection.LEFT, MoveDirection.FORWARD, MoveDirection.RIGHT};

    protected FlatlandNeuralNetwork(int[] topology, double[] weight) {
        super(topology, weight, true);
    }

    private int getMaxOutput(){
        INDArray outputLayer = activations[activations.length-1];

        int maxIndex = -1;
        double maxOutput = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < outputLayer.length(); i++){
            double output_i = outputLayer.getDouble(i);

            if (output_i > maxOutput) {
                maxIndex = i;
                maxOutput = output_i;
            }
        }

        return maxIndex;
    }

    private MoveDirection getMoveFromMaxIndex(int index){
        return dirs[index];
    }

    public MoveDirection getNextMove(boolean[] food, boolean[] poison) {
        // Set activations on input neurons from senses
        for (int i = 0; i < 3; i++){
            double activationLevel = food[i] ? 1 : 0;
            setInput(i, activationLevel);
        }

        for (int i = 0; i < 3; i++){
            double activationLevel = poison[i] ? 1 : 0;
            setInput(i+3, activationLevel);
        }

        propagate();

        return getMoveFromMaxIndex(getMaxOutput());
    }
}