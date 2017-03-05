package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting;

import com.sun.org.apache.xml.internal.security.Init;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions.LearningRuleNotFoundException;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions.TransferFunctionNotFoundException;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface.TestingPreferences;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Alexander Keidel, 22397868 on 15/06/2016.
 * Initial prototype of my masters project application.
 * The idea is to have a testing framework to decide on the best suited artificial neural network architecture for any
 * given data set.
 */
public class InitialPrototype implements GlobalVariablesInterface, Runnable {
    public static NeuralNetworkArchitectureTester neuralNetworkArchitectureTester = new NeuralNetworkArchitectureTester();
    public static TestingPreferences testingPreferences = new TestingPreferences();
    private TextArea parentTextArea;
    private ProgressBar parentProgressBar;

    public InitialPrototype(TextArea parentTextArea, ProgressBar progressBar){
        this.parentTextArea = parentTextArea;
        this.parentProgressBar = progressBar;
    }

    private void startPrototype(){
        String workingDirectory = System.getProperty("user.dir");
        workingDirectory = workingDirectory.replace("\\", "/"); //replace all backward slashes with forward slashes to be used for file paths. See http://stackoverflow.com/questions/1701839/string-replaceall-single-backslashes-with-double-backslashes and http://stackoverflow.com/questions/4871051/getting-the-current-working-directory-in-java
        System.out.println("Working Directory = " + workingDirectory);

        NeuralNetworkSettingsListGenerator generator = new NeuralNetworkSettingsListGenerator(); //careful using this! possible null pointers, only for debugging!
//        File trainingSet = new File(System.getProperty("user.dir") + "/Data Sets/Bi3 Data Sets/Demographics Training Set");
//        File testSet = new File(System.getProperty("user.dir") + "/Data Sets/Bi3 Data Sets/Demographics Test Set");
        File trainingSet = testingPreferences.getTrainingDataFile();
        File testSet = testingPreferences.getTestDataFile();
        int inputNeuronCount = testingPreferences.getInputLayers();
        int outputNeuronCount = testingPreferences.getOutputLayers();
        int maxHiddenLayerCount = testingPreferences.getMaximumHiddenLayers();
        int maxHiddenLayerSize = testingPreferences.getMaximumHiddenLayerSize();
        int minHiddenLayerSize = 0;
        String baseName = testingPreferences.getTestName();
        //neuralNetworkArchitectureTester.createAndTestNeuralNetworkStructures(trainingSet, testSet,"Test Set Not Yet Generated!", "Supervised Demographics Data", 4, 1, 5, generator.getAllPossibleTransferFunctions(), generator.getAllPossibleLearningRules(), DEFAULT_PERFORMANCE_REQUIERD_MINIMUM);

        try {
            neuralNetworkArchitectureTester.trainAndTestNeuralNetworkStructures(trainingSet, testSet, baseName, inputNeuronCount, outputNeuronCount, maxHiddenLayerCount, minHiddenLayerSize, maxHiddenLayerSize, testingPreferences.getTransferFunctions(), testingPreferences.getLearningRules(), testingPreferences.getPerformance(), parentProgressBar);

        } catch (LearningRuleNotFoundException le){
            le.printStackTrace();
        } catch (TransferFunctionNotFoundException te){
            te.printStackTrace();
        }
        /**
         * See http://stackoverflow.com/questions/2914375/getting-file-path-in-java for saving in current file location
         */
//        try {
//            neuralNetworkArchitectureTester.saveCurrentNetworkSettingsToFile();
//            neuralNetworkArchitectureTester.saveCurrentPerceptronToFile();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    @Override
    public void run() {
        startPrototype();
    }

    public void updateAssosiatedTextArea(){
        parentTextArea.appendText(neuralNetworkArchitectureTester.strDump.toString()); //dump string into gui
        parentTextArea.setScrollTop(Double.MAX_VALUE); //scroll down
    }

    /**
     * Save this object to a file with the specified file
     * @param file
     * @return
     */
    public boolean saveNeuralNetworkTesterToFile(File file){
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(neuralNetworkArchitectureTester); //write this object
            oos.flush();
            oos.close();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean loadNeuralNetworkTesterFromFile(File file){
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            neuralNetworkArchitectureTester = (NeuralNetworkArchitectureTester) ois.readObject();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}