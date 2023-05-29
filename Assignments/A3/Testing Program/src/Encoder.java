import java.util.ArrayList;

public class Encoder {
    // This encodes the whole file
    public static ArrayList<double[]> encodeFile(ArrayList<String> file) {
        ArrayList<double[]> encodedFile = new ArrayList<>();
        for (String line : file) {
            // Split the line into an array of values
            String[] values = line.split(",");
            encodedFile.add(encode(values));
        }

        return encodedFile;
    }

    // This function uses one-hot encoding to encode the breast cancer data
    public static double[] encode(String[] values) {
        ArrayList<Double> encoded = new ArrayList<>();

        // Convert the recurrence or non-recurrence value to a numeric value
        String recurrence = values[0];
        encoded.add(encodeClass(recurrence));

        // Convert the ange range to a numeric value
        String ageRange = values[1];
        encoded.add(encodeAgeRange(ageRange));

        // Convert the menopause value to a numeric value
        String menopause = values[2];
        encoded.add(encodeMenopause(menopause));

        // Convert the tumor size to a numeric value
        String tumorSize = values[3];
        encoded.add(encodeTumorSize(tumorSize));

        // Convert the inv-nodes value to a numeric value
        String invNodes = values[4];
        encoded.add(encodeInvNodes(invNodes));

        // Convert the node-caps value to a numeric value
        String nodeCaps = values[5];
        encoded.add(encodeNodeCaps(nodeCaps));

        // Convert the deg-malig value to a numeric value
        String degMalig = values[6];
        encoded.add(encodeDegMalig(degMalig));

        // Convert the breast value to a numeric value
        String breast = values[7];
        encoded.add(encodeBreast(breast));

        // Convert the breast-quad value to a numeric value
        String breastQuad = values[8];
        encoded.add(encodeBreastQuad(breastQuad));

        // Convert the irradiat value to a numeric value
        String irradiat = values[9];
        encoded.add(encodeIrradiat(irradiat));

        // Convert the arraylist to an array
        double[] encodedArray = new double[encoded.size()];
        for (int i = 0; i < encoded.size(); i++) {
            encodedArray[i] = encoded.get(i);
        }

        return encodedArray;
    }

