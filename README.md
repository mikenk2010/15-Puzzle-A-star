RMIT 15 Puzzle Solver - README
==============================

Description:
-------------------------------------
This project implements a 15-puzzle solver using the Iterative Deepening A* (IDA*) algorithm with the Manhattan Distance heuristic. It validates solvability, computes optimal move sequences, and supports step-by-step output for multiple test cases.

![image](https://github.com/user-attachments/assets/6c29879c-3d75-43b9-ad62-c4d482ed602e)


Team Members and Contribution Scores:
-------------------------------------
- Chau Le Hoang (s3715228@rmit.edu.vn)           - 20% - contribute on...
- Nguyen Khac Bao (s4139514@rmit.edu.vn)         - 20% - contribute on...
- Nghia Nguyen Quoc Trong (s3343711@rmit.edu.vn) - 20% - contribute on...
- Trieu Trinh Tac (s3671760@student.rmit.edu.au) - 20% - contribute on...
- Hung Ha Quoc (s3926578@rmit.edu.vn)            - 20% - contribute on...

How to Compile and Run:
------------------------
1. Make sure you have Java installed (JDK 8+).
2. Save the code to a file named: `RMIT_15_Puzzle_Solver_AStar.java`
3. Open terminal or command prompt and navigate to the folder containing the file.

To compile:
`javac RMIT_15_Puzzle_Solver_AStar.java`

To run:
`java RMIT_15_Puzzle_Solver_AStar`

Expected Output:
----------------
- Each test case will print the puzzle before solving.
- The move sequence (e.g., "LLDR") will be printed.
- Execution time for solving will be displayed.
- Puzzle output will indicate whether it is solvable.

Algorithm Used:
---------------
- Iterative Deepening A* (IDA*) with Manhattan Distance heuristic
- DFS-based path search bounded by f(n) = g(n) + h(n)
- Ensures optimal path for 15-puzzle within time/memory limits

Note:
-----
This program handles 4x4 tile puzzles only. The blank tile is represented by 0.
Solvable is checked based on parity of inversions and blank row position.
