package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;

/**
 * Created by Alexander Keidel, 22397868 on 12/02/2017.
 * Interface class which holds values for the GUI
 * All values inside this interface are "public static final".
 */
public interface GUIValues {
    String TITLE = "Neural Network Prototype";

    //Menu bar items

    String FILE_MENU = "File";
    //sub menus
    String FILE_MENU_SAVE_PROJECT = "Save Project";
    String FILE_MENU_LOAD_PROJECT = "Load Project";
    String FILE_MENU_GENERATE_CHART = "Generate Chart";


    String NEURAL_NETWORK_MENU = "Neural Network";
    //sub menus
    String NEURAL_NETWORK_MENU_SELECT_TRAINING_SET = "Select Training Set";
    String NEURAL_NETWORK_MENU_SELECT_TEST_SET = "Select Test Set";
    //Changing performance value should be a slider or text box//String NEURAL_NETWORK_MENU_CHANGE_PERFORMANCE_VALUE = "Change Performance Value";
    String NEURAL_NETWORK_MENU_SAVE_NETWORK = "Save Network";
    String NEURAL_NETWORK_MENU_LOAD_NETWORK = "Load Network";


    String OPTIONS_MENU = "Options";
    //sub menus


    String HELP_MENU = "Help";
    //sub menus
    String HELP_MENU_ABOUT = "About";
    String ABOUT_MENU_TEXT = "This prototype was created by Alexander Keidel.\nContact: Alexander.Keidel@Go.Edgehill.ac.uk";



}
