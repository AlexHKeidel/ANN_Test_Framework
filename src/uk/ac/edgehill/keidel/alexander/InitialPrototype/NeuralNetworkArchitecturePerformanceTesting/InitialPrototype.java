package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting;

import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions.LearningRuleNotFoundException;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions.TransferFunctionNotFoundException;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface.TestingPreferences;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alexander Keidel, 22397868 on 15/06/2016.
 * Initial prototype of my masters project application.
 * The idea is to have a testing framework to decide on the best suited artificial neural network architecture for any
 * given data set.
 */
public class InitialPrototype implements GlobalVariablesInterface, Runnable {
    public static NeuralNetworkArchitectureTester neuralNetworkArchitectureTester = new NeuralNetworkArchitectureTester();
    public static TestingPreferences testingPreferences = new TestingPreferences();

    private void startPrototype(){
        String workingDirectory = System.getProperty("user.dir");
        workingDirectory = workingDirectory.replace("\\", "/"); //replace all backward slashes with forward slashes to be used for file paths. See http://stackoverflow.com/questions/1701839/string-replaceall-single-backslashes-with-double-backslashes and http://stackoverflow.com/questions/4871051/getting-the-current-working-directory-in-java
        System.out.println("Working Directory = " + workingDirectory);

        NeuralNetworkSettingsListGenerator generator = new NeuralNetworkSettingsListGenerator(); //careful using this! possible null pointers, only for debugging!
        File trainingSet = new File(System.getProperty("user.dir") + "/Data Sets/Bi3 Data Sets/Demographics Training Set");
        File testSet = new File(System.getProperty("user.dir") + "/Data Sets/Bi3 Data Sets/Demographics Test Set");
        //neuralNetworkArchitectureTester.createAndTestNeuralNetworkStructures(trainingSet, testSet,"Test Set Not Yet Generated!", "Supervised Demographics Data", 4, 1, 5, generator.getAllPossibleTransferFunctions(), generator.getAllPossibleLearningRules(), DEFAULT_PERFORMANCE_REQUIERD_MINIMUM);

        try {
            neuralNetworkArchitectureTester.trainAndTestNeuralNetworkStructures(trainingSet, testSet, "baseName Test", 4, 1, testingPreferences.getMaximumHiddenLayers(), 1, testingPreferences.getMaximumHiddenLayerSize(), testingPreferences.getTransferFunctions(), testingPreferences.getLearningRules(), testingPreferences.getPerformance());
        } catch (LearningRuleNotFoundException le){
            le.printStackTrace();
        } catch (TransferFunctionNotFoundException te){
            te.printStackTrace();
        }
        /**
         * See http://stackoverflow.com/questions/2914375/getting-file-path-in-java for saving in current file location
         */
//        try {
//            neuralNetworkArchitectureTester.saveCurrentNetworkSettingsToFile();
//            neuralNetworkArchitectureTester.saveCurrentPerceptronToFile();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    @Override
    public void run() {
        startPrototype();
    }
}