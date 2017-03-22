package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.DataSetNormalisation;

import uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.GlobalVariablesInterface;

import java.io.*;

/**
 * Created by Alexander Keidel, 22397868 on 19/10/2016.
 * Normaliser for the data sets provided by Business Intelligence 3 (Bi3)
 * This class should not be run more than once.
 * Provides a format for the artificial neural network architecture testing prototype to work with
 */
public class Bi3Normaliser implements GlobalVariablesInterface {
    private FileReader fr;
    private BufferedReader br;
    private FileWriter fw;
    private final String FOOTFALL_FILE_PATH = System.getProperty("user.dir") + "/Data Sets/Bi3 Data Sets/Footfall Data Comma Delimited.csv"; //OLD "D:/OneDrive/Edge Hill/Final Year Project/Interesting Datasets/Bi3 Data Sets/Footfall Data Comma Delimited.csv"; //make sure this is according to the file path on the machine
    private final String DEMOGRAPHICS_FILE_PATH = System.getProperty("user.dir") + "/Data Sets/Bi3 Data Sets/Demographics Data Comma Delimited.csv";//OLD "D:/OneDrive/Edge Hill/Final Year Project/Interesting Datasets/Bi3 Data Sets/Demographics Data Comma Delimited.csv"; //make sure this is according to the file path on the machine
    private final String DIRECTORY = System.getProperty("user.dir") + "/Data Sets/Bi3 Data Sets/"; //OLD "D:/OneDrive/Edge Hill/Final Year Project/Interesting Datasets/Bi3 Data Sets/";
    private File footfallFile, demographicsFile;
    public Bi3Normaliser(){
        footfallFile = new File(FOOTFALL_FILE_PATH);
        demographicsFile = new File (DEMOGRAPHICS_FILE_PATH);
        generateSupervisedDemograhpicsFile();
    }

    /**
     * Generate two files based on the provided demographics data set.
     * The files are split 70% training and 30% testing.
     */
    private void generateSupervisedDemograhpicsFile(){
        try {
            br = new BufferedReader(new FileReader(demographicsFile));
            double counter = 0;
            String fullText = "";
            String currentLine = "";
            while((currentLine = br.readLine()) != null){ //for each line in the file (stops when currentLine == null, i.e. the last line when the file ends)
                //currentLine = currentLine.replace("\n", ""); //remove the new line from the file
                //@TODO make sense of those data sets
//                double[] tmp = calculateFootfallOutputValues(currentLine);
//                currentLine += "," + tmp[0] + "," + tmp[1] + "\n"; //adding output values to the file
                fullText += normaliseToTrainingData(currentLine); //add the line to the full body of text
                counter++; //increment counter
            }
            br.close();
            //split the full text into training and test set (70 / 30 distribution)
            //@TODO make the selection random, instead of taking the first 70% for training and the last 30% for testing
            /**
             * The following code which splits the data set in two does not quite work, one line gets split, but have been deleted manually
             * Suggestion: Since we know how many lines there are, we could add the full lines up to a certain point within
             * the while loop before this code
             */
            double fullTextLength = fullText.length();
            String trainingData = fullText.substring(0, (int) Math.round(fullTextLength * 0.7) + 1); //start index inclusive, end index exclusive!
            String testData = fullText.substring((int) Math.round(fullTextLength * 0.7), (int) fullTextLength - 1);
            fw = new FileWriter(new File(DIRECTORY + "Demographics Training Set")); //new file location for the create file
            fw.write(trainingData);
            fw.flush();
            fw = new FileWriter(new File(DIRECTORY + "Demographics Test Set"));
            fw.write(testData);
            fw.flush();
            fw.close();

        } catch (Exception ioex){
            ioex.printStackTrace();
        }
    }

