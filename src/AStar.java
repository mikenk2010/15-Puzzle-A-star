import java.util.*;

public class AStar {
    private static final int N = 4;
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private static final char[] MOVES = {'U', 'D', 'L', 'R'};

    public String solve(int[][] puzzle) {
        if (!Helper.isValid(puzzle)) {
            return Helper.INVALID_PUZZLE_MESSAGE;
        }

        if (!Helper.isSolvable(puzzle)) {
            return Helper.UNSOLVABLE_MESSAGE;
        }

        int[] blankPosition = Helper.getBlankPosition(puzzle);
        int startX = blankPosition[0];
        int startY = blankPosition[1];

        State start = new State(Helper.deepCopy(puzzle), "", 0, startX, startY);
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(s -> s.cost + Helper.manhattan(s.board)));
        Set<String> visited = new HashSet<>();

        queue.add(start);

        while (!queue.isEmpty()) {
            State current = queue.poll();
            String key = current.serialize();

            if (visited.contains(key)) continue;

            visited.add(key);

            if (Helper.isGoalV2(current.board)) {
                System.out.println("Solution found in " + current.path.length() + " moves:");
                State replay = new State(Helper.deepCopy(puzzle), "", 0, startX, startY);
                Helper.print(replay.board);
                for (char move : current.path.toCharArray()) {
                    replay = move(replay, move);
                    System.out.println("Move: " + move);
                    Helper.print(replay.board);
                }

                return current.path;
            }

            for (int direction = 0; direction < 4; direction++) {
                int newRow = current.blankRow + DIRECTIONS[direction][0];
                int newColumn = current.blankColumn + DIRECTIONS[direction][1];

                if (newRow < 0 || newRow >= N || newColumn < 0 || newColumn >= N) continue;

                int[][] newBoard = Helper.deepCopy(current.board);
                newBoard[current.blankRow][current.blankColumn] = newBoard[newRow][newColumn];
                newBoard[newRow][newColumn] = 0;

                State next = new State(
                        newBoard,
                        current.path + MOVES[direction], // Append move direction
                        current.cost + 1, // Increment move cost
                        newRow, newColumn // Update blank position
                );

                if (!visited.contains(next.serialize())) {
                    queue.add(next);
                }
            }
        }

        return "No solution found";
    }

    private State move(State state, char moveChar) {
        int dx = 0, dy = 0;

        if (moveChar == 'U') dx = 1;  // Tile goes up → blank moves down
        else if (moveChar == 'D') dx = -1; // Tile goes down → blank moves up
        else if (moveChar == 'L') dy = 1; // Tile goes left → blank moves right
        else if (moveChar == 'R') dy = -1; // Tile goes right → blank moves left

        // Calculate new coordinates for the blank tile after movement
        int newRow = state.blankRow + dx;
        int newColumn = state.blankColumn + dy;

        // Create a deep copy of the current board to avoid modifying the original
        int[][] newBoard = Helper.deepCopy(state.board);

        // Swap the tile at the destination with the blank
        newBoard[state.blankRow][state.blankColumn] = newBoard[newRow][newColumn]; // Move tile into blank position
        newBoard[newRow][newColumn] = 0; // Set new blank position

        return new State(newBoard, state.path + moveChar, state.cost + 1, newRow, newColumn);
    }
}