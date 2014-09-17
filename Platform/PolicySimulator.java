/* Author: Mingcheng Chen */

import java.util.Random;
import java.util.ArrayList;
import java.io.*;
import java.io.PrintWriter;
import java.util.Scanner;

public class PolicySimulator {
  public static void main(String[] args) {
    if (args.length != 4) {
      System.out.println("Usage: java PolicySimulator <world> <policy_file> <knows_thief> <steps>");
      return;
    }

    World world;

    try {
      Class c = Class.forName(args[0]);
      world = (World)c.newInstance();
    } catch (Exception e) {
      System.out.println("Error: invalid world class");
      return;
    }

    String policyFile = args[1];

    boolean thiefKnown;

    if (args[2].equals("y")) {
      thiefKnown = true;
    } else if (args[2].equals("n")) {
      thiefKnown = false;
    } else {
      System.out.println("Error: invalid knows_thief");
      return;
    }

    int steps;

    try {
      steps = Integer.parseInt(args[3]);
    } catch (Exception e) {
      System.out.println("Error: invalid steps");
      return;
    }

    (new PolicySimulator(world, policyFile, thiefKnown, steps)).simulate();
  }

  public PolicySimulator(World world, String policyFile, boolean thiefKnown, int steps) {
    this.world = world;
    this.policyFile = policyFile;
    this.thiefKnown = thiefKnown;
    this.steps = steps;
    this.rand = new Random();
  }

  private int[] readPolicy() {
    int[] actions = new int[this.world.getNumberOfStates()];

    try {
      Scanner scanner = new Scanner(new BufferedReader(new FileReader(this.policyFile)));

      ArrayList<String> commands = new ArrayList<String>();
      while (scanner.hasNextLine()) {
        commands.add(scanner.nextLine());
      }

      if (commands.size() != actions.length) {
        System.out.println("Error: The policy file has different number of states.");
        System.exit(0);
      }

      for (int i = 0; i < actions.length; i++) {
        actions[i] = commands.get(i).charAt(0) - '0';
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return actions;
  }

  private void OutputState(int robotRow, int robotCol, boolean hasP1, boolean hasP2, double totalReward) {
    char[][] maze = new char[this.world.getNumberOfRows()][this.world.getNumberOfCols()];

    for (int i = 0; i < this.world.getNumberOfRows(); i++) {
      for (int j = 0; j < this.world.getNumberOfCols(); j++) {
        if (this.world.meetsThief(i, j)) {
          maze[i][j] = 'T';
          continue;
        }
        if (this.world.slipperiness(i, j) > 0.0) {
          maze[i][j] = '#';
          continue;
        }
        if (this.world.hasCustomer(i, j) > 0) {
          maze[i][j] = (char)((int)'0' + this.world.hasCustomer(i, j));
          continue;
        }
        maze[i][j] = ' ';
      }
    }

    maze[this.world.getNumberOfRows() - 1][0] = 'C';
    maze[robotRow][robotCol] = 'R';

    for (int i = 0; i < maze.length; i++) {
      System.out.println(maze[i]);
    }
    if (hasP1 && hasP2) {
      System.out.println("Carrying both packages");
    } else if (hasP1) {
      System.out.println("Only carrying the package for customer 1");
    } else if (hasP2) {
      System.out.println("Only carrying the package for customer 2");
    } else {
      System.out.println("Carrying nothing");
    }

    System.out.println("Total reward is " + totalReward + ".");
  }

  public void simulate() {
    if (this.thiefKnown) {
      this.world.beAwareOfThief();
    } else {
      this.world.beIgnorantOfThief();
    }

    this.world.initialize();

    int[] actions = this.readPolicy();

    int initialState = this.world.getInitialState();

    int robotRow = this.world.getNumberOfRows() - 1;
    int robotCol = 0;

    int thiefRow = -1;

    if (this.world.knowsThief()) {
      thiefRow = this.world.getThiefRow();
    }

    boolean hasP1 = true, hasP2 = true;

    int currState = initialState;

    double totalReward = 0.0;

    this.OutputState(robotRow, robotCol, hasP1, hasP2, totalReward);

    Scanner scanner = new Scanner(System.in);

    for (int step = 0; step < this.steps; step++) {
      scanner.nextLine();

      int action = actions[currState];

      int nextRobotRow = robotRow + directions[action][0];
      int nextRobotCol = robotCol + directions[action][1];

      if (nextRobotRow < 0 || nextRobotCol < 0 || nextRobotRow >= this.world.getNumberOfRows() ||
                                                  nextRobotCol >= this.world.getNumberOfCols()) {
        nextRobotRow = robotRow;
        nextRobotCol = robotCol;
      }

      robotRow = nextRobotRow;
      robotCol = nextRobotCol;

      this.world.evolve();

      if (this.world.knowsThief()) {
        thiefRow = this.world.getThiefRow();
      }

      double reward = 0.0;

      if (!hasP1 && !hasP2) {  // has nothing (needs to go back to the company)
        reward = 0.0;

        if (this.world.inCompany(robotRow, robotCol)) {  // load new packages
          hasP1 = true;
          hasP2 = true;
        }
      } else {  // has at least one package (successfully delivered one)
        if (hasP1 && this.world.hasCustomer(robotRow, robotCol) == 1) {
          hasP1 = false;
        }
        if (hasP2 && this.world.hasCustomer(robotRow, robotCol) == 2) {
          hasP2 = false;
        }

        if (!hasP1 && !hasP2) {  // successful delivery
          reward = rewardBySuccessfulDelivery;
        } else if (this.world.meetsThief(robotRow, robotCol)) {
          hasP1 = false;
          hasP2 = false;
          reward = -lossByThief;
        } else if (this.world.slipperiness(robotRow, robotCol) > 0.0) {
          if (rand.nextDouble() <= this.world.slipperiness(robotRow, robotCol)) {
            hasP1 = false;
            hasP2 = false;
            reward = -lossBySlipperiness;
          } else {
            reward = 0.0;
          }
        }
      }

      totalReward += reward;
      int newState = this.world.knowsThief() ? this.world.getState(robotRow, robotCol, thiefRow, hasP1, hasP2) :
                                               this.world.getState(robotRow, robotCol, hasP1, hasP2);

      currState = newState;

      this.OutputState(robotRow, robotCol, hasP1, hasP2, totalReward);
    }
  }

  private static final int numOfActions = 4;
  private static final int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};  // NESW
  private static final double lossBySlipperiness = 0.5;
  private static final double lossByThief = 2.0;
  private static final double rewardBySuccessfulDelivery = 1.0;

  private World world;
  private boolean thiefKnown;
  private int steps;
  private String policyFile;

  private Random rand;
}
