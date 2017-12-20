import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/* *
 *  Class to represent an N-Queen puzzle board. The
 *  board is represented by an int array with
 *  the index representing the queen's horizontal
 *  position and the value of that index representing
 *  the queen's vertical position
 *
 *  Example: n = 4, configuration = {0,2,1,3}
 *        Resulting Board:
 *        Q - - -
 *        - - Q -
 *        - Q - -
 *        - - - Q
 * */
public class NQueenBoard {

  private int[] board;
  private int steps; // only to help track step cost, not used in algorithms

  /* *
   * Create the board. Default position of the
   * queens is all on the top row.
   * */
  public NQueenBoard(int size) {
    board = new int[size];
  }

  public int[] getBoard() {
    return board;
  }

  /* *
   *  Method to populate an N-queen instance.
   * */
  public void populate(int[] configuration) {
    // Make sure the input array is at least as long as the board
    if (configuration.length < board.length) {
      configuration = Arrays.copyOf(configuration, board.length);
    }

    // Copy the board
    for (int i = 0; i < board.length; i++) {
      if (configuration[i] >= board.length) {
        throw new IllegalArgumentException("Index " + i + " in configuration " +
                                           "array with value " + configuration[i] +
                                           " is an invalid index.");
      }

      board[i] = configuration[i];
    }
  }

  /* *
   * Method to randomize the queens' locations.
   * */
  public void randomizeBoard() {
    Random r = new Random(System.currentTimeMillis());
    int[] configuration = new int[board.length];
    for (int i = 0; i < configuration.length; i++) {
      configuration[i] = r.nextInt(board.length);
    }
    populate(configuration);
  }

  public boolean isSolved() {
    return getNumberOfConflicts(board) == 0;
  }

  /* *
   * Get the number of pairs of queens that are attacking
   * each other.
   * */
  private int getNumberOfConflicts(int[] testBoard) {
    int totalConflicts = 0;

    // To test if queens are on the same row, check if the
    // value of their indices is equal. To check if they
    // are on the same diagonal, check if their horizontal
    // distance from each other is equal to their vertical
    // distance from each other.
    for (int i = 0; i < testBoard.length; i++) {
      for (int j = i+1; j < testBoard.length; j++) {
        if (testBoard[i] == testBoard[j] || Math.abs(i - j) == Math.abs(testBoard[i] - testBoard[j])) {
          totalConflicts++;
        }
      }
    }

    return totalConflicts;
  }

  /* *
   * Print the current board to the screen
   * */
  public void printBoard() {
    int[][] fullBoard = new int[board.length][];
    for (int i = 0; i < fullBoard.length; i++) {
      fullBoard[i] = new int[board.length];
    }

    for (int i = 0; i < board.length; i++) {
      fullBoard[board[i]][i] = 1;
    }

    for (int i = 0; i < fullBoard.length; i++) {
      for (int j = 0; j < fullBoard[i].length; j++) {
        if (fullBoard[i][j] == 0) {
            System.out.print("-");
        } else {
            System.out.print("Q");
        }
      }
      System.out.println();
    }
  }

  /* *
   * Method to perform a simple steepest hill climbing search
   * The returned ArrayList contains 2 elements:
   *    Index 0 - The time to execute
   *    Index 1 - The search cost
   * */
  public ArrayList<Object> steepestHillSearch() {
    ArrayList<Object> results = new ArrayList<Object>();
    long time1 = System.currentTimeMillis();
    steps = 0;
    while (true) {
      int[] succ = getBestSuccessor(board);
      int currVal = getNumberOfConflicts(board);
      int succVal = getNumberOfConflicts(succ);
      if (succVal >= currVal) {
        long time2 = System.currentTimeMillis();
        results.add(time2 - time1);
        results.add(steps);
        return results;
      }
      board = succ;
    }
  }

  /* *
   * Helper Method to steepest hill climbing search. It
   * finds the best value successor (in this case the one with
   * the least conflicts) of the given board.
   * */
  private int[] getBestSuccessor(int[] testBoard) {
    int conflicts = getNumberOfConflicts(testBoard);
    int[] currBest = Arrays.copyOf(testBoard, testBoard.length);

    // If the board is already solved, there are no best successors
    if (conflicts == 0) {
      return testBoard;
    }

    for (int i = 0; i < testBoard.length; i++) {
      int currIndex = testBoard[i];
      for (int j = 0; j < testBoard.length; j++) {
        if (currIndex != j) {
          // Move queen to a new position and check conflicts
          testBoard[i] = j;
          int currConflicts = getNumberOfConflicts(testBoard);

          // If the current arrangement has no conflicts return it
          if (currConflicts == 0) {
              return testBoard;
            // If the new arrangement has less conflicts, update currBest
          } else if (currConflicts < conflicts) {
              currBest = Arrays.copyOf(testBoard, testBoard.length);
              conflicts = currConflicts;
          }

          // Move the queen back to original position
          testBoard[i] = currIndex;
          steps++;
        }
      }
    }

    return currBest;
  }

  /* *
   * Method to perform the min conflicts algorithm for the n-queen
   * problem configured as a CSP
   * The returned ArrayList contains 2 elements:
   *    Index 0 - The time to execute
   *    Index 1 - The search cost
   * */
  public ArrayList<Object> minConflicts() {
    // Set max number of steps to run before deciding failure
    final int MAX_STEPS = 1000;
    int[] curr = Arrays.copyOf(board, board.length);

    ArrayList<Object> results = new ArrayList<Object>();

    Random r = new Random(System.currentTimeMillis());
    long time1 = System.currentTimeMillis(), time2;
    int searchCost = 0;
    for (int i = 0; i < MAX_STEPS; i++) {
      if (getNumberOfConflicts(curr) == 0) {
        time2 = System.currentTimeMillis();
        board = Arrays.copyOf(curr, curr.length);
        results.add(time2 - time1);
        results.add(searchCost);
        return results;
      }

      int nextIndex;
      do {
        nextIndex = r.nextInt(curr.length);
      } while(!hasConflicts(curr, nextIndex));
      int newVal = getSingleMinConflicts(curr, nextIndex);
      curr[nextIndex] = newVal;
      searchCost++;
    }
    board = Arrays.copyOf(curr, curr.length);
    time2 = System.currentTimeMillis();
    results.add(time2 - time1);
    results.add(searchCost);
    return results;
  }

  /* *
   * Returns true if the queen at the given index
   * has at least 1 conflict
   * */
  private boolean hasConflicts(int[] testBoard, int index) {
    return getConflicts(testBoard, index) > 0;
  }

  private int getConflicts(int[] testBoard, int index) {
    int conflicts = 0;
    for (int i = 0; i < testBoard.length; i++) {
      if (i != index && (testBoard[i] == testBoard[index] || Math.abs(i - index) == Math.abs(testBoard[i] - testBoard[index]))) {
        conflicts++;
      }
    }
    return conflicts;
  }

  /* *
   * Helper Functions for the min conflicts algorithm. It gets the
   * value for the given variable with the least conflicts.
   * */
  private int getSingleMinConflicts(int[] testBoard, int index) {
    int[] tempBoard = Arrays.copyOf(testBoard, board.length);
    int min = getConflicts(testBoard, index), currVal = testBoard[index];

    for (int i = 0; i < testBoard.length; i++) {
      tempBoard[index] = i;
      int currConflicts = getConflicts(tempBoard, index);
      if (currConflicts <= min) {
        min = currConflicts;
        currVal = i;
      }
    }
    return currVal;
  }
}
