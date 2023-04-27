public class App {
    public static void main(String[] args) throws Exception {
        
        // The names of the instances to run
        String[] instanceNames = {
            "f1_l-d_kp_10_269",
            "f2_l-d_kp_20_878",
            "f3_l-d_kp_4_20",
            "f4_l-d_kp_4_11",
            "f5_l-d_kp_15_375",
            "f6_l-d_kp_10_60",
            "f7_l-d_kp_7_50",
            "f8_l-d_kp_23_10000",
            "f9_l-d_kp_5_80",
            "f10_l-d_kp_20_879",
            "knapPI_1_100_1000_1"
        };

        // Run the algorithms
        runGA(instanceNames);
        runACO(instanceNames);

        // Write the results to a file
        FileHandler.writeSummary();
    }

    /**
     * Run the GA algorithm
     * @param instanceNames
     */
    private static void runGA(String[] instanceNames) {
        GA ga = new GA();

        System.out.println();
        System.out.println("GA");
        System.out.println();

        for (String instanceName : instanceNames) {
            float startTime = System.nanoTime(); // Get the start time
            double bestSolution = ga.run(instanceName); // Run the algorithm
            float endTime = System.nanoTime(); // Get the end time
            
            // Get the total time in seconds
            float totalTime = (endTime - startTime) / 1000000000;
            totalTime = Math.round(totalTime * 100) / 100f;

            // Write the results to a file
            FileHandler.writeData(instanceName, bestSolution, totalTime, "GA");

            System.out.println("Instance: " + instanceName);
            System.out.println("Best solution: " + bestSolution);
            System.out.println("Known optimum: " + KnapsackInstances.getOptimum(instanceName));
            System.out.println("Total time: " + totalTime + " seconds");
            System.out.println();
        }
    }

    /**
     * Run the ACO algorithm
     * @param instanceNames
     */
    private static void runACO(String[] instanceNames) {
        ACO aco = new ACO();

        System.out.println();
        System.out.println("ACO");
        System.out.println();

        for (String instanceName : instanceNames) {
            float startTime = System.nanoTime(); // Get the start time
            double bestSolution = aco.run(instanceName); // Run the algorithm
            float endTime = System.nanoTime(); // Get the end time
            
            // Get the total time in seconds
            float totalTime = (endTime - startTime) / 1000000000;
            totalTime = Math.round(totalTime * 100) / 100f;

            // Write the results to a file
            FileHandler.writeData(instanceName, bestSolution, totalTime, "ACO");

            System.out.println("Instance: " + instanceName);
            System.out.println("Best solution: " + bestSolution);
            System.out.println("Known optimum: " + KnapsackInstances.getOptimum(instanceName));
            System.out.println("Total time: " + totalTime + " seconds");
            System.out.println();
        }
    }
}
