import java.util.List;

public class ILS {

    private String dataset;
    private List<int[]> tests;
    
    ILS(String dataset, List<int[]> tests) {
        tests = this.tests;
        dataset = this.dataset;
    }

    public void run() {
        for (int i = 0; i < tests.size(); i++) {
            search(tests.get(i), i);
        }
    }

    private void search(int[] values, int index) {
        // Initialize variables 
        int startTime = (int) System.currentTimeMillis();
        int numBins = 0;
        int optimalSolution = HelperFunctions.getOptimum(dataset, index);




        int endTime = (int) System.currentTimeMillis();
        int timeToComplete = endTime - startTime;
        FileHandler.writeData(dataset, timeToComplete, numBins, index, optimalSolution);
    }
}
