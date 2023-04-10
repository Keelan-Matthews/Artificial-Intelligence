import java.util.Random;

// Define the class that represents a chromosome in the population
public class Chromosome implements Comparable<Chromosome> {
    // The genes of the chromosome comprise of 1s and 0s, where 1 means the item is in the knapsack, 
    // and 0 means the item is not in the knapsack
    public int[] genes; 
    public int fitness;
    public static final double MUTATION_RATE = 0.05;
    private Knapsack knapsack;

    public Chromosome(int[] genes, Knapsack knapsack) {
        this.knapsack = knapsack;
        this.genes = genes;
        this.fitness = knapsack.getFitness(genes);
    }

    public Chromosome(int[] genes) {
        this.genes = genes;
    }

    /**
     * Compare this chromosome to another chromosome
     * @param other
     * @return 1 if this chromosome is better than the other chromosome, -1 if this chromosome is worse than the other chromosome, 0 if they are equal
     */
    public int compareTo(Chromosome other) {
        return other.fitness - this.fitness;
    }

    /**
     * Perform crossover between this chromosome and another chromosome
     * @param other
     * @return the new child chromosome
     */
    public Chromosome crossover(Chromosome other) {
        int[] childGenes = new int[genes.length];

        // Randomly select a crossover point
        int crossoverPoint = new Random().nextInt(genes.length);

        // Copy the genes from the first parent up to the crossover point
        for (int i = 0; i < crossoverPoint; i++) {
            childGenes[i] = genes[i];
        }
        for (int i = crossoverPoint; i < genes.length; i++) {
            childGenes[i] = other.genes[i];
        }
        return new Chromosome(childGenes);
    }

    
    // Mutate the chromosome with a small probability
    public void mutate() {
        for (int i = 0; i < genes.length; i++) {
            // If the random number is less than the mutation rate, then flip the gene
            if (new Random().nextDouble() < MUTATION_RATE) {
                genes[i] = 1 - genes[i];
            }
        }

        // Update the fitness of the chromosome
        fitness = knapsack.getFitness(genes);
    }
}

