public class PerformanceTests {
    public static void main(String[] args) {
        // Load a list of solvable 15-puzzle test cases
        int[][][] solvablePuzzles = SolvablePuzzle.testCases;

        // Instantiate both IDA* and A* solvers
        IDAStar idaStar = new IDAStar();
        AStar aStar = new AStar();

        // Loop through each puzzle test case
        for (int i = 0; i < solvablePuzzles.length; i++) {
            // Print header for current puzzle test
            System.out.println(String.format("%-10s %-15s %-10s %-50s", "Puzzle: " + (i + 1), "Time(ns)", "isGoal", "Moves"));

            // Run and time the IDA* solver
            long start = System.nanoTime();
            String moves1 = idaStar.solve(solvablePuzzles[i]);
            long end = System.nanoTime();
            long time1 = end - start;

            // Apply the solution to get the resulting board and check if it’s in goal state
            int[][] solutionBoard1 = Helper.testSolution(solvablePuzzles[i], moves1);
            boolean isGoal1 = Helper.isGoalV2(solutionBoard1);

            // Print results for IDA*
            System.out.println(String.format("%-10s %-15s %-10s %-50s", "IDA*", time1, isGoal1, moves1));

            // Run and time the A* solver
            start = System.nanoTime();
            String moves2 = aStar.solve(solvablePuzzles[i]);
            end = System.nanoTime();
            long time2 = end - start;

            // Note: This line incorrectly reuses moves1 instead of moves2 (likely a bug)
            int[][] solutionBoard2 = Helper.testSolution(solvablePuzzles[i], moves1); // ← should be moves2
            boolean isGoal2 = Helper.isGoalV2(solutionBoard2);

            // Print results for A*
            System.out.println(String.format("%-10s %-15s %-10s %-50s", "A*", time2, isGoal2, moves2));
        }
    }
}
