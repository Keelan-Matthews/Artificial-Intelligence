import java.util.HashMap;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {

        String[] datasets = {
            "Falkenauer/Falkenauer_T",
            "Falkenauer/Falkenauer_U",
            "Hard28",
            "Scholl/Scholl_1",
            "Scholl/Scholl_2",
            "Scholl/Scholl_3",
            "Schwerin/Schwerin_1",
            "Schwerin/Schwerin_2",
            "Waescher",
        };

        // Generate a hash map of the dataset names and a list of the values in each text file

        // In this HashMap, the key is the name of the dataset and the value is a list of arrays,
        // The list contains an array for each text file in the dataset, and the array contains
        // the values in the text file
        HashMap<String, List<int[]>> datasetValues = new HashMap<>();
        HashMap<String, String[]> datasetNames = new HashMap<>();

        for (String dataset : datasets) {
            datasetValues.put(dataset, HelperFunctions.getValues(dataset));
            datasetNames.put(dataset, FileHandler.getTextFileNames(dataset));
        }

        // Run the ILS algorithm on each dataset
        ILS ils = new ILS(datasets[0], datasetValues.get(datasets[0]), datasetNames.get(datasets[0]));
        ils.run();

        // for (String dataset : datasets) {
        //     System.out.println("Dataset: " + dataset);
        //     // ILS ils = new ILS(datasetValues.get(dataset));
        //     // ils.run();
        // }
    }
}
