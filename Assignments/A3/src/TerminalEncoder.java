import java.util.ArrayList;
import java.util.HashMap;

public class TerminalEncoder {
    // This encodes the whole file
    public static ArrayList<HashMap<String, Boolean>> encodeFile(ArrayList<String> file) {
        ArrayList<HashMap<String, Boolean>> encodedFile = new ArrayList<>();

        for (String line : file) {
            encodedFile.add(encode(line));
        }

        return encodedFile;
    } 

    public static ArrayList<Boolean> encodeCategories(ArrayList<String> file) {
        ArrayList<Boolean> encoded = new ArrayList<Boolean>();
        // For each line in the file, split the line into values and encode the recurrence-events value with true or false
        for (String line : file) {
            String[] values = line.split(",");
            String recurrence = values[0];
            encoded.add(recurrence.equals("recurrence-events"));
        }

        return encoded;
    }
    
    public static HashMap<String, Boolean> encode(String input) {
        String[] values = input.split(",");
        HashMap<String, Boolean> encoded = new HashMap<String, Boolean>();

        // Convert the ange range to a numeric value
        String ageRange = values[1];
        for (int i = 10; i < 100; i += 10) {
            encoded.put("age_" + i + "_" + (i + 9), ageRange.equals(i + "-" + (i + 9)));
        }

        // Convert the menopause value to a numeric value
        String menopause = values[2];
        encoded.put("lt40", menopause.equals("lt40"));
        encoded.put("ge40", menopause.equals("ge40"));
        encoded.put("premeno", menopause.equals("premeno"));

        // Convert the tumor size to a numeric value
        String tumorSize = values[3];
        for (int i = 0; i < 50; i += 5) {
            encoded.put("tumor_size_" + i + "_" + (i + 4), tumorSize.equals(i + "-" + (i + 4)));
        }

        // Convert the inv-nodes value to a numeric value
        String invNodes = values[4];
        for (int i = 0; i < 12; i += 3) {
            encoded.put("inv_nodes_" + i + "_" + (i + 2), invNodes.equals(i + "-" + (i + 2)));
        }

        // Convert the node-caps value to a numeric value
        String nodeCaps = values[5];
        encoded.put("node_caps", nodeCaps.equals("yes"));

        // Convert the deg-malig value to a numeric value
        String degMalig = values[6];
        for (int i = 1; i < 4; i++) {
            encoded.put("deg_malig_" + i, degMalig.equals(Integer.toString(i)));
        }

        // Convert the breast value to a numeric value
        String breast = values[7];
        encoded.put("breast_left", breast.equals("left"));

        // Convert the breast-quad value to a numeric value
        String breastQuad = values[8];
        encoded.put("breast_quad_left_low", breastQuad.equals("left_low"));
        encoded.put("breast_quad_left_up", breastQuad.equals("left_up"));
        encoded.put("breast_quad_right_low", breastQuad.equals("right_low"));
        encoded.put("breast_quad_right_up", breastQuad.equals("right_up"));
        encoded.put("breast_quad_central", breastQuad.equals("central"));

        // Convert the irradiat value to a numeric value
        String irradiat = values[9];
        encoded.put("irradiat", irradiat.equals("yes"));

        return encoded;
    }
}
