package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;

import org.neuroph.core.learning.LearningRule;
import org.neuroph.util.TransferFunctionType;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions.LearningRuleNotFoundException;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions.TransferFunctionNotFoundException;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.GlobalVariablesInterface;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alexander Keidel, 22397868 on 17/02/2017.
 * Serializable testing preferences object set by the {@link SelectTestPreferencesScreen} and read by {@link MainInterface}
 */
public class TestingPreferences implements Serializable, GlobalVariablesInterface {

    private ArrayList<String> transferFunctionNames = new ArrayList<>();
    private ArrayList<String> learningRuleNames = new ArrayList<>();
    private float performance = 100.0f; //default is unusable!
    private String testName = "Default";
    private int inputLayers = 0;
    private int outputLayers = 0;
//    private int maximumHiddenLayers = 0; //default is 0
//    private int minimumHiddenLayerSize = 0; //default is 0
//    private int maximumHiddenLayerSize = 0; //default is 0
    private ArrayList<ArrayList<Integer>> hiddenLayerVariants = new ArrayList<>();
    private File trainingDataFile;
    private File testDataFile;
    private File overfittingTestDataFile;
    private char datasetDelimiter = ','; //standard delimiter

    public TestingPreferences(){}

    /**
     * Return the transfer function objects based on the transfer function names.
     * @return
     * @throws TransferFunctionNotFoundException
     */
    public ArrayList<TransferFunctionType> getTransferFunctions() throws TransferFunctionNotFoundException {
        ArrayList<TransferFunctionType> transferFunctionTypes = new ArrayList<>();
        for(String s : transferFunctionNames){ //for each desired transfer function
            for(TransferFunctionType t : TRANSFER_FUNCTION_TYPES){ //for each type in all possible types
                //System.out.println(t.getTypeLabel());
                //System.out.println(s);
                if(t.getTypeLabel().equals(s)){ //match found
                    transferFunctionTypes.add(t); //add the transfer function to the list
                    break;
                }
            }
        }
        if(transferFunctionTypes.size() != transferFunctionNames.size()){
            //System.out.println(transferFunctionTypes.size());
            //System.out.println(transferFunctionNames.size());
            throw new TransferFunctionNotFoundException("Some transfer function does not exist!");
        }
        return transferFunctionTypes;
    }

    /**
     * Return the learning rules as learning rule objects based on their names.
     * @TODO look into different solution for breaking the loops, like http://stackoverflow.com/questions/886955/breaking-out-of-nested-loops-in-java
     * @return
     * @throws LearningRuleNotFoundException
     */
    public ArrayList<LearningRule> getLearningRules() throws LearningRuleNotFoundException {
        ArrayList<LearningRule> learningRules = new ArrayList<>();
        //convert strings into learning rule objects
        for(String s : learningRuleNames){
            for(LearningRule l : LEARNING_RULES){ //for each learning rule in all possible learning rules
                //System.out.println(l.getClass().getSimpleName());
                //System.out.println(s);
                if(l.getClass().getSimpleName().equals(s)){ //match found
                    learningRules.add(l); //add the learning rule to the list
                    break; //we can stop looping
                }
            }
        }
        if(learningRules.size() != learningRuleNames.size()){
            throw new LearningRuleNotFoundException("Some learning rule does not exist!");
        }
        return learningRules;
    }

