package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting;

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

    public NeuralNetworkSettings(String name, int inputNeurons, int outputNeurons, ArrayList<Integer> hiddenLayers, TransferFunctionType transferFunctionType, LearningRule learningRule, TrainingSet trainingSet, TrainingSet<SupervisedTrainingElement> testSet){
        this.setName(name);
        this.setInputNeurons(inputNeurons);
        this.setOutputNeurons(outputNeurons);
        this.setHiddenLayers(hiddenLayers);
        this.setTransferFunctionType(transferFunctionType);
        this.learningRule = learningRule;
        this.trainingSet = trainingSet;
        this.testSet = testSet;
    }

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
        neuralNetwork.learn(trainingSet); //learn the training set

        ArrayList<Double> outputValues = new ArrayList<>();
        for(int i = 0; i < testSet.elements().size(); i++){ //for each element in the test set
            neuralNetwork.setInput(testSet.elementAt(i).getInput()); //set the input up based on the values in the test set
            neuralNetwork.calculate(); //calculate the result based on the test set
            outputValues.add(neuralNetwork.getOutput()[0]); //get the output
        }
        performanceScore = calculateStandardDeviation(outputValues); //calculate the performance score of this neural network
        System.out.println("My performance score is = " + performanceScore);
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
}
