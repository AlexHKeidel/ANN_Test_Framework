package uk.ac.edgehill.keidel.alexander.InitialPrototype;

import org.neuroph.contrib.matrixmlp.MatrixMomentumBackpropagation;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.transfer.*;
import org.neuroph.nnet.learning.*;
import org.neuroph.util.TransferFunctionType;

import java.util.ArrayList;

/**
 * Created by Alex on 23/09/2016.
 */
public class NeuralNetworkSettingsListGenerator extends NeuralNetworkSettings{
    private static ArrayList<LearningRule> learningRules = new ArrayList<>(); //list of all applicable learning rules
    private static ArrayList<TransferFunctionType> transferFunctions = new ArrayList<>(); //list of all applicable transferfunctions
    private ArrayList<NeuralNetworkSettings> neuralNetworkList = new ArrayList<>();

    public NeuralNetworkSettingsListGenerator(String baseName, int inputNeurons, int outputNeurons){
        generateList(baseName, inputNeurons, outputNeurons);
    }

    private void initArrayLists(){
        //START adding all possible learning rules supported by neuroph 2.92 to the ArrayList
        learningRules.add(new BackPropagation());
        learningRules.add(new DynamicBackPropagation());
        learningRules.add(new AntiHebbianLearning());
        learningRules.add(new BinaryHebbianLearning());
        learningRules.add(new BinaryDeltaRule());
        learningRules.add(new CompetitiveLearning());
        learningRules.add(new ConvolutionalBackpropagation());
        learningRules.add(new GeneralizedHebbianLearning());
        learningRules.add(new HopfieldLearning());
        learningRules.add(new InstarLearning());
        learningRules.add(new AntiHebbianLearning());
        learningRules.add(new KohonenLearning());
        learningRules.add(new LMS());
        learningRules.add(new MomentumBackpropagation());
        learningRules.add(new OjaLearning());
        learningRules.add(new OutstarLearning());
        learningRules.add(new PerceptronLearning());
        learningRules.add(new RBFLearning());
        learningRules.add(new ResilientPropagation());
        learningRules.add(new SigmoidDeltaRule());
        learningRules.add(new MomentumBackpropagation());
        learningRules.add(new SupervisedHebbianLearning());
        learningRules.add(new MatrixMomentumBackpropagation());
        //END

        //START adding all possible transfer functions supported by neuroph 2.92 to the ArrayList
        transferFunctions.add(TransferFunctionType.GAUSSIAN);
        transferFunctions.add(TransferFunctionType.LINEAR);
        transferFunctions.add(TransferFunctionType.LOG);
        transferFunctions.add(TransferFunctionType.RAMP);
        transferFunctions.add(TransferFunctionType.SGN);
        transferFunctions.add(TransferFunctionType.SIGMOID);
        transferFunctions.add(TransferFunctionType.SIN);
        transferFunctions.add(TransferFunctionType.STEP);
        transferFunctions.add(TransferFunctionType.TANH);
        transferFunctions.add(TransferFunctionType.TRAPEZOID);

        /** wrong data type (?)
        transferFunctions.add(new Gaussian());
        transferFunctions.add(new Linear());
        transferFunctions.add(new Log());
        transferFunctions.add(new Ramp());
        transferFunctions.add(new RectifiedLinear());
        transferFunctions.add(new Sgn());
        transferFunctions.add(new Sigmoid());
        transferFunctions.add(new Sin());
        transferFunctions.add(new Step());
        transferFunctions.add(new Tanh());
        transferFunctions.add(new Trapezoid());
         */
        //END
    }

    private boolean generateList(String baseName, int inputNeurons, int outputNeurons){
        try{
            initArrayLists();
            int counter = 0;
            for(LearningRule rule : learningRules){
                for(TransferFunctionType tf : transferFunctions){
                    for(int i = 0; i < 4; i++){
                        ArrayList<Integer> tmp = new ArrayList<>();
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
                        }
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
