package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting;

import javafx.util.Pair;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alexander Keidel, 22397868 on 15/06/2016.
 * Representing the architecture of a neural network based on multi-layered Perceptrons.
 * This includes the number of input, output and hidden layer neurons, transfer function, learning rule as well as
 * a performance score measured in standard deviation.
 * Further variables include the Neuroph neural network itself, a training set, a test set, an overfitting test set or
 * secondary / development test set, and arraylists of pairs representing the performances on both the test set and overfitting set.
 * Also contains a neural network object which will be trained by the {@link NeuralNetworkArchitectureTester}.
 */
public class NeuralNetworkSettings implements Serializable, Runnable {
    private String name;
    private int inputNeurons;
    private int outputNeurons;
    private ArrayList<Integer> hiddenLayers;
    private TransferFunctionType transferFunctionType;
    private LearningRule learningRule;
    private double performanceScore = 1.0f; //standard deviation
    private NeuralNetwork neuralNetwork;
    private TrainingSet trainingSet;
    private TrainingSet<SupervisedTrainingElement> testSet;
    private TrainingSet<SupervisedTrainingElement> overfittingTestSet;
    private ArrayList<Pair<Integer, Double>> testSetPerformances = new ArrayList<>();
    private ArrayList<Pair<Integer, Double>> overfittingTestSetPerformances = new ArrayList<>();

    private static StringBuffer parentStrDump;

    public NeuralNetworkSettings(){}

    @Deprecated
    public NeuralNetworkSettings(String name, int inputNeurons, int outputNeurons, ArrayList<Integer> hiddenLayers, TransferFunctionType transferFunctionType, LearningRule learningRule){
        this.setName(name);
        this.setInputNeurons(inputNeurons);
        this.setOutputNeurons(outputNeurons);
        this.setHiddenLayers(hiddenLayers);
        this.setTransferFunctionType(transferFunctionType);
        this.learningRule = learningRule;
    }

    public NeuralNetworkSettings(String name, int inputNeurons, int outputNeurons, ArrayList<Integer> hiddenLayers, TransferFunctionType transferFunctionType, LearningRule learningRule, TrainingSet trainingSet, TrainingSet<SupervisedTrainingElement> testSet, TrainingSet<SupervisedTrainingElement> overfittingSet, StringBuffer parentBuffer){
        this.setName(name);
        this.setInputNeurons(inputNeurons);
        this.setOutputNeurons(outputNeurons);
        this.setHiddenLayers(hiddenLayers);
        this.setTransferFunctionType(transferFunctionType);
        this.learningRule = learningRule;
        this.trainingSet = trainingSet;
        this.testSet = testSet;
        this.overfittingTestSet = overfittingSet;
        this.parentStrDump = parentBuffer;
    }

    /**
     * Main feature of this class.
     * Set up the neural network based on the given parameters for neuron counts, tranfer function, learning rule, training and test sets etc.
     * Trains the neural network, providing insight into performance along the way.
     */
    private void trainNeuralNetworkWithSettings(){
        //init neural network
        ArrayList<Integer> neuronCountInLayers = new ArrayList<>();
        neuronCountInLayers.add(inputNeurons); //add input neurons
        for(int layer : hiddenLayers){
            neuronCountInLayers.add(layer); //add hidden layers
        }
        neuronCountInLayers.add(outputNeurons); //add output neurons
        neuralNetwork = new MultiLayerPerceptron(neuronCountInLayers, transferFunctionType); //set up multi-layered perceptron
        neuralNetwork.setLearningRule(learningRule); //set learning rule
        //System.out.println(name + " training started");
        //neuralNetwork.learn(trainingSet); //learn the training set (without a new thread)
        int iterationCounter = 0;
        neuralNetwork.randomizeWeights(); //randomise all weights
        testNeuralNetworkPerformance(iterationCounter); //initial test before training
        iterationCounter++;
        neuralNetwork.learnInNewThread(trainingSet);
        //neuralNetwork.pauseLearning();
        int localMaximumCounter = 0;
        while(iterationCounter < 1000){
            if(localMaximumCounter > 50) break; //break loop after the performance has not changed a certain amount of times
            //System.out.println("counter = " + iterationCounter);
            neuralNetwork.pauseLearning();
            localMaximumCounter += testNeuralNetworkPerformance(iterationCounter);
            neuralNetwork.resumeLearning();
            iterationCounter++;
        }
        //System.out.println(convertNeuralNetworkSettingsToReadableString());
        parentStrDump.append(convertNeuralNetworkSettingsToReadableString() + "\n");
        for(int i = 0; i < testSetPerformances.size(); i++){
            Pair<Integer, Double> testPair = testSetPerformances.get(i);
            Pair<Integer, Double> overfittingPair = overfittingTestSetPerformances.get(i);
            //System.out.println("test#" + testPair.getKey() + " = " + testPair.getValue());
            //System.out.println("over#" + overfittingPair.getKey() + " = " + overfittingPair.getValue());
        }
    }

