import java.util.ArrayList;
import java.util.List;

public class TabuSearch {
    private int MAX_ITERATIONS; // The maximum number of iterations to run the algorithm for
    private int MAX_CAPACITY; // The maximum capacity of each bin

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
            // Generate a neighbour solution
            List<List<Integer>> neighbourSolution = getNeighbourSolution(currentSolution);
            // If the neighbour solution is better than the current solution, set it as the
            // current solution
            if (HelperFunctions.getSolutionCost(neighbourSolution) < HelperFunctions.getSolutionCost(currentSolution)) {
                currentSolution = neighbourSolution;
            }
            // If the neighbour solution is better than the best solution, set it as the best
            // solution
            if (HelperFunctions.getSolutionCost(neighbourSolution) < HelperFunctions.getSolutionCost(bestSolution)) {
                bestSolution = neighbourSolution;
            }
        }

        // Print the results
        int endTime = (int) System.currentTimeMillis();
        int timeTaken = endTime - startTime;
        int solutionCost = HelperFunctions.getSolutionCost(bestSolution);
        int solutionSize = bestSolution.size();
        int solutionQuality = HelperFunctions.getSolutionQuality(solutionCost, optimalSolution);
        int[] result = { solutionCost, solutionSize, solutionQuality, timeTaken };
        testResults.add(result);
    }

    private List<List<Integer>> getInitialSolution() {
        List<List<Integer>> solution = new ArrayList<>();
        List<Integer> bin = new ArrayList<>();
        int binCapacity = 0;

        for (int i = 0; i < values.length; i++) {
            // If the value can fit in the bin, add it to the bin
            if (binCapacity + values[i] <= MAX_CAPACITY) {
                bin.add(values[i]);
                binCapacity += values[i];
            }
            // If the value cannot fit in the bin, add the bin to the solution and create a
            // new bin
            else {
                solution.add(bin);
                bin = new ArrayList<>();
                bin.add(values[i]);
                binCapacity = values[i];
            }
        }
        // Add the last bin to the solution
        solution.add(bin);

        return solution;
    }

    private List<List<Integer>> getNeighbourSolution(List<List<Integer>> solution) {
        List<List<Integer>> neighbourSolution = new ArrayList<>();
        List<Integer> bin = new ArrayList<>();
        int binCapacity = 0;

        // For each bin in the solution
        for (int i = 0; i < solution.size(); i++) {
            // For each value in the bin
            for (int j = 0; j < solution.get(i).size(); j++) {
                // If the value can fit in the bin, add it to the bin
                if (binCapacity + solution.get(i).get(j) <= MAX_CAPACITY) {
                    bin.add(solution.get(i).get(j));
                    binCapacity += solution.get(i).get(j);
                }
                // If the value cannot fit in the bin, add the bin to the solution and create a
                // new bin
                else {
                    neighbourSolution.add(bin);
                    bin = new ArrayList<>();
                    bin.add(solution.get(i).get(j));
                    binCapacity = solution.get(i).get(j);
                }
            }
            // Add the last bin to the solution
            neighbourSolution.add(bin);
        }

        return neighbourSolution;
    }


}
