package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.io.FileOutputAdapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Keidel, 22397868 on 15/06/2016.
 */
public class CustomPerceptron implements GlobalVariablesInterface {
    private NeuralNetwork customPerceptron;
    private List<NeuralNetworkSettings> networkList; //list or collection of all tested networks
    private NeuralNetworkSettings currentNetworkSettings;
    private NeuralNetworkSettings bestNetworkSettings = new NeuralNetworkSettings();
    private List<NeuralNetworkSettings> networkSettingsList;
    private NeuralNetworkSettingsListGenerator neuralNetworkSettingsListGenerator;

    public CustomPerceptron(){
    }

    /**
     * Creates and tests different structures of multilayered Perceptrons against one another, with the specified training and testing sets.
     * The best result will be returned by the method.
     * @param inputNeuronCount
     * @param outputNeuronCount
     * @param trainingSetName
     * @param performanceLimit
     * @return
     */
    public NeuralNetworkSettings createAndTestNeuralNetworkStructures(int inputNeuronCount, int outputNeuronCount, String trainingSetName, String testSetName, float performanceLimit){
        /**
        //BEGIN "Setting initial Testing values"
        ArrayList<Integer> hiddenLayers = new ArrayList<>();
        hiddenLayers.add(0,inputNeuronCount); //initial hidden layers
        TransferFunctionType tft = TransferFunctionType.GAUSSIAN;
        LearningRule lrnRule = new BackPropagation();
        currentNetworkSettings = new NeuralNetworkSettings("Initial Settings", inputNeuronCount, outputNeuronCount, hiddenLayers, tft, lrnRule); //
        TrainingSet currentTrainingSet = TrainingSet.createFromFile(DEFAULT_FILE_PATH + DEFAULT_TRAINING_SET_NAME, inputNeuronCount, outputNeuronCount, ","); //setting training set location
        TrainingSet<SupervisedTrainingElement> currentTestSet = TrainingSet.createFromFile(DEFAULT_FILE_PATH + DEFAULT_TEST_SET_NAME, inputNeuronCount, outputNeuronCount, ","); //setting testing set with file location
        performanceLimit = DEFAULT_PERFORMANCE_REQUIERD_MINIMUM;
        //END
         */
        neuralNetworkSettingsListGenerator = new NeuralNetworkSettingsListGenerator(testSetName, inputNeuronCount, outputNeuronCount); //a new settings generator with the specified values
        ArrayList<NeuralNetworkSettings> allSettings = neuralNetworkSettingsListGenerator.getNeuralNetworkList();

        int networkCounter = 0;
        ArrayList<Integer> hiddenLayers = new ArrayList<>();
        TransferFunctionType tft;
        LearningRule lrnRule;
        TrainingSet currentTrainingSet = TrainingSet.createFromFile(DEFAULT_FILE_PATH + DEFAULT_TRAINING_SET_NAME, inputNeuronCount, outputNeuronCount, DEFAULT_SEPARATOR); //setting training set location
        TrainingSet<SupervisedTrainingElement> currentTestSet = TrainingSet.createFromFile(DEFAULT_FILE_PATH + DEFAULT_TEST_SET_NAME, inputNeuronCount, outputNeuronCount, ","); //setting testing set with file location
        do{
            try{
                hiddenLayers = allSettings.get(networkCounter).getHiddenLayers();
                tft = allSettings.get(networkCounter).getTransferFunctionType();
                lrnRule = allSettings.get(networkCounter).getLearningRule();
                currentNetworkSettings = new NeuralNetworkSettings(testSetName + " #" + networkCounter, inputNeuronCount, outputNeuronCount, hiddenLayers, tft, lrnRule);

            } catch (Exception e){
                e.printStackTrace();
                System.out.println("No more possible settings to test.");
                try{
                    System.out.print("\nFinished training and testing network.\n\nFinal result:\nBest found neural network settings provide a correctness level of " + bestNetworkSettings.getPerformanceScore() +"%.");
                    if(bestNetworkSettings.getPerformanceScore() == 0.0f){
                        System.out.println("No network was tested. Please report this error.");
                        return null;
                    }
                    return bestNetworkSettings;
                } catch (Exception innerE){
                    innerE.printStackTrace();
                }
            }
            //adding the integer values defining the layers of the perceptron into an arraylist
            ArrayList<Integer> neuronCountInLayers = new ArrayList<>();
            neuronCountInLayers.add(inputNeuronCount);
            for(int layer : hiddenLayers){ //adding hidden layers (same count as input layers)
                neuronCountInLayers.add(layer);
            }
            neuronCountInLayers.add(outputNeuronCount); //adding output layers
            customPerceptron = new MultiLayerPerceptron(neuronCountInLayers, currentNetworkSettings.getTransferFunctionType()); //create a custom multi layered perceptron with the given input, hidden and output layers, as well as transfer function
            customPerceptron.setLearningRule(currentNetworkSettings.getLearningRule()); //setting learning rule
            customPerceptron.learn(currentTrainingSet); //training network with the training set
            ArrayList<Double> tmpvalues = new ArrayList<Double>();
            for(int i = 0; i < currentTestSet.elements().size(); i ++){
                customPerceptron.setInput(currentTestSet.elementAt(i).getInput()); //set input values
                customPerceptron.calculate(); //calculate result
                tmpvalues.add(customPerceptron.getOutput()[0]); //add the result to the list of doubles
            }
            currentNetworkSettings.setPerformanceScore(calculateStandardDeviation(tmpvalues));
            System.out.println("Required Standard Deviation = " + performanceLimit + "\n" + convertNeuralNetworkSettingsToReadableString(currentNetworkSettings));
            if(currentNetworkSettings.getPerformanceScore() < bestNetworkSettings.getPerformanceScore())bestNetworkSettings = currentNetworkSettings; //replace the best network settings
            networkCounter++; //incrementing the network counter
        }
        while(bestNetworkSettings.getPerformanceScore() > performanceLimit); //while the best performance is under the specified limit (default at 97%) keep testing network structures
        //System.out.print("\nFinished training and testing network.\n\nFinal result:\nBest found neural network settings provide a correctness level of " + bestNetworkSettings.getPerformanceScore() +"%.");
        System.out.println("\nFinished training and testing networks. Best result is the following architecture:\n");
        System.out.print(convertNeuralNetworkSettingsToReadableString(bestNetworkSettings));
        return bestNetworkSettings; //return the best found network settings.
    }

