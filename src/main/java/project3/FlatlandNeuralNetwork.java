package project3;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class FlatlandNeuralNetwork {
    private INDArray inputs;
    private INDArray weights;
    private INDArray outputs;

    public FlatlandNeuralNetwork(int[] topology, double[] weights) {
        this.inputs = Nd4j.zeros(topology[0]);
        this.outputs = Nd4j.zeros(topology[1]);
        this.weights = Nd4j.create(weights, topology);
    }

    public void propagate() {
        this.outputs = inputs.mmul(weights);
    }

    public void setInput(int index, double value){
        this.inputs.putScalar(index, value);
    }

    public INDArray getOutputs() {
        return outputs;
    }
}
