/**
 * IDA* (Iterative Deepening A*) solver implementation for the 15-puzzle
 */
public class IDAStar {
    private final int N = 4; // Board size: 4x4 for the 15-puzzle
    private final int MAX_MOVES = 1_000_000; // Upper limit to avoid infinite paths

    // Directions for blank (0) movement: down, up, right, left
    private final int[] dx = {1, -1, 0, 0};  // Vertical movement
    private final int[] dy = {0, 0, 1, -1};  // Horizontal movement
    private final char[] moveChar = {'U', 'D', 'L', 'R'};  // Corresponding move chars (tile moves)

    private int[][] board = new int[N][N]; // Current puzzle board
    private int threshold;                 // Current threshold for IDA*
    private String solution;               // Stores the final solution path
    private StringBuilder path;            // Current path being explored
    private int blankRow = 0;              // Blank tile row
    private int blankColumn = 0;           // Blank tile column

    // Public method to solve a given 15-puzzle using IDA*
    public String solve(int[][] puzzle) {
        // Validate input
        if (!Helper.isValid(puzzle)) {
            return Helper.INVALID_PUZZLE_MESSAGE;
        }

        // Check for solvable
        if (!Helper.isSolvable(puzzle)) {
            return Helper.UNSOLVABLE_MESSAGE;
        }

        // Deep copy input puzzle to avoid modifying the original
        board = Helper.deepCopy(puzzle);

        // Initialize heuristic threshold using Manhattan distance
        threshold = Helper.manhattan(board);

        path = new StringBuilder();  // Reset path
        solution = null;             // Reset solution

        // Get initial blank position
        int[] blankPosition = Helper.getBlankPosition(board);
        blankRow = blankPosition[0];
        blankColumn = blankPosition[1];

        // Iteratively deepen the search using DFS until a solution is found or deemed unsolvable
        while (true) {
            int t = dfs(blankRow, blankColumn, 0, -1); // Start DFS with cost 0 and no previous direction

            // Solution found
            if (solution != null) {
                if (solution.length() > MAX_MOVES) {
                    throw new RuntimeException("Solution exceeds maximum allowed moves");
                }
                return solution;
            }

            // No more paths to explore
            if (t == Integer.MAX_VALUE) {
                throw new RuntimeException("Unsolvable puzzle");
            }

            // Increase threshold and continue
            threshold = t;
        }
    }

    // Recursive depth-first search with cost-limited pruning based on threshold
    private int dfs(int blankRow, int blankColumn, int cost, int previousDirection) {
        // Calculate f(n) = g(n) + h(n)
        int f = cost + Helper.manhattan(board);

        // Prune paths exceeding the current threshold
        if (f > threshold) {
            return f;
        }

        // Goal state check
        if (Helper.isGoalV2(board)) {
            solution = path.toString();  // Record solution
            return -1;                   // Signal found
        }

        // Tracks the minimum f value beyond current threshold
        int min = Integer.MAX_VALUE;

        // Explore all four directions
        for (int direction = 0; direction < N; direction++) {
            // Skip reversing the previous move (e.g., U after D)
            if (previousDirection != -1 && (direction ^ 1) == previousDirection) continue;

            // Calculate new blank position
            int newRow = blankRow + dx[direction];
            int newColumn = blankColumn + dy[direction];

            // Ignore out-of-bound moves
            if (newRow < 0 || newRow >= N || newColumn < 0 || newColumn >= N) continue;

            // Swap blank and tile to apply the move
            Helper.swap(board, blankRow, blankColumn, newRow, newColumn);
            this.blankRow = newRow;
            this.blankColumn = newColumn;

            // Record move character (e.g., 'L', 'R')
            path.append(moveChar[direction]);

            // Recurse deeper
            int t = dfs(newRow, newColumn, cost + 1, direction);

            // If solution found, propagate signal
            if (t == -1) return -1;

            // Track the lowest threshold overrun
            if (t < min) min = t;

            // Undo move (backtrack)
            path.setLength(path.length() - 1);
            Helper.swap(board, newRow, newColumn, blankRow, blankColumn);
            this.blankRow = blankRow;
            this.blankColumn = blankColumn;
        }

        // Return the minimum cost overrun
        return min;
    }

    // Getter for the final board after solving
    public int[][] getBoard() {
        return board;
    }
}