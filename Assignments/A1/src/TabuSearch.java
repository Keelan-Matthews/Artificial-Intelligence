import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TabuSearch {
    private int MAX_ITERATIONS; // The maximum number of iterations to run the algorithm for
    private int MAX_TABU_SIZE; // The maximum size of the tabu list
    private int MAX_CAPACITY; // The maximum capacity of each bin

    private Random random;
    
    private String dataset; // The name of the dataset
    private List<int[]> testResults; // A list of the results of each test
    private List<int[]> tests; // A list of the tests to run
    private String[] testNames; // A list of the names of the tests to run
    private int[] values; // The values to be packed into bins

    public TabuSearch(String dataset, List<int[]> tests, String[] testNames) {
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

        FileHandler.printSummary(testResults, dataset, "TabuSearch");
    }

    private void solve(int[] v, int index) {
        // Initialize variables
        this.values = v;
        int startTime = (int) System.currentTimeMillis();
        int optimalSolution = HelperFunctions.getOptimum(dataset, index);

        MAX_CAPACITY = v[1];
        MAX_ITERATIONS = v[1];
        MAX_TABU_SIZE = v[0];

        // Remove the first two values from the array, as they are not part of the values
        // to be packed
        int[] temp = new int[v.length - 2];
        for (int i = 2; i < v.length; i++) {
            temp[i - 2] = v[i];
        }
        this.values = temp;

        // Get the initial solution
        List<List<Integer>> currentSolution = HelperFunctions.getInitialSolution(values, MAX_CAPACITY);
        // Get the initial best solution
        List<List<Integer>> bestSolution = currentSolution;
        // Create the tabu list
        List<List<List<Integer>>> tabuList = new ArrayList<>();

        // Run the algorithm for a maximum number of iterations
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            // Perturb the current solution
            List<List<Integer>> newSolution = perturb(currentSolution);

            // See if the new solution is tabu
            if (!isTabu(newSolution, tabuList)) {
                // If it is not tabu, see if it is better than the current best solution
                if (newSolution.size() < bestSolution.size()) {
                    // If it is better, set it as the new best solution
                    bestSolution = newSolution;
                }
                // Set the new solution as the current solution
                currentSolution = newSolution;
                // Update the tabu list
                updateTabuList(newSolution, tabuList);
            }
        }



        int endTime = (int) System.currentTimeMillis();
        int timeToComplete = endTime - startTime;
        testResults.add(new int[] { timeToComplete, bestSolution.size(), optimalSolution });
        FileHandler.writeData(dataset, timeToComplete, bestSolution, index, optimalSolution, "TabuSearch", testNames[index]);
    }

    private boolean isTabu(List<List<Integer>> solution, List<List<List<Integer>>> tabuList) {
        for (List<List<Integer>> tabuSolution : tabuList) {
            if (HelperFunctions.compareSolutions(solution, tabuSolution)) {
                return true;
            }
        }
        return false;
    }

    private void updateTabuList(List<List<Integer>> solution, List<List<List<Integer>>> tabuList) {
        // If the tabu list is full, remove the first solution
        if (tabuList.size() == MAX_TABU_SIZE) {
            tabuList.remove(0);
        }
        // Add the new solution to the tabu list
        tabuList.add(solution);
    }

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
            perturbedSolution = HelperFunctions.bestFit(perturbedSolution, item, MAX_CAPACITY);
        }

        return perturbedSolution;
    } 
}
