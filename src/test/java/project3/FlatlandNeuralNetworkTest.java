package project3;

import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;

import static org.junit.Assert.*;

public class FlatlandNeuralNetworkTest {

    @Test
    public void testPropagate() throws Exception {
        SigmoidNeuralNetwork simpleNetwork = new SigmoidNeuralNetwork(new int[]{3, 3}, new double[]{1,1,1,1,1,1,1,1,1,1,1,1});

        simpleNetwork.setInput(0, 1);
        simpleNetwork.setInput(1, 1);
        simpleNetwork.setInput(2, 1);

        simpleNetwork.propagate();

        double sigmoid_4 = 1.0/(1+Math.pow(Math.E, -4));

        assertEquals(Nd4j.create(new double[]{sigmoid_4, sigmoid_4, sigmoid_4}, new int[]{3}), simpleNetwork.getOutputs());
    }
}