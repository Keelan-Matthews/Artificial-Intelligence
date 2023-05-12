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

    // public int interpret(Node root, double[] input) {
    //     Node currentNode = root;
    //     int index = 0;
    //     while (!currentNode.isLeaf()) {
    //         String category = currentNode.getCategory();
    //         String[] values = currentNode.getValues();
    //         String value = values[index];
    //         index = 0;
    //         for (int i = 0; i < input.length; i++) {
    //             if (input[i] == value) {
    //                 index = i;
    //                 break;
    //             }
    //         }
    //         currentNode = currentNode.getChildren()[index];
    //     }
    //     String[] values = currentNode.getValues();
    //     boolean[] leafDecisions = currentNode.getLeafDecisions();
    //     for (int i = 0; i < values.length; i++) {
    //         if (leafDecisions[i]) {
    //             if (input[index] == values[i]) {
    //                 return 1;
    //             } else {
    //                 return 0;
    //             }
    //         }
    //     }
    //     return 0;
    // }

    // This function will take in a category and the input, and return the string of 1s and 0s
    // associated with that category
    // public String getBinaryString(String category, double[] input) {
    //     // switch statement for each category
        
    // }
}
