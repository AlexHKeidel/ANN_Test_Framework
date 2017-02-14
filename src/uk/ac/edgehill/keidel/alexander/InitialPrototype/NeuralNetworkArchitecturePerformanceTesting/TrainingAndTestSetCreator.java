package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting;

import java.io.*;
import java.util.Random;

/**
 * Created by Alexander Keidel, 22397868 on 16/06/2016.
 * Creates custom training and test sets based on pseudo random number generation
 */
public class TrainingAndTestSetCreator implements GlobalVariablesInterface {
    //private final String PATH = "C:/Users/Borgelman/SkyDrive/Edge Hill/Final Year Project/NeurophPrototypes/";
    //private final String FILE_NAME = "Randomly Generated Training Data.csv";
    private final int DEFAULT_LINES = 10000;
    public TrainingAndTestSetCreator(){
        try {
            createFile();
        } catch (IOException e) {
            //System.out.println("Failed creating test set file.");
            e.printStackTrace();
        }
    }

    /**
     * Create the default training and testing files for the initial prototype custom perceptron tester.
     * 5 Input values, 1 output value (always 1.0)
     * Every tenth generated entry for the training set is added to the test set, which is used in {@link NeuralNetworkArchitectureTester#createAndTestNeuralNetworkStructures(int, int, String, String, float)}
     * @return
     * @throws IOException
     */
    private boolean createFile() throws IOException {
        System.out.println("Started creating test set");
        Random randy = new Random();
        //In order to simulate normalised data for a neural network use float values between -1 or 0 and 1
        String trainingData = "";
        String testData = "";

        int counter = 0;
        while(counter < DEFAULT_LINES){
            String tmp = "";
            for(int i = 0; i < 5; i++){
                tmp += String.valueOf(randy.nextFloat()) + ","; //add a random float value plus a comma separator
            }

            trainingData += tmp + "1.0\n"; //add a supervised training data value for 1, as in "expected output" to train the network with
            counter++;
            if(counter % 10 == 0){ //ever 10 increments add the value to the test set, generating a test set with every tenth value, thus 1/10 of the size of the training set
                testData += tmp + "1.0\n"; //add a supervised training data value for 1, as in "expected output" to train the network with
            }
        }
        File trainingFile = new File(DEFAULT_FILE_PATH + DEFAULT_TRAINING_SET_NAME);
        FileWriter fw = new FileWriter(trainingFile);
        fw.write(trainingData);
        fw.flush();
        //fw.close();
        //System.out.println("Finished creating test set.");

        System.out.println("Writing test set.");
        File testFile = new File(DEFAULT_FILE_PATH + DEFAULT_TEST_SET_NAME);
        fw = new FileWriter(testFile);
        fw.write(testData);
        fw.flush();
        fw.close();
        System.out.println("Done writing training and test set to files.");
        return true; //success
    }

    public static void main(String[] args){
        TrainingAndTestSetCreator t = new TrainingAndTestSetCreator();
    }
}
