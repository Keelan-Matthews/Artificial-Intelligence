import java.util.ArrayList;

public class App {
    public static ArrayList<String> testFile = new ArrayList<>();
    public static int numCorrect = 0;
    public static void main(String[] args) throws Exception {
        ArrayList<String> file = FileHandler.readFile("breast-cancer.data");

        //======= NEURAL NETWORK =======//
        ArrayList<double[]> encoded = Encoder.encodeFile(file);

        // Split the encoded file into a training and testing set
        ArrayList<double[]> trainingSet = new ArrayList<>();
        ArrayList<double[]> testingSet = new ArrayList<>();
        for (int i = 0; i < encoded.size(); i++) {
            if (i % 2 == 0) {
                trainingSet.add(encoded.get(i));
            } else {
                testingSet.add(encoded.get(i));
            }
        }

        // Reverse the order of the training set so that the network doesn't learn the
        // data in order
        for (int i = 0; i < trainingSet.size() / 2; i++) {
            double[] temp = trainingSet.get(i);
            trainingSet.set(i, trainingSet.get(trainingSet.size() - i - 1));
            trainingSet.set(trainingSet.size() - i - 1, temp);
        }

        // Get the size of the input layer
        int inputSize = encoded.get(0).length - 1;

        NeuralNetwork nn = new NeuralNetwork(inputSize, 10, 0.001);
        nn.train(trainingSet, 4000);
        printFile(testingSet, nn);

        //======= GENETIC PROGRAMMING =======//
        // Create a new GPClassifier
        // GPClassifier gp = new GPClassifier();
        // gp.run();
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
            System.out.print("prediction: recurrence-events    | output: " + prediction + " label: " + label);

            // If the prediction is correct, output "correct", else output "incorrect"
            if (label == 1) {
                System.out.print("\033[92m correct\033[0m");
                numCorrect++;
            } else {
                System.out.print("\033[91m incorrect\033[0m");
            }
        } else {
            System.out.print("prediction: no-recurrence-events | output: " + prediction + " label: " + label);

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
