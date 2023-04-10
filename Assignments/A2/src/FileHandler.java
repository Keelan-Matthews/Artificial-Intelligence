import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static String dataPath = "Knapsack Instances/";
    private static String resultsPath = "Results/";
    private static String nl = System.getProperty("line.separator");

    private static List<Integer[]> performanceValuesILS = new ArrayList<>();
    private static List<Double> performanceTimesILS = new ArrayList<>();
    private static List<String> performanceValueNamesILS = new ArrayList<>();
    private static List<Integer[]> performanceValuesTS = new ArrayList<>();
    private static List<Double> performanceTimesTS = new ArrayList<>();
    private static List<String> performanceValueNamesTS = new ArrayList<>();

    
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
    public static void writeData(String path, int timeToComplete, List<List<Integer>> bestSolution, int index, int optimalSolution, String algorithm, String testName) {
        try {
            String directory = resultsPath + path + "/" + algorithm + "/SOL_" + testName;
            File file = new File(directory);
            file.createNewFile();
            int numBins = bestSolution.size();

            FileWriter writer = new FileWriter(file);
            writer.write("Time to complete: " + timeToComplete + "ms");
            writer.write(nl);
            writer.write("Number of bins: " + numBins);
            writer.write(nl);
            writer.write("Optimal solution: " + optimalSolution);
            writer.write(nl);
            // if number of bins used equals the optimal solution, the solution is optimal,
            // if number of bins used is at most 1 more than the optimal solution, the solution is near-optimal,
            // otherwise the solution is sub-optimal
            writer.write("Optimization level: " + (numBins <= optimalSolution ? "OPTIMAL" : (numBins == optimalSolution + 1 ? "NEAR-OPTIMAL" : "SUB-OPTIMAL")));
            writer.write(nl);
            writer.write("Optimized percentage: " + (int) Math.round(((double) optimalSolution / numBins) * 100) + "%");
            writer.write(nl);
            writer.write(nl);
            for (List<Integer> bin : bestSolution) {
                writer.write("[ ");
                for (Integer item : bin)
                writer.write(item + " ");
                writer.write("] ");
                writer.write("S: " + bin.stream().mapToInt(Integer::intValue).sum());
                writer.write(nl);
            }
            
            writer.close();

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public static void printSummary(List<int[]> testResults, String path, String algorithm) {
        // The first value in the integer array is the time to complete the test,
        // Get the average time to complete all the tests in the list
        int totalTime = 0;
        int totalTests = testResults.size();

        // The second value in the integer array is the number of bins used,
        // Get the total number of tests where the number of bins used equals the optimal solution (third value in the integer array)
        int numOptimalSolutions = 0;

        // Get the total number of tests where the number of bins used is within 1 of the optimal solution
        int numNearOptimalSolutions = 0;

        for (int[] testResult : testResults) {
            totalTime += testResult[0];

            if (testResult[1] == testResult[2]) {
                numOptimalSolutions++;
            }

            if (testResult[1] == testResult[2] + 1) {
                numNearOptimalSolutions++;
            }
        }
        double averageTime = totalTime / totalTests;
        averageTime = Math.round(averageTime * 100.0f) / 100.0f;

        // Print the summary to a new tset file called Summary.txt
        try {
            String filename = algorithm == "ILS" ? "/ILSSummary.txt" : "/TabuSummary.txt";
            String directory = resultsPath + path + filename;
            File file = new File(directory);
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(path);
            writer.write(nl);
            writer.write(nl);
            writer.write("Average time to complete: " + averageTime + "ms");
            writer.write(nl);
            writer.write("Opt: " + numOptimalSolutions);
            writer.write(nl);
            writer.write("Opt-1: " + numNearOptimalSolutions);
            writer.write(nl);
            writer.write("Sum: " + totalTests);
            writer.close();
            System.out.println("Successfully wrote to " + directory);

            // Add the summary to the hashmap
            if (algorithm == "ILS") {
                performanceValuesILS.add(new Integer[] {numOptimalSolutions, numNearOptimalSolutions, totalTests});
                performanceValueNamesILS.add(path);
                performanceTimesILS.add(averageTime);
            } else {
                performanceValuesTS.add(new Integer[] {numOptimalSolutions, numNearOptimalSolutions, totalTests});
                performanceValueNamesTS.add(path);
                performanceTimesTS.add(averageTime);
            }

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

    public static void printOverallPerformance(String algorithm) {
        String folder = algorithm == "ILS" ? "ILSPerformance/" : "TabuPerformance/";
        String filename = algorithm == "ILS" ? "ILSPerformance.txt" : "TabuPerformance.txt";
        // Print the summary to a new text file called Performance
        try {
            String directory = resultsPath + folder + filename;
            File file = new File(directory);
            file.createNewFile();

            FileWriter writer = new FileWriter(file);

            List<Integer[]> performanceValues = algorithm == "ILS" ? performanceValuesILS : performanceValuesTS;
            List<String> performanceValueNames = algorithm == "ILS" ? performanceValueNamesILS : performanceValueNamesTS;
            List<Double> performanceTimes = algorithm == "ILS" ? performanceTimesILS : performanceTimesTS;
            
            // Print the summary for each entry in the hashmap
            for (int i = 0; i < performanceValueNames.size(); i++) {
                writer.write("Dataset: " + performanceValueNames.get(i));
                writer.write(nl);
                writer.write("Optimal: " + performanceValues.get(i)[0] + " | Near: " + performanceValues.get(i)[1] + " | Sum: " + performanceValues.get(i)[2]);
                writer.write(nl);
                writer.write("Average time: " + performanceTimes.get(i) + "ms");
                writer.write(nl);
                writer.write("---------------------------------------");
                writer.write(nl);
                writer.write(nl);
            }
            writer.close();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}
