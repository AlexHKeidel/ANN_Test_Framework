package uk.ac.edgehill.keidel.alexander.InitialPrototype;

import org.neuroph.nnet.MultiLayerPerceptron;

/**
 * Created by Alexander Keidel, 22397868 on 15/06/2016.
 */
public class InitialPrototype {
    public static void main(String[] args){
        CustomPerceptron cp = new CustomPerceptron();
        cp.createAndTestNeuralNetworkStructures(5,1, "trainingset name", "testset name", 0.97f);
    }
}