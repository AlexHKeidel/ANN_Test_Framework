package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting;

import org.neuroph.core.learning.LearningRule;
import org.neuroph.util.TransferFunctionType;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Alexander Keidel, 22397868 on 21/09/2016.
 * Global variables used throughout the application
 */
public interface GlobalVariablesInterface { //all values contained here are "public static final"
    String DEFAULT_FILE_PATH = System.getProperty("user.dir").replace("\\", "/") + "\\"; //"D:/OneDrive/Edge Hill/Final Year Project/NeurophPrototypes/";
            //"C:/Users/Alex/OneDrive/Edge Hill/Final Year Project/NeurophPrototypes/";
            // make sure this coincides with the project path on the machine!
    String DEFAULT_TEST_SET_NAME = "Default Perceptron Test Set.csv";
    String DEFAULT_TRAINING_SET_NAME = "Default Perceptron Training Set.csv";
    String DEFAULT_PERCEPTRON_NAME = "custom perceptron";
    float DEFAULT_PERFORMANCE_REQUIERD_MINIMUM = 0.9999f; //unused since implementation of standard deviation
    float EXPECTED_STANDARD_DEVIATION_LIMIT = 0.00001f;
    String DEFAULT_SEPARATOR = ",";

    ArrayList<TransferFunctionType> TRANSFER_FUNCTION_TYPES = new NeuralNetworkSettingsListGenerator().getAllPossibleTransferFunctions();
    ArrayList<LearningRule> LEARNING_RULES = new NeuralNetworkSettingsListGenerator().getAllPossibleLearningRules();
}
