package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Created by Alexander Keidel, 22397868 on 13/02/2017.
 * Starts a new stage with the about screen information
 */
public class AboutScreen implements GUIValues {
    public AboutScreen (Stage parentStage){
        Stage myStage = new Stage(); //new stage
        //see http://stackoverflow.com/questions/19953306/block-parent-stage-until-child-stage-closes
        myStage.initModality(Modality.WINDOW_MODAL); //set modality
        myStage.initOwner(parentStage); //make the parent Stage the owner, so this window has to be closed in order for the user to return to the main part of the program

        myStage.setTitle("About"); //title of the stage
        BorderPane borderPane = new BorderPane(); //new border pane
        TextArea aboutTextArea = new TextArea(); //scrollable text area that will contain a description
        aboutTextArea.setEditable(false); //not editable
        aboutTextArea.setText(ABOUT_MENU_TEXT); //set the text
        borderPane.setCenter(aboutTextArea); //make it center of the border layout
        Scene myScene = new Scene(borderPane, 400, 350); //400 by 350
        myStage.setScene(myScene); //set the scene
        myStage.show(); //show scene
    }

}
