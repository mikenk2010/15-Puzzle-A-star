import java.util.*;

/**
 * RMIT 15 Puzzle Solver using A* Search Algorithm
 * - Uses Manhattan Distance as the heuristic
 * - Returns optimal solution path if solvable
 * - Prints move steps and board after each move
 */
public class RMIT_15_Puzzle_Solver_AStar {

    /**
     * Size of the puzzle grid (4x4 for 15 Puzzle).
     * Each tile is uniquely numbered 1 to 15, with a single empty space (0).
     */
    private static final int N = 4;

    /**
     * Goal state of the 15 Puzzle.
     * When tiles match this layout, the puzzle is considered solved.
     */
    private static final int[][] GOAL = {
        {1, 2, 3, 4},
        {5, 6, 7, 8},
        {9, 10, 11, 12},
        {13, 14, 15, 0}
    };

    /**
     * Represents a single puzzle state:
     * - board: current puzzle layout
     * - path: accumulated move sequence (e.g., "LLUR")
     * - cost: number of moves taken so far (g in A*)
     * - blankX/Y: position of the blank tile (0)
     */
    static class State {
        int[][] board;
        String path;
        int cost;
        int blankX, blankY;

        State(int[][] board, String path, int cost, int blankX, int blankY) {
            this.board = board;
            this.path = path;
            this.cost = cost;
            this.blankX = blankX;
            this.blankY = blankY;
        }

        /**
         * Convert board to a string for hashing/visited set
         */
        String serialize() {
            StringBuilder sb = new StringBuilder();
            for (int[] row : board)
                for (int val : row)
                    sb.append(val).append(',');
            return sb.toString();
        }

        /**
         * Check if this board matches the GOAL state
         */
        boolean isGoal() {
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    if (board[i][j] != GOAL[i][j])
                        return false;
            return true;
        }

