import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileHandler {
    private static String dataPath = "Knapsack_Instances/"; // Path to the data files
    private static String resultsPath = "Results/"; // Path to the results files
    private static String nl = System.getProperty("line.separator"); // New line character

    private static HashMap<String, Double[]> GAValues = new HashMap<String, Double[]>(); // Store the values for the GA
    private static HashMap<String, Double[]> ACOValues = new HashMap<String, Double[]>(); // Store the values for the ACO

    /**
     * Get the values of the items in the knapsack instance
     * @param instanceName
     * @return
     */
    public static ArrayList<Double> getValues(String instanceName) {
        ArrayList<Double> itemValues = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath + instanceName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Each line has two numbers separated by a space,
                // the first number is the weight of the item and the second number is the value of the item.
                // The first line is different as the first number is the number of items in the file,
                // and the second number is the capacity of the knapsack (on the same line)
                String[] numbers = line.split(" ");

                itemValues.add(Double.parseDouble(numbers[0]));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        // Remove the first value as it is the number of items in the file
        itemValues.remove(0);

        return itemValues;
    }

    /**
     * Get the weights of the items in the knapsack instance
     * @param instanceName
     * @return
     */
    public static ArrayList<Double> getWeights(String instanceName) {
        ArrayList<Double> itemWeights = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath + instanceName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] numbers = line.split(" ");

                itemWeights.add(Double.parseDouble(numbers[1]));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        // Remove the first value as it is the capacity of the file
        itemWeights.remove(0);

        return itemWeights;
    }

    /**
     * Get the capacity of the knapsack instance
     * @param instanceName
     * @return
     */
    public static double getCapacity(String instanceName) {
        double capacity = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath + instanceName))) {
            String line;
            
            // Get the second number in the first line of the file
            line = br.readLine();
            String[] numbers = line.split(" ");
            capacity = Double.parseDouble(numbers[1]);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return capacity;
    }

    /**
     * Get the number of items in the knapsack instance
     * @param instanceName
     * @return
     */
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
     * Write the results of the algorithm to a text file
     * @param instanceName
     * @param bestSolution
     * @param timeTaken
     */
    public static void writeData(String instanceName, double bestSolution, float timeTaken, String algorithm) {
        // Add the data to the HashMaps first
        if (algorithm.equals("GA")) {
            Double[] values = {bestSolution, (double) timeTaken};
            GAValues.put(instanceName, values);
        }
        else if (algorithm.equals("ACO")) {
            Double[] values = {bestSolution, (double) timeTaken};
            ACOValues.put(instanceName, values);
        }

        // Write a new text file with the instance name to the results path

        // Result directory
        String directory = resultsPath + algorithm + "/" + instanceName + ".txt";

        // Retrieve known optimum solution
        double knownOptimum = KnapsackInstances.getOptimum(instanceName);

        try {
            File file = new File(directory);
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write("Best Solution: " + bestSolution + " | Opt: " + knownOptimum);
            writer.write(nl);
            writer.write("Time Taken: " + timeTaken + "s");
            writer.write(nl);
            writer.close();
        }
        catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    /**
     * Write the summary of the results to a text file
     */
    public static void writeSummary() {
        // Write to a new text file using the Hashmap values of all the instances
        // Result directory
        String directory = resultsPath + "Summary.txt";

        try {
            File file = new File(directory);
            file.createNewFile();

            FileWriter writer = new FileWriter(file);

            // Write the GA results
            writer.write("GA Results");
            writer.write(nl);
            writer.write("====================================================");
            writer.write(nl);
            for (String instanceName : GAValues.keySet()) {
                Double[] values = GAValues.get(instanceName);
                double bestSolution = values[0];
                double timeTaken = values[1];

                // Round the time to 2 decimal places
                timeTaken = Math.round(timeTaken * 100.0) / 100.0;

                // Retrieve known optimum solution
                double knownOptimum = KnapsackInstances.getOptimum(instanceName);

                writer.write(instanceName + " | Sol: " + bestSolution + " Opt: " + knownOptimum + " Time: " + timeTaken + (knownOptimum == bestSolution ? " | OPTIMAL" : " | SUB"));
                writer.write(nl);
            }

            // Write the ACO results
            writer.write("ACO Results");
            writer.write(nl);
            writer.write("====================================================");
            writer.write(nl);
            for (String instanceName : ACOValues.keySet()) {
                Double[] values = ACOValues.get(instanceName);
                double bestSolution = values[0];
                double timeTaken = values[1];

                // Round the time to 2 decimal places
                timeTaken = Math.round(timeTaken * 100.0) / 100.0;

                // Retrieve known optimum solution
                double knownOptimum = KnapsackInstances.getOptimum(instanceName);

                // When writing, ensure that the distance between the columns is consistent
                writer.write(instanceName + " | Sol: " + bestSolution + " Opt: " + knownOptimum + " Time: " + timeTaken + (knownOptimum == bestSolution ? " | OPTIMAL" : " | SUB"));
                writer.write(nl);
            }

            writer.close();
        }
        catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}
