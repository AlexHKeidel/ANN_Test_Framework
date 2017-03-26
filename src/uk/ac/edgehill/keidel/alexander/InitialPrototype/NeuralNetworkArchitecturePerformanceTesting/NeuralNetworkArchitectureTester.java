package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting;

import com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl;
import com.sun.corba.se.impl.orbutil.threadpool.WorkQueueImpl;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import com.sun.corba.se.spi.orbutil.threadpool.WorkQueue;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.codehaus.groovy.util.ArrayIterator;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.io.FileInputAdapter;
import org.neuroph.util.io.FileOutputAdapter;
import org.omg.CORBA.TIMEOUT;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface.MainInterface;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface.NeuralNetworkTestScreen;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Alexander Keidel, 22397868 on 15/06/2016.
 * Custom Perceptron class which will do a number of different tasks. It trains and test neural networks with a given
 * data set. It will compare their performance based on the specified standard deviation performance delimiter.
 */
public class NeuralNetworkArchitectureTester implements GlobalVariablesInterface, Serializable {
    private NeuralNetwork customPerceptron;
    private ArrayList<NeuralNetworkSettings> neuralNetworkSettingsList = new ArrayList<>(); //array list of all neural network settings
    private NeuralNetworkSettings currentNetworkSettings;
    private NeuralNetworkSettings bestNetworkSettings = new NeuralNetworkSettings();
    private ArrayList<NeuralNetworkSettings> networkSettingsList = new ArrayList<>(); //array list of all network settings
    private ArrayList<NeuralNetwork> allNeuralNets = new ArrayList<>();
    private ArrayList<NeuralNetwork> loadedNeuralNets = new ArrayList<>();
    private NeuralNetworkSettingsListGenerator neuralNetworkSettingsListGenerator;
    public StringBuffer strDump = new StringBuffer(); //string buffer that can be read from outside this class
    public static float progressPercentage = 0.0f;
    private int totalThreadCount = 0;
    private int completedThreadCount = 0;

    public NeuralNetworkArchitectureTester(){
    }

