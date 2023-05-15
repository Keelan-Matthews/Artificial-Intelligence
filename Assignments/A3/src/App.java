import java.util.ArrayList;
import java.util.Random;

public class App {
    public static ArrayList<String> testFile = new ArrayList<>();
    public static int numNNCorrect = 0;
    public static int numGPCorrect = 0;
    public static long seed = 0; // Randomly splice the encoded file

    public static void main(String[] args) throws Exception {
        ArrayList<String> file = FileHandler.readFile("breast-cancer.data");
        ArrayList<double[]> encoded = Encoder.encodeFile(file);

        // Split the encoded file into a training and testing set
        ArrayList<double[]> trainingSet = new ArrayList<>();
        ArrayList<double[]> testingSet = new ArrayList<>();
        {
            // Splice the encoded set
            ArrayList<double[]> splicedEncodes = splice(encoded);

            // Split the spliced encoded set into a training (80%) and testing set (20%)
            for (int i = 0; i < splicedEncodes.size(); i++) {
                if (i < splicedEncodes.size() * 0.7) {
                    trainingSet.add(splicedEncodes.get(i));
                } else {
                    testingSet.add(splicedEncodes.get(i));
                }
            }
        }

        // ======= NEURAL NETWORK =======//
        // Get the size of the input layer
        int inputSize = encoded.get(0).length - 1;
        NeuralNetwork nn = new NeuralNetwork(inputSize, 7, 0.001, seed);
        nn.train(trainingSet, 4000);
        printNNFile(testingSet, nn);

        // ======= GENETIC PROGRAMMING =======//
        GPClassifier gp = new GPClassifier(seed);
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

        // Get the F-measure
        double positiveFMeasure = nn.positiveFMeasure(encodedFile);
        double negativeFMeasure = nn.negativeFMeasure(encodedFile);

        // Round the F-measure to 3 decimal places
        positiveFMeasure = Math.round(positiveFMeasure * 1000.0) / 1000.0;
        negativeFMeasure = Math.round(negativeFMeasure * 1000.0) / 1000.0;

        // Print the F-measure
        System.out.println("\nPositive F-measure: " + positiveFMeasure);
        System.out.println("Negative F-measure: " + negativeFMeasure);

        // Print the accuracy of the neural network
        // If accuracy is greater than 70%, print it green, else print it red
        if ((double) numNNCorrect / encodedFile.size() > 0.6) {
            System.out.print("\033[92m");
        } else {
            System.out.print("\033[91m");
        }
        System.out.println("\nAccuracy: " + numNNCorrect + "/" + encodedFile.size() + " = "
                + Math.round((double) numNNCorrect / encodedFile.size() * 1000.0) / 10.0 + "% | Weka: 76.6 %");
        System.out.println();

        // Reset the color
        System.out.print("\033[0m");
    }

    public static void printGPFile(ArrayList<double[]> encodedFile, GPClassifier gp) {
        numGPCorrect = 0;
        for (int i = 0; i < encodedFile.size(); i++) {
            boolean prediction = gp.predict(encodedFile.get(i));

            if (prediction) {
                numGPCorrect++;
            }
        }

        // Get the F-measure
        double positiveFMeasure = gp.positiveFMeasure(encodedFile);
        double negativeFMeasure = gp.negativeFMeasure(encodedFile);

        // Round the F-measure to 3 decimal places
        positiveFMeasure = Math.round(positiveFMeasure * 1000.0) / 1000.0;
        negativeFMeasure = Math.round(negativeFMeasure * 1000.0) / 1000.0;

        // Print the F-measure
        System.out.println("\nPositive F-measure: " + positiveFMeasure + " | Weka: 0.374");
        System.out.println("Negative F-measure: " + negativeFMeasure + " | Weka: 0.856");

        // Print the accuracy of the neural network
        // If accuracy is greater than 70%, print it green, else print it red
        if ((double) numGPCorrect / encodedFile.size() > 0.6) {
            System.out.print("\033[92m");
        } else {
            System.out.print("\033[91m");
        }
        System.out.println("\nAccuracy: " + numGPCorrect + "/" + encodedFile.size() + " = "
                + Math.round((double) numGPCorrect / encodedFile.size() * 1000.0) / 10.0 + "% | Weka: 76.6 %");
        System.out.println();

        // Reset the color
        System.out.print("\033[0m");
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

    // Mix up all the line positions randomly
    public static ArrayList<double[]> splice(ArrayList<double[]> lines) {
        ArrayList<double[]> splicedLines = new ArrayList<>();
        ArrayList<Integer> usedPositions = new ArrayList<>();
        Random random = new Random(seed);

        // Loop through all the lines
        for (int i = 0; i < lines.size(); i++) {
            // Get a random position
            int position = random.nextInt(lines.size());

            // If the position has not been used, add it to the spliced lines
            if (!usedPositions.contains(position)) {
                splicedLines.add(lines.get(position));
                usedPositions.add(position);
            } else {
                // If the position has been used, decrement i so that the line is not skipped
                i--;
            }

            // If all the positions have been used, reset the used positions
            if (usedPositions.size() == lines.size()) {
                usedPositions.clear();
            }
        }

        return splicedLines;
    }
}
