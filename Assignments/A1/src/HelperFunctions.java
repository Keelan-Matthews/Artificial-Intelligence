import java.util.ArrayList;
import java.util.List;

public class HelperFunctions {

    private int MAX_CAPACITY = 1000;

    // Get each path in the datasets array that matches the dataset parameter
    public static List<String> getDatasetPaths(String dataset) {

        String[] datasets = {
            "Falkenauer/Falkenauer_T",
            "Falkenauer/Falkenauer_U",
            "Hard28",
            "Scholl/Scholl_1",
            "Scholl/Scholl_2",
            "Scholl/Scholl_3",
            "Schwerin/Schwerin_1",
            "Schwerin/Schwerin_2",
            "Waescher"
        };

        List<String> matchingPaths = new ArrayList<>();
        for (String path : datasets) {
            if (path.contains(dataset)) {
                matchingPaths.add(path);
            }
        }
        return matchingPaths;
    }


    public int getBinCapacity() {
        return MAX_CAPACITY;
    }

    public List<int[]> getValues(String dataset) {
        
        List<String> paths = getDatasetPaths(dataset);

        // Get the values from the dataset
        List<int[]> values = new ArrayList<>();
        
        for (String path : paths) {
            // Get the names of the files in the directory
            String[] files = FileHandler.getTextFileNames(path);

            // Get the values from each file
            for (String file : files) {
                values.add(FileHandler.readData(path + "/" + file).stream().mapToInt(i -> i).toArray());
            }
        }

        return values;
    }

}