    /**
     * The main function for this prototype. It will produce an array list of trained neural networks
     * with all the different specified learning rules and transfer functions using the provided training and test set.
     * @param trainingSetFile Training set file
     * @param testSetFile Test set file
     * @param baseName Base name for all neural networks (for human readability)
     * @param inputNeuronCount Number of input neurons. Must be in accordance with the training and test set files
     * @param outputNeuronCount Number of output neurons. Must be in accordance with the training and test set files
     * @param hiddenLayerVariants hidden layer variations to be tested
     * @param desiredTransferFunctions ArrayList of all desired transfer functions for this test
     * @param desiredLearningRules ArrayList of all desired learning rules for this test
     * @param performanceLimit Performance limit based on standard deviation between the output of a trained set against the actual output from the test set
     * @param progressBar Progress bar for the interface, updated when the training and testing of a single neural network architecture has finished.
     * @return True if successful, false if some error occurred
     */
    public boolean trainAndTestNeuralNetworkStructures(File trainingSetFile, File testSetFile, File overfittingFile, String baseName, int inputNeuronCount, int outputNeuronCount, ArrayList<ArrayList<Integer>> hiddenLayerVariants, ArrayList<TransferFunctionType> desiredTransferFunctions, ArrayList<LearningRule> desiredLearningRules, float performanceLimit, ProgressBar progressBar, TextArea textArea){
        try{
            //create test set and training set
            //TrainingSet trainingSet = TrainingSet.createFromFile(trainingSetFile.getPath(), inputNeuronCount, outputNeuronCount, ",");
            //TrainingSet<SupervisedTrainingElement> testSet = TrainingSet.createFromFile(testSetFile.getPath(), inputNeuronCount, outputNeuronCount, ",");
            //TrainingSet<SupervisedTrainingElement> overfittingSet = TrainingSet.createFromFile(overfittingFile.getPath(), inputNeuronCount, outputNeuronCount, ",");
            int networkCounter = 1;

            //thread pool requirements
            /**
             * see http://www.journaldev.com/1069/threadpoolexecutor-java-thread-pool-example-executorservice
             * https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html
             * https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html
             * https://docs.oracle.com/javase/tutorial/essential/concurrency/pools.html
             */
            RejectedExecutionHandler reh = new ThreadPoolExecutor.DiscardPolicy(); //rejection handler
            ThreadFactory threadFactory = Executors.defaultThreadFactory(); //default thread factory
            BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(1000);
            BlockingQueue<Runnable> queue = new SynchronousQueue<>(true);
            ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 1000, 10, TimeUnit.SECONDS, queue, threadFactory, reh);
            //ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 1000, 10, TimeUnit.DAYS, blockingQueue, threadFactory, reh);
            //for each learning rule and transfer function
            for(LearningRule rule : desiredLearningRules){
                for(TransferFunctionType transferFunctionType : desiredTransferFunctions) {
                    for (int i = 0; i < hiddenLayerVariants.size(); i++) { //for each hidden layer siz
                        //@TODO add variation for hidden layer sizes so not all hidden layers are the same size!
                        NeuralNetworkSettings network = new NeuralNetworkSettings(baseName + " #" + networkCounter, inputNeuronCount, outputNeuronCount, hiddenLayerVariants.get(i), transferFunctionType, rule, trainingSetFile, testSetFile, overfittingFile, strDump);
                        neuralNetworkSettingsList.add(network); //add the network settings to the array list of all neural network settings
                        Thread t = new Thread(network); //assign new thread to the network
                        executor.execute(t); //add the thread to the executor
                        totalThreadCount++; //increment total thread counter
                        networkCounter++; //increment network counter for base name to number all neural nets
                        System.out.println("Thread added to executor");
                    }
                }
            }
            while(true){
                //System.out.println(executor.getActiveCount());
                if(completedThreadCount == totalThreadCount) break; //break loop
                if(completedThreadCount != (int) executor.getCompletedTaskCount()){
                    completedThreadCount = (int) executor.getCompletedTaskCount();
                    //System.out.println("completed " + completedThreadCount + "/" + totalThreadCount);
                    textArea.appendText("" + strDump); //append main text area
                    strDump.setLength(0); //reset dump
                    //StextArea.setText("strDump = " + strDump);
                    float progress = ((float) completedThreadCount / (float) totalThreadCount); //calculate the overall progress
                    //System.out.println("progress = " + progress + "%");
                    progressBar.setProgress(progress); //update progress bar
                }
            }
            return true; //success
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

//    /**
//     * @deprecated
//     * @param numbers
//     * @param index
//     * @return
//     */
//    public ArrayList<ArrayList<Integer>> getAllPossibleHiddenLayers(ArrayList<Integer> numbers, int index){
//        ArrayList<ArrayList<Integer>> allCombinations = new ArrayList<>();
//        if(index == numbers.size() - 1){ //end condition
//            return new ArrayList<ArrayList<Integer>>();
//        }
//
//        //numbers.stream().
//        return allCombinations;
//    }

    /**
     * @deprecated
     * Convert the given two integer values into an arraylist of all the number between fromNum and toNum, passing it to {@link NeuralNetworkArchitectureTester#getAllPossiblePermutations(ArrayList)}
     * @param fromNum integer value of the first  value in the list (lowest, inclusive)
     * @param toNum integer value of the last value in the list (highest, inclusive)
     * @return
     */
    public ArrayList<ArrayList<Integer>> getAllPossiblePermutations(int fromNum, int toNum){
        if(fromNum > toNum){ //error
            return new ArrayList<>();
        }
        ArrayList<Integer> numbers = new ArrayList<>();
        for(int i = fromNum; i <= toNum; i++){
            numbers.add(i);
        }
        return getAllPossiblePermutations(numbers);
    }

    /**
     * @deprecated
     * See http://stackoverflow.com/questions/4640034/calculating-all-of-the-subsets-of-a-set-of-numbers
     *
     * @param numbers
     * @return
     */
    public ArrayList<ArrayList<Integer>> getAllPossiblePermutations(ArrayList<Integer> numbers){
        ArrayList<ArrayList<Integer>> allPermutations = new ArrayList<>();
        if(numbers.isEmpty()){
            allPermutations.add(new ArrayList<Integer>());
            return allPermutations;
        }
        ArrayList<Integer> list = new ArrayList<>(numbers);
        int head = list.get(0); //head of list
        ArrayList<Integer> tail = new ArrayList<>(list.subList(1, list.size())); //tail of list
        for(ArrayList<Integer> set : getAllPossiblePermutations(tail)){ //recursive call
            ArrayList<Integer> newSet = new ArrayList<>();
            newSet.add(head);
            newSet.addAll(set);
            allPermutations.add(newSet);
            allPermutations.add(set);
        }
        System.out.println("allPermutations.size() = " + allPermutations.size());
        return allPermutations;
    }

//    private Map<String, ArrayList<Integer>> getAllPossiblePermutations(int fromNum, int toNum, int layers){
//        Map<String, ArrayList<Integer>> allPermutations = new HashMap<>();
//
//
//        return allPermutations;
//    }

//    /**
//     * Creates and tests different structures of multilayered Perceptrons against one another, with the specified training and testing sets.
//     * The best result will be returned by the method.
//     * @deprecated  new version up above!!!
//     * @param inputNeuronCount Integer amount of input nodes
//     * @param outputNeuronCount Integer amount of output nodes
//     * @param trainingSetName Name of the training set name
//     * @param performanceLimit Decimal value for the performance limit. Based on the desired standard deviation {@link GlobalVariablesInterface#DEFAULT_PERFORMANCE_REQUIERD_MINIMUM}
//     * @return
//     */
//
//    public NeuralNetworkSettings createAndTestNeuralNetworkStructures(File trainingSet, File testSet, String testSetName, String trainingSetName, int inputNeuronCount, int outputNeuronCount, int maximumHiddenLayerCount, ArrayList<TransferFunctionType> desiredTransferFunctions, ArrayList<LearningRule> desiredLearningRules, float performanceLimit){
//        /**
//        //BEGIN "Setting initial Testing values"
//        ArrayList<Integer> hiddenLayers = new ArrayList<>();
//        hiddenLayers.add(0,inputNeuronCount); //initial hidden layers
//        TransferFunctionType tft = TransferFunctionType.GAUSSIAN;
//        LearningRule lrnRule = new BackPropagation();
//        currentNetworkSettings = new NeuralNetworkSettings("Initial Settings", inputNeuronCount, outputNeuronCount, hiddenLayers, tft, lrnRule); //
//        TrainingSet currentTrainingSet = TrainingSet.createFromFile(DEFAULT_FILE_PATH + DEFAULT_TRAINING_SET_NAME, inputNeuronCount, outputNeuronCount, ","); //setting training set location
//        TrainingSet<SupervisedTrainingElement> currentTestSet = TrainingSet.createFromFile(DEFAULT_FILE_PATH + DEFAULT_TEST_SET_NAME, inputNeuronCount, outputNeuronCount, ","); //setting testing set with file location
//        performanceLimit = DEFAULT_PERFORMANCE_REQUIERD_MINIMUM;
//        //END
//         */
//        neuralNetworkSettingsListGenerator = new NeuralNetworkSettingsListGenerator(testSetName, inputNeuronCount, outputNeuronCount, maximumHiddenLayerCount, desiredTransferFunctions, desiredLearningRules); //a new settings generator with the specified values
//        ArrayList<NeuralNetworkSettings> allPossibleNetworkSettings = neuralNetworkSettingsListGenerator.getNeuralNetworkList();
//
//        int networkCounter = 0;
//        ArrayList<Integer> hiddenLayers = new ArrayList<>();
//        TransferFunctionType tft;
//        LearningRule lrnRule;
//
////        TrainingSet currentTrainingSet = TrainingSet.createFromFile(DEFAULT_FILE_PATH + DEFAULT_TRAINING_SET_NAME, inputNeuronCount, outputNeuronCount, DEFAULT_SEPARATOR); //setting training set location
////        TrainingSet<SupervisedTrainingElement> currentTestSet = TrainingSet.createFromFile(DEFAULT_FILE_PATH + DEFAULT_TEST_SET_NAME, inputNeuronCount, outputNeuronCount, ","); //setting testing set with file location
//        //TrainingSet currentTrainingSet = TrainingSet.createFromFile(trainingSet.getPath(), inputNeuronCount, outputNeuronCount, ",");
//        //TrainingSet<SupervisedTrainingElement> currentTestSet = TrainingSet.createFromFile(testSet.getPath(), inputNeuronCount, outputNeuronCount, ",");
//        do{
//            try{
//                System.out.println("In try catch block iteration " + networkCounter);
//                hiddenLayers = allPossibleNetworkSettings.get(networkCounter).getHiddenLayers();
//                tft = allPossibleNetworkSettings.get(networkCounter).getTransferFunctionType();
//                lrnRule = allPossibleNetworkSettings.get(networkCounter).getLearningRule();
//                currentNetworkSettings = new NeuralNetworkSettings(testSetName + " #" + networkCounter, inputNeuronCount, outputNeuronCount, hiddenLayers, tft, lrnRule);
//                networkSettingsList.add(currentNetworkSettings); //append the list of the settings with the next item
//
//            } catch (Exception e){
//                e.printStackTrace();
//                System.out.println("No more possible settings to test.");
//                strDump.append("No more possible settings to test.\n");
//                try{
//                    System.out.print("\nFinished training and testing network.\n\nFinal result:\nBest found neural network settings provide a correctness level of " + bestNetworkSettings.getPerformanceScore() +"%.");
//                    strDump.append("\nFinished training and testing network.\n\nFinal result:\nBest found neural network settings provide a correctness level of " + bestNetworkSettings.getPerformanceScore() +"%.\n");
//                    if(bestNetworkSettings.getPerformanceScore() == 0.0f){
//                        System.out.println("No network was tested. Please report this error.");
//                        strDump.append("No network was tested. Please report this error.\n");
//                        return null;
//                    }
//                    return bestNetworkSettings;
//                } catch (Exception innerE){
//                    innerE.printStackTrace();
//                }
//            }
//            //adding the integer values defining the layers of the perceptron into an arraylist
//            ArrayList<Integer> neuronCountInLayers = new ArrayList<>();
//            neuronCountInLayers.add(inputNeuronCount);
//            for(int layer : hiddenLayers){ //adding hidden layers (same count as input layers)
//                neuronCountInLayers.add(layer);
//            }
//            neuronCountInLayers.add(outputNeuronCount); //adding output layers
//            customPerceptron = new MultiLayerPerceptron(neuronCountInLayers, currentNetworkSettings.getTransferFunctionType()); //create a custom multi layered perceptron with the given input, hidden and output layers, as well as transfer function
//            customPerceptron.setLearningRule(currentNetworkSettings.getLearningRule()); //setting learning rule
//            System.out.println("Training neural network.");
//            //customPerceptron.learnInNewThread(currentTrainingSet);
//            /**
//             * @TODO fix multi threading!
//             * This is done with a sepearte function. This is now deprecated
//             * start array list of threads to train all perceptrons
//             * each thread first trains the network structure, and then tests it
//             * create ranking of all network structures (charts?)
//             * rank the array list with a sort function
//             */
//            //customPerceptron.learn(currentTrainingSet); //training network with the training set
//            System.out.println("Finished training neural network");
//            ArrayList<Double> tmpvalues = new ArrayList<Double>();
//            for(int i = 0; i < currentTestSet.elements().size(); i ++){
//                customPerceptron.setInput(currentTestSet.elementAt(i).getInput()); //set input values
//                customPerceptron.calculate(); //calculate result
//                tmpvalues.add(customPerceptron.getOutput()[0]); //add the result to the list of doubles
//            }
//            currentNetworkSettings.setPerformanceScore(calculateStandardDeviation(tmpvalues));
//            System.out.println("Required Standard Deviation = " + performanceLimit + "\n" + convertNeuralNetworkSettingsToReadableString(currentNetworkSettings));
//            strDump.append("Required Standard Deviation = " + performanceLimit + "\n" + convertNeuralNetworkSettingsToReadableString(currentNetworkSettings) + "\n");
//            if(currentNetworkSettings.getPerformanceScore() < bestNetworkSettings.getPerformanceScore())bestNetworkSettings = currentNetworkSettings; //replace the best network settings
//            networkCounter++; //incrementing the network counter
//        }
//        while(bestNetworkSettings.getPerformanceScore() > performanceLimit); //while the best performance is under the specified limit (default at 97%) keep testing network structures
//        //System.out.print("\nFinished training and testing network.\n\nFinal result:\nBest found neural network settings provide a correctness level of " + bestNetworkSettings.getPerformanceScore() +"%.");
//        System.out.println("\nFinished training and testing networks. Best result is the following architecture:\n");
//        System.out.print(convertNeuralNetworkSettingsToReadableString(bestNetworkSettings));
//        strDump.append("\nFinished training and testing networks. Best result is the following architecture:\n" + convertNeuralNetworkSettingsToReadableString(bestNetworkSettings) + "\n");
//
//        synchronized (strDump){
//            strDump.notify();
//        }
//
//        return bestNetworkSettings; //return the best found network settings.
////    }

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
     * @throws IOException
     */
    public void saveCurrentNetworkSettingsToFile() throws IOException {
        FileOutputStream fos = new FileOutputStream("customNetworkSettings");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(currentNetworkSettings);
    }

    /**
     * Load the network settings from a file into the program
     * @return
     * @throws IOException
     * @deprecated
     */
    public boolean loadCurrentNetworkSettingsFromFile() throws IOException{
        System.out.println("(currentNetworkSettings).getName() = " + (currentNetworkSettings).getName());
        FileInputStream fis = new FileInputStream((currentNetworkSettings).getName());
        ObjectInputStream ois = new ObjectInputStream(fis);
        try {
            currentNetworkSettings = (NeuralNetworkSettings) ois.readObject();
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Load a specified neural network into the arraylist of loaded nets
     * @param fileName
     * @return
     */
    public boolean loadNetworkSettingsFromFile(String fileName) {
        try{
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ios = new ObjectInputStream(fis);
            loadedNeuralNets.add((NeuralNetwork) ios.readObject());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Load the network settings from the specified file name into the program
     * @param fileName
     * @return
     * @throws IOException
     */
    public boolean loadCurrentNetworkSettingsFromFile(String fileName) throws IOException{
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        try {
            currentNetworkSettings = (NeuralNetworkSettings) ois.readObject();
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Save the customPerceptron object as a file
     * @throws IOException
     */
    public void saveCurrentPerceptronToFile() throws IOException {
        FileOutputStream fos = new FileOutputStream("customPerceptron"); //name of the current network settings name
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(customPerceptron);
    }

    /**
     * Loads a customPerceptron from file
     * @return
     * @throws IOException
     */
    public boolean loadPerceptronFromFile(String fileName) throws  IOException {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ios = new ObjectInputStream(fis);
            customPerceptron = (NeuralNetwork) ios.readObject();
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Loads settings file into program
     * @param fileName
     * @return
     * @throws IOException
     */
    public boolean loadPerceptronSettingsFromFile(String fileName) throws IOException {
        try{
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ios = new ObjectInputStream(fis);
            currentNetworkSettings = (NeuralNetworkSettings) ios.readObject();
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Save this object to a file with the specified String as name of the file
     * @param name
     * @return
     */
    public boolean saveAllToFile(String name){
        try{
            FileOutputStream fos = new FileOutputStream(new File(name));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this); //write this object
            oos.flush();
            oos.close();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }





    /**
     * Getter for the array list of neural network settings
     * @return Array List of all the {@link NeuralNetworkSettings} in this object.
     */
    public ArrayList<NeuralNetworkSettings> getNetworkSettingsList() {
        return networkSettingsList;
    }

    public NeuralNetworkSettings getCurrentNetworkSettings() {
        return currentNetworkSettings;
    }

    public ArrayList<NeuralNetworkSettings> getNeuralNetworkSettingsList() {
        return neuralNetworkSettingsList;
    }
}
