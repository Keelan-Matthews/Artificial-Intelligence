public class TreeInterpreter {
    // This function takes in a line of input values and a tree.
    // It then traverses through each node in the tree and determiens its category.
    // It will then retrieve the value of the input for that category, and traverse
    // the child node corresponding to that value.
    // the first value in the line of input is the label.
    // When the interpreter is done traversing down the tree and gets to the leaf,
    // it must compare the label and the output of the leaf to determine whether
    // the tree was correct or not.
    // It will then return the accuracy of the tree.

    public static boolean interpret(Node root, double[] input) {
        Node currentNode = root;
        int index = 0;
        double label = input[0]; // The first value in the input is the label
        double[] inputs = trimClassAttribtue(input); // Remove the class attribute from the input
        int categoryIndex = 0;

        while (!currentNode.isLeaf()) {
            // Category index determines which input we compare to the values
            categoryIndex = currentNode.getIndex();
            double[] encoded = currentNode.getValues();

            // Find the index of the value in the input based on the category index
            for (int i = 0; i < encoded.length; i++) {
                if (inputs[categoryIndex] == encoded[i]) {
                    index = i;
                    break;
                }
            }

            // Traverse down the tree, selecting the child node that corresponds to the
            // value of the input
            currentNode = currentNode.getChildren()[index];
        }

        // Compare the label and the output of the leaf to determine whether the tree
        // was correct or not
        double[] encoded = currentNode.getValues();
        boolean[] leafDecisions = currentNode.getLeafDecisions(); // Yes or no per value. If yes, then the value is the
                                                                  // label

        // Get the index of the value in the input for the leaf
        index = 0;
        for (int i = 0; i < encoded.length; i++) {
            if (inputs[categoryIndex] == encoded[i]) {
                index = i;
                break;
            }
        }

        // Determine if the leaf decision for that value is yes or no
        // Convert the boolean to an integer
        double decision = leafDecisions[index] ? 1.0 : 0.0;

        return decision == label;
    }

    // Function to remove the class attribute from the input
    public static double[] trimClassAttribtue(double[] input) {
        if (input.length == 8) {
            return input;
        }

        double[] trimmed = new double[input.length - 1];
        for (int i = 1; i < input.length; i++) {
            trimmed[i - 1] = input[i];
        }
        return trimmed;
    }
}
