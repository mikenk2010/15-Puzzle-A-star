public class Node {
    int[][] board;
    int blankRow, blankColumn;
    String path;
    int cost;
    int priority;
    int previousDirection;

    Node(int[][] board, int blankRow, int blankColumn, String path, int cost, int previousDirection) {
        this.board = Helper.deepCopy(board);
        this.blankRow = blankRow;
        this.blankColumn = blankColumn;
        this.path = path;
        this.cost = cost;
        priority = this.cost + Helper.manhattan(this.board);
        this.previousDirection = previousDirection;
    }
}