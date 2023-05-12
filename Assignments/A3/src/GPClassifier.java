import java.util.ArrayList;
import java.util.Random;

public class GPClassifier {
    private static final int POPULATION_SIZE = 100;
    private static final int MAX_GENERATIONS = 50;
    private static final int MAX_TREE_SIZE = 100;
    private static final double MUTATION_RATE = 0.1;
    private static final double CROSSOVER_RATE = 0.9;
    private static final int TOURNAMENT_SIZE = 5;
    private static final int MAX_DEPTH = 3;

    private final ArrayList<String> categories;
    private final ArrayList<String[]> data;
    private Random random;
    private long seed = 0;

    public GPClassifier() {
        this.random = new Random(seed);

        this.categories = new ArrayList<>();
        this.data = new ArrayList<>();

        // Add the categories
        categories.add("age");
        categories.add("menopause");
        categories.add("tumor-size");
        categories.add("inv-nodes");
        categories.add("node-caps");
        categories.add("deg-malig");
        categories.add("breast");
        categories.add("breast-quad");
        categories.add("irradiat");

        // Add the possible values for each category
        String[] age = { "10-19", "20-29", "30-39", "40-49", "50-59", "60-69",
                "70-79", "80-89", "90-99" };
        String[] menopause = { "lt40", "ge40", "premeno" };
        String[] tumorSize = { "0-4", "5-9", "10-14", "15-19", "20-24", "25-29",
                "30-34", "35-39", "40-44", "45-49", "50-54", "55-59" };
        String[] invNodes = { "0-2", "3-5", "6-8", "9-11", "12-14", "15-17",
                "18-20", "21-23", "24-26", "27-29", "30-32", "33-35", "36-39" };
        String[] nodeCaps = { "yes", "no" };
        String[] degMalig = { "1", "2", "3" };
        String[] breast = { "left", "right" };
        String[] breastQuad = { "left-up", "left-low", "right-up", "right-low",
                "central" };
        String[] irradiat = { "yes", "no" };

        // Add the possible values to the data
        data.add(age);
        data.add(menopause);
        data.add(tumorSize);
        data.add(invNodes);
        data.add(nodeCaps);
        data.add(degMalig);
        data.add(breast);
        data.add(breastQuad);
        data.add(irradiat);
    }

    // Function to generate a random tree
    public Node generateRandomTree(int maxDepth) {
        int randomIndex = random.nextInt(categories.size());
        Node node = new Node(categories.get(randomIndex), data.get(randomIndex));

        if (maxDepth == 0) {
            // If the max depth has been reached, return a leaf node
            node.setLeaf();
            return node;
        }

        // Randomly decide whether to make the node a leaf or not only if it isn't the
        // first node
        if (maxDepth != MAX_DEPTH && random.nextBoolean()) {
            node.setLeaf();
        } else {
            // If the node is not a leaf, generate a random tree for each value
            // of the node
            Node[] children = new Node[node.getValues().length];

            for (int i = 0; i < children.length; i++) {
                children[i] = generateRandomTree(maxDepth - 1);
            }
            node.setChildren(children);
        }

        return node;
    }

    public void run() {
        // Generate the initial population
        Node[] population = new Node[POPULATION_SIZE];

        for (int i = 0; i < population.length; i++) {
            population[i] = generateRandomTree(MAX_DEPTH);
        }

        // Run the algorithm for the specified number of generations
        // for (int i = 0; i < MAX_GENERATIONS; i++) {
        //     // Evaluate the fitness of each individual

        // }
    }

    // Function to print a tree
    public void printTree(Node node, int level) {
        if (node == null) {
            return;
        }

        // Print the category of the node
        System.out.println();
        System.out.println("==CATEGORY==");
        System.out.println(node.getCategory());

        // If the node is a leaf, print its value
        if (node.isLeaf()) {
            System.out.print("Leaf: ");
            // Print out all the leaf values
            for (int i = 0; i < node.getValues().length; i++) {
                System.out.print(node.getValues()[i] + " ");
            }
            System.out.println();
        } else {
            // If the node is not a leaf, print its children
            System.out.println();
            for (int i = 0; i < node.getValues().length; i++) {
                System.out.println(node.getValues()[i] + ": ");
                printTree(node.getChildren()[i], level + 1);
            }
        }
    }
}
