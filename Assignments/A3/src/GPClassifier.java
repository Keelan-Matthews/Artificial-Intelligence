import java.util.ArrayList;
import java.util.Random;

public class GPClassifier {
    private static final int POPULATION_SIZE = 100;
    private static final int MAX_GENERATIONS = 50;
    private static final double MUTATION_RATE = 0.1;
    private static final double CROSSOVER_RATE = 0.9;
    private static final int TOURNAMENT_SIZE = 20;
    private static final int MAX_DEPTH = 4;

    private final ArrayList<String> categories;
    private final ArrayList<String[]> data;
    private ArrayList<double[]> encodedData;
    private Random random;
    private long seed = 1;
    private Node bestTree;

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

    public void train(ArrayList<double[]> inputsList) {
        // Generate the initial population
        Node[] population = new Node[POPULATION_SIZE];

        for (int i = 0; i < population.length; i++) {
            population[i] = generateRandomTree(MAX_DEPTH);
        }

        // Run the algorithm for the specified number of generations
        for (int i = 0; i < MAX_GENERATIONS; i++) {
            // Evaluate the fitness of each individual
            double[] fitness = evaluateFitness(population, inputsList);

            // Find the best individual in the population
            double bestFitness = fitness[0];

            for (int j = 1; j < fitness.length; j++) {
                if (fitness[j] > bestFitness) {
                    bestFitness = fitness[j];
                }
            }

            // System.out.println("Generation " + i + ":");
            // System.out.println("Fitness: " + bestFitness);

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
                    mutate(child);
                }

                nextGeneration[j] = child;
            }

            // Replace the old population with the new one
            population = nextGeneration;
        }

        // Find the best individual in the population
        int bestIndex = 0;
        double[] fitness = evaluateFitness(population, inputsList);

        for (int j = 1; j < population.length; j++) {
            if (fitness[j] > fitness[bestIndex]) {
                bestIndex = j;
            }
        }

        System.out.println("Final generation:");
        printTree(population[bestIndex], 0);
        System.out.println("Fitness: " + fitness[bestIndex]);

        bestTree = population[bestIndex];
    }

    /**
     * Predicts the class of the given inputs using the best tree found during training
     * @param inputs
     * @return
     */
    public boolean predict(double[] inputs) {
        if (bestTree == null) {
            System.out.println("The classifier has not been trained yet.");
            return false;
        }

        return TreeInterpreter.interpret(bestTree, inputs);
    }

    /**
     * Generates a random tree with the given max depth
     * 
     * @param maxDepth
     * @return
     */
    public Node generateRandomTree(int maxDepth) {
        int randomIndex = random.nextInt(categories.size());
        Node node = new Node(categories.get(randomIndex), encodedData.get(randomIndex));

        if (maxDepth == 0) {
            // If the max depth has been reached, return a leaf node
            node.setLeaf(true);
            return node;
        }

        // Randomly decide whether to make the node a leaf or not only if it isn't the
        // first node with a lower probability of making it a leaf
        if (random.nextDouble() < 0.3 && maxDepth != MAX_DEPTH) {
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

    /**
     * Function to evaluate the fitness of each individual in the population
     * 
     * @param population
     * @param inputsList
     * @return
     */
    public double[] evaluateFitness(Node[] population, ArrayList<double[]> inputsList) {
        double[] fitness = new double[population.length];

        for (int i = 0; i < population.length; i++) {
            // Run each tree through all the inputs and calculate the number of
            // correct classifications / total number of classifications as the fitness
            int correctClassifications = 0;

            for (double[] inputs : inputsList) {
                if (TreeInterpreter.interpret(population[i], inputs)) {
                    correctClassifications++;
                }
            }

            fitness[i] = (double) correctClassifications / inputsList.size();
        }

        return fitness;
    }

    /**
     * Function to perform tournament selection
     * 
     * @param population
     * @param fitness
     * @param tournamentSize
     * @return
     */
    public Node tournamentSelection(Node[] population, double[] fitness, int tournamentSize) {

        // Pick the TOURNAMENT_SIZE random individuals from the population
        Node[] tournament = new Node[tournamentSize];
        double[] tournamentFitness = new double[tournamentSize];

        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.length);
            tournament[i] = population[randomIndex];
            tournamentFitness[i] = fitness[randomIndex];
        }

        // Find the best individual in the tournament
        int bestIndex = 0;

        for (int i = 1; i < tournamentSize; i++) {
            if (tournamentFitness[i] > tournamentFitness[bestIndex]) {
                bestIndex = i;
            }
        }

        return tournament[bestIndex];
    }

    /**
     * Function to perform crossover between two parents
     * 
     * @param parent1
     * @param parent2
     * @return
     */
    public Node crossover(Node parent1, Node parent2) {
        // Clone the parents to preserve their structure
        Node child1 = parent1.clone();
        Node child2 = parent2.clone();

        // Randomly select a node from each parent to perform the crossover
        Node node1 = child1.getRandomNode();
        Node node2 = child2.getRandomNode();

        // Swap the children of the selected nodes
        Node[] children1 = node1.getChildren();
        Node[] children2 = node2.getChildren();

        int randomIndex1 = random.nextInt(children1.length);
        int randomIndex2 = random.nextInt(children2.length);
        Node temp = children1[randomIndex1];
        children1[randomIndex1] = children2[randomIndex2];
        children2[randomIndex2] = temp;

        // Prune the children if the maximum depth is exceeded
        child1.pruneToDepth(MAX_DEPTH);
        child2.pruneToDepth(MAX_DEPTH);

        // Randomly choose one of the children as the new child
        return random.nextBoolean() ? child1 : child2;
    }

    /**
     * Function to mutate a node
     * 
     * @param node
     */
    public void mutate(Node node) {
        // If the node is a leaf node, randomly choose a new value for it
        if (node.isLeaf()) {
            double[] values = node.getValues();
            int randomIndex = random.nextInt(values.length);
            node.getLeafDecisions()[randomIndex] = random.nextBoolean();
        } else {
            // If the node is not a leaf node, recursively mutate one of its children
            Node[] children = node.getChildren();
            int randomIndex = random.nextInt(children.length);
            Node child = children[randomIndex];
            mutate(child);
        }
    }

    // Function to print a tree
    public void printTree(Node node, int level) {
        if (node == null) {
            return;
        }

        String indent = "";
        for (int i = 0; i < level; i++) {
            indent += "  ";
        }

        if (node.isLeaf()) {
            System.out.println(indent + "- " + node.getCategory() + " = [LEAF]");
        } else {
            System.out.println(indent + "- " + node.getCategory() + " = {");
            for (int i = 0; i < node.getValues().length; i++) {
                System.out.print(indent + "    " + Math.round((double) node.getValues()[i] * 1000.0) / 10.0  + " -> ");
                printTree(node.getChildren()[i], level + 1);
            }
            System.out.println(indent + "}");
        }
    }
}
