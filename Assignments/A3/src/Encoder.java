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
        double[] encoded = new double[values.length];

        // Convert the recurrence or non-recurrence value to a numeric value
        String recurrence = values[0];
        if (recurrence.equals("recurrence-events")) {
            encoded[0] = 1.0;
        } else {
            encoded[0] = 0.0;
        }

        // Convert the ange range to a numeric value
        String ageRange = values[1];

        switch (ageRange) {
            case "10-19":
                encoded[1] = 1.0;
                break;
            case "20-29":
                encoded[1] = 2.0;
                break;
            case "30-39":
                encoded[1] = 3.0;
                break;
            case "40-49":
                encoded[1] = 4.0;
                break;
            case "50-59":
                encoded[1] = 5.0;
                break;
            case "60-69":
                encoded[1] = 6.0;
                break;
            case "70-79":
                encoded[1] = 7.0;
                break;
            case "80-89":
                encoded[1] = 8.0;
                break;
            case "90-99":
                encoded[1] = 9.0;
                break;
            default:
                encoded[1] = 0.0;
                break;
        }

        // Convert the menopause value to a numeric value
        String menopause = values[2];
        if (menopause.equals("lt40")) {
            encoded[2] = 1;
        } else if (menopause.equals("ge40")) {
            encoded[2] = 2;
        } else {
            encoded[2] = 3;
        }

        // Convert the tumor size to a numeric value
        String tumorSize = values[3];
        switch (tumorSize) {
            case "0-4":
                encoded[3] = 1.0;
                break;
            case "5-9":
                encoded[3] = 2.0;
                break;
            case "10-14":
                encoded[3] = 3.0;
                break;
            case "15-19":
                encoded[3] = 4.0;
                break;
            case "20-24":
                encoded[3] = 5.0;
                break;
            case "25-29":
                encoded[3] = 6.0;
                break;
            case "30-34":
                encoded[3] = 7.0;
                break;
            case "35-39":
                encoded[3] = 8.0;
                break;
            case "40-44":
                encoded[3] = 9.0;
                break;
            case "45-49":
                encoded[3] = 10.0;
                break;
            case "50-54":
                encoded[3] = 11.0;
                break;
            case "55-59":
                encoded[3] = 12.0;
                break;
            default:
                encoded[3] = 0.0;
        }

        // Convert the inv-nodes value to a numeric value
        String invNodes = values[4];
        switch (invNodes) {
            case "0-2":
                encoded[4] = 1.0;
                break;
            case "3-5":
                encoded[4] = 2.0;
                break;
            case "6-8":
                encoded[4] = 3.0;
                break;
            case "9-11":
                encoded[4] = 4.0;
                break;
            case "12-14":
                encoded[4] = 5.0;
                break;
            case "15-17":
                encoded[4] = 6.0;
                break;
            case "18-20":
                encoded[4] = 7.0;
                break;
            case "21-23":
                encoded[4] = 8.0;
                break;
            case "24-26":
                encoded[4] = 9.0;
                break;
            case "27-29":
                encoded[4] = 10.0;
                break;
            case "30-32":
                encoded[4] = 11.0;
                break;
            case "33-35":
                encoded[4] = 12.0;
                break;
            case "36-39":
                encoded[4] = 13.0;
                break;
            default:
                encoded[4] = 0.0;
        }

        // Convert the node-caps value to a numeric value
        String nodeCaps = values[5];
        if (nodeCaps.equals("yes")) {
            encoded[5] = 1;
        } else {
            encoded[5] = 0;
        }

        // Convert the deg-malig value to a numeric value
        String degMalig = values[6];
        switch (degMalig) {
            case "1":
                encoded[6] = 1.0;
                break;
            case "2":
                encoded[6] = 2.0;
                break;
            case "3":
                encoded[6] = 3.0;
                break;
            default:
                encoded[6] = 0.0;
        }

        // Convert the breast value to a numeric value
        String breast = values[7];
        if (breast.equals("left")) {
            encoded[7] = 1.0;
        } else {
            encoded[7] = 0.0;
        }

        // Convert the breast-quad value to a numeric value
        String breastQuad = values[8];
        switch (breastQuad) {
            case "left_up":
                encoded[8] = 1.0;
                break;
            case "left_low":
                encoded[8] = 2.0;
                break;
            case "right_up":
                encoded[8] = 3.0;
                break;
            case "right_low":
                encoded[8] = 4.0;
                break;
            case "central":
                encoded[8] = 5.0;
                break;
            default:
                encoded[8] = 0.0;
        }

        // Convert the irradiat value to a numeric value
        String irradiat = values[9];
        if (irradiat.equals("yes")) {
            encoded[9] = 1.0;
        } else {
            encoded[9] = 0.0;
        }

        return encoded;
    }
}