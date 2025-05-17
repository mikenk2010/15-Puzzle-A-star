/**
 * Main class to run and evaluate the 15-puzzle solver using IDA* (Iterative Deepening A*)
 */
public class RMIT_15_Puzzle_Solver {
    public static void main(String[] args) {
        // Create an instance of the IDA* solver
        IDAStar solver = new IDAStar();

        // Load an array of solvable test cases (each one is a 4x4 puzzle)
        int[][][] solvableTests = SolvablePuzzle.testCases;

        // Loop through each test case
        for (int i = 0; i < solvableTests.length; i++) {
            System.out.println("---- Test " + (i + 1) + " ----");

            // Print the initial board state
            System.out.println("Initial:");

            Helper.print(solvableTests[i]);

            // Record the start time before solving
            long t0 = System.currentTimeMillis();

            // Solve the puzzle and get the move sequence
            String moves = solver.solve(solvableTests[i]);

            // Record the end time after solving
            long t1 = System.currentTimeMillis();

            // Display the solution: number of moves and time taken in milliseconds
            System.out.println("Solution (" + moves.length() + " moves, " + (t1 - t0) + " ms):");
            System.out.println(moves);

            // Print the final board state after applying the moves
            System.out.println("Final:");
            Helper.print(solver.getBoard());

            // Add an empty line for readability between test cases
            System.out.println();
        }
    }
}