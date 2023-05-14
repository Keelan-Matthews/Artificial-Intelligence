import java.util.Random;

public class Node {
    private int index;
    private boolean isLeaf = false;
    private String category;
    private double[] values; // The number of child nodes this node must have
    private boolean[] leafDecisions; // Assigning yes or no to each value if it is a leaf
    private Node[] children; // One node for every value
    private Random random;
    private long seed;
    private double fitness = 0;

    public Node(String category, double[] values) {
        this.category = category;
        this.values = values;
        this.random = new Random(seed);
        this.index = getCategoryIndex(category);
    }

    public int getIndex() {
        return index;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;

        // Randomly decide whether each value is yes or no
        leafDecisions = new boolean[values.length];
        for (int i = 0; i < leafDecisions.length; i++) {
            leafDecisions[i] = random.nextBoolean();
        }
    }

    public String getCategory() {
        return category;
    }

    public double[] getValues() {
        return values;
    }

    public boolean[] getLeafDecisions() {
        return leafDecisions;
    }

    // Function to set the child nodes for each value
    public void setChildren(Node[] children) {
        this.children = children;
    }

    public void setChild(Node child) {
        // Randomly select a value and assign the child node to it
        int randomIndex = random.nextInt(values.length);
        children[randomIndex] = child;
    }

    public Node clone() {
        Node clone = new Node(category, values);
        clone.setLeaf(isLeaf);
        clone.setChildren(children);
        return clone;
    }

    public Node[] getChildren() {
        return children;
    }

    private int getCategoryIndex(String category) {
        int index = 0;
        switch (category) {
            case "age":
                index = 0;
                break;
            case "menopause":
                index = 1;
                break;
            case "tumor-size":
                index = 2;
                break;
            case "inv-nodes":
                index = 3;
                break;
            case "node-caps":
                index = 4;
                break;
            case "deg-malig":
                index = 5;
                break;
            case "breast":
                index = 6;
                break;
            case "breast-quad":
                index = 7;
                break;
            case "irradiat":
                index = 8;
                break;
            default:
                index = -1;
                break;
        }
        return index;
    }

    /**
     * This function will return a random node from the tree. The probability of
     * selecting a node is inversely proportional to its depth.
     * 
     * @return
     */
    public Node getRandomNode() {
        double p = 1.0;
        Node currentNode = this;
        Node parentNode = currentNode;

        while (!currentNode.isLeaf()) {
            int numChildren = currentNode.getValues().length;
            int randomIndex = random.nextInt(numChildren);

            // Examine all the children of the current node. If they are all leaves,
            // then return the current node's parent.
            boolean allLeaves = true;
            for (int i = 0; i < numChildren; i++) {
                if (!currentNode.getChildren()[i].isLeaf()) {
                    allLeaves = false;
                    break;
                }
            }
            if (allLeaves) {
                return currentNode;
            }

            // Otherwise, select a random child node
            parentNode = currentNode;
            currentNode = currentNode.getChildren()[randomIndex];
            p *= 0.5; // decrease the probability as depth increases
            if (currentNode.isLeaf() || random.nextDouble() > p) {
                break; // with probability p, stop descending the tree
            }
        }
        return parentNode;
    }


    // This function will prune any nodes that are beyond the specified depth
    public void pruneToDepth(int depth) {
        pruneBeyondDepthHelper(this, 1, depth);
    }

    private void pruneBeyondDepthHelper(Node node, int currentDepth, int maxDepth) {
        if (currentDepth >= maxDepth) {
            // Remove all children
            node.setLeaf(true);
        } else if (!node.isLeaf()) {
            for (Node child : node.getChildren()) {
                pruneBeyondDepthHelper(child, currentDepth + 1, maxDepth);
            }
        }
    }
}
