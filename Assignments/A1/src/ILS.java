import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ILS {
    private static final int MAX_ITERATIONS = 100; // The maximum number of iterations to run the algorithm for
    private static final int MAX_CAPACITY = 1000; // The maximum capacity of each bin
    private static final int NUM_NEIGHBOURS = 10; // The number of neighbours to generate for each iteration

    private Random random;

    private String dataset; // The name of the dataset
    private List<int[]> testResults; // A list of the results of each test
    private List<int[]> tests; // A list of the tests to run
    private int[] values; // The values to be packed into bins

    public ILS(String dataset, List<int[]> tests) {
        this.dataset = dataset;
        this.tests = tests;

        testResults = new ArrayList<>();
        random = new Random();
    }

    public void run() {
        for (int i = 0; i < tests.size(); i++) {
            solve(tests.get(i), i);
        }

        FileHandler.printSummary(testResults, dataset);
    }

    private void solve(int[] v, int index) {
        // Initialize variables
        this.values = v;
        int startTime = (int) System.currentTimeMillis();
        int numBins = 0;
        int optimalSolution = HelperFunctions.getOptimum(dataset, index);

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
            // If the new solution is better than the current best solution, set it as the new best solution
            if (calculateFitness(newSolution) > calculateFitness(bestSolution)) {
                bestSolution = newSolution;
            }
            currentSolution = newSolution;
        }

        // Calculate the number of bins used in the best solution
        numBins = bestSolution.size();

        int endTime = (int) System.currentTimeMillis();
        int timeToComplete = endTime - startTime;
        testResults.add(new int[] { timeToComplete, numBins, optimalSolution });
        FileHandler.writeData(dataset, timeToComplete, numBins, index, optimalSolution, "ILS");
    }

    private List<List<Integer>> getInitialSolution() {
        // Generate initial solution by greedily packing items into bins in
        // non-increasing order of size
        List<List<Integer>> solution = new ArrayList<>();
        int remainingCapacity = MAX_CAPACITY;

        for (int i = 0; i < this.values.length; i++) {
            if (this.values[i] <= remainingCapacity) {
                // If we have not yet created a bin, create one
                if (solution.isEmpty()) {
                    solution.add(new ArrayList<>());
                }

                // Add the item to the last bin
                solution.get(solution.size() - 1).add(i);
                remainingCapacity -= values[i];
            } else {
                // Create a new bin and add the item to it
                solution.add(new ArrayList<>());
                solution.get(solution.size() - 1).add(i);
                remainingCapacity = MAX_CAPACITY - values[i];
            }
        }
        
        return solution;
    }

    private int calculateFitness(List<List<Integer>> solution) {
        // Calculate fitness as the number of used bins
        int numUsedBins = 0;
        for (List<Integer> bin : solution) {
            if (!bin.isEmpty()) {
                numUsedBins++;
            }
        }
        return numUsedBins;
    }

    private List<List<Integer>> perturb(List<List<Integer>> solution) {
        // Perturb solution by randomly adding or removing a number of items between bins
        List<List<Integer>> perturbedSolution = new ArrayList<>();

        // Deep copy the solution
        for (List<Integer> bin : solution) {
            perturbedSolution.add(new ArrayList<>(bin));
        }

        // Randomly move items between bins this many times
        int numItemsToMove = random.nextInt(NUM_NEIGHBOURS + 1);

        // Move items between bins
        for (int i = 0; i < numItemsToMove; i++) {
            // Randomly select two bins
            int binIndexA = random.nextInt(perturbedSolution.size());
            int binIndexB = random.nextInt(perturbedSolution.size());
            
            // Make sure the bins are different
            while (binIndexA == binIndexB) {
                binIndexB = random.nextInt(perturbedSolution.size());
            }

            // Make sure the bin is not empty
            if (!perturbedSolution.get(binIndexA).isEmpty()) {
                // Randomly select an item from the first bin
                int itemIndex = random.nextInt(perturbedSolution.get(binIndexA).size());
                int item = perturbedSolution.get(binIndexA).get(itemIndex);

                // If the item fits in the second bin, move it
                if (item <= calculateRemainingCapacity(perturbedSolution.get(binIndexB))) {
                    perturbedSolution.get(binIndexA).remove(itemIndex);
                    perturbedSolution.get(binIndexB).add(item);
                }
            }
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

    private List<List<Integer>> localSearch(List<List<Integer>> solution) {
        // Perform local search on the solution by removing items from bins
        // and adding them to other bins if they fit
        List<List<Integer>> localSearchSolution = new ArrayList<>();

        // Deep copy the solution
        for (List<Integer> bin : solution) {
            localSearchSolution.add(new ArrayList<>(bin));
        }

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

                // Try to add the item to another bin
                if (itemRemoved) {
                    for (int k = 0; k < localSearchSolution.size(); k++) {
                        if (item <= calculateRemainingCapacity(localSearchSolution.get(k))) {
                            localSearchSolution.get(k).add(item);
                            break;
                        }
                    }
                }
            }
        }
        return localSearchSolution;
    }
}