    /**
     * Convert the testing preferences into a readable String
     * @return String in human readable format summarising all testing preferences stored in this objectminhidlayerFormatter
     */
    public String getPreferencesInReadableFormat(){
        String readable = "Testing Preferences: " + testName + "\n";
        readable += "Input neurons: " + inputLayers + "\n";
        readable += "Output neurons: " + outputLayers + "\n";
        String tmphdlyrs = "";
        for(ArrayList<Integer> arrs : hiddenLayerVariants) {
            for(int h : arrs) {
                tmphdlyrs += "(" + h + ") ";
            }
            tmphdlyrs += "\n";
        }
        readable += "Hidden layers variations:\n" + tmphdlyrs + "\n";
//        readable += "Maximum hidden layers: " + getMaximumHiddenLayers() + "\n";
//        readable += "Minimum hidden layer size: " + getMinimumHiddenLayerSize() + "\n";
//        readable += "Maximum hidden layer size: " + getMaximumHiddenLayerSize() + "\n";
        readable += "Performance score (total MSE): " + getPerformance() + "\n";
        readable += "Transfer functions:\n";
        for(String s : getTransferFunctionNames()){
            readable += s + "\n";
        }
        readable += "Learning rules: \n";
        for(String s : getLearningRuleNames()){
            readable += s + "\n";
        }
        readable += "Training set file name: " + getTrainingDataFile().getName() + "\n";
        readable += "Test set file name: " + getTestDataFile().getName() + "\n";
        readable += "Overfitting set file name: " + getOverfittingTestDataFile().getName() + "\n";
        readable += "Data set delimiter: \'" + getDatasetDelimiter() + "\'\n";
        readable += "-----End of testing preferences-----\n\n";
        return readable;
    }


    public ArrayList<String> getTransferFunctionNames() {
        return transferFunctionNames;
    }

    public void setTransferFunctionNames(ArrayList<String> transferFunctionNames) {
        this.transferFunctionNames = transferFunctionNames;
    }

    public ArrayList<String> getLearningRuleNames() {
        return learningRuleNames;
    }

    public void setLearningRuleNames(ArrayList<String> learningRuleNames) {
        this.learningRuleNames = learningRuleNames;
    }

    public float getPerformance() {
        return performance;
    }

    public void setPerformance(float performance) {
        this.performance = performance;
    }

//    public int getMaximumHiddenLayers() {
//        return maximumHiddenLayers;
//    }
//
//    public void setMaximumHiddenLayers(int maximumHiddenLayers) {
//        this.maximumHiddenLayers = maximumHiddenLayers;
//    }
//
//    public int getMaximumHiddenLayerSize() {
//        return maximumHiddenLayerSize;
//    }
//
//    public void setMaximumHiddenLayerSize(int maximumHiddenLayerSize) {
//        this.maximumHiddenLayerSize = maximumHiddenLayerSize;
//    }

    public File getTrainingDataFile() {
        return trainingDataFile;
    }

    public void setTrainingDataFile(File trainingDataFile) {
        this.trainingDataFile = trainingDataFile;
    }

    public File getTestDataFile() {
        return testDataFile;
    }

    public void setTestDataFile(File testDataFile) {
        this.testDataFile = testDataFile;
    }

    public int getInputLayers() {
        return inputLayers;
    }

    public void setInputLayers(int inputLayers) {
        this.inputLayers = inputLayers;
    }

    public int getOutputLayers() {
        return outputLayers;
    }

    public void setOutputLayers(int outputLayers) {
        this.outputLayers = outputLayers;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

//    public int getMinimumHiddenLayerSize() {
//        return minimumHiddenLayerSize;
//    }
//
//    public void setMinimumHiddenLayerSize(int minimumHiddenLayerSize) {
//        this.minimumHiddenLayerSize = minimumHiddenLayerSize;
//    }

    public File getOverfittingTestDataFile() {
        return overfittingTestDataFile;
    }

    public void setOverfittingTestDataFile(File overfittingTestDataFile) {
        this.overfittingTestDataFile = overfittingTestDataFile;
    }

    public ArrayList<ArrayList<Integer>> getHiddenLayerVariants() {
        return hiddenLayerVariants;
    }

    public String getHiddenLayerVariantsAsString(){
        String variants = "";
        for(ArrayList<Integer> structure : hiddenLayerVariants){
            String tmp = "";
            for(int neurons : structure){
                tmp += neurons + ",";
            }
            tmp = tmp.substring(0, tmp.length() - 1) + ";";
            variants += tmp;
        }
        return variants;
    }

    public void setHiddenLayerVariants(ArrayList<ArrayList<Integer>> hiddenLayerVariants) {
        this.hiddenLayerVariants = hiddenLayerVariants;
    }

    public char getDatasetDelimiter() {
        return datasetDelimiter;
    }

    public void setDatasetDelimiter(char datasetDelimiter) {
        this.datasetDelimiter = datasetDelimiter;
    }
}