        /**
         * Nicely prints the current board state
         */
        void printBoard() {
            for (int[] row : board) {
                for (int val : row) {
                    System.out.print((val == 0 ? "  " : String.format("%2d", val)) + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /**
     * Directions the blank tile (0) can move:
     * - Down, Up, Right, Left
     * Represented as (dx, dy) pairs.
     */
    private static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    /**
     * Corresponding characters for each move direction:
     * - U: Tile moves Up (blank moves Down)
     * - D: Tile moves Down (blank moves Up)
     * - L: Tile moves Left (blank moves Right)
     * - R: Tile moves Right (blank moves Left)
     */
    private static final char[] MOVES = {'U', 'D', 'L', 'R'};

    /**
     * Solve the 15 puzzle using A* search from the initial puzzle input
     * @param puzzle - 4x4 int array representing puzzle state
     * @return String of move directions (e.g., "LRUL") or 'No solution found'
     */
    public String solve(int[][] puzzle) {
        // 1. Locate the blank tile (value 0) in the puzzle
        int startX = -1, startY = -1;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (puzzle[i][j] == 0) {
                    startX = i;
                    startY = j;
                }

        // 2. Create the initial state with:
        // - a deep copy of the puzzle
        // - an empty move path
        // - cost = 0 (number of moves so far)
        // - blank tile coordinates
        State start = new State(deepCopy(puzzle), "", 0, startX, startY);

        // 3. Create a priority queue (min-heap) sorted by f(n) = g(n) + h(n)
        // where g(n) is the cost so far and h(n) is the heuristic (Manhattan Distance)
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(s -> s.cost + heuristic(s.board)));

        // 4. Use a set to keep track of visited states (by string serialization)
        Set<String> visited = new HashSet<>();

        // 5. Add the starting state to the queue
        queue.add(start);

        // 6. A* Search Loop
        while (!queue.isEmpty()) {
            // 6.1 Retrieve the lowest-cost state
            State current = queue.poll();
            String key = current.serialize();

            // 6.2 Skip already visited states to avoid loops
            if (visited.contains(key)) continue;

            visited.add(key);

            // 6.3 If the goal state is reached, print the solution steps and return path
            if (current.isGoal()) {
                System.out.println("Solution found in " + current.path.length() + " moves:");
                State replay = new State(deepCopy(puzzle), "", 0, startX, startY);
                replay.printBoard();
                for (char move : current.path.toCharArray()) {
                    replay = move(replay, move);
                    System.out.println("Move: " + move);
                    replay.printBoard();
                }

                // Return move sequence string
                return current.path;
            }

            // 6.4 Try all 4 possible directions (Up, Down, Left, Right)
            for (int d = 0; d < 4; d++) {
                int nx = current.blankX + DIRS[d][0];
                int ny = current.blankY + DIRS[d][1];

                // Skip invalid moves (outside of puzzle boundaries)
                if (nx < 0 || nx >= N || ny < 0 || ny >= N) continue;

                // 6.5 Create a new board by moving the blank tile to the new position
                int[][] newBoard = deepCopy(current.board);

                // Move tile into blank
                newBoard[current.blankX][current.blankY] = newBoard[nx][ny];

                // Blank moves to the new position
                newBoard[nx][ny] = 0;

                // 6.6 Construct the new state
                State next = new State(
                        newBoard,
                        current.path + MOVES[d], // Append move direction
                        current.cost + 1, // Increment move cost
                        nx, ny // Update blank position
                );

                // 6.7 Add the new state to the queue if it's not visited
                if (!visited.contains(next.serialize())) {
                    queue.add(next);
                }
            }
        }

        // 7. If queue is empty and goal not found, return failure
        return "No solution found";
    }

    /**
     * Simulates a single move of the blank tile in the specified direction.
     * This method creates a new puzzle state based on the current one, applies the move,
     * and returns the new state without modifying the original board.
     *
     * @param state The current puzzle state (board, move path, blank position).
     * @param dir   The move direction character:
     *              - 'U' means tile moves up (blank goes down)
     *              - 'D' means tile moves down (blank goes up)
     *              - 'L' means tile moves left (blank goes right)
     *              - 'R' means tile moves right (blank goes left)
     * @return A new State object representing the result of applying the move.
     */
    private State move(State state, char dir) {
        int dx = 0, dy = 0;

        // Determine direction vector (blank movement) based on move character
        if (dir == 'U') dx = 1;  // Tile goes up → blank moves down
        else if (dir == 'D') dx = -1; // Tile goes down → blank moves up
        else if (dir == 'L') dy = 1; // Tile goes left → blank moves right
        else if (dir == 'R') dy = -1; // Tile goes right → blank moves left

        // Calculate new coordinates for the blank tile after movement
        int nx = state.blankX + dx;
        int ny = state.blankY + dy;

        // Create a deep copy of the current board to avoid modifying the original
        int[][] newBoard = deepCopy(state.board);

        // Swap the tile at the destination with the blank
        newBoard[state.blankX][state.blankY] = newBoard[nx][ny]; // Move tile into blank position
        newBoard[nx][ny] = 0; // Set new blank position

        // Return the new state:
        // - Updated board
        // - Appended move path
        // - Incremented cost
        // - Updated blank position
        return new State(newBoard, state.path + dir, state.cost + 1, nx, ny);
    }


    /**
     * Creates a deep copy of the given puzzle board.
     * This is essential to preserve state immutability when generating new nodes.
     *
     * @param board The current 4x4 puzzle board
     * @return A new 4x4 matrix with identical values to the original
     */
    private int[][] deepCopy(int[][] board) {
        int[][] copy = new int[N][N];
        for (int i = 0; i < N; i++){
            // Efficient row-wise copy
            System.arraycopy(board[i], 0, copy[i], 0, N);
        }
        return copy;
    }

    /**
     * Heuristic function for A* search: calculates the total Manhattan Distance
     * for all numbered tiles from their current positions to their goal positions.
     *
     * Manhattan Distance (also known as L1 norm or taxicab distance) is the sum of the
     * horizontal and vertical distances between a tile's current position and its target
     * position in the goal state.
     *
     * This heuristic is:
     * - Admissible: it never overestimates the true cost to reach the goal.
     * - Consistent: each move changes the total distance by at most 1.
     *
     * @param board The current 4x4 puzzle board.
     * @return An integer representing the total estimated cost (heuristic h(n)) from the current state to the goal.
     */
    private int heuristic(int[][] board) {
        // Total Manhattan distance
        int dist = 0;

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                int val = board[i][j];

                // Skip the blank tile (0) as it does not need to be positioned
                if (val != 0) {
                    // Calculate the target position for the current tile value
                    int targetX = (val - 1) / N; // Row in the goal state
                    int targetY = (val - 1) % N; // Column in the goal state

                    // Add the Manhattan distance between current and target position
                    dist += Math.abs(i - targetX) + Math.abs(j - targetY);
                }
            }

        // Return the total heuristic cost for this board
        return dist;
    }

    /**
     * Main test runner for multiple test cases
     */
    public static void main(String[] args) {
        int[][][] testCases = {
                {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 0, 14, 15}}, // LL
                {{14, 5, 7, 9}, {2, 13, 11, 0}, {6, 1, 12, 3}, {15, 10, 8, 4}}, // DRRRULDLULUURDDRURDLDRULULDDLUUURRRDLLURDLDDLUUU
//                {{8, 10, 0, 5}, {14, 9, 1, 4}, {6, 12, 13, 15}, {7, 2, 3, 11}},
//                {{15, 4, 1, 12}, {6, 7, 14, 0}, {8, 13, 2, 10}, {3, 11, 5, 9}},
//                {{2, 9, 15, 0}, {6, 11, 5, 14}, {13, 4, 1, 3}, {8, 10, 7, 12}},
//                {{4, 3, 13, 8}, {2, 11, 0, 12}, {10, 1, 7, 14}, {15, 6, 9, 5}},
//                {{9, 7, 6, 0}, {15, 4, 5, 12}, {8, 3, 10, 2}, {14, 1, 11, 13}},
//                {{13, 14, 0, 3}, {6, 15, 7, 2}, {1, 4, 5, 11}, {10, 8, 12, 9}},
//                {{4, 2, 5, 10}, {15, 3, 9, 8}, {6, 11, 13, 14}, {12, 0, 7, 1}},
//                {{4, 13, 9, 6}, {8, 14, 10, 0}, {11, 15, 7, 5}, {12, 1, 2, 3}},
//                {{4, 3, 0, 8}, {7, 2, 11, 10}, {12, 15, 5, 13}, {1, 9, 6, 14}}
        };

        for (int i = 0; i < testCases.length; i++) {
            System.out.println("Test Case #" + (i + 1));
            RMIT_15_Puzzle_Solver_AStar solver = new RMIT_15_Puzzle_Solver_AStar();
            long start = System.currentTimeMillis();
            String solution = solver.solve(testCases[i]);
            long end = System.currentTimeMillis();
            System.out.println("Moves: " + solution);
            System.out.println("Time: " + (end - start) + "ms");
            System.out.println("=====================================\n\n");
        }
    }
}
