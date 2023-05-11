import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {
    private static String dataPath = "Dataset/"; // Path to the data files

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
}
