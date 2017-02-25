package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting;

import org.neuroph.contrib.matrixmlp.MatrixMomentumBackpropagation;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.nnet.learning.*;
import org.neuroph.util.TransferFunctionType;

import java.util.ArrayList;

/**
 * Created by Alex on 23/09/2016.
 * Creates a list of all the possible configurations of learning rules and transfer function combinations.
 */
public class NeuralNetworkSettingsListGenerator extends NeuralNetworkSettings{
    private static ArrayList<LearningRule> allPossibleLearningRules = new ArrayList<>(); //list of all possible learning rules
    private static ArrayList<TransferFunctionType> allPossibleTransferFunctions = new ArrayList<>(); //list of all possible transfer functions
    private ArrayList<NeuralNetworkSettings> neuralNetworkList = new ArrayList<>();
    private boolean allPossibleListGenerated = false; //flag to see if the list is generated

    public NeuralNetworkSettingsListGenerator(String baseName, int inputNeurons, int outputNeurons, int possibleHiddenLayers, ArrayList<TransferFunctionType> desiredTransferFunctions, ArrayList<LearningRule> desiredLearningRules){
        generateList(baseName, inputNeurons, outputNeurons, possibleHiddenLayers, desiredTransferFunctions, desiredLearningRules);
    }

    public NeuralNetworkSettingsListGenerator(){
        populateAllPossibleLearningRulesAndTransferFunctionsIntoArraryLists();
    }

    public  ArrayList<LearningRule> getAllPossibleLearningRules() {
        return allPossibleLearningRules;
    }

    public  ArrayList<TransferFunctionType> getAllPossibleTransferFunctions() {
        return allPossibleTransferFunctions;
    }

    private void populateAllPossibleLearningRulesAndTransferFunctionsIntoArraryLists(){
        //START adding all possible learning rules supported by neuroph 2.92 to the ArrayList
        allPossibleLearningRules.clear();
        allPossibleLearningRules.add(new BackPropagation());
        allPossibleLearningRules.add(new DynamicBackPropagation());
        allPossibleLearningRules.add(new AntiHebbianLearning());
        allPossibleLearningRules.add(new BinaryHebbianLearning());
        allPossibleLearningRules.add(new BinaryDeltaRule());
        allPossibleLearningRules.add(new CompetitiveLearning());
        allPossibleLearningRules.add(new ConvolutionalBackpropagation());
        allPossibleLearningRules.add(new GeneralizedHebbianLearning());
        allPossibleLearningRules.add(new HopfieldLearning());
        allPossibleLearningRules.add(new InstarLearning());
        allPossibleLearningRules.add(new AntiHebbianLearning());
        allPossibleLearningRules.add(new KohonenLearning());
        allPossibleLearningRules.add(new LMS());
        allPossibleLearningRules.add(new MomentumBackpropagation());
        allPossibleLearningRules.add(new OjaLearning());
        allPossibleLearningRules.add(new OutstarLearning());
        allPossibleLearningRules.add(new PerceptronLearning());
        allPossibleLearningRules.add(new RBFLearning());
        allPossibleLearningRules.add(new ResilientPropagation());
        allPossibleLearningRules.add(new SigmoidDeltaRule());
        allPossibleLearningRules.add(new MomentumBackpropagation());
        allPossibleLearningRules.add(new SupervisedHebbianLearning());
        allPossibleLearningRules.add(new MatrixMomentumBackpropagation());
        //END

        //START adding all possible transfer functions supported by neuroph 2.92 to the ArrayList
        allPossibleTransferFunctions.clear();
        allPossibleTransferFunctions.add(TransferFunctionType.GAUSSIAN);
        allPossibleTransferFunctions.add(TransferFunctionType.LINEAR);
        allPossibleTransferFunctions.add(TransferFunctionType.LOG);
        allPossibleTransferFunctions.add(TransferFunctionType.RAMP);
        allPossibleTransferFunctions.add(TransferFunctionType.SGN);
        allPossibleTransferFunctions.add(TransferFunctionType.SIGMOID);
        allPossibleTransferFunctions.add(TransferFunctionType.SIN);
        allPossibleTransferFunctions.add(TransferFunctionType.STEP);
        allPossibleTransferFunctions.add(TransferFunctionType.TANH);
        allPossibleTransferFunctions.add(TransferFunctionType.TRAPEZOID);

        /** wrong data type (?)
        allPossibleTransferFunctions.add(new Gaussian());
        allPossibleTransferFunctions.add(new Linear());
        allPossibleTransferFunctions.add(new Log());
        allPossibleTransferFunctions.add(new Ramp());
        allPossibleTransferFunctions.add(new RectifiedLinear());
        allPossibleTransferFunctions.add(new Sgn());
        allPossibleTransferFunctions.add(new Sigmoid());
        allPossibleTransferFunctions.add(new Sin());
        allPossibleTransferFunctions.add(new Step());
        allPossibleTransferFunctions.add(new Tanh());
        allPossibleTransferFunctions.add(new Trapezoid());
         */
        //END
        allPossibleListGenerated = true; //flag set to true
    }


