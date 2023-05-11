import java.util.ArrayList;

public class App {
    public static ArrayList<String> testFile = new ArrayList<>();
    public static void main(String[] args) throws Exception {

        ArrayList<String> file = FileHandler.readFile("breast-cancer.data");
        ArrayList<double[]> encoded = Encoder.encodeFile(file);

        // Use this data to train a new neural network
        NeuralNetwork nn = new NeuralNetwork(8, 24, 0.001);

        // Train the neural network
        nn.train(encoded, 10000);

        // testFile.add("no-recurrence-events,60-69,ge40,30-34,0-2,no,2,left,left_low,yes");
        // testFile.add("no-recurrence-events,30-39,premeno,20-24,0-2,no,3,left,central,no");
        // testFile.add("recurrence-events,40-49,premeno,25-29,0-2,no,3,left,left_up,no");

        // testFile.add("no-recurrence-events,60-69,ge40,10-14,0-2,no,2,left,left_low,no");
        // testFile.add("no-recurrence-events,50-59,premeno,25-29,3-5,no,2,right,left_up,yes");
        // testFile.add("no-recurrence-events,40-49,premeno,20-24,0-2,no,3,right,left_low,yes");
        // testFile.add("no-recurrence-events,40-49,premeno,35-39,0-2,yes,3,right,left_up,yes");
        // testFile.add("no-recurrence-events,40-49,premeno,35-39,0-2,yes,3,right,left_low,yes");

        // testFile.add("recurrence-events,40-49,ge40,30-34,3-5,no,3,left,left_low,no");
        // testFile.add("recurrence-events,50-59,ge40,30-34,3-5,no,3,left,left_low,no");

        // ArrayList<double[]> encodedTestFile = Encoder.encodeFile(testFile);

        // Test the neural network
        printFile(encoded, nn);
    }

    public static void printFile(ArrayList<double[]> encodedFile, NeuralNetwork nn) {
        for (int i = 0; i < encodedFile.size(); i++) {
            // Get the label and input from the encoded file
            double label = encodedFile.get(i)[0];
            double prediction = nn.predict(encodedFile.get(i));

            // Print the prediction
            printPrediction(prediction, label);
        }
    }

    public static void printPrediction(double prediction, double label) {
        // Round prediction to 1 decimal place
        prediction = Math.round(prediction * 10.0) / 10.0;
        // If the prediction is closer to 1, output recurrence-events, else output
        // no-recurrence-events
        if (prediction > 0.5) {
            System.out.print("recurrence-events    | output: " + prediction + " label: " + label);

            // If the prediction is correct, output "correct", else output "incorrect"
            if (label == 1) {
                System.out.print("\033[92m correct\033[0m");
            } else {
                System.out.print("\033[91m incorrect\033[0m");
            }
        } else {
            System.out.print("no-recurrence-events | output: " + prediction + " label: " + label);

            // If the prediction is correct, output "correct", else output "incorrect"
            if (label == 0) {
                System.out.print("\033[92m correct\033[0m");
            } else {
                System.out.print("\033[91m incorrect\033[0m");
            }
        }

        System.out.println();
    }
}
