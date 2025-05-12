/**
 * RMIT 15 Puzzle Solver using A* Search Algorithm (array-only version)
 * This implementation avoids Java collections (no PriorityQueue, HashSet, etc.)
 * It uses fixed-size arrays for managing open/visited states.
 * Heuristic: Manhattan Distance
 * Search Strategy: A* with manual best-node selection
 * Goal: Solve any solvable 15-puzzle optimally and print each move.
 */
public class RMIT_15_Puzzle_Solver_AStar {

    private static final int N = 4;
    private static final int MAX_STATES = 100000;

    // Goal state for reference
    private static final int[][] GOAL = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };

    /**
     * Represents a single board state in the search tree.
     * Contains the current board, the move path taken to reach it,
     * cost so far (g), and blank tile coordinates.
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

        /** Convert board to a unique string for comparison (used in visited list) */
        String serialize() {
            StringBuilder sb = new StringBuilder();
            for (int[] row : board)
                for (int val : row)
                    sb.append(val).append(',');
            return sb.toString();
        }

        /** Check if the board matches the goal state */
        boolean isGoal() {
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    if (board[i][j] != GOAL[i][j])
                        return false;
            return true;
        }

        /** Prints the current board state */
        void printBoard() {
            for (int[] row : board) {
                for (int val : row)
                    System.out.print((val == 0 ? "  " : String.format("%2d", val)) + " ");
                System.out.println();
            }
            System.out.println();
        }
    }

    private static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private static final char[] MOVES = {'U', 'D', 'L', 'R'};

    /**
     * A* solver method: explores states from initial to goal using array-based structures.
     * Avoids Java Collections. Finds the optimal path based on Manhattan Distance.
     * @param puzzle the 4x4 board to solve (0 = blank)
     * @return the sequence of moves (e.g. "ULDR") or failure message
     */
    public String solve(int[][] puzzle) {
        // Locate the blank (0)
        int startX = -1, startY = -1;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (puzzle[i][j] == 0) {
                    startX = i;
                    startY = j;
                }

        State start = new State(deepCopy(puzzle), "", 0, startX, startY);

        // Manual queues using arrays
        State[] openList = new State[MAX_STATES];
        String[] visited = new String[MAX_STATES];
        int openSize = 0;
        int visitedSize = 0;

        openList[openSize++] = start;

        while (openSize > 0) {
            // Find the state in openList with the lowest f = g + h
            int bestIdx = -1;
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < openSize; i++) {
                State s = openList[i];
                int score = s.cost + heuristic(s.board);
                if (score < bestScore) {
                    bestScore = score;
                    bestIdx = i;
                }
            }

            // Pop best state
            State current = openList[bestIdx];
            for (int i = bestIdx; i < openSize - 1; i++) {
                openList[i] = openList[i + 1];
            }
            openSize--;

            // Check visited
            String key = current.serialize();
            boolean alreadyVisited = false;
            for (int i = 0; i < visitedSize; i++) {
                if (visited[i].equals(key)) {
                    alreadyVisited = true;
                    break;
                }
            }
            if (alreadyVisited) continue;
            visited[visitedSize++] = key;

            if (current.isGoal()) {
                System.out.println("Solution found in " + current.path.length() + " moves:");
                State replay = new State(deepCopy(puzzle), "", 0, startX, startY);
                replay.printBoard();
                for (char move : current.path.toCharArray()) {
                    replay = move(replay, move);
                    System.out.println("Move: " + move);
                    replay.printBoard();
                }
                return current.path;
            }

            // Explore neighbors
            for (int d = 0; d < 4; d++) {
                int nx = current.blankX + DIRS[d][0];
                int ny = current.blankY + DIRS[d][1];
                if (nx < 0 || nx >= N || ny < 0 || ny >= N) continue;

                // Generate a new state by moving the blank
                int[][] newBoard = deepCopy(current.board);
                newBoard[current.blankX][current.blankY] = newBoard[nx][ny];
                newBoard[nx][ny] = 0;
                State next = new State(newBoard, current.path + MOVES[d], current.cost + 1, nx, ny);

                String nextKey = next.serialize();
                boolean alreadyQueued = false;
                for (int i = 0; i < visitedSize; i++) {
                    if (visited[i].equals(nextKey)) {
                        alreadyQueued = true;
                        break;
                    }
                }
                if (!alreadyQueued && openSize < MAX_STATES) {
                    openList[openSize++] = next;
                }
            }
        }

        return "No solution found";
    }

    /** Simulates a move and returns the new state after moving the blank in a given direction */
    private State move(State state, char dir) {
        int dx = 0, dy = 0;
        if (dir == 'U') dx = 1;
        else if (dir == 'D') dx = -1;
        else if (dir == 'L') dy = 1;
        else if (dir == 'R') dy = -1;

        int nx = state.blankX + dx;
        int ny = state.blankY + dy;

        int[][] newBoard = deepCopy(state.board);
        newBoard[state.blankX][state.blankY] = newBoard[nx][ny];
        newBoard[nx][ny] = 0;

        return new State(newBoard, state.path + dir, state.cost + 1, nx, ny);
    }

    /** Deep copies a 4x4 board to preserve immutability */
    private int[][] deepCopy(int[][] board) {
        int[][] copy = new int[N][N];
        for (int i = 0; i < N; i++)
            System.arraycopy(board[i], 0, copy[i], 0, N);
        return copy;
    }

    /** Manhattan Distance heuristic: estimates cost from current to goal state */
    private int heuristic(int[][] board) {
        int dist = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                int val = board[i][j];
                if (val != 0) {
                    int targetX = (val - 1) / N;
                    int targetY = (val - 1) % N;
                    dist += Math.abs(i - targetX) + Math.abs(j - targetY);
                }
            }
        return dist;
    }

    /** Test harness: runs sample test cases and measures performance. */
    public static void main(String[] args) {
        int[][][] testCases = {
                {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 0, 14, 15}}, // LL
//                {{14, 5, 7, 9}, {2, 13, 11, 0}, {6, 1, 12, 3}, {15, 10, 8, 4}}, // DRRRULDLULUURDDRURDLDRULULDDLUUURRRDLLURDLDDLUUU
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
