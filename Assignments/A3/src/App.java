import java.util.ArrayList;

public class App {
    public static ArrayList<String> testFile = new ArrayList<>();
    public static int numNNCorrect = 0;
    public static int numGPCorrect = 0;

    public static void main(String[] args) throws Exception {
        ArrayList<String> file = FileHandler.readFile("breast-cancer.data");
        ArrayList<double[]> encoded = Encoder.encodeFile(file);

        // Split the encoded file into a training and testing set
        ArrayList<double[]> trainingSet = new ArrayList<>();
        ArrayList<double[]> testingSet = new ArrayList<>();
        {
            for (int i = 0; i < encoded.size(); i++) {
                if (i % 2 == 0) {
                    trainingSet.add(encoded.get(i));
                } else {
                    testingSet.add(encoded.get(i));
                }
            }

            // Splice the sets
            trainingSet = splice(trainingSet);
            testingSet = splice(testingSet);
        }

        // ======= NEURAL NETWORK =======//
        // Get the size of the input layer
        // int inputSize = encoded.get(0).length - 1;
        // NeuralNetwork nn = new NeuralNetwork(inputSize, 10, 0.001);
        // nn.train(trainingSet, 4000);
        // printNNFile(testingSet, nn);

        // ======= GENETIC PROGRAMMING =======//
        GPClassifier gp = new GPClassifier();
        gp.train(trainingSet);
        printGPFile(testingSet, gp);
    }

    public static void printNNFile(ArrayList<double[]> encodedFile, NeuralNetwork nn) {
        numNNCorrect = 0;
        for (int i = 0; i < encodedFile.size(); i++) {
            // Get the label and input from the encoded file
            double label = encodedFile.get(i)[0];
            double prediction = nn.predict(encodedFile.get(i));

            // Print the prediction
            printPrediction(prediction, label);
        }

        // Print the accuracy of the neural network
        System.out.println("\nAccuracy: " + numNNCorrect + "/" + encodedFile.size() + " = "
                + Math.round((double) numNNCorrect / encodedFile.size() * 1000.0) / 10.0 + "%");
        System.out.println();
    }

    public static void printGPFile(ArrayList<double[]> encodedFile, GPClassifier gp) {
        numGPCorrect = 0;
        for (int i = 0; i < encodedFile.size(); i++) {
            boolean prediction = gp.predict(encodedFile.get(i));

            if (prediction) {
                System.out.println("\033[92m Correctly classified \033[0m");
                numGPCorrect++;
            } else {
                System.out.println("\033[91m Incorrectly classified \033[0m");
            }
        }

        // Print the accuracy of the neural network
        System.out.println("\nAccuracy: " + numGPCorrect + "/" + encodedFile.size() + " = "
                + Math.round((double) numGPCorrect / encodedFile.size() * 1000.0) / 10.0 + "%");
        System.out.println();
    }

    public static void printPrediction(double prediction, double label) {
        // Round prediction to 1 decimal place
        prediction = Math.round(prediction * 10.0) / 10.0;
        // If the prediction is closer to 1, output recurrence-events, else output
        // no-recurrence-events
        if (prediction > 0.5) {
            System.out.print("prediction: recurrence-events    | output: " + prediction + " label: " + label);

            // If the prediction is correct, output "correct", else output "incorrect"
            if (label == 1) {
                System.out.print("\033[92m correct\033[0m");
                numNNCorrect++;
            } else {
                System.out.print("\033[91m incorrect\033[0m");
            }
        } else {
            System.out.print("prediction: no-recurrence-events | output: " + prediction + " label: " + label);

            // If the prediction is correct, output "correct", else output "incorrect"
            if (label == 0) {
                System.out.print("\033[92m correct\033[0m");
                numNNCorrect++;
            } else {
                System.out.print("\033[91m incorrect\033[0m");
            }
        }

        System.out.println();
    }

    // This function splices the arrayList lines by taking a top line, then bottom
    // line
    // and then the next top line and bottom line and so on
    public static ArrayList<double[]> splice(ArrayList<double[]> lines) {
        ArrayList<double[]> spliced = new ArrayList<>();
        for (int i = 0; i < lines.size() / 2; i++) {
            spliced.add(lines.get(i));
            spliced.add(lines.get(lines.size() - i - 1));
        }
        return spliced;
    }
}