    /**
     * Generate a list of all the desired multi-layered perceptron architecture configurations.
     * @TODO add size value for minimum and maximum hidden layer sizes
     * @param baseName base name for all configurations, usually named after the training set
     * @param inputNeurons number of input neurons
     * @param outputNeurons number of output neurons
     * @param possibleHiddenLayers number of possible layers
     * @param desiredTransferFunctions list of desired transfer functions, if the list is null or empty all possibilities will be usd
     * @param desiredLearningRules list of desired learning rules, if the list is null or empty all possibilities will be used
     * @return
     */
    private boolean generateList(String baseName, int inputNeurons, int outputNeurons, int possibleHiddenLayers, ArrayList<TransferFunctionType> desiredTransferFunctions, ArrayList<LearningRule> desiredLearningRules){ //uses all possible learning rules and transfer function possibilities
        try{
            if(desiredTransferFunctions == null || desiredTransferFunctions.isEmpty()){ //if the list is empty use all possibilities
                if(!allPossibleListGenerated || allPossibleTransferFunctions == null | allPossibleTransferFunctions.isEmpty()) populateAllPossibleLearningRulesAndTransferFunctionsIntoArraryLists(); //generate the list if it is not already generated
                desiredTransferFunctions = allPossibleTransferFunctions; //assign all possible ones
            }
            if(desiredLearningRules == null || desiredLearningRules.isEmpty()){ //if the list is empty use all possibilities
                if(!allPossibleListGenerated || allPossibleLearningRules == null || allPossibleLearningRules.isEmpty()) populateAllPossibleLearningRulesAndTransferFunctionsIntoArraryLists(); //generate the list if it is not already generated
                desiredLearningRules = allPossibleLearningRules; //assign all possible ones
            }

            int counter = 0;
            for(LearningRule rule : desiredLearningRules){
                for(TransferFunctionType tf : desiredTransferFunctions){
                    for(int i = 0; i < possibleHiddenLayers; i++){
                        ArrayList<Integer> tmp = new ArrayList<>();
                        for(int j = 0; j <= i;j ++){ //adding hidden layers based on selected complexity (based on #possibleHiddenlayers parameter
                            tmp.add(inputNeurons);
                        }
                        neuralNetworkList.add(new NeuralNetworkSettings(baseName + " Settigns #" + counter,  inputNeurons, outputNeurons,tmp, tf, rule));

                        /** Unoptimised old code
                        switch(i){
                            case 0: //try one hidden layer with the size of input neurons
                                tmp.add(inputNeurons);
                                neuralNetworkList.add(new NeuralNetworkSettings(baseName + " Settigns #" + counter,  inputNeurons, outputNeurons,tmp, tf, rule));
                                break;

                            case 1: //try two hidden layers with the size of input neurons
                                tmp.add(inputNeurons);
                                tmp.add(inputNeurons);
                                neuralNetworkList.add(new NeuralNetworkSettings(baseName + " Settigns #" + counter,  inputNeurons, outputNeurons,tmp, tf, rule));
                                break;

                            case 2: //try three hidden layers with the size of input neurons
                                tmp.add(inputNeurons);
                                tmp.add(inputNeurons);
                                tmp.add(inputNeurons);
                                neuralNetworkList.add(new NeuralNetworkSettings(baseName + " Settigns #" + counter,  inputNeurons, outputNeurons,tmp, tf, rule));
                                break;

                            case 3: //try three hidden layers with the size of input neurons
                                tmp.add(inputNeurons);
                                tmp.add(inputNeurons);
                                tmp.add(inputNeurons);
                                tmp.add(inputNeurons);
                                neuralNetworkList.add(new NeuralNetworkSettings(baseName + " Settigns #" + counter,  inputNeurons, outputNeurons,tmp, tf, rule));
                                break;

                            default:
                                return false; //Error
                        } */
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true; //success
    }

    public ArrayList<NeuralNetworkSettings> getNeuralNetworkList(){
        return neuralNetworkList;
    }
}
