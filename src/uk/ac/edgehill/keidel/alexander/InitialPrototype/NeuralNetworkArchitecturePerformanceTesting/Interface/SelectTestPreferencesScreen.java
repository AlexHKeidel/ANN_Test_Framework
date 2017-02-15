package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;

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
import org.neuroph.core.learning.LearningRule;
import org.neuroph.util.TransferFunctionType;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.GlobalVariablesInterface;

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
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); //make sure you can't close tabs
        Tab boundariesTab = new Tab(PREFERENCES_SCREEN_BOUNDARIES_TAB);
        boundariesTab.setClosable(false);//make sure you can't close the tab
        boundariesTab.setContent(generateBoundariesContent());
        Tab learningRulesTab = new Tab(PREFERENCES_SCREEN_LEARNING_RULES_TAB);
        learningRulesTab.setClosable(false);//make sure you can't close tabs
        learningRulesTab.setContent(generateLearningRuleContent());
        Tab transferFunctionsTab = new Tab(PREFERENCES_SCREEN_TRANSFER_FUNCTIONS_TAB);
        transferFunctionsTab.setClosable(false);//make sure you can't close tabs
        transferFunctionsTab.setContent(generateTransferFunctionContent());
        tabPane.getTabs().addAll(learningRulesTab, transferFunctionsTab);


        //setup border pane
        BorderPane borderPane = new BorderPane(); //new border pane
        borderPane.setBackground(PREFERENCES_SCREEN_BACKGROUND); //set background colour
        borderPane.setBottom(buttonHBox); //add cancel buttons to bottom
        borderPane.setAlignment(buttonHBox, Pos.BOTTOM_CENTER); //align hbox bottom center
        Text preferenceInstructions = new Text(PREFERENCES_SCREEN_INSTRUCTIONS); //instructinos textfield (single line)
        borderPane.setTop(preferenceInstructions);
        borderPane.setAlignment(preferenceInstructions, Pos.TOP_CENTER);
        borderPane.setCenter(tabPane);


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
        ScrollPane scrollPane = new ScrollPane();
        
        return scrollPane;
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
            vBox.getChildren().add(c);
            vBox.setMargin(c, new Insets(5));
        }
        scrollPane.setContent(vBox);
        return scrollPane;
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
            vBox.getChildren().add(c);
            vBox.setMargin(c, new Insets(5));
        }
        scrollPane.setContent(vBox);
        return scrollPane;
    }

    private void closeWindow() {
        myStage.close();
    }

    private boolean savePreferences(){
        try{

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
