package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * Created by Alexander Keidel, 22397868 on 12/02/2017.
 * Interface class which holds values for the GUI
 * All values inside this interface are "public static final".
 */
public interface GUIValues {
    String TITLE = "Neural Network Prototype";

    //buttons at the bottom of main UI
    String START_PROCEDURE_BUTTON_TEXT = "Start";
    String SETUP_BUTTON_TEXT = "Setup";
//    int BUTTON_WIDTH = 80;
//    int BUTTON_HEIGHT = 35;
    float PREF_BUTTON_WIDTH = 80.0f;
    float PREF_BUTTON_HEIGHT = 35.0f;
    Insets BUTTON_INSETS = new Insets(5);

    //Menu bar items

    String FILE_MENU = "File";
    //sub menus
    String FILE_MENU_SAVE_PROJECT = "Save Project";
    String FILE_MENU_SAVE_AS = "Save as...";
    String FILE_MENU_LOAD_PROJECT = "Load Project";
    String FILE_MENU_LOAD_PROJECT_FILECHOOSER_TITLE = "Select a project file to load";
    String FILE_MENU_GENERATE_CHART = "Generate Chart";


    String NEURAL_NETWORK_MENU = "Neural Network";
    //sub menus
    String NEURAL_NETWORK_MENU_SELECT_TRAINING_SET = "Select Training Set";
    String NEURAL_NETWORK_MENU_SELECT_TEST_SET = "Select Test Set";
    //Changing performance value should be a slider or text box//String NEURAL_NETWORK_MENU_CHANGE_PERFORMANCE_VALUE = "Change Performance Value";
    String NEURAL_NETWORK_MENU_SAVE_NETWORK = "Save Network";
    String NEURAL_NETWORK_MENU_LOAD_NETWORK = "Load Network";
    String NEURAL_NETWORK_MENU_LOAD_NETWORK_FILECHOOSER_TITLE = "Select a neural network file to load";
    String NEURAL_NETWORK_MENU_TRAINING_PREFERENCES = "Training Preferences";


    String OPTIONS_MENU = "Options";
    //sub menus
    String OPTIONS_MENU_PREFERENCES = "Preferences";


    String HELP_MENU = "Help";
    //sub menus
    String HELP_MENU_ABOUT = "About";
    String ABOUT_MENU_TEXT = "Artificial Neural Network Architecture Evaluation Tool\nThis prototype was created by Alexander Keidel.\nContact: Alexander.Keidel@Go.Edgehill.ac.uk";


    //Preference screen values
    String PREFERENCES_SCREEN_INSTRUCTIONS = "Select items to test";
    String PREFERENCES_SCREEN_LEARNING_RULES_TAB = "Learning rules";
    String PREFERENCES_SCREEN_TRANSFER_FUNCTIONS_TAB = "Transfer functions";
    String PREFERENCES_SCREEN_BOUNDARIES_TAB = "Test Boundaries";
    String PREFERENCES_SCREEN_BOUNDARIES_NETWORK_TESTING_TITLE = "Network Testing Options";
    String PREFERENCES_SCREEN_BOUNDARIES_TEST_NAME = "Test Name: ";
    String PREFERENCES_SCREEN_BOUNDARIES_INPUT_LAYER_SIZE = "Input Neurons: ";
    String PREFERENCES_SCREEN_BOUNDARIES_OUTPUT_LAYER_SIZE = "Output Neurons: ";
    String PREFERENCES_SCREEN_BOUNDARIES_NETWORK_TESTING_PERFORMANCE_SCORE = "Performance Limit (Standard Deviation): ";
    String PREFERENCES_SCREEN_BOUNDARIES_MAXIMUM_HIDDEN_LAYERS_TEXT = "Maximum hidden layers: ";
    String PREFERENCES_SCREEN_BOUNDARIES_MINIMUM_HIDDEN_LAYER_SIZE_TEXT = "Minimum size of each hidden layer: ";
    String PREFERENCES_SCREEN_BOUNDARIES_MAXIMUM_HIDDEN_LAYER_SIZE_TEXT = "Maximum size of each hidden layer: ";
    String PREFERENCES_SCREEN_BOUNDARIES_TEST_SET_FILE_NAME = "Test set file: ";
    String PREFERENCES_SCREEN_BOUNDARIES_OVERFITTING_SET_FILE_NAME = "Overfitting set file: ";
    String PREFERENCES_SCREEN_BOUNDARIES_TRAINING_SET_FILE_NAME = "Training set file: ";
    String PREFERENCES_SCREEN_BOUNDARIES_SELECT_BUTTON = "Select";
//    String PREFERENCES_SCREEN_BOUNDARIES_
//    String PREFERENCES_SCREEN_BOUNDARIES_
//    String PREFERENCES_SCREEN_BOUNDARIES_

    //note that the default background colour is Color.web("#F4F4F4")
    Background PREFERENCES_SCREEN_BACKGROUND = new Background(new BackgroundFill(Color.web("#AAA"), CornerRadii.EMPTY, Insets.EMPTY));

}
