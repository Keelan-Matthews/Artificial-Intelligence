import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    
    public static List<Integer> readData(String path) {
        List<Integer> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return data;
    }

    public static String[] getTextFileNames(String path) {
        List<String> fileNames = new ArrayList<>();

        File directory = new File(path);
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                fileNames.add(file.getName());
            }
        }

        String[] fileNamesArray = new String[fileNames.size()];
        return fileNames.toArray(fileNamesArray);
    }
}
