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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.InitialPrototype;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.NeuralNetworkSettings;

import java.io.IOException;
import java.time.LocalDateTime;

public class MainInterface extends Application implements GUIValues {
    private Button StartProcedureButton;
    private TextArea ANNInfoTextArea;
    private BarChart barChart;
    private InitialPrototype prototype;
    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        prototype = new InitialPrototype();
        initialiseResources(primaryStage); //init all the resources for the GUI
    }

    private boolean initialiseResources(Stage primaryStage){
        try {
            primaryStage.setTitle(TITLE); //set the title

            //set up menu bar
            MenuBar menuBar = setupMenuBar();


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

            //See http://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
            BorderPane borderPane = new BorderPane(); //new border pane
            borderPane.setTop(menuBar);
            borderPane.setLeft(ANNInfoTextArea);
            borderPane.setCenter(barChart);
            borderPane.setBottom(StartProcedureButton);

            Insets customInsets = new Insets(5, 5 , 5 , 5); //create new inset values
            VBox vBox = new VBox(10); //spacing defined in constructor
            vBox.setPadding(customInsets); //set padding of custom insets
            Text vboxTitle = new Text("VBox");
            vboxTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            vBox.getChildren().add(vboxTitle); //add the text as title

            //Old Grid pane layout
//            GridPane groot = new GridPane();
//            Insets customInsets = new Insets(5, 5 , 5 , 5); //create new inset values
//
//            groot.setPadding(customInsets); //use insets for padding
//
//            //StackPane root = new StackPane(); //new Stack Pane
//
//            // Add all the items to the pane
//            groot.add(barChart, 0, 0);
//            groot.add(ANNInfoTextArea, 0, 1);
//            groot.setMargin(ANNInfoTextArea, customInsets); //setting margin
//            groot.add(StartProcedureButton, 0, 2);
//            groot.setMargin(StartProcedureButton, customInsets);

//            //See https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/GridPane.html
//            ColumnConstraints column1 = new ColumnConstraints();
//            column1.setPercentWidth(100);
//            ColumnConstraints column2 = new ColumnConstraints();
//            column2.setPercentWidth(0);
//            groot.getColumnConstraints().setAll(column1, column2);
//
//            Scene myScene = new Scene(groot, 1024, 860); //new scene with specified size
//            ((GridPane) myScene.getRoot()).getChildren().addAll(menuBar); //add the menu bar to the scene
            //primaryStage.setScene(myScene); //Set the scene with width and height
            primaryStage.setScene(new Scene(borderPane, 1024, 860));
            primaryStage.show(); //show the scene
            //loadOnStartup(); //load the dataset if it exists
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
        populateBarChart();
    }

    /**
     * Populate the bar chart with the neural network performance data
     */
    private void populateBarChart(){
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

    /**
     * Set up the menu bar for the interface
     * See http://docs.oracle.com/javafx/2/ui_controls/menu_controls.htm
     * @return
     */
    private MenuBar setupMenuBar(){
        MenuBar menuBar = new MenuBar();
        //File menu and sub menus
        final Menu fileMenu = new Menu(FILE_MENU); //file menu
        final MenuItem fileSaveProject = new MenuItem(FILE_MENU_SAVE_PROJECT);

        //Options menu and sub menus
        final Menu optionsMenu = new Menu(OPTIONS_MENU);

        //Neural Network Menu and sub menus
        final Menu nnMenu = new Menu(NEURAL_NETWORK_MENU);
        final MenuItem loadNNMenuItem = new MenuItem(NEURAL_NETWORK_MENU_LOAD_NETWORK);
        final MenuItem saveNNMenuItem = new MenuItem(NEURAL_NETWORK_MENU_SAVE_NETWORK);
        final MenuItem selectTrainingSetMenuItem = new MenuItem(NEURAL_NETWORK_MENU_SELECT_TRAINING_SET);
        final MenuItem selectTestSetMenuItem = new MenuItem(NEURAL_NETWORK_MENU_SELECT_TEST_SET);
        nnMenu.getItems().addAll(loadNNMenuItem, saveNNMenuItem, selectTrainingSetMenuItem, selectTestSetMenuItem);

        //Help menu and sub menus
        final Menu helpMenu = new Menu(HELP_MENU);
        final MenuItem aboutMenuItem = new MenuItem(HELP_MENU_ABOUT);
        aboutMenuItem.setOnAction(e -> showAboutScreen());
        helpMenu.getItems().addAll(aboutMenuItem);
//        final MenuItem fileLoad = new MenuItem("Load Neural Network");
//        final MenuItem selectDataset = new MenuItem("Select Dataset");
//        final MenuItem selectTrainingset = new MenuItem("Select Training Set");
//        final MenuItem selectTestset = new MenuItem("Select");
//        fileSave.setOnAction(event -> saveOperation()); //save button functionality
//        fileMenu.getItems().add(fileSave); //add the MenuItem to the Menu
//        final Menu optionsMenu = new Menu("Options"); //options menu
//        final Menu helpMenu = new Menu("Help"); //help menu
        menuBar.getMenus().addAll(fileMenu, optionsMenu, nnMenu, helpMenu);

        return menuBar;
    }

    private void showAboutScreen() {
        AboutScreen as = new AboutScreen(primaryStage);
    }

    /**
     * Saves all the custom perceptron data
     * @return
     */
    private boolean saveOperation(){
        try{
            prototype.cp.saveCurrentNetworkSettingsToFile();
            prototype.cp.saveCurrentPerceptronToFile();
            ANNInfoTextArea.appendText("\nSaved best network settings and perceptron file to disk.\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Load the previous state of the program if one exists
     * @return
     * @deprecated
     */
    private boolean loadOnStartup(){
        try{
            prototype.cp.loadPerceptronFromFile("customPerceptron");
            prototype.cp.loadPerceptronSettingsFromFile("customNetworkSettings");
            populateBarChart();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

    }

    /**
     * Load the specified parameters into the program.
     * @param fileName
     * @return
     */
    private boolean loadOperation(String fileName){
        try{

            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