    /**
     * @
     * @param line
     * @return
     */
    @Deprecated private double[] calculateFootfallOutputValues(String line){
        double[] outputs = new double[2];
        String[] indvValues = line.split(","); //split the line at commas
        outputs[0] = Integer.parseInt(indvValues[4]) / 8000;
        outputs[1] = Integer.parseInt(indvValues[5]) / 50;
        return outputs;
    }

//    /**
//     * Takes one line of the data and converts it according to human specified logic
//     * @param oData
//     * @return
//     * @deprecated Do not use, old and outdated!
//     */
//    private String normaliseToTrainingData(String oData) { //pass old data
//        double[] supervisedLearningOutcome = new double[2];
//        String[] indvValues = oData.split(","); //split the line at commas
//        String nData = ""; //this dataset ignores the db primary key (ID) and the number of the capture device used (they are all 1), i.e. indvValues[0] and indvValues[1]
//        if(indvValues[2].equals("M")) nData += "1.0,"; else nData += "0.0,"; //add numeric values on male and female plus a comma delimiter
////        nData += (double) Integer.parseInt(indvValues[3]) / 100; //age divided by 100
////        supervisedLearningOutcome[0] = (double) Integer.parseInt(indvValues[4]) / 8000; //time in camera divided by 8000
////        supervisedLearningOutcome[1] = (double) Integer.parseInt(indvValues[5]) / 50; //time looking at the camera divided by 50
//        nData += (double) Integer.parseInt(indvValues[3]); //old //age divided by 100
////        supervisedLearningOutcome[0] = (double) Integer.parseInt(indvValues[4]); //time in camera divided by 8000
////        supervisedLearningOutcome[1] = (double) Integer.parseInt(indvValues[5]); //time looking at the camera divided by 50
////        nData += supervisedLearningOutcome[0] + "," + supervisedLearningOutcome[1] + "," + supervisedLearningOutcome[0] + "," + supervisedLearningOutcome[1];
//
//        return nData;
//    }

    /**
     * Converts the values from the Demographics Database provided by Bi3
     * The format is as follows:
     * person ID,   machine ID, gender, attendance time (in view of camera, seconds?), attention Time (looking at camera, in seconds?), and a time stamp.
     * For the purpose of this the person id, machine id, and time stamp will be ignored.
     * @TODO add the time stamp into the input values
     * This leaves four input values,
     * gender, age, attendance time, and attention time
     * It was decided that to provide some supervision to the data, younger people are more valuable for the outcome.
     * This leads to the equation used in this code.
     * @param originalData original line of data from the Bi3 Demographics file
     * @return newly formatted string for neural network training and testing
     */
    private String normaliseToTrainingData(String originalData){
        String newData = ""; //new data string
        String[] individualLines = originalData.split(","); //split the data at commas
        double age = Double.parseDouble(individualLines[3]);
        double attendanceTime = Double.parseDouble(individualLines[4]);
        double attentionTime = Double.parseDouble(individualLines[5]);

        //supervised learning outcome
        //@TODO play around with this outcome value and the algorithm to calculate it!
        //Tried versions:
        /**
         * Version #1:
         * (attendanceTime + (attentionTime * 2)) / age;
         * We take the attendance time, add the attentionTime times two (this means that attention time is regarded as
         * twice as valuable as attendance time) and divide by age. This means a higher age leads to a smaller value,
         * thus valuing younger people more. Gender is not in this equation. (At this stage the date is not part of the input neurons!)
         */
        double supervisedLearningOutcome = (attendanceTime + (attentionTime * 2)) / age;

        //lines [0] and [1] are ignored. (person id and machine id)
        if(individualLines[2].equals("M") || individualLines[2].equals('M')) newData += "1.0"; else newData += "0.0"; //if person is male set to 1.0 else 0.0 (there is no gender discrimination intended here, values are chosen at random!"
        newData += "," + age + "," + attendanceTime + "," + attentionTime + "," + supervisedLearningOutcome + "\n"; //add comma delimiter and age, attendance time, attention time, and the supervised learning outcome.
        return newData; //return new data
    }

    public static void main(String[] args){
        Bi3Normaliser bi3n = new Bi3Normaliser();
    }
}
