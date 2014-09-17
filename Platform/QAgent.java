/* Author: Mingcheng Chen */

import java.util.Random;
import java.util.ArrayList;

public class QAgent implements Agent {
  public QAgent() {
    this.rand = new Random();
  }

  public void initialize(int numOfStates, int numOfActions) {
    this.qValue = new double[numOfStates][numOfActions];
    this.numOfStates = numOfStates;
    this.numOfActions = numOfActions;
  }

  private double bestUtility(int state) {

  }

  public int chooseAction(int state) {

  }

  public void updatePolicy(double reward, int action,
                           int oldState, int newState) {

  }

  public Policy getPolicy() {

  }

  private static final double discount = 0.9;
  private static final double rate = 0.1;
  private static final double epsilon = 0.05;

  private int numOfStates;
  private int numOfActions;
  private double[][] qValue;
  private Random rand;
}
