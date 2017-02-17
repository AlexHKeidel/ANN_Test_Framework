package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.NumberStringConverter;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.util.TransferFunctionType;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.GlobalVariablesInterface;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
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
    private ArrayList<String> selectedTransferFunctions = new ArrayList<>();
    private ArrayList<String> selectedLearningRules = new ArrayList<>();
    private float desiredPerformance = 0; //desired performance level selected by the user
    private final String PREFERENCES_PREFIX = "pref_";
    private TestingPreferences testingPreferences = new TestingPreferences(); //testing preference object

    public SelectTestPreferencesScreen(Stage parentStage){
        myStage = new Stage(); //new stage
        //see http://stackoverflow.com/questions/19953306/block-parent-stage-until-child-stage-closes
        myStage.initModality(Modality.WINDOW_MODAL); //set modality
        myStage.initOwner(parentStage); //make the parent Stage the owner, so this window has to be closed in order for the user to return to the main part of the program
        myStage.initStyle(StageStyle.UNDECORATED); //remove minimize, maximise and close buttons from stage
        myStage.setResizable(false); //can not resize stage
        myStage.setTitle("Neural Network Testing Preferences"); //set title
        loadPreferences(); //load preferences from file


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
    }

    /**
     * Generate the content for the boundaries tab
     * @return
     */
    private Node generateBoundariesContent() {
        ScrollPane scrollPane = new ScrollPane(); //scroll pane to contain the vBox
        VBox vBox = new VBox(); //vertical box to contain the test boundary fields
        Text networkTestOptionsText = new Text(PREFERENCES_SCREEN_BOUNDARIES_NETWORK_TESTING_TITLE);
        HBox performanceHbox = new HBox(); //Hbox to contain the performance instructions plus field
        Text performanceText = new Text(PREFERENCES_SCREEN_BOUNDARIES_NETWORK_TESTING_PERFORMANCE_SCORE);
        TextField performanceNumberField = new TextField(); //performance setter text field for only numbers
        NumberStringConverter numberStringConverter = new NumberStringConverter(); //number to string converter
        TextFormatter<Number> formatter = new TextFormatter(numberStringConverter); //assign converter
        performanceNumberField.setTextFormatter(formatter); //assign formatter
        formatter.valueProperty().addListener((observable, oldValue, newValue) -> validatePerformanceValue(observable, oldValue, newValue, formatter));
        performanceHbox.getChildren().addAll(performanceText, performanceNumberField); //add items to hbox
        vBox.getChildren().addAll(networkTestOptionsText, performanceHbox); //add items to vbox
        scrollPane.setContent(vBox); //add vbox to scrollpane
        return scrollPane;
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

            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
