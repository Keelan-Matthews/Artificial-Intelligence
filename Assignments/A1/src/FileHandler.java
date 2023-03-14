import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static String dataPath = "Datasets/";
    private static String resultsPath = "Results/";
    private static String nl = System.getProperty("line.separator");
    
    /**
     * Reads the data from a text file and returns it as a list of integers
     * @param path
     * @return
     */
    public static List<Integer> readData(String path) {
        List<Integer> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath + path))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return data;
    }

    /**
     * Writes the results of the algorithm to a text file
     * @param path
     * @param timeToComplete
     * @param numBins
     */
    public static void writeData(String path, int timeToComplete, int numBins, int index, int optimalSolution, String algorithm) {
        try {
            String directory = resultsPath + path + "/" + algorithm + "/SOL_" + index + ".txt";
            File file = new File(directory);
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write("Time to complete: " + timeToComplete + "ms");
            writer.write(nl);
            writer.write("Number of bins: " + numBins);
            writer.write(nl);
            writer.write("Optimal solution: " + optimalSolution);
            writer.write(nl);
            writer.write("Optimization quality: " + ((optimalSolution - numBins == 0) ? "OPTIMAL" : (optimalSolution - numBins) == 1 ? "NEAR-OPTIMAL" : "SUB-OPTIMAL"));
            writer.close();
            System.out.println("Successfully wrote to " + directory);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public static void printSummary(List<int[]> testResults, String path) {
        // The first value in the integer array is the time to complete the test,
        // Get the average time to complete all the tests in the list
        int totalTime = 0;
        int totalTests = testResults.size();
        for (int[] testResult : testResults) {
            totalTime += testResult[0];
        }
        float averageTime = totalTime / totalTests;
        averageTime = Math.round(averageTime * 100.0f) / 100.0f;

        // The second value in the integer array is the number of bins used,
        // Get the total number of tests where the number of bins used equals the optimal solution (third value in the integer array)
        int numOptimalSolutions = 0;
        for (int[] testResult : testResults) {
            if (testResult[1] == testResult[2]) {
                numOptimalSolutions++;
            }
        }

        // Get the total number of tests where the number of bins used is within 1 of the optimal solution
        int numNearOptimalSolutions = 0;
        for (int[] testResult : testResults) {
            if (testResult[1] == testResult[2] - 1) {
                numNearOptimalSolutions++;
            }
        }

        // Print the summary to a new tset file called Summary.txt
        try {
            String directory = resultsPath + path + "/ILSSummary.txt";
            File file = new File(directory);
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write("Average time to complete: " + averageTime + "ms");
            writer.write(nl);
            writer.write("Opt: " + numOptimalSolutions);
            writer.write(nl);
            writer.write("Opt-1: " + numNearOptimalSolutions);
            writer.write(nl);
            writer.write("Sum: " + totalTests);
            writer.close();
            System.out.println("Successfully wrote to " + directory);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    /**
     * Returns the names of all the text files in the given directory
     * @param path
     * @return
     */
    public static String[] getTextFileNames(String path) {
        List<String> fileNames = new ArrayList<>();

        File directory = new File(dataPath + path);

        File[] files = directory.listFiles();

        // Add the names of the files to the list
        for (File file : files) {
            if (file.isFile()) {
                fileNames.add(file.getName());
            }
        }

        String[] fileNamesArray = new String[fileNames.size()];
        return fileNames.toArray(fileNamesArray);
    }
}
