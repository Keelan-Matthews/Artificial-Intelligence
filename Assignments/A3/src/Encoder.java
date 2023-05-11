import java.util.ArrayList;

public class Encoder {
    // This encodes the whole file
    public static ArrayList<double[]> encodeFile(ArrayList<String> file) {
        ArrayList<double[]> encodedFile = new ArrayList<>();
        for (String line : file) {
            encodedFile.add(encode(line));
        }

        return encodedFile;
    }

    // This function uses one-hot encoding to encode the breast cancer data
    public static double[] encode(String input) {
        String[] values = input.split(",");
        ArrayList<Integer> encoded = new ArrayList<>();

        // Convert the recurrence or non-recurrence value to a numeric value
        String recurrence = values[0];
        if (recurrence.equals("recurrence-events")) {
            encoded.add(1);
        } else {
            encoded.add(0);
        }

        // Convert the ange range to a numeric value
        String ageRange = values[1];
        // if the ageRange equals the string, set it to 1, else set it to 0
        for (int i = 10; i < 100; i += 10) {
            if (ageRange.equals(i + "-" + (i + 9))) {
                encoded.add(1);
            } else {
                encoded.add(0);
            }
        }

        // Convert the menopause value to a numeric value
        String menopause = values[2];
        if (menopause.equals("lt40")) {
            encoded.add(1);
            encoded.add(0);
            encoded.add(0);
        } else if (menopause.equals("ge40")) {
            encoded.add(0);
            encoded.add(1);
            encoded.add(0);
        } else if (menopause.equals("premeno")) {
            encoded.add(0);
            encoded.add(0);
            encoded.add(1);
        } else {
            encoded.add(0);
            encoded.add(0);
            encoded.add(0);
        }

        // Convert the tumor size to a numeric value
        String tumorSize = values[3];
        // test for 0-4, 5-9 etc by incrementing in 5
        for (int i = 0; i < 60; i += 5) {
            if (tumorSize.equals(i + "-" + (i + 4))) {
                encoded.add(1);
            } else {
                encoded.add(0);
            }
        }

        // Convert the inv-nodes value to a numeric value
        String invNodes = values[4];
        // test for 0-2, 3-5 etc by incrementing in 3
        for (int i = 0; i < 38; i += 3) {
            if (invNodes.equals(i + "-" + (i + 2))) {
                encoded.add(1);
            } else {
                encoded.add(0);
            }
        }

        // Convert the node-caps value to a numeric value
        String nodeCaps = values[5];
        if (nodeCaps.equals("yes")) {
            encoded.add(1);
            encoded.add(0);
        } else if (nodeCaps.equals("no")) {
            encoded.add(0);
            encoded.add(1);
        } else {
            encoded.add(0);
            encoded.add(0);
        }

        // Convert the deg-malig value to a numeric value
        String degMalig = values[6];
        // test for 1, 2, 3
        for (int i = 1; i < 4; i++) {
            if (degMalig.equals(Integer.toString(i))) {
                encoded.add(1);
            } else {
                encoded.add(0);
            }
        }
        

        // Convert the breast value to a numeric value
        String breast = values[7];
        if (breast.equals("left")) {
            encoded.add(1);
            encoded.add(0);
        } else if (breast.equals("right")) {
            encoded.add(0);
            encoded.add(1);
        } else {
            encoded.add(0);
            encoded.add(0);
        }

        // Convert the breast-quad value to a numeric value
        String breastQuad = values[8];
        if (breastQuad.equals("left_up")) {
            encoded.add(1);
            encoded.add(0);
            encoded.add(0);
            encoded.add(0);
            encoded.add(0);
        } else if (breastQuad.equals("left_low")) {
            encoded.add(0);
            encoded.add(1);
            encoded.add(0);
            encoded.add(0);
            encoded.add(0);
        } else if (breastQuad.equals("right_up")) {
            encoded.add(0);
            encoded.add(0);
            encoded.add(1);
            encoded.add(0);
            encoded.add(0);
        } else if (breastQuad.equals("right_low")) {
            encoded.add(0);
            encoded.add(0);
            encoded.add(0);
            encoded.add(1);
            encoded.add(0);
        } else if (breastQuad.equals("central")) {
            encoded.add(0);
            encoded.add(0);
            encoded.add(0);
            encoded.add(0);
            encoded.add(1);
        } else {
            encoded.add(0);
            encoded.add(0);
            encoded.add(0);
            encoded.add(0);
            encoded.add(0);
        }

        // Convert the irradiat value to a numeric value
        String irradiat = values[9];
        if (irradiat.equals("yes")) {
            encoded.add(1);
            encoded.add(0);
        } else if (irradiat.equals("no")) {
            encoded.add(0);
            encoded.add(1);
        } else {
            encoded.add(0);
            encoded.add(0);
        }

        // Convert the arraylist to an array
        double[] encodedArray = new double[encoded.size()];
        for (int i = 0; i < encoded.size(); i++) {
            encodedArray[i] = encoded.get(i);
        }

        return encodedArray;
    }
}