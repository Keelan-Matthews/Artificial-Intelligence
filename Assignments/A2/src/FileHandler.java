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

    // Overload getValues to deal with doubles
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

    // Overload getWeights to deal with doubles
    public static ArrayList<Double> getWeights(String instanceName) {
        ArrayList<Double> itemWeights = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath + instanceName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Each line has two numbers separated by a space,
                // the first number is the weight of the item and the second number is the value of the item.
                // The first line is different as the first number is the number of items in the file,
                // and the second number is the capacity of the knapsack (on the same line)
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

    // Overload getCapacity to deal with doubles
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
}
