import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {
    private static String dataPath = "Dataset/"; // Path to the data files
    private static String resultsPath = "Results/"; // Path to the results files
    private static String nl = System.getProperty("line.separator"); // New line character

    public static ArrayList<String> readFile(String dataset) {
        ArrayList<String> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath + dataset))) {
            String line;
            while ((line = br.readLine()) != null) {
                items.add(line);
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return items;
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

            // Write the ACO results
            writer.write("ACO Results");
            writer.write(nl);
            writer.write("====================================================");
            writer.write(nl);

            writer.close();
        }
        catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}
