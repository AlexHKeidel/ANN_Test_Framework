package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * Created by Alexander Keidel, 22397868 on 13/02/2017.
 * Starts a new stage with the about screen information
 */
public class AboutScreen implements GUIValues {
    private Stage myStage;
    public AboutScreen (Stage parentStage){
        myStage = new Stage(); //new stage
        //see http://stackoverflow.com/questions/19953306/block-parent-stage-until-child-stage-closes
        myStage.initModality(Modality.WINDOW_MODAL); //set modality
        myStage.initOwner(parentStage); //make the parent Stage the owner, so this window has to be closed in order for the user to return to the main part of the program
        myStage.initStyle(StageStyle.UNDECORATED); //remove minimize, maximise and close buttons from stage
        myStage.setResizable(false); //can not resize stage
        myStage.setTitle("About"); //title of the stage
        BorderPane borderPane = new BorderPane(); //new border pane
        borderPane.setPadding(new Insets(5));
        borderPane.setBorder(Border.EMPTY);
        TextArea aboutTextArea = new TextArea(); //scrollable text area that will contain a description
        aboutTextArea.setEditable(false); //not editable
        aboutTextArea.setText(ABOUT_MENU_TEXT); //set the text
        borderPane.setCenter(aboutTextArea); //make it center of the border layout
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> closeStage());
        borderPane.setBottom(closeButton); //add close button to pane
        borderPane.setAlignment(closeButton, Pos.BOTTOM_CENTER); //align button bottom centered
        Scene myScene = new Scene(borderPane, 400, 350); //400 by 350
        myStage.setScene(myScene); //set the scene
        myStage.show(); //show scene
    }

    private void closeStage() {
        myStage.close();
    }

}
