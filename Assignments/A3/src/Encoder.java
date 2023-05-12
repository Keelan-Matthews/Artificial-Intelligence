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
        ArrayList<Double> encoded = new ArrayList<>();

        // Convert the recurrence or non-recurrence value to a numeric value
        String recurrence = values[0];
        if (recurrence.equals("recurrence-events")) {
            encoded.add(1.0);
        } else {
            encoded.add(0.0);
        }

        // Convert the ange range to a numeric value
        String ageRange = values[1];
        double[] ageRangeValues = new double[9];
        // if the ageRange equals the string, set it to 1, else set it to 0
        for (int i = 10; i < 100; i += 10) {
            if (ageRange.equals(i + "-" + (i + 9))) {
                ageRangeValues[i / 10 - 1] = 1;
            } else {
                ageRangeValues[i / 10 - 1] = 0;
            }
        }

        // Normalize the age range values to be between 0 and 1
        encoded.add(oneHotNormalization(ageRangeValues));

        // Convert the menopause value to a numeric value
        String menopause = values[2];
        if (menopause.equals("lt40")) {
            encoded.add(oneHotNormalization(new double[] {1, 0, 0}));
        } else if (menopause.equals("ge40")) {
            encoded.add(oneHotNormalization(new double[] {0, 1, 0}));
        } else if (menopause.equals("premeno")) {
            encoded.add(oneHotNormalization(new double[] {0, 0, 1}));
        } else {
            encoded.add(oneHotNormalization(new double[] {0, 0, 0}));
        }

        // Convert the tumor size to a numeric value
        String tumorSize = values[3];
        double[] tumorSizeValues = new double[12];
        // test for 0-4, 5-9 etc by incrementing in 5
        for (int i = 0; i < 60; i += 5) {
            if (tumorSize.equals(i + "-" + (i + 4))) {
                tumorSizeValues[i / 5] = 1;
            } else {
                tumorSizeValues[i / 5] = 0;
            }
        }

        // Normalize the tumor size values to be between 0 and 1
        encoded.add(oneHotNormalization(tumorSizeValues));

        // Convert the inv-nodes value to a numeric value
        String invNodes = values[4];
        double[] invNodesValues = new double[13];
        // test for 0-2, 3-5 etc by incrementing in 3
        for (int i = 0; i < 38; i += 3) {
            if (invNodes.equals(i + "-" + (i + 2))) {
                invNodesValues[i / 3] = 1;
            } else {
                invNodesValues[i / 3] = 0;
            }
        }

        // Normalize the inv-nodes values to be between 0 and 1
        encoded.add(oneHotNormalization(invNodesValues));

        // Convert the node-caps value to a numeric value
        String nodeCaps = values[5];
        if (nodeCaps.equals("yes")) {
            encoded.add(oneHotNormalization(new double[] {1, 0}));
        } else if (nodeCaps.equals("no")) {
            encoded.add(oneHotNormalization(new double[] {0, 1}));
        } else {
            encoded.add(oneHotNormalization(new double[] {0, 0}));
        } 

        // Convert the deg-malig value to a numeric value
        String degMalig = values[6];
        double[] degMaligValues = new double[3];
        // test for 1, 2, 3
        for (int i = 1; i < 4; i++) {
            if (degMalig.equals(Integer.toString(i))) {
                degMaligValues[i - 1] = 1;
            } else {
                degMaligValues[i - 1] = 0;
            } 
        }

        // Normalize the deg-malig values to be between 0 and 1
        encoded.add(oneHotNormalization(degMaligValues));
        

        // Convert the breast value to a numeric value
        String breast = values[7];
        if (breast.equals("left")) {
            encoded.add(oneHotNormalization(new double[] {1, 0}));
        } else if (breast.equals("right")) {
            encoded.add(oneHotNormalization(new double[] {0, 1}));
        } else {
            encoded.add(oneHotNormalization(new double[] {0, 0}));
        } 

        // Convert the breast-quad value to a numeric value
        String breastQuad = values[8];
        if (breastQuad.equals("left_up")) {
            encoded.add(oneHotNormalization(new double[] {1, 0, 0, 0, 0}));
        } else if (breastQuad.equals("left_low")) {
            encoded.add(oneHotNormalization(new double[] {0, 1, 0, 0, 0}));
        } else if (breastQuad.equals("right_up")) {
            encoded.add(oneHotNormalization(new double[] {0, 0, 1, 0, 0}));
        } else if (breastQuad.equals("right_low")) {
            encoded.add(oneHotNormalization(new double[] {0, 0, 0, 1, 0}));
        } else if (breastQuad.equals("central")) {
            encoded.add(oneHotNormalization(new double[] {0, 0, 0, 0, 1}));
        } else {
            encoded.add(oneHotNormalization(new double[] {0, 0, 0, 0, 0}));
        } 

        // Convert the irradiat value to a numeric value
        String irradiat = values[9];
        if (irradiat.equals("yes")) {
            encoded.add(oneHotNormalization(new double[] {1, 0}));
        } else if (irradiat.equals("no")) {
            encoded.add(oneHotNormalization(new double[] {0, 1}));
        } else {
            encoded.add(oneHotNormalization(new double[] {0, 0}));
        } 

        // Convert the arraylist to an array
        double[] encodedArray = new double[encoded.size()];
        for (int i = 0; i < encoded.size(); i++) {
            encodedArray[i] = encoded.get(i);
        }

        return encodedArray;
    }

    public static double oneHotNormalization(double[] x) {
        int index = -1;
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 1.0) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return 0.0; // no 1 found, return 0
        }
        return (double) index / (x.length - 1);
    }
}