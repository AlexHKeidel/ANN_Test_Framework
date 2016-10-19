package uk.ac.edgehill.keidel.alexander.InitialPrototype;

import org.neuroph.nnet.MultiLayerPerceptron;

/**
 * Created by Alexander Keidel, 22397868 on 15/06/2016.
 */
public class InitialPrototype implements GlobalVariablesInterface{
    public static void main(String[] args){
        CustomPerceptron cp = new CustomPerceptron();
        cp.createAndTestNeuralNetworkStructures(5,1, "trainingset name", "testset name", DEFAULT_PERFORMANCE_REQUIERD_MINIMUM);
    }
}