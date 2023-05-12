import java.util.Random;

public class Node {
    private boolean isLeaf = false;
    private String category;
    private String[] values; // The number of child nodes this node must have
    private boolean[] leafDecisions; // Assigning yes or no to each value if it is a leaf
    private Node[] children; // One node for every value
    private Random random;
    private long seed;

    public Node(String category, String[] values) {
        this.category = category;
        this.values = values;
        this.random = new Random(seed);
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf() {
        isLeaf = true;

        // Randomly decide whether each value is yes or no
        leafDecisions = new boolean[values.length];
        for (int i = 0; i < leafDecisions.length; i++) {
            leafDecisions[i] = random.nextBoolean();
        }
    }

    public String getCategory() {
        return category;
    }

    public String[] getValues() {
        return values;
    }

    public boolean[] getLeafDecisions() {
        return leafDecisions;
    }

    // Function to set the child nodes for each value
    public void setChildren(Node[] children) {
        this.children = children;
    }

    public Node[] getChildren() {
        return children;
    }
}
