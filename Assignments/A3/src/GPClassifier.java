import java.util.ArrayList;
import java.util.Random;

public class GPClassifier {
    private static final int POPULATION_SIZE = 100;
    private static final int MAX_GENERATIONS = 50;
    private static final double MUTATION_RATE = 0.1;
    private static final double CROSSOVER_RATE = 0.9;
    private static final int TOURNAMENT_SIZE = 5;
    private static final int MAX_DEPTH = 5;

    private final ArrayList<String> categories;
    private final ArrayList<String[]> data;
    private ArrayList<double[]> encodedData;
    private Random random;
    private long seed = 1;

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
        String[] breastQuad = { "left_up", "left_low", "right_up", "right_low",
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

        // Encode the data
        encodedData = new ArrayList<>();
        encodedData = Encoder.encodeData(data);
    }

    // Function to generate a random tree
    public Node generateRandomTree(int maxDepth) {
        int randomIndex = random.nextInt(categories.size());
        Node node = new Node(categories.get(randomIndex), encodedData.get(randomIndex));

        if (maxDepth == 0) {
            // If the max depth has been reached, return a leaf node
            node.setLeaf(true);
            return node;
        }

        // Randomly decide whether to make the node a leaf or not only if it isn't the
        // first node
        if (maxDepth != MAX_DEPTH && random.nextBoolean()) {
            node.setLeaf(true);
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

    public void run(ArrayList<double[]> inputsList) {
        // Generate the initial population
        Node[] population = new Node[POPULATION_SIZE];

        for (int i = 0; i < population.length; i++) {
            population[i] = generateRandomTree(MAX_DEPTH);
        }

        // Run the algorithm for the specified number of generations
        for (int i = 0; i < MAX_GENERATIONS; i++) {
            // Evaluate the fitness of each individual
            double[] fitness = new double[POPULATION_SIZE];

            for (int j = 0; j < population.length; j++) {
                // Run each tree through all the inputs and calculate the number of
                // correct classifications / total number of classifications as the fitness
                int correctClassifications = 0;

                for (double[] inputs : inputsList) {
                    if (TreeInterpreter.interpret(population[j], inputs)) {
                        correctClassifications++;
                    }
                }

                fitness[j] = (double) correctClassifications / inputsList.size();

                // If the fitness is 1, then the tree is perfect and we can stop
                // evaluating
                if (fitness[j] == 1) {
                    System.out.println("Perfect tree found!");
                    printTree(population[j], 0);
                    return;
                }
            }

            // Find the best individual in the population
            int bestIndex = 0;
            double bestFitness = fitness[0];

            for (int j = 1; j < fitness.length; j++) {
                if (fitness[j] > bestFitness) {
                    bestIndex = j;
                    bestFitness = fitness[j];
                }
            }

            System.out.println("Generation " + i + ":");
            printTree(population[bestIndex], 0);
            System.out.println("Fitness: " + bestFitness);

            // Create the next generation
            Node[] nextGeneration = new Node[POPULATION_SIZE];

            for (int j = 0; j < nextGeneration.length; j++) {
                // Select two parents using tournament selection
                Node parent1 = tournamentSelection(population, fitness, TOURNAMENT_SIZE);
                Node parent2 = tournamentSelection(population, fitness, TOURNAMENT_SIZE);

                // Perform crossover based on the crossover rate
                Node child;

                if (random.nextDouble() < CROSSOVER_RATE) {
                    child = crossover(parent1, parent2);
                } else {
                    child = parent1;
                }

                // Perform mutation based on the mutation rate
                if (random.nextDouble() < MUTATION_RATE) {
                    child = mutate(child);
                }

                nextGeneration[j] = child;
            }

            // Replace the old population with the new one
            population = nextGeneration;
        }
    }

    // Function to perform tournament selection
    public Node tournamentSelection(Node[] population, double[] fitness, int tournamentSize) {
        // Randomly select individuals from the population and return the best
        // individual out of the selected ones

        int bestIndex = random.nextInt(population.length);

        for (int i = 0; i < tournamentSize - 1; i++) {
            int randomIndex = random.nextInt(population.length);

            if (fitness[randomIndex] > fitness[bestIndex]) {
                bestIndex = randomIndex;
            }
        }

        return population[bestIndex];
    }

    // Function to perform crossover, ensuring that pruning will occur if the
    // maximum depth is reached
    public Node crossover(Node parent1, Node parent2) {
        
        // Randomly select a depth and a node from each parent
        // and swap the subtrees at those nodes.
        // If the maximum depth is reached, then cut the subtree off 
        // at the maximum depth.

        int depth1 = random.nextInt(MAX_DEPTH + 1);
        Node node1 = getNodeAtDepth(parent1, depth1);

        int depth2 = random.nextInt(MAX_DEPTH + 1);
        Node node2 = getNodeAtDepth(parent2, depth2);

        Node child = parent1.clone();

        if (depth1 == 0) {
            child = node2;
        } else {
            Node parent1Node = getNodeAtDepth(child, depth1 - 1);
            parent1Node.setChild(node2);
        }

        if (depth2 == 0) {
            child = node1;
        } else {
            Node parent2Node = getNodeAtDepth(child, depth2 - 1);
            parent2Node.setChild(node1);
        }

        return child;
    }

    // Function to perform mutation
    public Node mutate(Node node) {
        // Randomly select a node in the tree and replace it with a random tree
        // of the same depth
        int depth = random.nextInt(MAX_DEPTH + 1);
        Node nodeToMutate = getNodeAtDepth(node, depth);

        // Randomly decide whether to make the node a leaf or not
        if (random.nextBoolean()) {
            nodeToMutate.setLeaf(true);
        }

        // If the node is not a leaf, generate a random tree for each value
        // of the node
        if (!nodeToMutate.isLeaf()) {
            Node[] children = new Node[nodeToMutate.getValues().length];

            for (int i = 0; i < children.length; i++) {
                children[i] = generateRandomTree(MAX_DEPTH - depth);
            }
            nodeToMutate.setChildren(children);
        }

        return node;
    }

    // Function to get the node at a certain depth
    public Node getNodeAtDepth(Node node, int depth) {
        if (depth == 0) {
            return node;
        }

        if (node.isLeaf()) {
            return node;
        }

        Node[] children = node.getChildren();

        // Randomly select a child node
        int randomIndex = random.nextInt(children.length);

        return getNodeAtDepth(children[randomIndex], depth - 1);
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
