import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {
    private static String dataPath = "Knapsack_Instances/";
    private static String resultsPath = "Results/";
    private static String nl = System.getProperty("line.separator");

    public static ArrayList<Integer> getValues(String instanceName) {
        ArrayList<Integer> itemValues = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath + instanceName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Each line has two numbers separated by a space,
                // the first number is the weight of the item and the second number is the value of the item.
                // The first line is different as the first number is the number of items in the file,
                // and the second number is the capacity of the knapsack (on the same line)
                String[] numbers = line.split(" ");

                itemValues.add(Integer.parseInt(numbers[0]));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        // Remove the first value as it is the number of items in the file
        itemValues.remove(0);

        return itemValues;
    }

    public static ArrayList<Integer> getWeights(String instanceName) {
        ArrayList<Integer> itemWeights = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath + instanceName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Each line has two numbers separated by a space,
                // the first number is the weight of the item and the second number is the value of the item.
                // The first line is different as the first number is the number of items in the file,
                // and the second number is the capacity of the knapsack (on the same line)
                String[] numbers = line.split(" ");

                itemWeights.add(Integer.parseInt(numbers[1]));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        // Remove the first value as it is the capacity of the file
        itemWeights.remove(0);

        return itemWeights;
    }

    public static int getCapacity(String instanceName) {
        int capacity = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath + instanceName))) {
            String line;
            
            // Get the second number in the first line of the file
            line = br.readLine();
            String[] numbers = line.split(" ");
            capacity = Integer.parseInt(numbers[1]);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return capacity;
    }

    public static int getSize(String instanceName) {
        int itemCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath + instanceName))) {
            String line;
            
            // Get the first number in the first line of the file
            line = br.readLine();
            String[] numbers = line.split(" ");
            itemCount = Integer.parseInt(numbers[0]);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return itemCount;
    }

    /**
     * Writes the results of the algorithm to a text file
     * @param path
     * @param timeToComplete
     * @param numBins
     */
    public static void writeData(String instanceName, double optimum, float timeTaken) {

    }

    // public static void printSummary(List<int[]> testResults, String path, String algorithm) {
    //     // The first value in the integer array is the time to complete the test,
    //     // Get the average time to complete all the tests in the list
    //     int totalTime = 0;
    //     int totalTests = testResults.size();

    //     // The second value in the integer array is the number of bins used,
    //     // Get the total number of tests where the number of bins used equals the optimal solution (third value in the integer array)
    //     int numOptimalSolutions = 0;

    //     // Get the total number of tests where the number of bins used is within 1 of the optimal solution
    //     int numNearOptimalSolutions = 0;

    //     for (int[] testResult : testResults) {
    //         totalTime += testResult[0];

    //         if (testResult[1] == testResult[2]) {
    //             numOptimalSolutions++;
    //         }

    //         if (testResult[1] == testResult[2] + 1) {
    //             numNearOptimalSolutions++;
    //         }
    //     }
    //     double averageTime = totalTime / totalTests;
    //     averageTime = Math.round(averageTime * 100.0f) / 100.0f;

    //     // Print the summary to a new tset file called Summary.txt
    //     try {
    //         String filename = algorithm == "ILS" ? "/ILSSummary.txt" : "/TabuSummary.txt";
    //         String directory = resultsPath + path + filename;
    //         File file = new File(directory);
    //         file.createNewFile();

    //         FileWriter writer = new FileWriter(file);
    //         writer.write(path);
    //         writer.write(nl);
    //         writer.write(nl);
    //         writer.write("Average time to complete: " + averageTime + "ms");
    //         writer.write(nl);
    //         writer.write("Opt: " + numOptimalSolutions);
    //         writer.write(nl);
    //         writer.write("Opt-1: " + numNearOptimalSolutions);
    //         writer.write(nl);
    //         writer.write("Sum: " + totalTests);
    //         writer.close();
    //         System.out.println("Successfully wrote to " + directory);

    //         // Add the summary to the hashmap
    //         if (algorithm == "ILS") {
    //             performanceValuesILS.add(new Integer[] {numOptimalSolutions, numNearOptimalSolutions, totalTests});
    //             performanceValueNamesILS.add(path);
    //             performanceTimesILS.add(averageTime);
    //         } else {
    //             performanceValuesTS.add(new Integer[] {numOptimalSolutions, numNearOptimalSolutions, totalTests});
    //             performanceValueNamesTS.add(path);
    //             performanceTimesTS.add(averageTime);
    //         }

    //     } catch (IOException e) {
    //         System.err.format("IOException: %s%n", e);
    //     }
    // }

    // public static void printOverallPerformance(String algorithm) {
    //     String folder = algorithm == "ILS" ? "ILSPerformance/" : "TabuPerformance/";
    //     String filename = algorithm == "ILS" ? "ILSPerformance.txt" : "TabuPerformance.txt";
    //     // Print the summary to a new text file called Performance
    //     try {
    //         String directory = resultsPath + folder + filename;
    //         File file = new File(directory);
    //         file.createNewFile();

    //         FileWriter writer = new FileWriter(file);

    //         List<Integer[]> performanceValues = algorithm == "ILS" ? performanceValuesILS : performanceValuesTS;
    //         List<String> performanceValueNames = algorithm == "ILS" ? performanceValueNamesILS : performanceValueNamesTS;
    //         List<Double> performanceTimes = algorithm == "ILS" ? performanceTimesILS : performanceTimesTS;
            
    //         // Print the summary for each entry in the hashmap
    //         for (int i = 0; i < performanceValueNames.size(); i++) {
    //             writer.write("Dataset: " + performanceValueNames.get(i));
    //             writer.write(nl);
    //             writer.write("Optimal: " + performanceValues.get(i)[0] + " | Near: " + performanceValues.get(i)[1] + " | Sum: " + performanceValues.get(i)[2]);
    //             writer.write(nl);
    //             writer.write("Average time: " + performanceTimes.get(i) + "ms");
    //             writer.write(nl);
    //             writer.write("---------------------------------------");
    //             writer.write(nl);
    //             writer.write(nl);
    //         }
    //         writer.close();
    //     } catch (IOException e) {
    //         System.err.format("IOException: %s%n", e);
    //     }
    // }
}
