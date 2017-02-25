package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.NumberStringConverter;
import org.encog.util.file.Directory;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.util.TransferFunctionType;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions.LearningRuleNotFoundException;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions.TransferFunctionNotFoundException;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.GlobalVariablesInterface;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Alexander Keidel, 22397868 on 15/02/2017.
 * Screen to select test preferences for network structures, hidden layer count (plus min and max sizes)
 * Different tabs provide different settings
 * Save and cancel button on the bottom
 * Modal window so you cannot go back to the main menu without closing (either cancel or save)
 *
 */
public class SelectTestPreferencesScreen extends Stage implements GUIValues, GlobalVariablesInterface {
    private Stage myStage;
    private TabPane allPreferencesTabPane;
    private ArrayList<String> checkBoxItems = new ArrayList<>(); //array list of items that have been checked in this screen
    private ArrayList<CheckBox> transferFunctionCheckBoxes = new ArrayList<>();
    private ArrayList<CheckBox> learningRuleCheckBoxes = new ArrayList<>();
    private TextField desiredTestName = new TextField();
    private TextField desiredPerformanceTextField = new TextField();
    private TextField desiredMaxHidLayersTextField = new TextField();
    private TextField desiredMaxHidLayerSizeTextField = new TextField();
    private TextField desiredInputLayerSizeTextField = new TextField();
    private TextField desiredOutputLayerSizeTextField = new TextField();
    private Text desiredTrainingSetText = new Text();
    private Text desiredTestSetText = new Text();

    private ArrayList<String> selectedTransferFunctions = new ArrayList<>();
    private ArrayList<String> selectedLearningRules = new ArrayList<>();
    private float desiredPerformance = 0; //desired performance level selected by the user
    private final String PREFERENCES_PREFIX = "pref_";
    private TestingPreferences testingPreferences = new TestingPreferences(); //testing preference object
    private String testPreferencesName = "Default";
    private int inputLayers = 0;
    private int outputLayers = 0;
    private int maximumHiddenLayers = 0;
    private int maximumHiddenLayerSize = 0;
    private File trainingSetFile;
    private File testSetFile;

    public SelectTestPreferencesScreen(Stage parentStage){
        myStage = new Stage(); //new stage
        //see http://stackoverflow.com/questions/19953306/block-parent-stage-until-child-stage-closes
        myStage.initModality(Modality.WINDOW_MODAL); //set modality
        myStage.initOwner(parentStage); //make the parent Stage the owner, so this window has to be closed in order for the user to return to the main part of the program
        myStage.initStyle(StageStyle.UNDECORATED); //remove minimize, maximise and close buttons from stage
        myStage.setResizable(false); //can not resize stage
        myStage.setTitle("Neural Network Testing Preferences"); //set title



        //set up Hbox for buttons
        HBox buttonHBox = new HBox(); //horizontal box
        Button saveButton = new Button("Save"); //save button
        saveButton.setOnAction(e -> savePreferences());
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> closeWindow());
        buttonHBox.getChildren().addAll(saveButton, cancelButton);
        buttonHBox.setAlignment(Pos.BASELINE_CENTER); //align hbox baseline center
        buttonHBox.setPadding(new Insets(5));
        buttonHBox.setMargin(saveButton, new Insets(5)); //set save button marginmargin
        buttonHBox.setMargin(cancelButton, new Insets(5)); //set cancel button margin

