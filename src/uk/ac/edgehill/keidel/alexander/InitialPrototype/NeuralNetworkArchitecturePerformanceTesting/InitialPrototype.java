package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting;

import org.neuroph.core.learning.LearningRule;
import org.neuroph.util.TransferFunctionType;

import java.util.ArrayList;

/**
 * Created by Alexander Keidel, 22397868 on 15/06/2016.
 * Initial prototype of my masters project application.
 * The idea is to have a testing framework to decide on the best suited artificial neural network architecture for any
 * given data set.
 */
public class InitialPrototype implements GlobalVariablesInterface {
    public NeuralNetworkArchitectureTester cp = new NeuralNetworkArchitectureTester();


    public void startPrototype(){
        String workingDirectory = System.getProperty("user.dir");
        workingDirectory = workingDirectory.replace("\\", "/"); //replace all backward slashes with forward slashes to be used for file paths. See http://stackoverflow.com/questions/1701839/string-replaceall-single-backslashes-with-double-backslashes and http://stackoverflow.com/questions/4871051/getting-the-current-working-directory-in-java
        System.out.println("Working Directory = " + workingDirectory);

        NeuralNetworkSettingsListGenerator generator = new NeuralNetworkSettingsListGenerator(); //careful using this! possible null pointers, only for debugging!
        cp.createAndTestNeuralNetworkStructures("test set name", "training set name", 5, 1, 5, generator.getAllPossibleTransferFunctions(), generator.getAllPossibleLearningRules(), DEFAULT_PERFORMANCE_REQUIERD_MINIMUM);

        /**
         * See http://stackoverflow.com/questions/2914375/getting-file-path-in-java for saving in current file location
         */

        try {
            cp.saveCurrentNetworkSettingsToFile();
            cp.saveCurrentPerceptronToFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}