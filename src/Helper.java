public class Helper {

    // O(1)
    public static int getValue(int[] array, int index) {
        if (index < 0 || index >= array.length) {
            return -1;
        }

        return array[index];
    }

    // O(1)
    public static int getValue(int[][] grid, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= grid.length || columnIndex < 0 || columnIndex >= grid[0].length) {
            return -1;
        }
        return grid[rowIndex][columnIndex];
    }

    //O(1)
    public static int to1DIndex(int rowIndex, int columnIndex, int columnCount) {
        return rowIndex * columnCount + columnIndex;
    }

    //O(1)
    public static int[] to2DIndex(int index, int rowCount, int columnCount) {
        return new int[]{index / rowCount, index % columnCount};
    }

    //O(N*N)
    // print 2d array
    public static void print(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            System.out.print("|");
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + "\t");
            }
            System.out.print("|");
            System.out.println();
        }
    }

    //O(N)
    // print 1d array
    public static void print(int[] array) {
        System.out.print("|");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.print("|");
        System.out.println();
    }

    //O(N)
    public static void printWithIndex(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println("[" + i + "]: " + array[i] + " ");
        }
    }

    // O(N*N)
    public static int[][] to2DArray(int[] grid1D) {
        if (grid1D == null || grid1D.length == 0) {
            return new int[0][0];
        }

        int size = (int) Math.sqrt(grid1D.length);
        int[][] grid2D = new int[size][size];
        int index = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid2D[i][j] = grid1D[index];
                index++;
            }
        }

        return grid2D;
    }

    public static boolean isGoal(int[][] board) {
        int N = board.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // i * N + j is the position,
                // i * N + j  + 1 is the expected number in that position,
                int expected = i * N + j + 1;
                if (expected == N * N) {
                    expected = 0;
                }

                if (board[i][j] != expected) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isValid(int[][] puzzle) {
        int N = puzzle.length;
        int gridSize = N * N;
        boolean[] seen = new boolean[gridSize]; // Track occurrences of numbers

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int number = puzzle[i][j];

                // Invalid number check
                if (number < 0 || number > gridSize) {
                    return false;
                }

                // Duplicate detected
                if (seen[number]) {
                    return false;
                }

                // Mark number as seen
                seen[number] = true;
            }
        }

        return true; // No duplicates found
    }

    public static boolean isSolvable(int[][] puzzle) {
        int N = puzzle.length;
        int[] flattenedPuzzle = new int[N * N];
        int blankRow = 0;
        int parity = 0;
        int row = 0;

        // Convert 2D puzzle to 1D array
        int index = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                flattenedPuzzle[index++] = puzzle[i][j];
                if (puzzle[i][j] == 0) {
                    blankRow = i + 1; // Track blank tile row (counting from top)
                }
            }
        }


        // Count inversions & track blank row (counting from bottom)
        for (int i = 0; i < flattenedPuzzle.length; i++) {
            if (i % N == 0) { // Move to next row
                row++;
            }
            if (flattenedPuzzle[i] == 0) { // Found blank tile
                blankRow = N - row; // Convert top-down index to bottom-up index
                continue;
            }
            for (int j = i + 1; j < flattenedPuzzle.length; j++) {
                if (flattenedPuzzle[j] != 0 && flattenedPuzzle[i] > flattenedPuzzle[j]) {
                    parity++; // Count inversions
                }
            }
        }

        // Solvability conditions
        boolean isEvenGrid = (N % 2 == 0);
        boolean parityCheck = (parity % 2 == 0);

        if (isEvenGrid) { // Even grid case
            if (blankRow % 2 == 0) { // Blank is in odd row (from bottom)
                return parityCheck;
            } else { // Blank is in even row (from bottom)
                return !parityCheck;
            }
        } else { // Odd grid case
            return parityCheck;
        }
    }
}
