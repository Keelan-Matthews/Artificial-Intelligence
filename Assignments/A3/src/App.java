import java.util.ArrayList;
import java.util.HashMap;

public class App {
    public static ArrayList<String> testFile = new ArrayList<>();
    public static int numCorrect = 0;
    public static void main(String[] args) throws Exception {
        ArrayList<String> file = FileHandler.readFile("breast-cancer.data");
        ArrayList<double[]> encoded = Encoder.encodeFile(file);

        // Use this data to train a new neural network
        NeuralNetwork nn = new NeuralNetwork(8, 8, 0.001);

        // Train the neural network
        nn.train(encoded, 10000);

        // Test the neural network
        printFile(encoded, nn);



        ArrayList<HashMap<String, Boolean>> terminalNodes = TerminalEncoder.encodeFile(file);
        ArrayList<Boolean> encodedCategories = TerminalEncoder.encodeCategories(file);

        // Use this data to train a new Genetic Programming algorithm
    }

    public static void printFile(ArrayList<double[]> encodedFile, NeuralNetwork nn) {
        numCorrect = 0;
        for (int i = 0; i < encodedFile.size(); i++) {
            // Get the label and input from the encoded file
            double label = encodedFile.get(i)[0];
            double prediction = nn.predict(encodedFile.get(i));

            // Print the prediction
            printPrediction(prediction, label);
        }

        // Print the accuracy of the neural network
        System.out.println("\nAccuracy: " + numCorrect + "/" + encodedFile.size() + " = "
                + Math.round((double) numCorrect / encodedFile.size() * 1000.0) / 10.0 + "%");
        System.out.println();
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
                numCorrect++;
            } else {
                System.out.print("\033[91m incorrect\033[0m");
            }
        } else {
            System.out.print("no-recurrence-events | output: " + prediction + " label: " + label);

            // If the prediction is correct, output "correct", else output "incorrect"
            if (label == 0) {
                System.out.print("\033[92m correct\033[0m");
                numCorrect++;
            } else {
                System.out.print("\033[91m incorrect\033[0m");
            }
        }

        System.out.println();
    }
}
