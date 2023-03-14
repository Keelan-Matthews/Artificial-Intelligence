import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ILS {
    private int MAX_ITERATIONS; // The maximum number of iterations to run the algorithm for
    private int MAX_CAPACITY; // The maximum capacity of each bin

    private Random random;

    private String dataset; // The name of the dataset
    private List<int[]> testResults; // A list of the results of each test
    private List<int[]> tests; // A list of the tests to run
    private String[] testNames; // A list of the names of the tests to run
    private int[] values; // The values to be packed into bins

    public ILS(String dataset, List<int[]> tests, String[] testNames) {
        this.dataset = dataset;
        this.tests = tests;
        this.testNames = testNames;

        testResults = new ArrayList<>();
        random = new Random();
    }

    public void run() {
        for (int i = 0; i < tests.size(); i++) {
            solve(tests.get(i), i);
        }

        FileHandler.printSummary(testResults, dataset, "ILS");
    }

    private void solve(int[] v, int index) {
        // Initialize variables
        this.values = v;
        int startTime = (int) System.currentTimeMillis();
        int optimalSolution = HelperFunctions.getOptimum(dataset, index);

        MAX_CAPACITY = v[1];
        MAX_ITERATIONS = v[1];

        // Remove the first two values from the array, as they are not part of the values
        // to be packed
        int[] temp = new int[v.length - 2];
        for (int i = 2; i < v.length; i++) {
            temp[i - 2] = v[i];
        }
        this.values = temp;



        // Generate initial solution
        List<List<Integer>> currentSolution = getInitialSolution();
        // Set it as the best solution
        List<List<Integer>> bestSolution = currentSolution;

        // Run the algorithm for a maximum number of iterations
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            // Perturb the current solution
            List<List<Integer>> newSolution = perturb(currentSolution);
            // Perform local search on the perturbed solution
            newSolution = localSearch(newSolution);
            // If the new solution is better than the current best solution, set it as the
            // new best solution
            if (newSolution.size() < bestSolution.size()) {
                bestSolution = newSolution;
            }
            currentSolution = newSolution;
        }



        int endTime = (int) System.currentTimeMillis();
        int timeToComplete = endTime - startTime;
        testResults.add(new int[] { timeToComplete, bestSolution.size(), optimalSolution });
        FileHandler.writeData(dataset, timeToComplete, bestSolution, index, optimalSolution, "ILS", testNames[index]);
    }

    /**
     * Generate an initial solution by adding items to bins using the best fit algorithm
     * @return
     */
    private List<List<Integer>> getInitialSolution() {
        Arrays.sort(values);
        HelperFunctions.reverse(values);

        List<List<Integer>> solution = new ArrayList<>();

        // Add items to bins using the best fit function
        for (int item : values) {
            solution = bestFit(solution, item);
        }

        return solution;
    }

    /**
     * Peturb the solution by removing half of the items from their bins and then packing 
     * them into new bins using the best fit algorithm
     * @param solution
     * @return
     */
    private List<List<Integer>> perturb(List<List<Integer>> solution) {
        List<List<Integer>> perturbedSolution = new ArrayList<>();

        // Deep copy the solution
        for (List<Integer> bin : solution) {
            perturbedSolution.add(new ArrayList<>(bin));
        }

        // Randomly remove half of the items from their bins
        int numItemsToRemove = solution.size() / 2;
        List<Integer> removedItems = new ArrayList<>();

        while (numItemsToRemove > 0) {
            // Randomly select a bin
            int binIndex = random.nextInt(perturbedSolution.size());

            // Make sure the bin is not empty
            if (!perturbedSolution.get(binIndex).isEmpty()) {
                // Randomly select an item from the bin
                int itemIndex = random.nextInt(perturbedSolution.get(binIndex).size());
                int item = perturbedSolution.get(binIndex).get(itemIndex);

                // Remove the item from the bin
                perturbedSolution.get(binIndex).remove(itemIndex);
                removedItems.add(item);

                // If the bin is now empty, remove it
                if (perturbedSolution.get(binIndex).isEmpty()) {
                    perturbedSolution.remove(binIndex);
                }

                numItemsToRemove--;
            }
        }

        // Pack the removed items into new bins using the best fit algorithm
        for (int item : removedItems) {
            perturbedSolution = bestFit(perturbedSolution, item);
        }

        return perturbedSolution;
    }

    private int calculateRemainingCapacity(List<Integer> bin) {
        // Calculate remaining capacity of a bin
        int remainingCapacity = MAX_CAPACITY;
        for (int item : bin) {
            remainingCapacity -= item;
        }
        return remainingCapacity;
    }

    /**
     * Perform local search on the solution by removing items from bins and adding them to
     * other bins if they fit
     * @param solution
     * @return
     */
    private List<List<Integer>> localSearch(List<List<Integer>> solution) {
        List<List<Integer>> localSearchSolution = new ArrayList<>();

        // Deep copy the solution
        for (List<Integer> bin : solution) {
            localSearchSolution.add(new ArrayList<>(bin));
        }

        int currentFitness = localSearchSolution.size();
        boolean improved = true;

        while (improved) {
            improved = false;

            // Remove items from bins and add them to other bins if they fit
            for (int i = 0; i < localSearchSolution.size(); i++) {
                for (int j = 0; j < localSearchSolution.get(i).size(); j++) {

                    int item = localSearchSolution.get(i).get(j);
                    boolean itemRemoved = false;

                    // Try to remove the item from the bin
                    if (item <= calculateRemainingCapacity(localSearchSolution.get(i))) {
                        localSearchSolution.get(i).remove(j);
                        itemRemoved = true;
                    }

                    // Try to add the item to another bin using best fit
                    if (itemRemoved) {
                        localSearchSolution = bestFit(localSearchSolution, item);
                    }

                    // If the solution has improved, set improved to true
                    if (localSearchSolution.size() < currentFitness) {
                        improved = true;
                        currentFitness = localSearchSolution.size();
                    }
                }

                // If the bin is now empty, remove it
                if (localSearchSolution.get(i).isEmpty()) {
                    localSearchSolution.remove(i);
                }
            }
        }
        return localSearchSolution;
    }

    /**
     * Finds the bin with the smallest remaining capacity that can fit the item
     * @param solution
     * @param item
     * @return
     */
    private List<List<Integer>> bestFit(List<List<Integer>> solution, int item) {
        int bestBinIndex = -1;
        int bestBinRemainingCapacity = MAX_CAPACITY;

        for (int i = 0; i < solution.size(); i++) {
            int remainingCapacity = calculateRemainingCapacity(solution.get(i));
            // If the item fits in the bin and the bin has the smallest remaining capacity
            if (item <= remainingCapacity && remainingCapacity < bestBinRemainingCapacity) {
                bestBinIndex = i;
                bestBinRemainingCapacity = remainingCapacity;
            }
        }

        // If no bin can fit the item, create a new bin
        if (bestBinIndex == -1) {
            solution.add(new ArrayList<>());
            bestBinIndex = solution.size() - 1;
        }

        // Add the item to the bin
        solution.get(bestBinIndex).add(item);

        return solution;
    }
}
