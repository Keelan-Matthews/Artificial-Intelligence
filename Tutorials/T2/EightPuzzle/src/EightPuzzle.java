import java.util.*;

public class EightPuzzle {
    // Define the goal state
    private static final int[] GOAL_STATE = {1, 2, 3, 4, 5, 6, 7, 8, 0};

    // Define the heuristic function
    private static int heuristic(int[] state) {
        // Calculate the sum of the Manhattan distances between each tile and its goal position
        int distance = 0;
        for (int i = 0; i < 9; i++) {
            if (state[i] != 0) {
                int rowDist = Math.abs(i / 3 - (state[i] - 1) / 3);
                int colDist = Math.abs(i % 3 - (state[i] - 1) % 3);
                distance += rowDist + colDist;
            }
        }
        return distance;
    }

    // Define the best-first search algorithm
    public static boolean bestFirstSearch(int[] startState) {
        PriorityQueue<int[]> frontier = new PriorityQueue<>((a, b) -> heuristic(a) - heuristic(b));
        frontier.offer(startState);
        Set<String> explored = new HashSet<>();
        while (!frontier.isEmpty()) {
            int[] state = frontier.poll();
            if (Arrays.equals(state, GOAL_STATE)) {
                return true;
            }
            explored.add(Arrays.toString(state));
            for (int[] neighbor : getNeighbors(state)) {
                if (!explored.contains(Arrays.toString(neighbor))) {
                    frontier.offer(neighbor);
                }
            }
        }
        return false;
    }

    // Define the A* algorithm
    public static boolean aStar(int[] startState) {
        PriorityQueue<int[]> frontier = new PriorityQueue<>((a, b) -> heuristic(a) + a[1] - heuristic(b) - b[1]);
        frontier.offer(new int[] {startState, 0});
        Map<String, Integer> explored = new HashMap<>();
        while (!frontier.isEmpty()) {
            int[] stateInfo = frontier.poll();
            int[] state = stateInfo[0];
            int g = stateInfo[1];
            if (Arrays.equals(state, GOAL_STATE)) {
                return true;
            }
            explored.put(Arrays.toString(state), g);
            for (int[] neighbor : getNeighbors(state)) {
                int neighborG = g + 1;
                String neighborKey = Arrays.toString(neighbor);
                if (!explored.containsKey(neighborKey) || neighborG < explored.get(neighborKey)) {
                    frontier.offer(new int[] {neighbor, neighborG});
                    explored.put(neighborKey, neighborG);
                }
            }
        }
        return false;
    }

    // Define a function to get the neighbors of a state
    public static List<int[]> getNeighbors(int[] state) {
        List<int[]> neighbors = new ArrayList<>();
        int index = indexOf(state, 0);
    
        if (index != 0 && index != 3 && index != 6) {
            // Move the tile left
            int[] neighbor = Arrays.copyOf(state, 9);
            swap(neighbor, index, index - 1);
            neighbors.add(neighbor);
        }
        if (index != 0 && index != 1 && index != 2) {
            // Move the tile up
            int[] neighbor = Arrays.copyOf(state, 9);
            swap(neighbor, index, index - 3);
            neighbors.add(neighbor);
        }
        if (index != 6 && index != 7 && index != 8) {
            // Move the tile down
            int[] neighbor = Arrays.copyOf(state, 9);
            swap(neighbor, index, index + 3);
            neighbors.add(neighbor);
        }
        if (index != 2 && index != 5 && index != 8) {
            // Move the tile right
            int[] neighbor = Arrays.copyOf(state, 9);
            swap(neighbor, index, index + 1);
            neighbors.add(neighbor);
        }
        return neighbors;
    }

    // Define a helper function to get the index of a value in an array
    private static int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    // Define a helper function to swap two elements in an array
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
