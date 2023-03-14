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
    public static void writeData(String path, int timeToComplete, int numBins, int index, int optimalSolution) {
        try {
            File file = new File(resultsPath + path + "/SOL_" + index + ".txt");
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write("Time to complete: " + timeToComplete + "ms");
            writer.write("Number of bins: " + numBins);
            writer.write("Optimal solution: " + optimalSolution);
            writer.close();
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

        File directory = new File(path);
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
