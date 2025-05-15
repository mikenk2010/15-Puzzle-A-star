public class AStar {
    private static final int N = 4;
    private static final int MAX_MOVES = 1_000_000;
    private static final int[] dx = {1, -1, 0, 0};
    private static final int[] dy = {0, 0, 1, -1};
    private static final char[] moveChar = {'U', 'D', 'L', 'R'};

    private int[][] board = new int[N][N];
    private int blankRow = 0;
    private int blankColumn = 0;

    public String solve(int[][] puzzle) {
        board = Helper.deepCopy(puzzle);
        blankRow = 0;
        blankColumn = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 0) {
                    blankRow = i;
                    blankColumn = j;
                }
            }
        }

//        MinHeap open = new MinHeap(10_000_000);
        BucketedPriorityQueue open = new BucketedPriorityQueue();
        HashSet closed = new HashSet(MAX_MOVES);

        Node start = new Node(board, blankRow, blankColumn, "", 0, -1);
        open.insert(start);

        while (!open.isEmpty()) {
            Node current = open.remove();

            if (Helper.isGoalV2(current.board)) {
                board = Helper.testSolution(board, current.path);
                return current.path;
            }

            String hash = hash(current.board);
            if (closed.contains(hash)) continue;
            closed.add(hash);

            for (int direction = 0; direction < 4; direction++) {
                if (current.previousDirection != -1 && (direction ^ 1) == current.previousDirection) continue;

                int newRow = current.blankRow + dx[direction];
                int newCol = current.blankColumn + dy[direction];

                if (newRow < 0 || newRow >= N || newCol < 0 || newCol >= N)
                    continue;

                int newCost = current.cost + 1;
                if (newCost > MAX_MOVES) continue;

                int[][] newGrid = Helper.deepCopy(current.board);
                Helper.swap(newGrid, current.blankRow, current.blankColumn, newRow, newCol);
                String newPath = current.path + moveChar[direction];

                Node neighbor = new Node(newGrid, newRow, newCol, newPath, newCost, direction);
                if (neighbor.priority > MAX_MOVES) continue;
                open.insert(neighbor);
            }
        }

        return "No solution";
    }

    public int[][] getBoard() {
        return board;
    }

    private String hash(int[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sb.append(grid[i][j]).append(',');
            }
        }
        return sb.toString();
    }
}
