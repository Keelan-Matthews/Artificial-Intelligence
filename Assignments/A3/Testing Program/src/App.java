import java.util.ArrayList;

public class App {
    public static ArrayList<String> testFile = new ArrayList<>();
    public static int numNNCorrect = 0;
    public static int numGPCorrect = 0;
    public static long seed = 0; // Randomly splice the encoded file

    public static void main(String[] args) throws Exception {
        ArrayList<String> file = FileHandler.readFile("breast-cancer.data");
        ArrayList<double[]> encoded = Encoder.encodeFile(file);

        // // ======= NEURAL NETWORK =======//
        // // Get the size of the input layer
        int inputSize = encoded.get(0).length - 1;
        NeuralNetwork nn = new NeuralNetwork(inputSize, 5, 0.001, seed);
        nn.train(encoded, 4000);


        // Await input from the console for the name of the file to test
        System.out.println("Enter the name of the file to test: ");
        String fileName = System.console().readLine();
        ArrayList<String> testingFile = FileHandler.readFile(fileName);
        ArrayList<double[]> testingSet = Encoder.encodeFile(testingFile);
    
        System.out.println();
        System.out.println("Testing " + fileName + " with " + testingSet.size() + " instances.");
        System.out.println();
        System.out.println("=======================================================");
        System.out.println("Neural Network Results");
        System.out.println("=======================================================");
        System.out.println();
        printNNFile(testingSet, nn);

        // ======= GENETIC PROGRAMMING =======//
        GPClassifier gp = new GPClassifier(seed);
        gp.train(encoded);

        System.out.println();
        System.out.println("=======================================================");
        System.out.println("Decision Tree Results");
        System.out.println("=======================================================");
        System.out.println();
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

        // Write the F-measure to a file
        String fMeasureLine = "\n================\nPositive F-measure: " + positiveFMeasure
                + "\nNegative F-measure: " + negativeFMeasure + "\n================";
        FileHandler.writeToFile(fMeasureLine, "NNResults.txt");

        // Print the accuracy of the neural network
        // If accuracy is greater than 70%, print it green, else print it red
        if ((double) numNNCorrect / encodedFile.size() > 0.6) {
        System.out.print("\033[92m");
        } else {
        System.out.print("\033[91m");
        }
        System.out.println("\nAccuracy: " + numNNCorrect + "/" + encodedFile.size() +
        " = "
        + Math.round((double) numNNCorrect / encodedFile.size() * 1000.0) / 10.0 + "% | Weka: 76.6 %");
        System.out.println();

        // Reset the color
        System.out.print("\033[0m");

        double roundedAccuracy = Math.round((double) numNNCorrect / encodedFile.size() * 1000.0) / 10.0;
        String accuracyLine = "\n================\nAccuracy " + roundedAccuracy + "%\n================";
        // Append the accuracy to the file
        FileHandler.writeToFile(accuracyLine, "NNResults.txt");
    }

    public static void printGPFile(ArrayList<double[]> encodedFile, GPClassifier gp) {
        numGPCorrect = 0;
        for (int i = 0; i < encodedFile.size(); i++) {
            double label = encodedFile.get(i)[0];
            boolean prediction = gp.predict(encodedFile.get(i));

            if (prediction) {
                System.out.print("\033[92m correct\033[0m");
                numGPCorrect++;
            }
            else {
                System.out.print("\033[91m incorrect\033[0m");
            }
            System.out.println(" | " + label);
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

        // Write the F-measure to a file
        String fMeasureLine = "\n================\nPositive F-measure: " + positiveFMeasure
                + " | Weka: 0.374\nNegative F-measure: " + negativeFMeasure + " | Weka: 0.856\n================";
        FileHandler.writeToFile(fMeasureLine, "GPResults.txt");

        // Print the accuracy of the neural network
        // If accuracy is greater than 70%, print it green, else print it red
        if ((double) numGPCorrect / encodedFile.size() > 0.6) {
        System.out.print("\033[92m");
        } else {
        System.out.print("\033[91m");
        }
        System.out.println("\nAccuracy: " + numGPCorrect + "/" + encodedFile.size() +
        " = "
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
            System.out.print("prediction: recurrence-events | output: " + prediction + " label: " + label); 

            // If the prediction is correct, output "correct", else output "incorrect"
            if (label == 1) {
                System.out.print("\033[92m correct\033[0m");
                numNNCorrect++;
            } else {
                System.out.print("\033[91m incorrect\033[0m");
            }
        } else {
            System.out.print("prediction: no-recurrence-events | output: " + prediction +
            " label: " + label);

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
}
