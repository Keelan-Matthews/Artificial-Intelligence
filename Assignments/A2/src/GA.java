import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GA {
    public static int TOURNAMENT_SIZE; // The number of chromosomes in the tournament
    public static int MAX_GENERATIONS; // The maximum number of generations
    public static int POPULATION_SIZE; // The number of chromosomes in the population
    private Knapsack knapsack;

    public GA() {
    }

    // This is the main method that will be called when the program is run
    public int run(String instanceName) {
        knapsack = KnapsackInstances.getInstance().getKnapsack(instanceName);
        POPULATION_SIZE = knapsack.getNumberOfItems();
        MAX_GENERATIONS = 1000;
        TOURNAMENT_SIZE = knapsack.getNumberOfItems() / 4;
        int bestSolution = 0;

        // Create the initial population, i.e. number of chromosomes
        ArrayList<Chromosome> population = new ArrayList<>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            // Generate a random set of genes from the knapsack for each chromosome
            int[] genes = knapsack.getGenes();
            population.add(new Chromosome(genes, knapsack));
        }

        // Evolve the population
        for (int i = 0; i < MAX_GENERATIONS; i++) {
            population = evolvePopulation(population);

            if (bestSolution < population.get(0).fitness) {
                bestSolution = population.get(0).fitness;
            }

            // If the best solution found so far is the optimal solution, then stop
            if (bestSolution == knapsack.getKnownOptimum()) {
                break;
            }
        }

        return bestSolution;
    }

    /**
     * Performs tournament selection on the population to get the parent chromosomes with the best fitness
     * and then performs crossover and mutation on the parent chromosomes to get the child chromosomes
     * @param population
     * @return the new population of child chromosomes
     */
    public ArrayList<Chromosome> evolvePopulation(ArrayList<Chromosome> population) {
        ArrayList<Chromosome> parentChromosomes = new ArrayList<>();

        // Perform tournament selection on the population to get the parent chromosomes with the best fitness
        for (int i = 0; i < POPULATION_SIZE / 2; i++) {
            Chromosome parent1 = tournamentSelection(population);
            Chromosome parent2 = tournamentSelection(population);
            parentChromosomes.add(parent1);
            parentChromosomes.add(parent2);
        }

        // Perform crossover and mutation on the parent chromosomes
        ArrayList<Chromosome> childChromosomes = new ArrayList<>();

        // the first two parents will be selected to make the first two children,
        // the third and fourth parents will be selected to make the third and fourth children, etc.
        for (int i = 0; i < parentChromosomes.size(); i += 2) {
            // This line gets the first parent chromosome and performs crossover with the second parent chromosome
            Chromosome child1 = parentChromosomes.get(i).crossover(parentChromosomes.get(i + 1));
            Chromosome child2 = parentChromosomes.get(i + 1).crossover(parentChromosomes.get(i));
            // Mutate the children (with a small probability)
            child1.mutate();
            child2.mutate();
            // Add the children to the childChromosomes list
            childChromosomes.add(child1);
            childChromosomes.add(child2);
        }

        // A BIT IFFY HERE
        // Sort the population by fitness and keep the best individuals
        // Collections.sort(population);
        Collections.sort(childChromosomes);

        // Add the best individuals from the previous generation to the new generation
        // for (int i = 0; i < POPULATION_SIZE / 2; i++) {
        //     childChromosomes.set(i, population.get(i));
        // }

        return childChromosomes;
    }

    /**
     * Performs tournament selection on the population to get the parent chromosomes with the best fitness
     * @param population
     * @return the parent chromosome with the best fitness
     */
    private static Chromosome tournamentSelection(ArrayList<Chromosome> population) {
        ArrayList<Chromosome> tournament = new ArrayList<>();

        // Select a random chromosome from the population and add it to the tournament for
        // TOURNAMENT_SIZE times
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            Chromosome individual = population.get(new Random().nextInt(population.size()));
            tournament.add(individual);
        }

        // Sort the tournament by fitness and return the best individual
        Collections.sort(tournament);
        return tournament.get(0);
    }

    // This method prints out the population in a nice format
    private static void printPopulation(ArrayList<Chromosome> population) {
        System.out.println("Population: ");
        for (Chromosome chromosome : population) {
            chromosome.print();
        }
        System.out.println();
    }
}
