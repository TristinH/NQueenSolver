import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Project2 {

  public static void main(String[] args) {
    // Create 21 Queen board
    NQueenBoard board = new NQueenBoard(21);

    Scanner reader = new Scanner(System.in);
    String input = "";

    while (!input.equals("4")) {
      menu();
      input = reader.nextLine();

      switch (input) {
        case "1":
          ArrayList<Object> results = findSolution(board);
          board.printBoard();
          System.out.println(Arrays.toString(board.getBoard()));
          System.out.println("Time to execute:");
          long time = (long) results.get(0);
          if (time == 0) {
              System.out.println("<1 ms");
          } else {
              System.out.println(time + " ms");
          }
          System.out.println("Search Cost");
          System.out.println(results.get(1));
          break;
        case "2":
          System.out.println("Enter number of test cases to use: ");
          String caseString = reader.nextLine();
          caseString = caseString.trim();
          int cases = 0;
          try {
              cases = Integer.parseInt(caseString);
          } catch(NumberFormatException e) {
              System.out.println("You must enter a number");
              continue;
          }
          results = solveRateHillClimbing(board, cases);
          System.out.println("Steepest Hill Climbing Results:");

          System.out.println("Average execution time:");
          Long timeTotal = (Long) results.get(0);
          System.out.println((double) timeTotal.longValue() / (double) cases + " ms");

          System.out.println("Average search cost:");
          Integer costTotal = (Integer) results.get(1);
          System.out.println(costTotal.doubleValue() / (double) cases);

          System.out.println("Solve rate:");
          System.out.println(((double) results.get(2) * 100) + "%");
          break;
        case "3":
          System.out.println("Enter number of test cases to use: ");
          caseString = reader.nextLine();
          caseString = caseString.trim();
          cases = 0;
          try {
              cases = Integer.parseInt(caseString);
          } catch(NumberFormatException e) {
              System.out.println("You must enter a number");
              continue;
          }
          results = solveRateMinConflicts(board, cases);
          System.out.println("Min Conflicts Results:");

          System.out.println("Average execution time:");
          timeTotal = (Long) results.get(0);
          System.out.println((double) timeTotal.longValue() / (double) cases + " ms");

          System.out.println("Average search cost:");
          costTotal = (Integer) results.get(1);
          System.out.println(costTotal.doubleValue() / (double) cases);

          System.out.println("Solve rate:");
          System.out.println(((double) results.get(2) * 100) + "%");
          break;
        case "4":
          return;
        default:
          System.out.println("Invalid option entered");
      }

      System.out.println();
    }

    reader.close();
  }

  public static void menu() {
    System.out.println("Select an option:");
    System.out.println("(1) Find a 21-Queen solution");
    System.out.println("(2) Test solve rate for steepest hill climbing algorithm");
    System.out.println("(3) Test solve rate for min conflicts algorithm");
    System.out.println("(4) Exit");
  }

  public static ArrayList<Object> findSolution(NQueenBoard board) {
    ArrayList<Object> results = null;
    do {
      board.randomizeBoard();
      results = board.minConflicts();
    } while (!board.isSolved());
    return results;
  }

  public static ArrayList<Object> solveRateHillClimbing(NQueenBoard board, int cases) {
    double rate = 0;
    int solvedCases = 0;
    ArrayList<Object> results = new ArrayList<Object>();
    results.add(new Long(0));
    results.add(new Integer(0));
    for (int i = 0; i < cases; i++) {
      board.randomizeBoard();
      ArrayList<Object> tempResults = board.steepestHillSearch();
      long time = (long) results.get(0) + (long) tempResults.get(0);
      results.set(0, time);

      int cost = (int) results.get(1) + (int) tempResults.get(1);
      results.set(1, cost);
      if (board.isSolved()) {
        solvedCases++;
      }
    }
    rate = (double) solvedCases / (double) cases;
    results.add(rate);
    return results;
  }

  public static ArrayList<Object> solveRateMinConflicts(NQueenBoard board, int cases) {
    double rate = 0;
    int solvedCases = 0;
    ArrayList<Object> results = new ArrayList<Object>();
    results.add(new Long(0));
    results.add(new Integer(0));
    for (int i = 0; i < cases; i++) {
      board.randomizeBoard();
      ArrayList<Object> tempResults = board.minConflicts();
      long time = (long) results.get(0) + (long) tempResults.get(0);
      results.set(0, time);

      int cost = (int) results.get(1) + (int) tempResults.get(1);
      results.set(1, cost);
      if (board.isSolved()) {
        solvedCases++;
      }
    }
    rate = (double) solvedCases / (double) cases;
    results.add(rate);
    return results;
  }
}
