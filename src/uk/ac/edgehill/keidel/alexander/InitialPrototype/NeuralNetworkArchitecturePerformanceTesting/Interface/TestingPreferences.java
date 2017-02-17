package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;

import org.neuroph.core.learning.LearningRule;
import org.neuroph.util.TransferFunctionType;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions.LearningRuleNotFoundException;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions.TransferFunctionNotFoundException;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.GlobalVariablesInterface;

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
    private int maximumHiddenLayers = 0; //default is 0
    private int maximumHiddenLayerSize = 0; //default is 0

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
                System.out.println(t.getTypeLabel());
                System.out.println(s);
                if(t.getTypeLabel().equals(s)){ //match found
                    transferFunctionTypes.add(t); //add the transfer function to the list
                    break;
                }
            }
        }
        if(transferFunctionTypes.size() != transferFunctionNames.size()){
            System.out.println(transferFunctionTypes.size());
            System.out.println(transferFunctionNames.size());
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
                System.out.println(l.getClass().getSimpleName());
                System.out.println(s);
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

    public int getMaximumHiddenLayers() {
        return maximumHiddenLayers;
    }

    public void setMaximumHiddenLayers(int maximumHiddenLayers) {
        this.maximumHiddenLayers = maximumHiddenLayers;
    }

    public int getMaximumHiddenLayerSize() {
        return maximumHiddenLayerSize;
    }

    public void setMaximumHiddenLayerSize(int maximumHiddenLayerSize) {
        this.maximumHiddenLayerSize = maximumHiddenLayerSize;
    }
}
