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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.InitialPrototype;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.NeuralNetworkSettings;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class MainInterface extends Application implements GUIValues {
    private Button startProcedureButton;
    private TextArea ANNInfoTextArea;
    private BarChart barChart;
    private ProgressBar progressBar;
    private int totalTrainingThreads = 0;
    private int currentTrainingThreads = 0;
    private static InitialPrototype prototype;
    public static Stage primaryStage;
    private TestingPreferences testingPreferences = new TestingPreferences(); //testing preferences file

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initialiseResources(primaryStage); //init all the resources for the GUI
        prototype = new InitialPrototype(ANNInfoTextArea, progressBar);
    }

    private boolean initialiseResources(Stage primaryStage){
        try {
            primaryStage.setTitle(TITLE); //set the title

            //set up menu bar
            MenuBar menuBar = setupMenuBar(primaryStage);

            VBox buttonsVBox = new VBox();
            Button selectTestingPreferencesButton = new Button("Setup");
            selectTestingPreferencesButton.setOnAction(e -> startSelectTestPreferencesScreen());
            startProcedureButton = new Button("Start");
            startProcedureButton.setOnAction(event -> startProcedure());
            //add progress bar to buttonsVBox
            progressBar = new ProgressBar();
            progressBar.setProgress(0);
            buttonsVBox.getChildren().addAll(progressBar, selectTestingPreferencesButton, startProcedureButton);


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
            borderPane.setRight(barChart);
            borderPane.setBottom(buttonsVBox);

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
//            groot.add(startProcedureButton, 0, 2);
//            groot.setMargin(startProcedureButton, customInsets);

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

    private void orderAllNetworkSettingsByPerformance(){
        ArrayList<NeuralNetworkSettings> allArchitectures = prototype.neuralNetworkArchitectureTester.getNetworkSettingsList();
        allArchitectures.sort((o1, o2) -> (comparePerformances(o1, o2)));
        for(NeuralNetworkSettings n : allArchitectures){
            System.out.println("n performance = " + n.getPerformanceScore());
        }
    }

    private int comparePerformances(NeuralNetworkSettings o1, NeuralNetworkSettings o2) {
        if(o1.getPerformanceScore() > o2.getPerformanceScore()){
            return 0;
        } else {
            return 1;
        }
    }


    private void startSelectTestPreferencesScreen() {
        SelectTestPreferencesScreen s = new SelectTestPreferencesScreen(primaryStage);
    }

    /**
     * Start the testing procedure
     */
    private void startProcedure(){
        //load the preferences file
        try {
            FileInputStream fis = new FileInputStream(testingPreferences.getClass().getSimpleName());
            ObjectInputStream ois = new ObjectInputStream(fis);
            testingPreferences = (TestingPreferences) ois.readObject(); //read object from file
            prototype.testingPreferences = testingPreferences; //set testing preferences inside prototype class
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(prototype);
        t.run();
        //startProcedureButton.setDisable(true);
//        synchronized (prototype.neuralNetworkArchitectureTester.strDump){
//            try {
//                prototype.neuralNetworkArchitectureTester.strDump.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        ANNInfoTextArea.appendText(prototype.neuralNetworkArchitectureTester.strDump.toString()); //insert the information
//        ANNInfoTextArea.setScrollTop(Double.MAX_VALUE); //scroll down
       // populateBarChart();
    }

    /**
     * Populate the bar chart with the neural network performance data
     */
    private void populateBarChart(){
        double worstStandardDeviation = 100;
        for(int i = 0; i < prototype.neuralNetworkArchitectureTester.getNetworkSettingsList().size(); i++){ //for each neural network setting
            NeuralNetworkSettings tmp = prototype.neuralNetworkArchitectureTester.getNetworkSettingsList().get(i); //assign the neural network setting to a local object
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

    private void populateTopFiveBarChart() {
        //orderAllNetworkSettingsByPerformance(); //sort the structures by performance
        barChart.getData().clear(); //clear the bar chart in case it is populated
        System.out.println("prototype = " + prototype.neuralNetworkArchitectureTester.getNeuralNetworkSettingsList());
        for(int i = 0; i < 4; i++){
            NeuralNetworkSettings tmp = prototype.neuralNetworkArchitectureTester.getNeuralNetworkSettingsList().get(i);
            XYChart.Series s = new XYChart.Series<String, Double>();
            s.setName(tmp.getLearningRule().getClass().getSimpleName() + " " + tmp.getTransferFunctionType().getTypeLabel());
            String structure = "" + tmp.getInputNeurons(); //generate structure
            for(int h : tmp.getHiddenLayers()){
                structure += " (" + h + ") ";
            }
            structure += tmp.getOutputNeurons();
            double score = (1 - tmp.getPerformanceScore()) * 100; //calculate performance
            s.getData().add(new XYChart.Data<>(structure, score));
            barChart.getData().add(s);
        }
    }

    /**
     * Set up the menu bar for the interface
     * See http://docs.oracle.com/javafx/2/ui_controls/menu_controls.htm
     * @return
     */
    private MenuBar setupMenuBar(Stage primaryStage){
        MenuBar menuBar = new MenuBar();
        //File menu and sub menus
        final Menu fileMenu = new Menu(FILE_MENU); //file menu
        final MenuItem fileLoadProject = new MenuItem(FILE_MENU_LOAD_PROJECT);
        fileLoadProject.setOnAction(e -> openFileSelectorToLoadProjectFile(primaryStage));
        final MenuItem fileSaveProject = new MenuItem(FILE_MENU_SAVE_PROJECT);
        final MenuItem fileGenerateChart = new MenuItem(FILE_MENU_GENERATE_CHART);
        fileGenerateChart.setOnAction(e -> populateTopFiveBarChart());
        fileMenu.getItems().addAll(fileLoadProject, fileSaveProject, fileGenerateChart); //register sub menu items


        //Options menu and sub menus
        final Menu optionsMenu = new Menu(OPTIONS_MENU);
        final MenuItem optionsPreferences = new MenuItem(OPTIONS_MENU_PREFERENCES);
        optionsMenu.getItems().addAll(optionsPreferences); //register sub menus

        //Neural Network Menu and sub menus
        final Menu nnMenu = new Menu(NEURAL_NETWORK_MENU);
        final MenuItem loadNNMenuItem = new MenuItem(NEURAL_NETWORK_MENU_LOAD_NETWORK);
        loadNNMenuItem.setOnAction(e -> openFileSelectorToNeuralNetworkFile(primaryStage));
        final MenuItem saveNNMenuItem = new MenuItem(NEURAL_NETWORK_MENU_SAVE_NETWORK);
        final MenuItem selectTrainingSetMenuItem = new MenuItem(NEURAL_NETWORK_MENU_SELECT_TRAINING_SET);
        final MenuItem selectTestSetMenuItem = new MenuItem(NEURAL_NETWORK_MENU_SELECT_TEST_SET);
        final MenuItem trainingPreferencesMenuItem = new MenuItem(NEURAL_NETWORK_MENU_TRAINING_PREFERENCES);
        nnMenu.getItems().addAll(loadNNMenuItem, saveNNMenuItem, selectTrainingSetMenuItem, selectTestSetMenuItem, trainingPreferencesMenuItem); //register submenu items

        //Help menu and sub menus
        final Menu helpMenu = new Menu(HELP_MENU);
        final MenuItem aboutMenuItem = new MenuItem(HELP_MENU_ABOUT);
        aboutMenuItem.setOnAction(e -> showAboutScreen()); //lambda expression
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


    /**
     * Display the about screen
     */
    private void showAboutScreen() {
        AboutScreen as = new AboutScreen(primaryStage);
    }

    /**
     * Saves all the custom perceptron data
     * @return
     */
    private boolean saveOperation(){
        try{
            prototype.neuralNetworkArchitectureTester.saveCurrentNetworkSettingsToFile();
            prototype.neuralNetworkArchitectureTester.saveCurrentPerceptronToFile();
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
            prototype.neuralNetworkArchitectureTester.loadPerceptronFromFile("customPerceptron");
            prototype.neuralNetworkArchitectureTester.loadPerceptronSettingsFromFile("customNetworkSettings");
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

    /**
     * Open a file selector for the application to load a project file
     * See http://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
     * @return
     */
    private boolean openFileSelectorToLoadProjectFile(Stage stage){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(FILE_MENU_LOAD_PROJECT_FILECHOOSER_TITLE);
            fileChooser.showOpenDialog(stage);

            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Open a file selector for the application to load a neural network file
     * See http://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
     * @return
     */
    private boolean openFileSelectorToNeuralNetworkFile(Stage stage){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(NEURAL_NETWORK_MENU_LOAD_NETWORK_FILECHOOSER_TITLE);
            File file = fileChooser.showOpenDialog(stage);
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
