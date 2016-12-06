package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting;

/**
 * Created by Alexander Keidel, 22397868 on 15/06/2016.
 * Initial prototype of my masters project application.
 * The idea is to have a testing framework to decide on the best suited artificial neural network architecture for any
 * given data set.
 */
public class InitialPrototype implements GlobalVariablesInterface{
    public static void main(String[] args){
        CustomPerceptron cp = new CustomPerceptron();
        cp.createAndTestNeuralNetworkStructures(5,1, "trainingset name", "testset name", EXPECTED_STANDARD_DEVIATION_LIMIT);
        /**
         * See http://stackoverflow.com/questions/2914375/getting-file-path-in-java for saving in current file location
         */

        try {
            cp.saveCurrentNetworkSettingsToFile();
            cp.saveCurrentPerceptronToFile();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}