    // This is also an encoder function, but returns the encoding for a specific category
    // instead of the entire row
    public static ArrayList<double[]> encodeData(ArrayList<String[]> data) {
        int index = 0; // 0 = age, 1 = menopause, 2 = tumor-size, 3 = inv-nodes, 4 = node-caps, 5 = deg-malig, 6 = breast, 7 = breast-quad, 8 = irradiat
        ArrayList<double[]> encoded = new ArrayList<>();
        
        for (String[] row : data) {
            switch (index) {
                case 0: {
                    double[] ageRange = new double[row.length];
                    for (int i = 0; i < row.length; i++) {
                        ageRange[i] = encodeAgeRange(row[i]);
                    }
                    encoded.add(ageRange);
                    break;
                }
                case 1: {
                    double[] menopause = new double[row.length];
                    for (int i = 0; i < row.length; i++) {
                        menopause[i] = encodeMenopause(row[i]);
                    }
                    encoded.add(menopause);
                    break;
                }
                case 2: {
                    double[] tumorSize = new double[row.length];
                    for (int i = 0; i < row.length; i++) {
                        tumorSize[i] = encodeTumorSize(row[i]);
                    }
                    encoded.add(tumorSize);
                    break;
                }
                case 3: {
                    double[] invNodes = new double[row.length];
                    for (int i = 0; i < row.length; i++) {
                        invNodes[i] = encodeInvNodes(row[i]);
                    }
                    encoded.add(invNodes);
                    break;
                }
                case 4: {
                    double[] nodeCaps = new double[row.length];
                    for (int i = 0; i < row.length; i++) {
                        nodeCaps[i] = encodeNodeCaps(row[i]);
                    }
                    encoded.add(nodeCaps);
                    break;
                }
                case 5: {
                    double[] degMalig = new double[row.length];
                    for (int i = 0; i < row.length; i++) {
                        degMalig[i] = encodeDegMalig(row[i]);
                    }
                    encoded.add(degMalig);
                    break;
                }
                case 6: {
                    double[] breast = new double[row.length];
                    for (int i = 0; i < row.length; i++) {
                        breast[i] = encodeBreast(row[i]);
                    }
                    encoded.add(breast);
                    break;
                }
                case 7: {
                    double[] breastQuad = new double[row.length];
                    for (int i = 0; i < row.length; i++) {
                        breastQuad[i] = encodeBreastQuad(row[i]);
                    }
                    encoded.add(breastQuad);
                    break;
                }
                case 8: {
                    double[] irradiat = new double[row.length];
                    for (int i = 0; i < row.length; i++) {
                        irradiat[i] = encodeIrradiat(row[i]);
                    }
                    encoded.add(irradiat);
                    break;
                }
            }
            index++;
        }

        return encoded;
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

    public static double encodeAgeRange(String ageRange) {
        double[] ageRangeValues = new double[9];
        // if the ageRange equals the string, set it to 1, else set it to 0
        for (int i = 10; i < 100; i += 10) {
            if (ageRange.equals(i + "-" + (i + 9))) {
                ageRangeValues[i / 10 - 1] = 1;
            } else {
                ageRangeValues[i / 10 - 1] = 0;
            }
        }

        return oneHotNormalization(ageRangeValues);
    }

    public static double encodeTumorSize(String tumorSize) {
        double[] tumorSizeValues = new double[12];
        // test for 0-4, 5-9 etc by incrementing in 5
        for (int i = 0; i < 60; i += 5) {
            if (tumorSize.equals(i + "-" + (i + 4))) {
                tumorSizeValues[i / 5] = 1;
            } else {
                tumorSizeValues[i / 5] = 0;
            }
        }

        return oneHotNormalization(tumorSizeValues);
    }

    public static double encodeInvNodes(String invNodes) {
        double[] invNodesValues = new double[13];
        // test for 0-2, 3-5 etc by incrementing in 3
        for (int i = 0; i < 38; i += 3) {
            if (invNodes.equals(i + "-" + (i + 2))) {
                invNodesValues[i / 3] = 1;
            } else {
                invNodesValues[i / 3] = 0;
            }
        }

        return oneHotNormalization(invNodesValues);
    }

    public static double encodeDegMalig(String degMalig) {
        double[] degMaligValues = new double[3];
        // test for 1, 2, 3
        for (int i = 1; i < 4; i++) {
            if (degMalig.equals(Integer.toString(i))) {
                degMaligValues[i - 1] = 1;
            } else {
                degMaligValues[i - 1] = 0;
            } 
        }

        return oneHotNormalization(degMaligValues);
    }

    public static double encodeBreast(String breast) {
        if (breast.equals("left")) {
            return oneHotNormalization(new double[] {1, 0});
        } else if (breast.equals("right")) {
            return oneHotNormalization(new double[] {0, 1});
        } else {
            return oneHotNormalization(new double[] {0, 0});
        } 
    }

    public static double encodeBreastQuad(String breastQuad) {
        if (breastQuad.equals("left_up")) {
            return oneHotNormalization(new double[] {1, 0, 0, 0, 0});
        } else if (breastQuad.equals("left_low")) {
            return oneHotNormalization(new double[] {0, 1, 0, 0, 0});
        } else if (breastQuad.equals("right_up")) {
            return oneHotNormalization(new double[] {0, 0, 1, 0, 0});
        } else if (breastQuad.equals("right_low")) {
            return oneHotNormalization(new double[] {0, 0, 0, 1, 0});
        } else if (breastQuad.equals("central")) {
            return oneHotNormalization(new double[] {0, 0, 0, 0, 1});
        } else {
            return oneHotNormalization(new double[] {0, 0, 0, 0, 0});
        } 
    }

    public static double encodeIrradiat(String irradiat) {
        if (irradiat.equals("yes")) {
            return oneHotNormalization(new double[] {1, 0});
        } else if (irradiat.equals("no")) {
            return oneHotNormalization(new double[] {0, 1});
        } else {
            return oneHotNormalization(new double[] {0, 0});
        } 
    }

    public static double encodeMenopause(String menopause) {
        if (menopause.equals("lt40")) {
            return oneHotNormalization(new double[] {1, 0, 0});
        } else if (menopause.equals("ge40")) {
            return oneHotNormalization(new double[] {0, 1, 0});
        } else if (menopause.equals("premeno")) {
            return oneHotNormalization(new double[] {0, 0, 1});
        } else {
            return oneHotNormalization(new double[] {0, 0, 0});
        } 
    }

    public static double encodeClass(String classification) {
        if (classification.equals("recurrence-events")) {
            return oneHotNormalization(new double[] {1, 0});
        } else if (classification.equals("no-recurrence-events")) {
            return oneHotNormalization(new double[] {0, 1});
        } else {
            return oneHotNormalization(new double[] {0, 0});
        } 
    }

    public static double encodeNodeCaps(String nodeCaps) {
        if (nodeCaps.equals("yes")) {
            return oneHotNormalization(new double[] {1, 0});
        } else if (nodeCaps.equals("no")) {
            return oneHotNormalization(new double[] {0, 1});
        } else {
            return oneHotNormalization(new double[] {0, 0});
        } 
    }
}