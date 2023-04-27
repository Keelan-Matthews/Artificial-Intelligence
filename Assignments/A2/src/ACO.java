import java.util.Arrays;
import java.util.Random;

public class ACO {
    private static final double ALPHA = 1.0;  // pheromone importance
    private static final double BETA = 2.0;   // heuristic importance
    private static final double RHO = 0.5;    // pheromone evaporation coefficient
    private static final int Q = 1;           // pheromone deposit amount
    private int MAX_ITERATIONS;  // maximum number of iterations
    
    private int numAnts;          // number of ants
    private static double[][] pheromone;      // pheromone matrix
    private static double[][] heuristic;      // heuristic information matrix
    private static double[] bestSolution;        // best solution found

    private Knapsack knapsack;
    private static Random rand = new Random();
    private int itemSize;

    public ACO() {
    }

    // This is the main method that will be called when the program is run
    public double run(String instanceName) {
        knapsack = KnapsackInstances.getInstance().getKnapsack(instanceName);
        itemSize = knapsack.getNumberOfItems();
        numAnts =itemSize;
        double totalValue = 0;
        MAX_ITERATIONS = 500;
        
        // Initialize pheromone and heuristic matrices
        pheromone = new double[itemSize][(int) knapsack.getKnapsackCapacity() + 1];
        heuristic = new double[itemSize][(int) knapsack.getKnapsackCapacity() + 1];

        for (int i = 0; i < itemSize; i++) {
            for (int j = 0; j <= knapsack.getKnapsackCapacity(); j++) {
                // Initialize pheromone matrix with a small value
                pheromone[i][j] = 0.1;
                // Initialize heuristic matrix with the ratio of value to weight
                heuristic[i][j] = knapsack.getItemValues()[i] / knapsack.getItemWeights()[i];
            }
        }

        // Initialize best solution
        bestSolution = new double[itemSize];

        // Run the algorithm for a maximum of MAX_ITERATIONS
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            // Create an ant for each iteration and generate a solution
            double[][] solutions = new double[numAnts][itemSize]; // Each ant will have a solution in a row

            // Generate a solution for each ant
            for (int j = 0; j < numAnts; j++) {
                solutions[j] = generateSolution(); // Using the updated pheromone and heuristic matrices
            }

            // Update pheromone matrix
            updatePheromones(solutions);
            // Update best solution
            updateBestSolution(solutions);

            totalValue = getTotalValueFromSolution(bestSolution);
        }