    private String convertNeuralNetworkSettingsToReadableString(NeuralNetworkSettings settings){
        String s = "";
        s += "Neural Network Settings\n" + "Name: " + settings.getName() + "\nPerformance Score (Standard Deviation): " + settings.getPerformanceScore() + "\nInput Neurons: " + settings.getInputNeurons() + "\n";
        String tmp = "";
        for (int i : settings.getHiddenLayers()) {
            tmp += "(" + i + ")";
        }
        s += "Hidden Layers: " + tmp + "\nOutput Neurons: " + settings.getOutputNeurons() + "\nTransfer Function: " + settings.getTransferFunctionType().getTypeLabel() + "\nLearning Rule: " + settings.getLearningRule().getClass().getSimpleName() + "\n";
        return s;
    }


    /**
     * See #calculateAverage(double [] v)
     * @param v
     * @return
     */
    private double calculateAverage(ArrayList<Double> v){
        double[] s = new double[v.size()];
        for(int i = 0; i < v.size(); i++){ //converting ArrayList of doubles to an array of doubles
            s[i] = v.get(i);
        }
        return calculateAverage(s);
    }

    /**
     * Calculate the average value of a an array of double values
     * @param v
     * @return
     */
    private double calculateAverage(double [] v){
        double a = 0.0f;
        for(int i = 0; i < v.length; i++){
            a += v[i];
        }
        return a / v.length;
    }

    /**
     * Calculate the standard deviation of the array list of doubles
     * @param v
     * @return
     */
    private double calculateStandardDeviation(ArrayList<Double> v) {
        double[] s = new double[v.size()];
        for(int i = 0; i < v.size(); i++){ //converting ArrayList of doubles to an array of doubles
            s[i] = v.get(i);
        }
        return calculateStandardDeviation(s);
    }

    /**
     * Calculate the standard deviation of the  array of doubles
     * see http://libweb.surrey.ac.uk/library/skills/Number%20Skills%20Leicester/page_19.htm
     * and
     * @param v
     * @return
     */
    private double calculateStandardDeviation(double [] v) {
        double mean = calculateAverage(v);
        double sum = 0.0f;
        for(double d : v) { //adding the sum of all values minues the mean squared, as in (x - m)^2 where x = the value and m = the mean / average
            sum += Math.pow(d - mean, 2);
        }
        sum /= (v.length - 1); //divide the sum by the number of values minus one
        return  Math.sqrt(sum); //retreive the square root of the sum which is known as standard deviation and return it
    }

    /**
     * Save the current network settings to a file in the specified file location
     *
     * @param fileLocation String for the file location to be written
     * @throws IOException
     */
    public void saveCurrentNetworkSettingsToFile(String fileLocation) throws IOException {
        FileOutputStream fos = new FileOutputStream(currentNetworkSettings.getName());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(currentNetworkSettings);
    }

    /**
     * Save the customPerceptron object as a file
     * @param fileLocation String for file location
     * @throws IOException
     */
    public void saveCurrentPerceptronToFile(String fileLocation) throws IOException {
        FileOutputStream fos = new FileOutputStream("customPerceptron"); //name of the current network settings name
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(customPerceptron);
    }
}
