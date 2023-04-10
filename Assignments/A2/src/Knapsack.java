import java.util.Random;

public class Knapsack {
    // This class is a Singleton that allows the other classes to access the items and knapsack capacity
    private static Knapsack instance = null;
    private static int[] ITEM_VALUES;
    private static int[] ITEM_WEIGHTS;
    private static int KNAPSACK_CAPACITY;

    private Knapsack() {
    }

    /**
     * Turn the class into a Singleton by only allowing one instance of the class to be created
     * @return the instance of the class
     */
    public static Knapsack getInstance() {
        if (instance == null) {
            instance = new Knapsack();
        }
        return instance;
    }

    /**
     * Get the total value of the items in the knapsack for the given genes
     * @param genes
     * @return the total value of the items in the knapsack for the given genes
     */
    public int getTotalValue(int[] genes) {
        int totalValue = 0;
        for (int i = 0; i < genes.length; i++) {
            // If the gene is 1, then add the value of the item to the total value
            if (genes[i] == 1) {
                totalValue += ITEM_VALUES[i];
            }
        }
        return totalValue;
    }

    /**
     * Get the total weight of the items in the knapsack for the given genes
     * @param genes
     * @return the total weight of the items in the knapsack for the given genes
     */
    public int getTotalWeight(int[] genes) {
        int totalWeight = 0;
        for (int i = 0; i < genes.length; i++) {
            // If the gene is 1, then add the weight of the item to the total weight
            if (genes[i] == 1) {
                totalWeight += ITEM_WEIGHTS[i];
            }
        }
        return totalWeight;
    }

    /**
     * Get the fitness of the chromosome by calculating the total value of the items in the knapsack
     * using the genes of the chromosome
     * @param genes
     * @return the total value of the items in the knapsack
     */
    public int getFitness(int[] genes) {
        int totalValue = getTotalValue(genes);
        int totalWeight = getTotalWeight(genes);
        // If the total weight exceeds the knapsack capacity, then the fitness is 0
        if (totalWeight > KNAPSACK_CAPACITY) {
            return 0;
        } else {
            return totalValue;
        }
    }

    /**
     * Generate a random set of genes
     * @return the randomly generated genes
     */
    public int[] getGenes() {
        int numberOfGenes = ITEM_VALUES.length;
        int[] genes = new int[numberOfGenes];

        // Randomly generate a 0 or 1 of a gene
        for (int i = 0; i < numberOfGenes; i++) {
            genes[i] = new Random().nextInt(2);
        }
        return genes;
    }

    public int[] getItemValues() {
        return ITEM_VALUES;
    }

    public int[] getItemWeights() {
        return ITEM_WEIGHTS;
    }

    public int getKnapsackCapacity() {
        return KNAPSACK_CAPACITY;
    }

    public int getNumberOfItems() {
        return ITEM_VALUES.length;
    }
}
