import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {
    private static String dataPath = "Dataset/"; // Path to the data files
    private static String resultsPath = "Results/";

    public static ArrayList<String> readFile(String dataset) {
        ArrayList<String> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath + dataset))) {
            String line;
            while ((line = br.readLine()) != null) {
                items.add(line);
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return items;
    }

    public static void writeNNResults(ArrayList<String> errorLine) {
        String filename = "NNResults.txt";

        // Delete the file if it exists
        java.io.File file = new java.io.File(resultsPath + filename);
        if (file.exists()) {
            file.delete();
        }

        // Write the error to the file in the format:
        String content = "";
        for (int i = 0; i < errorLine.size(); i++) {
            content += errorLine.get(i) + "\n";
        }
        writeToFile(content, filename);
    }

    public static void writeGPResults(ArrayList<Double> fitness, Node bestTree, double bestFitness) {
        String filename = "GPResults.txt";

        // Delete the file if it exists
        java.io.File file = new java.io.File(resultsPath + filename);
        if (file.exists()) {
            file.delete();
        }

        // Write the fitness to the file in the format:
        String content = "";
        for (int i = 0; i < fitness.size(); i++) {
            content += "Generation " + i + ":\n";
            content += "Fitness: " + Math.round((double) fitness.get(i) * 1000.0) / 10.0 + "\n";
        }
        writeToFile(content, filename);

        writeToFile("\n\n=============\nTREE\n=============\n\n", filename);

        // Write the best tree to the file
        printTree(bestTree, 0, filename);

        // Write the best fitness to the file
        writeToFile("\n\n====================\n", filename);
        double roundedFitness = Math.round((double) bestFitness * 1000.0) / 10.0;
        content = "Best Fitness: " + roundedFitness;
        writeToFile(content, filename);
        writeToFile("\n====================\n", filename);
    }

    // printTree variation that writes to a file instead of printing to the console
    public static void printTree(Node node, int level, String filename) {
        if (node == null) {
            return;
        }

        String indent = "";
        for (int i = 0; i < level; i++) {
            indent += "|  ";
        }

        if (node.isLeaf()) {
            writeToFile(indent + "+-- " + node.getCategory() + " = [LEAF]\n", filename);
        } else {
            writeToFile(indent + "+-- " + node.getCategory() + " = {\n", filename);
            for (int i = 0; i < node.getValues().length; i++) {
                writeToFile(indent + "|  " + Math.round((double) node.getValues()[i] * 1000.0) / 10.0 + " -> ",
                        filename);
                printTree(node.getChildren()[i], level + 1, filename);
            }
            writeToFile(indent + "}\n", filename);
        }
    }

    // Write the content to the file
    public static void writeToFile(String content, String filename) {
        String path = resultsPath + filename;

        // Append the content to the file
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(path), content.getBytes(),
                    java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}