    /**
     * Test the neural network performance on both the test set and the overfitting set, adding the respective
     * performance values to the correct array lists {@link #testSetPerformances} and {@link #overfittingTestSetPerformances}.
     * @param iteration Iteration counter used in storing the test performances as the key (in the key-value pairs).
     *     */
    private int testNeuralNetworkPerformance(final int iteration){
        int localMaximum = 0;
        //START Test set performance
        ArrayList<ArrayList<Double>> testSetOutputValues = new ArrayList<>();
        ArrayList<ArrayList<Double>> testSetActualOutputs = new ArrayList<>();
        for(int i = 0; i < testSet.elements().size(); i++){ //for each element in the test set
            neuralNetwork.setInput(testSet.elementAt(i).getInput()); //set the input up based on the values in the test set
            neuralNetwork.calculate(); //calculate the result based on the test set
            ArrayList<Double> outputs = new ArrayList<>();
            ArrayList<Double> actualOutputs = new ArrayList<>();
            for(int j = 0; j < neuralNetwork.getOutputNeurons().size(); j++){ //for every output neuron
                outputs.add(neuralNetwork.getOutput()[j]); //get neural network output
                actualOutputs.add(testSet.elements().get(i).getDesiredOutput()[j]); //get actual output from test set
            }
            testSetOutputValues.add(outputs);
            testSetActualOutputs.add(actualOutputs);
        }
        double testsetPerformance = calculateTotalMeanSquareError(testSetOutputValues, testSetActualOutputs); //calculate the performance score of this neural network
        //System.out.println(name + " testsetPerformance = " + testsetPerformance);
        if(performanceScore == testsetPerformance){
            localMaximum++; //local maximum detected
        } else {
            performanceScore = testsetPerformance;
        }

        testSetPerformances.add(new Pair<Integer, Double> (iteration, testsetPerformance)); //add new pair to performances
        //myscreen.updateTestSeries(testSetPerformances.get(iteration));
        //myscreen.updateTestSeries(new Pair<Integer, Double> (iteration, testsetPerformance));
        //System.out.println(convertNeuralNetworkSettingsToReadableString());
        //parentStrDump.append(convertNeuralNetworkSettingsToReadableString() + "\n");
        //END Test set performance

        //START Overfitting set performance
        ArrayList<ArrayList<Double>> overfittingTestSetOutputValues = new ArrayList<>();
        ArrayList<ArrayList<Double>> overfittingTestSetActualOutputs = new ArrayList<>();
        for(int i = 0; i < overfittingTestSet.elements().size(); i++){ //for each element in the test set
            neuralNetwork.setInput(overfittingTestSet.elementAt(i).getInput()); //set the input up based on the values in the test set
            neuralNetwork.calculate(); //calculate the result based on the test set
            ArrayList<Double> outputs = new ArrayList<>();
            ArrayList<Double> actualOutputs = new ArrayList<>();
            for(int j = 0; j < neuralNetwork.getOutputNeurons().size(); j++){ //for every output neuron
                outputs.add(neuralNetwork.getOutput()[j]); //get neural network output
                actualOutputs.add(overfittingTestSet.elements().get(i).getDesiredOutput()[j]); //get actual output from test set
            }
            overfittingTestSetOutputValues.add(outputs);
            overfittingTestSetActualOutputs.add(actualOutputs);
        }
        double overfittingTestSetPerformance = calculateTotalMeanSquareError(overfittingTestSetOutputValues, overfittingTestSetActualOutputs);
        //System.out.println(name + " overfittingTestSetPerformance = " + overfittingTestSetPerformance);
        overfittingTestSetPerformances.add(new Pair<Integer, Double> (iteration, overfittingTestSetPerformance)); //add new pair to performances
        //myscreen.updateTestSeries(testSetPerformances.get(iteration));
        //myscreen.updateTestSeries(new Pair<Integer, Double> (iteration, testsetPerformance));
        //System.out.println(convertNeuralNetworkSettingsToReadableString());
        //parentStrDump.append(convertNeuralNetworkSettingsToReadableString() + "\n");
        //END Overfitting set performance
        return localMaximum; //0, no local maximum detected
    }