        return totalValue;
    }

    /**
     * Generates a solution for the knapsack problem using the ACO algorithm
     * @return the solution
     */
    public double[] generateSolution() {
        // Create a new solution
        double[] solution = new double[itemSize];
        // Initialize the solution with 0
        Arrays.fill(solution, 0);
        double remainingCapacity = knapsack.getKnapsackCapacity();

        // Fill the knapsack with items for a solution
        while (remainingCapacity > 0) {
            // Calculate the probability of selecting each item, this will be used to select the next item and
            // increases the probability of selecting items with a high heuristic value and a high pheromone value
            double[] probabilities = new double[itemSize];
            double sumOfProbabilities = 0;

            // Probability calculation
            for (int i = 0; i < itemSize; i++) {
                // Only calculate the probability for items that are not already in the solution and
                // the weight of the item is less than the remaining capacity of the knapsack
                if (solution[i] == 0 && knapsack.getItemWeights()[i] <= remainingCapacity) {
                    probabilities[i] = Math.pow(pheromone[i][(int) remainingCapacity], ALPHA) * Math.pow(heuristic[i][(int) remainingCapacity], BETA);
                    sumOfProbabilities += probabilities[i];
                }
            }

            // If the sum of probabilities is 0, then there are no more items that can be added to the solution
            if (sumOfProbabilities == 0) {
                break;
            }

            // Select the next item probabilistically
            double randomValue = rand.nextDouble() * sumOfProbabilities;
            int nextItem = 0;

            for (int i = 0; i < itemSize; i++) {
                // Only select the next item if there is not already an item in the solution and
                // the weight of the item is less than the remaining capacity of the knapsack
                if (solution[i] == 0 && knapsack.getItemWeights()[i] <= remainingCapacity) {
                    // Subtract the probability of the item from the random value
                    randomValue -= probabilities[i];

                    // If the random value is less than or equal to 0, then the item is selected
                    if (randomValue <= 0) {
                        nextItem = i;
                        break;
                    }
                }
            }

            // Add the next item to the solution
            solution[nextItem] = 1;
            remainingCapacity -= knapsack.getItemWeights()[nextItem];
        }

        return solution;
    }

    /**
     * Updates the pheromone matrix using the solutions generated by the ants
     * @param solutions the solutions generated by the ants
     */
    public void updatePheromones(double[][] solutions) {
        // Evaporate each pheromone value
        for (int i = 0; i < itemSize; i++) {
            for (int j = 0; j <= knapsack.getKnapsackCapacity(); j++) {
                pheromone[i][j] *= (1.0 - RHO);
            }
        }

        // Deposit pheromone on the edges used by the ants for good solutions
        for (int i = 0; i < numAnts; i++) {
            double[] solution = solutions[i];
            double solValue = getTotalValueFromSolution(solution);
            double solWeight = getTotalWeightFromSolution(solution);

            if (solValue >= getTotalValueFromSolution(bestSolution) && solWeight <= knapsack.getKnapsackCapacity()) {
                // Deposit more pheromone on the edges used by the ant for good solutions
                for (int j = 0; j < itemSize; j++) {
                    if (solution[j] == 1) {
                        pheromone[j][(int) (knapsack.getKnapsackCapacity() - knapsack.getItemWeights()[j])] += (1.0 - RHO) * Q / solValue;
                    }
                }
            } else {
                // Deposit less pheromone on the edges used by the ant for bad solutions (1/10 of the good solution)
                for (int j = 0; j < itemSize; j++) {
                    if (solution[j] == 1) {
                        pheromone[j][(int) (knapsack.getKnapsackCapacity() - knapsack.getItemWeights()[j])] += (1.0 - RHO) * Q / (10 * solValue);
                    }
                }
            }
        }
    }

    /**
     * Updates the best solution found so far
     * @param solutions the solutions generated by the ants
     */
    public void updateBestSolution(double[][] solutions) {
        double bestValue = 0;
        double[] tempBestSolution = new double[itemSize];

        // Find the best solution
        for (int i = 0; i < numAnts; i++) {
            double solValue = getTotalValueFromSolution(solutions[i]);
            double solWeight = getTotalWeightFromSolution(solutions[i]);

            // Update the best solution
            if (solWeight <= knapsack.getKnapsackCapacity() && solValue > bestValue) {
                bestValue = solValue;
                tempBestSolution = solutions[i];
            }

            // Update the best solution found so far
            if (bestValue > getTotalValueFromSolution(bestSolution)) {
                bestSolution = tempBestSolution;
            }
        }
    }

    /**
     * Calculates the total value of a solution
     * @param solution
     * @return
     */
    private double getTotalValueFromSolution(double[] solution) {
        double totalValue = 0;

        // Add the value of each item in the solution if it is in the solution
        for (int i = 0; i < solution.length; i++) {
            totalValue += solution[i] * knapsack.getItemValues()[i];
        }

        return totalValue;
    }

    /**
     * Calculates the total weight of a solution
     * @param solution
     * @return
     */
    private double getTotalWeightFromSolution(double[] solution) {
        double totalWeight = 0;
        // Add the weight of each item in the solution if it is in the solution
        for (int i = 0; i < solution.length; i++) {
            totalWeight += solution[i] * knapsack.getItemWeights()[i];
        }

        return totalWeight;
    }
}
