package uk.ac.edgehill.keidel.alexander.InitialPrototype;

/**
 * Created by Alexander Keidel, 22397868 on 21/09/2016.
 */
public interface GlobalVariablesInterface { //all values contained here are "public static final"
    String DEFAULT_FILE_PATH = "D:/OneDrive/Edge Hill/Final Year Project/NeurophPrototypes/";
            //"C:/Users/Alex/OneDrive/Edge Hill/Final Year Project/NeurophPrototypes/";
            // make sure this coincides with the project path on the machine!
    String DEFAULT_TEST_SET_NAME = "Default Perceptron Test Set.csv";
    String DEFAULT_TRAINING_SET_NAME = "Default Perceptron Training Set.csv";
    float DEFAULT_PERFORMANCE_REQUIERD_MINIMUM = 0.9999f;
    float EXPECTED_STANDARD_DEVIATION_LIMIT = 0.00001f;
    String DEFAULT_SEPARATOR = ",";
}
