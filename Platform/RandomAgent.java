/* Author: Mingcheng Chen */

import java.util.Random;

public class RandomAgent implements Agent {
  public RandomAgent() {
    this.rand = new Random();
  }

  public void initialize(int numOfStates, int numOfActions) {
    this.iDoNotKnowWhyINeedThisArray = new double[numOfStates][numOfActions];
    this.numOfStates = numOfStates;
    this.numOfActions = numOfActions;
  }

  public int chooseAction(int state) {
    // Do not know what to do. Let's toss a coin.
    return rand.nextInt(this.numOfActions);
  }

  public void updatePolicy(double reward, int action, int oldState, int newState) {
    // What is reward?
    // What is action?
    // What is oldState?
    // What is newState?
    // Do you think I am gonna make this method empty?
    // You thought wrong.
    return;
  }

  public Policy getPolicy() {
    int[] actions = new int[this.numOfStates];
    for (int i = 0; i < this.numOfStates; i++) {
      actions[i] = rand.nextInt(this.numOfActions);
    }

    return new Policy(actions);
  }

  private Random rand;
  private int numOfStates;
  private int numOfActions;
  private double[][] iDoNotKnowWhyINeedThisArray;
}
