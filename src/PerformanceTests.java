public class PerformanceTests {
    public static void main(String[] args) {
        int[][][] solvablePuzzles = {
                SolvablePuzzle.puzzle1,
                SolvablePuzzle.puzzle2,
                SolvablePuzzle.puzzle3,
                SolvablePuzzle.puzzle4,
                SolvablePuzzle.puzzle5
        };

        System.out.println(String.format("%-10s %-10s %-10s %-10s %-10s", "Puzzle", "isGoal(ns)", "result", "isGoalV2(ns)", "result"));

        for (int i = 0; i < solvablePuzzles.length; i++) {
            long start = System.nanoTime();
            boolean result1 = Helper.isGoal(solvablePuzzles[i]);
            long end = System.nanoTime();
            long time1 = end - start;

            start = System.nanoTime();
            boolean result2 = Helper.isGoalV2(solvablePuzzles[i]);
            end = System.nanoTime();
            long time2 = end - start;

            System.out.println(String.format("%-10d %-10s %-10s %-10s %-10s", i + 1, time1, result1, time2, result2 ));
        }
    }
}