import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {
        // Define the puzzle instances
        int[][] instances = {
            {1, 0, 3, 4, 2, 5, 7, 8, 6},  // Easy
            {1, 2, 3, 4, 5, 6, 8, 7, 0},  // Medium
            {0, 1, 3, 4, 2, 5, 7, 8, 6}   // Hard
        };

        // Test the algorithms on the instances
        for (int i = 0; i < instances.length; i++) {
            int[] instance = instances[i];
            System.out.println("Instance " + (i + 1) + ": " + Arrays.toString(instance));

            long startTime = System.nanoTime();
            boolean bfsResult = EightPuzzle.bestFirstSearch(instance);
            long bfsTime = System.nanoTime() - startTime;

            System.out.println("Best-First Search Result: " + bfsResult);
            System.out.println("Best-First Search Time: " + bfsTime + " nanoseconds");

            startTime = System.nanoTime();
            boolean aStarResult = EightPuzzle.aStar(instance);
            long aStarTime = System.nanoTime() - startTime;
            
            System.out.println("A* Result: " + aStarResult);
            System.out.println("A* Time: " + aStarTime + " nanoseconds");
            System.out.println();
        }
    }
}
