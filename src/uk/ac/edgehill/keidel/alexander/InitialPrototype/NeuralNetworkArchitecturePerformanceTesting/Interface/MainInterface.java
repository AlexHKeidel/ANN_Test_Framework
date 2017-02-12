package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;/**
 * Created by Alexander Keidel, 22397868 on 12/02/2017.
 * Main Interface at testing stage
 * Used to display the information about the prototype
 * see:
 * http://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.htm
 * http://docs.oracle.com/javafx/2/get_started/hello_world.htm
 *
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.InitialPrototype;

import javax.swing.*;
import java.time.LocalDateTime;

public class MainInterface extends Application implements GUIValues {
    private Button StartProcedureButton;
    private TextArea ANNInfoTextArea;
    private InitialPrototype prototype;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initialiseResources(primaryStage); //init all the resources for the GUI
        prototype = new InitialPrototype();
    }

    private boolean initialiseResources(Stage primaryStage){
        try {
            primaryStage.setTitle(TITLE); //set the title

            StartProcedureButton = new Button("Start Procedure");
            StartProcedureButton.setOnAction(event -> startProcedure());

            ANNInfoTextArea = new TextArea();
            ANNInfoTextArea.setEditable(false); //cannot edit field
            ANNInfoTextArea.positionCaret(0);
            //ANNInfoTextArea.setText("Hello World!\nThis is a new line.\nah");

            GridPane groot = new GridPane();
            Insets customInsets = new Insets(5, 5 , 5 , 5); //create new inset values

            groot.setPadding(customInsets); //use insets for padding

            //StackPane root = new StackPane(); //new Stack Pane

            // Add all the items to the pane
            groot.add(ANNInfoTextArea, 0, 0);
            groot.setMargin(ANNInfoTextArea, customInsets); //setting margin
            groot.add(StartProcedureButton, 0, 1);
            groot.setMargin(StartProcedureButton, customInsets);

            //See https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/GridPane.html
            ColumnConstraints column1 = new ColumnConstraints(100,100,Double.MAX_VALUE); //new column constraints
            column1.setHgrow(Priority.ALWAYS);
            ColumnConstraints column2 = new ColumnConstraints(100);
            groot.getColumnConstraints().addAll(column1, column2); // first column gets any extra width


            primaryStage.setScene(new Scene(groot, 800, 600)); //Set the scene with width and height
            primaryStage.show(); //show the scene
            return true; //all went well
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false; //GUI failed to initialise
        }
    }

    private void startProcedure(){
        prototype.startPrototype();
        //StartProcedureButton.setDisable(true);
//        synchronized (prototype.cp.strDump){
//            try {
//                prototype.cp.strDump.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        ANNInfoTextArea.setText(""); //reset text
        ANNInfoTextArea.appendText(prototype.cp.strDump.toString()); //insert the information
        ANNInfoTextArea.setScrollTop(Double.MAX_VALUE); //scroll down
    }
}
