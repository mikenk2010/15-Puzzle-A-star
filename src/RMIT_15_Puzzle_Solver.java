
public class RMIT_15_Puzzle_Solver {
    private static final int N = 4;

    public boolean test(int[][] puzzle, String solution) {
        char[] moves = solution.toCharArray();

        // Deep copy to prevent modifying original puzzle
        int[][] testBoard = new int[N][N];
        for (int r = 0; r < N; r++) {
            System.arraycopy(puzzle[r], 0, testBoard[r], 0, N);
        }

        int[] position = getBlankPosition(testBoard);
        int currentBlankRow = position[0];
        int currentBlankColumn = position[1];
        Helper.print(testBoard);
        System.out.println("-----------------");
        for (int i = 0; i < moves.length; i++) {
            char move = moves[i];

            int newRow = currentBlankRow, newColumn = currentBlankColumn;

            switch (move) {
                case 'U':
                    newRow++;
                    break;
                case 'D':
                    newRow--;
                    break;
                case 'L':
                    newColumn++;
                    break;
                case 'R':
                    newColumn--;
                    break;
                default:
                    throw new RuntimeException("Invalid move");
            }

            swap(testBoard, currentBlankRow, currentBlankColumn, newRow, newColumn);
            currentBlankRow = newRow;
            currentBlankColumn = newColumn;
            System.out.println("Move [" + (i + 1) + "]: " + move);
            Helper.print(testBoard);
            System.out.println("-----------------");
        }

        return Helper.isGoal(testBoard);
    }


    private void swap(int[][] board, int sourceRow, int sourceColumn, int destinationRow, int destinationColumn) {
        int temp = board[sourceRow][sourceColumn];
        board[sourceRow][sourceColumn] = board[destinationRow][destinationColumn];
        board[destinationRow][destinationColumn] = temp;
    }

    public int[] getBlankPosition(int[][] grid) {
        int row = -1;
        int column = -1;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 0) {
                    row = i;
                    column = j;
                    return new int[]{row, column};
                }
            }
        }

        return new int[]{row, column};
    }

    // Optional main for quick testing
    public static void main(String[] args) {
        RMIT_15_Puzzle_Solver rmitSolver = new RMIT_15_Puzzle_Solver();
        ida_star solver = new ida_star();
        int[][][] solvableTests = SolveablePuzzle.testCases;
        int[][][] unsolvableTests = UnsolvablePuzzle.testCases;

        for (int i = 0; i < solvableTests.length; i++) {
            System.out.println("---- Test " + (i + 1) + " ----");
            System.out.println("Initial:");
            System.out.println("Is valid: " + solver.isValid(solvableTests[i]));
            System.out.println("Is solvable: " + solver.isSolvable(solvableTests[i]));

            solver.printBoard(solvableTests[i]);

            long t0 = System.currentTimeMillis();
            String moves = solver.solve(solvableTests[i]);
            long t1 = System.currentTimeMillis();

            System.out.println("Solution (" + moves.length() + " moves, " + (t1 - t0) + " ms):");

            System.out.println(moves);

            System.out.println("Final:");
            solver.printBoard();
            System.out.println();

            System.out.println("Testing solution: " + rmitSolver.test(solvableTests[i], moves) + "\n");
        }
    }
}