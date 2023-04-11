public class App {
    public static void main(String[] args) throws Exception {
        
        String[] instanceNames = {
            "f1_l-d_kp_10_269",
            "f2_l-d_kp_20_878",
            "f3_l-d_kp_4_20",
            "f4_l-d_kp_4_11",
            "f5_l-d_kp_15_375",
            "f6_l-d_kp_10_60",
            "f7_l-d_kp_7_50",
            "knapPI_1_100_1000_1",
            "f8_l-d_kp_23_10000",
            "f9_l-d_kp_5_80",
            "f10_l-d_kp_20_879"
        };

        GA ga = new GA();

        ga.run("f1_l-d_kp_10_269");

        // for (String instanceName : instanceNames) {
        //     if (instanceName.equals("f5_l-d_kp_15_375")) break; // stupid double precision
        //     // Run the Genetic Algorithm to solve the knapsack problem for the given instance
        //     ga.run(instanceName);
        // }

    }
}
