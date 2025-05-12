public final class UnsolvablePuzzle {
    // Swap 14 & 15 → inversion parity flipped
    public static int[][] puzzle1 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 15, 14, 0}
    };

    // Swap 11 & 12 → inversion parity flipped
    public static int[][] puzzle2 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 12, 11},
            {13, 14, 15, 0}
    };

    // Swap 7 & 8 → inversion parity flipped
    public static int[][] puzzle3 = {
            {1, 2, 3, 4},
            {5, 6, 8, 7},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };

    // Blank space in the wrong row for solvability
    public static int[][] puzzle4 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 0, 15}
    };

    // Duplicate puzzle (contains two '14' values)
    public static int[][] puzzleDuplicate1 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 14, 0} // Invalid due to duplicate '14'
    };

    public static int[][][] testCases = {
            puzzle1, puzzle2, puzzle3, puzzle4
    };
}
