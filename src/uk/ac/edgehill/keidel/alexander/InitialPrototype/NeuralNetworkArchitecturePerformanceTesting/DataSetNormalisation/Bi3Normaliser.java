package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.DataSetNormalisation;

import java.io.*;

/**
 * Created by Alexander Keidel, 22397868 on 19/10/2016.
 * Normaliser for the data sets provided by Business Intelligence 3 (Bi3)
 * This class should not be run more than once.
 * Provides a format for the artificial neural network architecture testing prototype to work with
 */
public class Bi3Normaliser {
    private FileReader fr;
    private BufferedReader br;
    private FileWriter fw;
    private final String FOOTFALL_FILE_PATH = "D:/OneDrive/Edge Hill/Final Year Project/Interesting Datasets/Bi3 Data Sets/Footfall Data Comma Delimited.csv"; //make sure this is according to the file path on the machine
    private final String DEMOGRAPHICS_FILE_PATH = "D:/OneDrive/Edge Hill/Final Year Project/Interesting Datasets/Bi3 Data Sets/Demographics Data Comma Delimited.csv"; //make sure this is according to the file path on the machine
    private final String DIRECTORY = "D:/OneDrive/Edge Hill/Final Year Project/Interesting Datasets/Bi3 Data Sets/";
    private File footfallFile, demographicsFile;
    public Bi3Normaliser(){
        footfallFile = new File(FOOTFALL_FILE_PATH);
        demographicsFile = new File (DEMOGRAPHICS_FILE_PATH);
        normaliseFootfallFile();
    }

    private void normaliseFootfallFile(){
        try {
            br = new BufferedReader(new FileReader(demographicsFile));
            String fullText = "";
            String currentLine = "";
            while((currentLine = br.readLine()) != null){ //for each line in the file (stops when currentLine == null, i.e. the last line when the file ends)
                //currentLine = currentLine.replace("\n", ""); //remove the new line from the file
                //@TODO make sense of those data sets
                double[] tmp = calculateFootfallOutputValues(currentLine);
                currentLine += "," + tmp[0] + "," + tmp[1] + "\n"; //adding output values to the file
                fullText += currentLine; //add the line to the full body of text
            }
            br.close();

            fw = new FileWriter(new File(DIRECTORY + "New Supervised Demographics.csv")); //new file location for the create file
            fw.write(fullText);
            fw.flush();
            fw.close();

        } catch (Exception ioex){
            ioex.printStackTrace();
        }
    }

    private double[] calculateFootfallOutputValues(String line){
        double[] outputs = new double[2];
        String[] indvValues = line.split(","); //split the line at commas
        outputs[0] = Integer.parseInt(indvValues[4]) / 8000;
        outputs[1] = Integer.parseInt(indvValues[5]) / 50;
        return outputs;
    }

    public static void main(String[] args){
        Bi3Normaliser bi3n = new Bi3Normaliser();
    }
}
