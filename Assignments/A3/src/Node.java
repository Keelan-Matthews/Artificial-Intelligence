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

    public Node(String category, double[] values) {
        this.category = category;
        this.values = values;
        this.random = new Random(seed);
        this.index = getCategoryIndex(category);
    }

    public int getIndex() {
        return index;
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
}
