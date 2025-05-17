public class Node {
    // The current state of the puzzle board
    int[][] board;

    // The current position of the blank tile (0)
    int blankRow, blankColumn;

    // The path taken to reach this state (as a string of moves like "UDLR")
    String path;

    // The cost (number of moves) from the start state to this node
    int cost;

    // The estimated total cost (f = g + h) used in informed search algorithms like A*
    int priority;

    // The direction taken to reach this node from its parent (used to avoid reversing moves)
    int previousDirection;

    // Constructor to create a new node with current board, position of blank, path so far, cost, and previous move
    Node(int[][] board, int blankRow, int blankColumn, String path, int cost, int previousDirection) {
        // Deep copy to prevent reference issues when board is mutated
        this.board = Helper.deepCopy(board);

        this.blankRow = blankRow;
        this.blankColumn = blankColumn;
        this.path = path;
        this.cost = cost;

        // Priority is computed as cost so far + Manhattan heuristic
        priority = this.cost + Helper.manhattan(this.board);

        this.previousDirection = previousDirection;
    }
}
