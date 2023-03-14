import java.util.ArrayList;
import java.util.List;

public class ILS {
    private static final int MAX_ITERATIONS = 1000;
    private static final int MAX_CAPACITY = 1000;

    private String dataset;
    private List<int[]> testResults = new ArrayList<>();
    private List<int[]> tests;
    
    public ILS(String dataset, List<int[]> tests) {
        this.dataset = dataset;
        this.tests = tests;
    }

    public void run() {
        for (int i = 0; i < tests.size(); i++) {
            search(tests.get(i), i);
        }

        FileHandler.printSummary(testResults, dataset);
    }

    private void search(int[] values, int index) {
        // Initialize variables 
        int startTime = (int) System.currentTimeMillis();
        int numBins = 0;
        int optimalSolution = HelperFunctions.getOptimum(dataset, index);




        int endTime = (int) System.currentTimeMillis();
        int timeToComplete = endTime - startTime;
        testResults.add(new int[] {timeToComplete, numBins, optimalSolution});
        FileHandler.writeData(dataset, timeToComplete, numBins, index, optimalSolution, "ILS");
    }

}
