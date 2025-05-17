public class AStar {
    // Size of the board (4x4 for 15-puzzle)
    private final int N = 4;

    // Limit on the maximum number of moves to avoid infinite loops
    private final int MAX_MOVES = 1_000_000;

    // Direction vectors for up, down, left, right (blank tile moves)
    private final int[] dx = {1, -1, 0, 0};
    private final int[] dy = {0, 0, 1, -1};

    // Corresponding move characters (used for solution path tracking)
    private final char[] moveChar = {'U', 'D', 'L', 'R'};

    // Puzzle board state
    private int[][] board = new int[N][N];

    // Position of the blank tile
    private int blankRow = 0;
    private int blankColumn = 0;

    // Main method to solve the puzzle using A* search algorithm
    public String solve(int[][] puzzle) {
        // Create a deep copy of the puzzle to avoid modifying input
        board = Helper.deepCopy(puzzle);

        // Get the initial blank tile position
        int[] blankPosition = Helper.getBlankPosition(board);
        blankRow = blankPosition[0];
        blankColumn = blankPosition[1];

        // Priority queue for open nodes (min-heap based on f-cost)
        MinHeap open = new MinHeap(10);

        // Hash set to track visited states (closed set)
        HashSet closed = new HashSet(10);

        // Initialize the start node and insert into open list
        Node start = new Node(board, blankRow, blankColumn, "", 0, -1);
        open.insert(start);

        try {
            // A* search loop
            while (!open.isEmpty()) {
                // Get the node with lowest priority (f-cost)
                Node current = open.remove();

                // If current state is goal, return the path taken
                if (Helper.isGoalV2(current.board)) {
                    board = Helper.testSolution(board, current.path); // Final board state
                    return current.path; // Return move sequence
                }

                // Generate hash and skip if already visited
                var hash = Helper.hash(current.board);
                if (closed.contains(hash)) continue;
                closed.add(hash);

                // Explore all 4 directions (U, D, L, R)
                for (int direction = 0; direction < 4; direction++) {
                    // Skip reverse direction (to avoid going back to parent state)
                    if (current.previousDirection != -1 && (direction ^ 1) == current.previousDirection) continue;

                    int newRow = current.blankRow + dx[direction];
                    int newCol = current.blankColumn + dy[direction];

                    // Skip out-of-bounds moves
                    if (newRow < 0 || newRow >= N || newCol < 0 || newCol >= N) continue;

                    // Increment path cost
                    int newCost = current.cost + 1;
                    if (newCost > MAX_MOVES) continue;

                    // Create a new state by swapping blank with target tile
                    int[][] newGrid = Helper.deepCopy(current.board);
                    Helper.swap(newGrid, current.blankRow, current.blankColumn, newRow, newCol);

                    // Append move character to path
                    String newPath = current.path + moveChar[direction];

                    // Create new node and calculate its priority
                    Node neighbor = new Node(newGrid, newRow, newCol, newPath, newCost, direction);
                    if (neighbor.priority > MAX_MOVES) continue;

                    // Insert into open list
                    open.insert(neighbor);
                }
            }
        } catch (Exception e) {
            // Print any unexpected errors during search
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }

        // If no solution is found after exhausting all options
        return "No solution";
    }

    // Return the final board state after solving
    public int[][] getBoard() {
        return board;
    }
}