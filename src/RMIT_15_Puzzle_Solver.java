import java.io.IOException;

public class RMIT_15_Puzzle_Solver {
    public static void main(String[] args) throws IOException {
        RMIT_15_Puzzle_Solver rmitSolver = new RMIT_15_Puzzle_Solver();
        IDAStar solver = new IDAStar();
        int[][][] solvableTests = SolvablePuzzle.testCases;

        for (int i = 0; i < solvableTests.length; i++) {
            System.out.println("---- Test " + (i + 1) + " ----");
            System.out.println("Initial:");

            Helper.print(solvableTests[i]);

            long t0 = System.currentTimeMillis();
            String moves = solver.solve(solvableTests[i]);
            long t1 = System.currentTimeMillis();

            System.out.println("Solution (" + moves.length() + " moves, " + (t1 - t0) + " ms):");
            System.out.println(moves);

            System.out.println("Final:");
            Helper.print(solver.getBoard());
            System.out.println();
        }
    }
}