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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.InitialPrototype;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.NeuralNetworkSettings;

import javax.swing.*;
import java.time.LocalDateTime;

public class MainInterface extends Application implements GUIValues {
    private Button StartProcedureButton;
    private TextArea ANNInfoTextArea;
    private BarChart barChart;
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
            ANNInfoTextArea.setText("Session Started.\n" + LocalDateTime.now().toLocalDate().toString() + " " + LocalDateTime.now().toLocalTime().toString() +"\n");
            ANNInfoTextArea.setEditable(false); //cannot edit field
            ANNInfoTextArea.positionCaret(0);
            //ANNInfoTextArea.setText("Hello World!\nThis is a new line.\nah");

            //set up bar char
            //see http://docs.oracle.com/javafx/2/charts/bar-chart.htm#CIHJFHDE
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            yAxis.setAutoRanging(false);
            yAxis.setUpperBound(100.0);
            yAxis.setLowerBound(95.0);
            barChart = new BarChart<String, Number>(xAxis, yAxis);
            barChart.setAnimated(false);
            barChart.setTitle("Best Performing ANN Structures");
            xAxis.setLabel("ANN Structure");
            yAxis.setLabel("Performance Score (Standard Deviation)");

            GridPane groot = new GridPane();
            Insets customInsets = new Insets(5, 5 , 5 , 5); //create new inset values

            groot.setPadding(customInsets); //use insets for padding

            //StackPane root = new StackPane(); //new Stack Pane

            // Add all the items to the pane
            groot.add(barChart, 0, 0);
            groot.add(ANNInfoTextArea, 0, 1);
            groot.setMargin(ANNInfoTextArea, customInsets); //setting margin
            groot.add(StartProcedureButton, 0, 2);
            groot.setMargin(StartProcedureButton, customInsets);


            //See https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/GridPane.html
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setPercentWidth(100);
            ColumnConstraints column2 = new ColumnConstraints();
            column2.setPercentWidth(0);
            groot.getColumnConstraints().setAll(column1, column2);


            primaryStage.setScene(new Scene(groot, 1024, 860)); //Set the scene with width and height
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
        ANNInfoTextArea.appendText(prototype.cp.strDump.toString()); //insert the information
        ANNInfoTextArea.setScrollTop(Double.MAX_VALUE); //scroll down
        populateBarChar();
    }

    private void populateBarChar(){
        double worstStandardDeviation = 100;
        for(int i = 0; i < prototype.cp.getNetworkSettingsList().size(); i++){ //for each neural network setting
            NeuralNetworkSettings tmp = prototype.cp.getNetworkSettingsList().get(i); //assign the neural network setting to a local object
            if(i == 0) worstStandardDeviation = tmp.getPerformanceScore(); //assign first value in the first iteration
            XYChart.Series s = new XYChart.Series<String, Double>(); //create a new series for the chart
            s.setName(tmp.getLearningRule().getClass().getSimpleName() + " " + tmp.getTransferFunctionType().getTypeLabel()); //set the name of the series
            String structure = "" + tmp.getInputNeurons(); //add input layer to string
            for(int n : tmp.getHiddenLayers()){ //add hidden layers to string
                structure += " (" + n + ") ";
            }
            structure += tmp.getOutputNeurons(); // +"TF: " + tmp.getTransferFunctionType().getTypeLabel() + " LR: " + tmp.getLearningRule().getClass().getSimpleName(); //add rest to string
            double score =  (1 - tmp.getPerformanceScore()) * 100; //calculate the performance
            if(worstStandardDeviation < tmp.getPerformanceScore()) worstStandardDeviation = tmp.getPerformanceScore(); //find the worst performance score for the graph to start at
            s.getData().add(new XYChart.Data<String, Double>(structure, score));
            barChart.getData().add(s);
        }
        System.out.println("worst standard deviation = " + worstStandardDeviation);
        ((NumberAxis) barChart.getYAxis()).setLowerBound(((1  - worstStandardDeviation) * 99.99)); //set lower bound for the chart
    }
}
