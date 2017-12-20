# NQueenSolver

The N-Queen problem involves placing N queens on an N x N chess board such that no pair of
queens can capture each other. This project solves this problem in two algorithms: The steepest
hill climbing algorithm and the min-conflicts algorithm.

Compile and run instructions:

To run ensure that you have the files names Project2.java and NQueenBoard.java
in the directory you intend to use to run the program. The main file to run is
Project2.java and can be run from the command line with the following:

javac Project2.java

java Project2

If successful, you will see a menu prompting you for input. You can enter the
values 1-4 to do the following:

1) Find a solution - If you enter 1, the program will find a solution to the
21 Queen problem and display it, with its array representation, search cost, and
execution time.

2) Test steepest hill climbing - If you enter 2, the program will prompt you to
enter the number of test cases to use to test the algorithm. It will then run
the test with the given number of instances using steepest hill climbing search
and return the average cost, average execution time, and success rate.

3) Test min conflicts - If you enter 3, the program will do the exact same thing
as 2, but use the min conflicts algorithm to test.

4) Exit - If you enter 4, the program will terminate.