    /**
     * Create a human readable string from the information contained in this object. This can be printed to the console
     * or some GUI.
     * @return a human readable string format representing the architecture of the neural network contained in this class.
     * This can be used to print to console or an interface supporting rich text format (rtf) or similar styles
     */
    public String convertNeuralNetworkSettingsToReadableString(){
        String s = "";
        s += "Neural Network Settings\n" + "Name: " + getName() + "\nPerformance Score (MSE): " + getPerformanceScore() + "\nInput Neurons: " + getInputNeurons() + "\n";
        String tmp = "";
        for (int i : getHiddenLayers()) {
            tmp += "(" + i + ")";
        }
        s += "Hidden Layers: " + tmp + "\nOutput Neurons: " + getOutputNeurons() + "\nTransfer Function: " + getTransferFunctionType().getTypeLabel() + "\nLearning Rule: " + getLearningRule().getClass().getSimpleName() + "\n";
        return s;
    }

    /**
     * Calculate the standard deviation of the  array of doubles
     * see http://libweb.surrey.ac.uk/library/skills/Number%20Skills%20Leicester/page_19.htm
     * @param v
     * @return
     */
    private double calculateStandardDeviation(double [] v) {
        double mean = calculateAverage(v);
        double sum = 0.0f;
        for(double d : v) { //adding the sum of all values minus the mean squared, as in (x - m)^2 where x = the value and m = the mean / average
            sum += Math.pow(d - mean, 2);
        }
        sum /= (v.length); //divide the sum by the number of values
        return  Math.sqrt(sum); //retrieve the square root of the sum which is known as standard deviation and return it
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
     * Calculate the average total mean square error of a total set of outputs from a neural network against the desired outputs from the test set.
     * @param outputs Output values of the trained neural network when applied to a test set
     * @param actualOutputs Output values from the test set
     * @return Total mean squared error rate across the dataset
     */
    private double calculateTotalMeanSquareError(ArrayList<ArrayList<Double>> outputs, ArrayList<ArrayList<Double>> actualOutputs){
        try{
            double mse = 0.0;
            for(int i = 0; i < outputs.size(); i++){ //for each output
                double errorAverage = 0.0;
                for(int j = 0; j < outputs.get(i).size(); j++){ //for each value (output neurons)
                    double error = actualOutputs.get(i).get(j) - outputs.get(i).get(j); //get the error
                    errorAverage += Math.pow(error, 2); //square the error
                }
                errorAverage = errorAverage / (outputs.get(i).size()); //divide by the number of output neurons
                mse += errorAverage; //add up to the total mean squared error
            }
            mse = mse / (outputs.size()); //divide by the number of rows to get the mean of all mean errors squared
            return mse;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return -1.0; //error
    }

    /**
     * Generates a short-hand readable summary of the {@link NeuralNetworkSettings} object.
     * @return Human-readable summary of the neural network structure to print to console or GUI.
     */
    public String getReadableShortName(){
        String readable = getName() + " " + getInputNeurons();
        for(int h : hiddenLayers){
            readable += "(" + h +")";
        }
        readable += outputNeurons + " " + getTransferFunctionType().getTypeLabel() + " " + getLearningRule().getClass().getSimpleName();
        return readable;
    }

    public int getInputNeurons() {
        return inputNeurons;
    }

    public void setInputNeurons(int inputNeurons) {
        this.inputNeurons = inputNeurons;
    }

    public int getOutputNeurons() {
        return outputNeurons;
    }

    public void setOutputNeurons(int outputNeurons) {
        this.outputNeurons = outputNeurons;
    }

    public ArrayList<Integer> getHiddenLayers() {
        return hiddenLayers;
    }

    public void setHiddenLayers(ArrayList<Integer> hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
    }

    public TransferFunctionType getTransferFunctionType() {
        return transferFunctionType;
    }

    public void setTransferFunctionType(TransferFunctionType transferFunctionType) {
        this.transferFunctionType = transferFunctionType;
    }

    public double getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(double performanceScore) {
        this.performanceScore = performanceScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LearningRule getLearningRule() {
        return learningRule;
    }

    public void setLearningRule(LearningRule learningRule) {
        this.learningRule = learningRule;
    }

    @Override
    public void run() {
        trainNeuralNetworkWithSettings();
    }

    public ArrayList<Pair<Integer, Double>> getTestSetPerformances() {
        return testSetPerformances;
    }

    public void setTestSetPerformances(ArrayList<Pair<Integer, Double>> testSetPerformances) {
        this.testSetPerformances = testSetPerformances;
    }

    public ArrayList<Pair<Integer, Double>> getOverfittingTestSetPerformances() {
        return overfittingTestSetPerformances;
    }

    public void setOverfittingTestSetPerformances(ArrayList<Pair<Integer, Double>> overfittingTestSetPerformances) {
        this.overfittingTestSetPerformances = overfittingTestSetPerformances;
    }

}