        //setup tab pane
        allPreferencesTabPane = new TabPane();
        allPreferencesTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); //make sure you can't close tabs
        Tab boundariesTab = new Tab(PREFERENCES_SCREEN_BOUNDARIES_TAB);
        boundariesTab.setClosable(false);//make sure you can't close the tab
        boundariesTab.setContent(generateBoundariesContent());
        Tab learningRulesTab = new Tab(PREFERENCES_SCREEN_LEARNING_RULES_TAB);
        learningRulesTab.setClosable(false);//make sure you can't close tabs
        learningRulesTab.setContent(generateLearningRuleContent());
        Tab transferFunctionsTab = new Tab(PREFERENCES_SCREEN_TRANSFER_FUNCTIONS_TAB);
        transferFunctionsTab.setClosable(false);//make sure you can't close tabs
        transferFunctionsTab.setContent(generateTransferFunctionContent());
        allPreferencesTabPane.getTabs().addAll(boundariesTab, learningRulesTab, transferFunctionsTab);


        //setup border pane
        BorderPane borderPane = new BorderPane(); //new border pane
        borderPane.setBackground(PREFERENCES_SCREEN_BACKGROUND); //set background colour
        borderPane.setBottom(buttonHBox); //add cancel buttons to bottom
        borderPane.setAlignment(buttonHBox, Pos.BOTTOM_CENTER); //align hbox bottom center
        Text preferenceInstructions = new Text(PREFERENCES_SCREEN_INSTRUCTIONS); //instructinos textfield (single line)
        borderPane.setTop(preferenceInstructions);
        borderPane.setAlignment(preferenceInstructions, Pos.TOP_CENTER);
        borderPane.setCenter(allPreferencesTabPane);


        borderPane.setPadding(new Insets(10)); //set padding to 10

        Scene myScene = new Scene(borderPane, 600, 600);
        myStage.setScene(myScene);
        myStage.show(); //show
        loadPreferences(); //load preferences from file
    }

    /**
     * Generate the content for the boundaries tab
     * @return
     */
    private Node generateBoundariesContent() {
        ScrollPane scrollPane = new ScrollPane(); //scroll pane to contain the vBox
        VBox vBox = new VBox(); //vertical box to contain the test boundary fields
        Text networkTestOptionsText = new Text(PREFERENCES_SCREEN_BOUNDARIES_NETWORK_TESTING_TITLE);

        //test preference name HBox
        HBox testNameHBox = new HBox();
        Text testNameText = new Text(PREFERENCES_SCREEN_BOUNDARIES_TEST_NAME);
        TextField textNameTextField = new TextField();
        desiredTestName = textNameTextField;
        testNameHBox.getChildren().addAll(testNameText, textNameTextField);

        //input neurons HBox
        HBox inputLayerHBox = new HBox();
        Text inputLayerText = new Text(PREFERENCES_SCREEN_BOUNDARIES_INPUT_LAYER_SIZE);
        TextField inputLayerTextField = new TextField();
        NumberStringConverter inputConverter = new NumberStringConverter();
        TextFormatter<Number> inputLayerFormatter = new TextFormatter<>(inputConverter);
        inputLayerTextField.setTextFormatter(inputLayerFormatter);
        inputLayerFormatter.valueProperty().addListener((observable, oldValue, newValue) -> validateInputLayers(observable, oldValue, newValue, inputLayerFormatter));
        desiredInputLayerSizeTextField = inputLayerTextField;
        inputLayerHBox.getChildren().addAll(inputLayerText, inputLayerTextField);

        //output neurons HBox
        HBox outputLayerHBox = new HBox();
        Text outputLayerText = new Text(PREFERENCES_SCREEN_BOUNDARIES_OUTPUT_LAYER_SIZE);
        TextField outputLayerTextField = new TextField();
        NumberStringConverter outputConverter = new NumberStringConverter();
        TextFormatter<Number> outputLayerFormatter = new TextFormatter<>(outputConverter);
        outputLayerTextField.setTextFormatter(outputLayerFormatter);
        outputLayerFormatter.valueProperty().addListener((observable, oldValue, newValue) -> validateOutputLayers(observable, oldValue, newValue, outputLayerFormatter));
        desiredOutputLayerSizeTextField = outputLayerTextField;
        outputLayerHBox.getChildren().addAll(outputLayerText, outputLayerTextField);

        //performance HBox
        HBox performanceHBox = new HBox(); //HBox to contain the performance instructions plus field
        Text performanceText = new Text(PREFERENCES_SCREEN_BOUNDARIES_NETWORK_TESTING_PERFORMANCE_SCORE);
        TextField performanceNumberField = new TextField(); //performance setter text field for only numbers
        NumberStringConverter n1 = new NumberStringConverter(); //number to string converter
        TextFormatter<Number> formatter1 = new TextFormatter(n1); //assign converter
        performanceNumberField.setTextFormatter(formatter1); //assign formatter
        formatter1.valueProperty().addListener((observable, oldValue, newValue) -> validatePerformanceValue(observable, oldValue, newValue, formatter1));
        desiredPerformanceTextField = performanceNumberField;
        performanceHBox.getChildren().addAll(performanceText, performanceNumberField); //add items to hbox

        //see above for comments (same structure) @TODO really this means that this should be separate methods
        HBox maximumHiddenLayersHBox = new HBox();
        Text maxHidLayText = new Text(PREFERENCES_SCREEN_BOUNDARIES_MAXIMUM_HIDDEN_LAYERS_TEXT);
        TextField maxHidLayTextField = new TextField();
        NumberStringConverter n2 = new NumberStringConverter();
        TextFormatter<Number> formatter2 = new TextFormatter<Number>(n2);
        maxHidLayTextField.setTextFormatter(formatter2);
        formatter2.valueProperty().addListener((observable, oldValue, newValue) -> validateMaxHiddenLayersValue(observable, oldValue, newValue, formatter2));
        desiredMaxHidLayersTextField = maxHidLayTextField;
        maximumHiddenLayersHBox.getChildren().addAll(maxHidLayText, maxHidLayTextField);

        //see above for comments (same structure)
        //maximum hidden layers
        HBox maximumHiddenLayerSizeHBox = new HBox();
        Text maxHidLaySizeText = new Text(PREFERENCES_SCREEN_BOUNDARIES_MAXIMUM_HIDDEN_LAYER_SIZE_TEXT);
        TextField maxHidLaySizeTextField = new TextField();
        NumberStringConverter n3 = new NumberStringConverter();
        TextFormatter<Number> formatter3 = new TextFormatter<Number>(n3);
        maxHidLaySizeTextField.setTextFormatter(formatter3);
        formatter3.valueProperty().addListener((observable, oldValue, newValue) -> validateMaxHiddenLayerSizeValue(observable, oldValue, newValue, formatter3));
        desiredMaxHidLayerSizeTextField = maxHidLaySizeTextField;
        maximumHiddenLayerSizeHBox.getChildren().addAll(maxHidLaySizeText, maxHidLaySizeTextField);

        //training set HBox
        HBox trainingSetHBox = new HBox();
        Text trainingSetPrefix = new Text(PREFERENCES_SCREEN_BOUNDARIES_TRAINING_SET_FILE_NAME);
        Text trainingSetCurrentFileName = new Text("no file selected");
        Button selectTrainingSetButton = new Button();
        selectTrainingSetButton.setText(PREFERENCES_SCREEN_BOUNDARIES_SELECT_BUTTON);
        selectTrainingSetButton.setOnAction(event -> selectTrainingSetAction(trainingSetCurrentFileName));
        desiredTrainingSetText = trainingSetCurrentFileName;
        trainingSetHBox.getChildren().addAll(trainingSetPrefix, trainingSetCurrentFileName, selectTrainingSetButton);

        //test set HBox
        HBox testSetHBox = new HBox();
        Text testSetPrefix = new Text(PREFERENCES_SCREEN_BOUNDARIES_TEST_SET_FILE_NAME);
        Text testSetCurrentFileName = new Text("no file selected");
        Button selectTestSetButton = new Button();
        selectTestSetButton.setText(PREFERENCES_SCREEN_BOUNDARIES_SELECT_BUTTON);
        selectTestSetButton.setOnAction(event -> selectTestSetAction(testSetCurrentFileName));
        desiredTestSetText = testSetCurrentFileName;
        testSetHBox.getChildren().addAll(testSetPrefix, testSetCurrentFileName, selectTestSetButton);


        vBox.getChildren().addAll(networkTestOptionsText, testNameHBox, inputLayerHBox, outputLayerHBox, maximumHiddenLayersHBox, maximumHiddenLayerSizeHBox, performanceHBox, trainingSetHBox, testSetHBox); //add items to vbox
        scrollPane.setContent(vBox); //add vbox to scrollpane
        return scrollPane;
    }


    /**
     * Open file chooser to select a training set file
     */
    private void selectTrainingSetAction(Text currentFileName) {
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(NEURAL_NETWORK_MENU_SELECT_TRAINING_SET);
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("comma delimited csv files only", ".csv"));
            fileChooser.setInitialDirectory(trainingSetFile);
            trainingSetFile = fileChooser.showOpenDialog(this);
            currentFileName.setText(trainingSetFile.getName());

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Open file chooser to select a test set file
     */
    private void selectTestSetAction(Text currentFileName) {
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(NEURAL_NETWORK_MENU_SELECT_TRAINING_SET);
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("comma delimited csv files only", ".csv"));
            fileChooser.setInitialDirectory(testSetFile);
            testSetFile = fileChooser.showOpenDialog(this);
            currentFileName.setText(testSetFile.getName());

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Validate the user input for the maximum hidden layer size value
     * The value must be over 0 and an integer. (positive integer)
     * @param observable
     * @param oldValue
     * @param newValue
     * @param formatter
     */
    private void validateMaxHiddenLayerSizeValue(ObservableValue<? extends Number> observable, Number oldValue, Number newValue, TextFormatter<Number> formatter) {
        if(newValue.floatValue() % 1 != 0){ //the value is not a whole number!
            formatter.setValue(oldValue);
        }
        if(newValue.intValue() < 0){ //value cannot be below 0
            formatter.setValue(oldValue);
        }
        //validated
        maximumHiddenLayerSize = newValue.intValue(); //assign value
    }

    /**
     * Validate the user input for the maximum hidden layers value.
     * This value can not be under 0 and must be an integer.
     * @param observable
     * @param oldValue
     * @param newValue
     * @param formatter
     */
    private void validateMaxHiddenLayersValue(ObservableValue<? extends Number> observable, Number oldValue, Number newValue, TextFormatter<Number> formatter) {
        if(newValue.floatValue() % 1 != 0){ //the value is not a whole number!
            formatter.setValue(oldValue);
        }
        if(newValue.intValue() < 0){ //value cannot be below 0
            formatter.setValue(oldValue);
        }
        //validated
        maximumHiddenLayers = newValue.intValue(); //assign value
    }

    private void validateInputLayers(ObservableValue<? extends Number> observable, Number oldValue, Number newValue, TextFormatter parentFormatter){
        try{
            if(newValue == null || newValue.longValue() < 0){
                if(oldValue == null){
                    parentFormatter.setValue(1);
                } else if(newValue.longValue() % 1 != 0){ //the number is not an integer
                    parentFormatter.setValue(oldValue);
                }
            }
            inputLayers = newValue.intValue();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void validateOutputLayers(ObservableValue<? extends Number> observable, Number oldValue, Number newValue, TextFormatter<Number> parentFormatter) {
        try{
            if(newValue == null || newValue.longValue() < 0){
                if(oldValue == null){
                    parentFormatter.setValue(1);
                } else if(newValue.longValue() % 1 != 0){ //the number is not an integer
                    parentFormatter.setValue(oldValue);
                }
            }
            outputLayers = newValue.intValue();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Validate the user entered performance value to make sure they enter something sensible
     */
    private void validatePerformanceValue(ObservableValue<? extends Number> observable, Number oldValue, Number newValue, TextFormatter parentFormatter) {
        try{
            if(newValue == null){
                if(oldValue == null){
                    //set to default value
                    parentFormatter.setValue(1);
                } else {
                    parentFormatter.setValue(oldValue);
                }
            }
            if(newValue.longValue() > 100.0 || newValue.longValue() < 0.000001){
                parentFormatter.setValue(oldValue); //reset value
            }
            desiredPerformance = newValue.floatValue(); //set desired performance
            System.out.println("desiredPerformance = " + desiredPerformance);
        } catch (Exception ex){
            ex.printStackTrace();
            //cannot perform operation NaN!
        }
    }

    /**
     * Generate a list of transfer functions inside a vbox inside a scrollpane
     * @return
     */
    private Node generateTransferFunctionContent() {
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();

        for(TransferFunctionType t : TRANSFER_FUNCTION_TYPES){
            CheckBox c = new CheckBox(t.getTypeLabel());
            transferFunctionCheckBoxes.add(c);
            c.setOnAction(event -> transferFunctionTicked(c.getText())); //add listener event to add or remove checked items
            vBox.getChildren().add(c);
            vBox.setMargin(c, new Insets(5));
        }
        scrollPane.setContent(vBox);
        return scrollPane;
    }

    private void checkBoxTicked(String text) {
        if(checkBoxItems.contains(text)){ //item already exists, must be unchecked!
            checkBoxItems.remove(text); //remove the item (uncheck!)
        } else {
            checkBoxItems.add(text);
        }

        System.out.println("Printing all checkBoxItems");
        for(String i : checkBoxItems){
            System.out.println(i);
        }
    }

    private void transferFunctionTicked(String text){
        if(selectedTransferFunctions.contains(text)){
            selectedTransferFunctions.remove(text);
        } else {
            selectedTransferFunctions.add(text);
        }
    }

    private void learningRuleTicked(String text){
        if(selectedLearningRules.contains(text)){
            selectedLearningRules.remove(text);
        } else {
            selectedLearningRules.add(text);
        }
    }

    /**
     * Generate the learning rule content based on a scrollable vbox which a checkbox for each learning rule
     * @return
     */
    private Node generateLearningRuleContent() {
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();
        for(LearningRule l : LEARNING_RULES){
            String name = l.getClass().getName().substring(l.getClass().getName().lastIndexOf('.') + 1, l.getClass().getName().length()); //get the name based on the last index of '.' (last package identifier) + 1 to exclude the .
            CheckBox c = new CheckBox(name);
            learningRuleCheckBoxes.add(c);
            c.setOnAction(event -> learningRuleTicked(c.getText())); //add listener event to add or remove checked items
            vBox.getChildren().add(c);
            vBox.setMargin(c, new Insets(5));
        }
        scrollPane.setContent(vBox);
        return scrollPane;
    }

    private void closeWindow() {
        myStage.close();
    }

    /**
     * Save all the values selected on the setup interface screen
     * @return
     */
    private boolean savePreferences(){
        try{
            //set the testingPreferences to the selected values
            testingPreferences.setLearningRuleNames(selectedLearningRules);
            testingPreferences.setTransferFunctionNames(selectedTransferFunctions);
            testingPreferences.setPerformance(desiredPerformance);
            testingPreferences.setMaximumHiddenLayers(maximumHiddenLayers);
            testingPreferences.setMaximumHiddenLayerSize(maximumHiddenLayerSize);
            testingPreferences.setTestName(testPreferencesName);
            testingPreferences.setInputLayers(inputLayers);
            testingPreferences.setOutputLayers(outputLayers);
            testingPreferences.setTrainingDataFile(trainingSetFile);
            testingPreferences.setTestDataFile(testSetFile);
            FileOutputStream fos = new FileOutputStream(testingPreferences.getClass().getSimpleName()); //new output stream with the name of the testing preferences class
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(testingPreferences);
            oos.flush();
            oos.close();
            //@TODO notify the main screen of changes
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally{
            closeWindow();
        }
    }

    /**
     * Load preferences from file
     */
    private boolean loadPreferences() {
        try{
            FileInputStream fis = new FileInputStream(testingPreferences.getClass().getSimpleName());
            ObjectInputStream ios = new ObjectInputStream(fis);
            testingPreferences = (TestingPreferences) ios.readObject();
            inputLayers = testingPreferences.getInputLayers();
            outputLayers = testingPreferences.getOutputLayers();
            testPreferencesName = testingPreferences.getTestName();
            selectedLearningRules = testingPreferences.getLearningRuleNames();
            selectedTransferFunctions = testingPreferences.getTransferFunctionNames();
            trainingSetFile = testingPreferences.getTrainingDataFile();
            testSetFile = testingPreferences.getTrainingDataFile();

            //display the correct values inside the UI

            desiredTestName.setText(String.valueOf(testingPreferences.getTestName()));
            desiredPerformanceTextField.setText(String.valueOf(testingPreferences.getPerformance()));
            desiredMaxHidLayersTextField.setText(String.valueOf(testingPreferences.getMaximumHiddenLayers()));
            desiredMaxHidLayerSizeTextField.setText(String.valueOf(testingPreferences.getMaximumHiddenLayerSize()));
            desiredInputLayerSizeTextField.setText(String.valueOf(testingPreferences.getInputLayers()));
            desiredOutputLayerSizeTextField.setText(String.valueOf(testingPreferences.getOutputLayers()));
            desiredTrainingSetText.setText(String.valueOf(testingPreferences.getTrainingDataFile().getName()));
            desiredTestSetText.setText(String.valueOf(testingPreferences.getTestDataFile().getName()));

            for(TransferFunctionType t : testingPreferences.getTransferFunctions()){
                //update selected transfer function to be set as ticked
                for(int i = 0; i < transferFunctionCheckBoxes.size(); i++){
                    if(transferFunctionCheckBoxes.get(i).getText().equals(t.getTypeLabel())){
                        transferFunctionCheckBoxes.get(i).setSelected(true); //selected to true
                        continue;
                    }
                }
            }

            for(LearningRule r : testingPreferences.getLearningRules()){
                for(int i = 0; i < learningRuleCheckBoxes.size(); i++){
                    if(learningRuleCheckBoxes.get(i).getText().equals(r.getClass().getSimpleName())){
                        learningRuleCheckBoxes.get(i).setSelected(true);
                        continue;
                    }
                }
            }

            return true;
        }
        catch(FileNotFoundException fex){
            fex.printStackTrace();
            return false; //file not found
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (TransferFunctionNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (LearningRuleNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
