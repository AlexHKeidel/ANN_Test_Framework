package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.DataSetNormalisation;

import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;

/**
 * Created by Alexander Keidel, 22397868 on 08/03/2017.
 * Crime data set normaliser for neural network training and testing
 */
public class CrimeDataNormaliser {
    public CrimeDataNormaliser(){
        convertDataSetToNeuralNetworkTrainingAndTestSet();

        //This example shows how to convert a string into a double value and print it out to the console
        //This concept is applied to the data set in order to create inputs for the training of neural networks
//        String st = "lallalalalalalallalllalalalala";
//        byte[] bytes = st.getBytes();
//        //double doublevalue = ByteBuffer.wrap(bytes).getDouble();
//        //System.out.println(String.valueOf(doublevalue));

//        String testString = "this is a test of some text that I want to convert into a decimal value independent of it's length";
//        byte[] bytes = testString.getBytes();
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//        String tmp = "";
//        int f = 0;
//        while((f = byteArrayInputStream.read()) != -1){
//            System.out.println(f);
//        }
    }

    /**
     * file crime2015.csv
     * Format: Crime ID,Month,Reported by,Falls within,Longitude,Latitude,Location,LSOA code,LSOA name,Crime type,Last outcome category,Context
     */
    private void convertDataSetToNeuralNetworkTrainingAndTestSet() {
        File inputFile = new File(System.getProperty("user.dir") + "/Data Sets/Crime Data/crime2015.csv");
        try {
            BufferedReader br = new BufferedReader(new FileReader((inputFile)));
            FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "/Data Sets/Crime Data/crime2015 full supervised set.csv");
            String currentLine = "";
            String fullNormalisedData = "";
            long counter = 0;
            try {
                while ((currentLine = br.readLine()) != null) {
                    if (!currentLine.equals("ERROR")) {
                        fullNormalisedData += normaliseCrime2015(currentLine);
                        counter++;
                        //System.out.println(normaliseCrime2015(currentLine));
                    }
                    //System.out.println("fullNormalisedData" + fullNormalisedData);
                    //trying to improve performance, every 10000 read lines write to file (append)
                    if(counter % 10000 == 0){
                        fileWriter.append(fullNormalisedData);
                        fileWriter.flush();
                        fullNormalisedData = "";
                        //System.out.println("appending file");
                    }
                    System.out.println(counter);
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
            fileWriter.write(fullNormalisedData);
            fileWriter.flush();
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the first attempt at creating a supervised training and testing set from the following data:
     * file crime2015.csv
     * Format: Crime ID,Month,Reported by,Falls within,Longitude,Latitude,Location,LSOA code,LSOA name,Crime type,Last outcome category,Context
     * The input values are: Month, Reported by, Falls within, Longitude, Latitude, Location, LSOA code, LSOA name
     * The output values are: Crime type
     * This example data set will be trying to prove or disprove that predicting the type of a crime based on this input data is possible
     */
    private String normaliseCrime2015(String currentLine) {
        try {
            String[] values = currentLine.split(","); //split current line into individual values
            String normalisedString = "";
            //ignore values[0] as it is the crime ID
            String[] normalisedValues = new String[9]; //8 inputs and 1 output (starting at 1 going through to 9
            for (int i = 0; i < normalisedValues.length; i++) {
                if(values[i + 1].equals("")) return "ERROR"; //make sure input & output count is correct
                byte[] bytes = values[i + 1].getBytes();
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                double readValue = -2;
                double totalValue = 0;
                while ((readValue = bais.read()) != -1) {
                    totalValue += readValue;
                }
                //System.out.println("totalValue = " + totalValue);
                normalisedValues[i] = String.valueOf(totalValue);
                normalisedString += normalisedValues[i] + ","; //concat the total string with the calculated value plus a comma delimiter
            }

            normalisedString = normalisedString.substring(0, normalisedString.length() - 1);

//        String input1 = String.valueOf(ByteBuffer.wrap(values[1].getBytes()).getDouble()); //convert the input value into a byte array, this into a double value, which is then saved "literally" into a string
//        String input2 = String.valueOf(ByteBuffer.wrap(values[2].getBytes()).getDouble()); //convert the input value into a byte array, this into a double value, which is then saved "literally" into a string
//        String input3 = String.valueOf(ByteBuffer.wrap(values[3].getBytes()).getDouble()); //convert the input value into a byte array, this into a double value, which is then saved "literally" into a string
//        String input4 = String.valueOf(ByteBuffer.wrap(values[4].getBytes()).getDouble()); //convert the input value into a byte array, this into a double value, which is then saved "literally" into a string
//        String input5 = String.valueOf(ByteBuffer.wrap(values[5].getBytes()).getDouble()); //convert the input value into a byte array, this into a double value, which is then saved "literally" into a string
//        String input6 = String.valueOf(ByteBuffer.wrap(values[6].getBytes()).getDouble()); //convert the input value into a byte array, this into a double value, which is then saved "literally" into a string
//        String input7 = String.valueOf(ByteBuffer.wrap(values[7].getBytes()).getDouble()); //convert the input value into a byte array, this into a double value, which is then saved "literally" into a string
//        String input8 = String.valueOf(ByteBuffer.wrap(values[8].getBytes()).getDouble()); //convert the input value into a byte array, this into a double value, which is then saved "literally" into a string
            return normalisedString + System.lineSeparator();
        } catch (Exception ex){
            ex.printStackTrace();
            return "ERROR";
        }
    }

    public static void main(String args[]){
        CrimeDataNormaliser cdn = new CrimeDataNormaliser();
    }
}
