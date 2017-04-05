package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.DataSetNormalisation;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Alexander Keidel, 22397868 on 27/03/2017.
 * Custom Normaliser to normalise training sets for neural networks
 * It finds the biggest value for each column and uses it to divide all other values
 * This is referred to as a maximum normaliser.
 */
public class CustomMaxNormaliser {
    private ArrayList<ArrayList<Double>> inputValues;
    private ArrayList<ArrayList<Double>> normalisedValues;
    private double[] largestNumberInColumn;
    private boolean normalisationFinished = false;

    /**
     * Constructor using a file and delimiter.
     * Requires rows to be split with a carriage return or new line.
     * @param inputFile input file
     * @param valueDelimiter delimiter (usually ",")
     * @throws FileNotFoundException
     */
    public CustomMaxNormaliser(File inputFile, String valueDelimiter) throws IOException {
        inputValues = new ArrayList<>(); //init
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String row = "";
        while((row = br.readLine()) != null){
            String[] values = row.split(valueDelimiter);
            ArrayList<Double> rowValues = new ArrayList<>(); //ArrayList of doubles to hold the values
            for(String v : values){
                rowValues.add(Double.parseDouble(v)); //parse the value and add it to the rowValues
            }
            inputValues.add(rowValues); //add the row values to the set of input values
        }
        normaliseValues();
    }

    /**
     * Constructor using array list of array list of double values
     * @param inputValues un-normalised training set in doubles
     */
    public CustomMaxNormaliser(ArrayList<ArrayList<Double>> inputValues){
        this.inputValues = inputValues;
        normaliseValues(); //normalise the values
    }

    /**
     * Normalise the values based on the largest number in each column (Max normalisation)
     */
    private void normaliseValues() {
        normalisedValues = new ArrayList<>(); //init array list
        largestNumberInColumn = new double[inputValues.get(0).size()]; //set the size of the array to the number of values in a row
        for(double d : largestNumberInColumn){ //init the array of largest numbers with zeroes
            d = 0.0;
        }
        //find the largest number in each column
        for(int i = 0; i < inputValues.size(); i++){ //for each row
            for(int j = 0; j < inputValues.get(i).size(); j++){ //for each value in the row (column)
                if(inputValues.get(i).get(j) > largestNumberInColumn[j]) largestNumberInColumn[j] = inputValues.get(i).get(j); //assign the value if it is bigger
            }
        }

        for(int i = 0; i < inputValues.size(); i++){ //for each row
            ArrayList<Double> normalisedRow = new ArrayList<>();
            for(int j = 0; j < inputValues.get(i).size(); j++){ //for each value in the row
                normalisedRow.add(inputValues.get(i).get(j) / largestNumberInColumn[j]); //add the value divided by the largest number in the row (max normalised)
            }
            normalisedValues.add(normalisedRow); //add the row the the set of normalised values
        }
        if(normalisedValues.size() != inputValues.size()) throw new IndexOutOfBoundsException("normalisation failed");
        normalisationFinished = true; //set flag to true
    }

    public ArrayList<ArrayList<Double>> getNormalisedValues() {
        return normalisedValues;
    }

    public boolean isNormalisationFinished() {
        return normalisationFinished;
    }

    /**
     * Save the normalised values in a file.
     * @param filePath file path
     * @param fileName file name
     * @param valueDelimiter delimiter for individual values
     * @param lineDelimiter delimiter for row ending (usually new line "\n")
     * @return
     */
    public boolean saveNormalisedValuesToFile(String filePath, String fileName, String valueDelimiter, String lineDelimiter){
        try{
            String outputString = getNormalisedValuesInStringFormat(valueDelimiter, lineDelimiter);
            File outputFile = new File(filePath + fileName);
            FileWriter fileWriter = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(outputString);
            bw.flush();
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Convert the normalised values to a readable string format for printing to the console or a file.
     * @param valueDelimiter delimiter between individual values in a row
     * @param lineDelimiter line delimiter (usually "\n" new line)
     * @return
     */
    public String getNormalisedValuesInStringFormat(String valueDelimiter, String lineDelimiter){
        String normalisedString = "";
        for(ArrayList<Double> row : normalisedValues){ //for each row
            String normalisedRow = "";
            for(double value : row){ //for each value in a row
                normalisedRow += value + valueDelimiter; //add the value plus the valueDelimiter
            }
            normalisedString += normalisedRow.substring(0, normalisedRow.length() - 1) + lineDelimiter; //add the row minus the last valueDelimiter plus the lineDelimiter to the final output string
        }
        return normalisedString;
    }
}
