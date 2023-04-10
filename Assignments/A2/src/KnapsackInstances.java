import java.util.HashMap;

/*
 * This class represents a collection of knapsack instances and is responsible for
 * returning the values, weights, and capacities of the knapsack instances that
 * the GA calls for
 */
public class KnapsackInstances {
    public static KnapsackInstances instance = null;
    private static HashMap<String, Double> knownOptimums = new HashMap<String, Double>();
    private static HashMap<String, int[]> knapsackValues = new HashMap<String, int[]>();
    private static HashMap<String, int[]> knapsackWeights = new HashMap<String, int[]>();
    private static HashMap<String, int[]> knapsackCapacities = new HashMap<String, int[]>();
    
    /**
     * Returns the singleton instance of the KnapsackInstances class
     * @return the singleton instance of the KnapsackInstances class
     */
    public static KnapsackInstances getInstance() {
        if (instance == null) {
            instance = new KnapsackInstances();
        }
        return instance;
    }

    public KnapsackInstances() {
        // Populate the known optimums
        knownOptimums.put("f1_l-d_kp_10_269", 295.0);
        knownOptimums.put("f2_l-d_kp_20_878", 1024.0);
        knownOptimums.put("f3_l-d_kp_4_20", 35.0);
        knownOptimums.put("f4_l-d_kp_4_11", 23.0);
        knownOptimums.put("f5_l-d_kp_15_375", 481.0694);
        knownOptimums.put("f6_l-d_kp_10_60", 52.0);
        knownOptimums.put("f7_l-d_kp_7_50", 107.0);
        knownOptimums.put("knapPI_1_100_1000_1", 9147.0);
        knownOptimums.put("f8_l-d_kp_23_10000", 9767.0);
        knownOptimums.put("f9_l-d_kp_5_80", 130.0);
        knownOptimums.put("f10_l-d_kp_20_879", 1025.0);

        // Populate the knapsack values, weights and capacities from the files
    }

    /**
     * Constructs and returns a new Knapsack object from the instance name,
     * which is used to retrieve the values, weights, and capacity of the knapsack
     * @param instanceName
     * @return a new Knapsack object
     */
    public Knapsack getKnapsack(String instanceName) {
        return new Knapsack(
            getKnapsackValues(instanceName), 
            getKnapsackWeights(instanceName), 
            getKnapsackCapacity(instanceName),
            getKnownOptimum(instanceName)
        );
    }

    public int[] getKnapsackValues(String instanceName) {
        return knapsackValues.get(instanceName);
    }

    public int[] getKnapsackWeights(String instanceName) {
        return knapsackWeights.get(instanceName);
    }

    public int getKnapsackCapacity(String instanceName) {
        return knapsackCapacities.get(instanceName)[0];
    }

    public double getKnownOptimum(String instanceName) {
        return knownOptimums.get(instanceName);
    }
}
