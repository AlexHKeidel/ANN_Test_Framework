package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Interface;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Pair;
import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.NeuralNetworkSettings;

/**
 * Created by Alexander Keidel, 22397868 on 26/03/2017.
 * Individual performance screen for neural network tests {@link NeuralNetworkSettings}
 */
public class NeuralNetworkTestScreen extends Stage implements GUIValues {
    NumberAxis xAxis;
    NumberAxis yAxis;
    LineChart<Number, Number> performanceLineChart;
    XYChart.Series testSetSeries;
    XYChart.Series overfittingSetSeries;
    Stage myStage;
    NeuralNetworkSettings mySettings;

    public NeuralNetworkTestScreen(NeuralNetworkSettings mySettings){
        this.mySettings = mySettings;
        myStage = new Stage();
        myStage.setTitle(mySettings.getName());
        xAxis = new NumberAxis(); //setup axis
        yAxis = new NumberAxis(); //setup axis
        performanceLineChart = new LineChart<Number, Number>(xAxis, yAxis); // new line chart
        performanceLineChart.setTitle("Performance (MSE): " + mySettings.getReadableShortName());
        testSetSeries = new XYChart.Series();
        testSetSeries.setName("Test set");
        overfittingSetSeries = new XYChart.Series();
        overfittingSetSeries.setName("Overfitting set");
        performanceLineChart.getData().addAll(testSetSeries, overfittingSetSeries);
        performanceLineChart.setCreateSymbols(false); //remove the circles from the graphs so just lines are left
        Scene scene = new Scene(performanceLineChart, 800, 400);
        generateGraphFromData();
        myStage.setScene(scene);
        myStage.show();
    }

    /**
     * Populate the line graph with the values present in the sets.
     */
    private void generateGraphFromData() {
        try {

        testSetSeries.getData().clear(); //clear data
        for (Pair<Integer, Double> pair : mySettings.getTestSetPerformances()) { //add all data
            testSetSeries.getData().add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
        }
        overfittingSetSeries.getData().clear(); //clear data
        for (Pair<Integer, Double> pair : mySettings.getOverfittingTestSetPerformances()) { //add all data
            overfittingSetSeries.getData().add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
        }
        } catch (Exception ex)
    {
        ex.printStackTrace();
    }
        //performanceLineChart.getData().clear(); //clear data from chart
        //performanceLineChart.getData().addAll(testSetSeries, overfittingSetSeries);
    }

    public void updateTestSeries(Pair<Integer, Double> vals){
        testSetSeries.getData().add(new XYChart.Data(vals.getKey(), vals.getValue())); //add the values to the series
    }

    public void updateOverfittingSeries(Pair<Integer, Double> vals){
        overfittingSetSeries.getData().add(new XYChart.Data(vals.getKey(), vals.getValue())); //add the values to the series

    }
}
