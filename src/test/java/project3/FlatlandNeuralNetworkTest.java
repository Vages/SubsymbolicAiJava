package project3;

import junit.framework.Assert;
import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;

import static org.junit.Assert.*;

public class FlatlandNeuralNetworkTest {

    @Test
    public void testPropagate() throws Exception {
        FlatlandNeuralNetwork simpleNetwork = new FlatlandNeuralNetwork(new int[]{3, 3}, new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1});

        simpleNetwork.setInput(0, 1);
        simpleNetwork.setInput(1, 1);
        simpleNetwork.setInput(2, 1);

        simpleNetwork.propagate();

        assertEquals(Nd4j.create(new double[]{3, 3, 3}, new int[]{3}), simpleNetwork.getOutputs());
    }
}