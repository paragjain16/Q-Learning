/* Author: Mingcheng Chen */

import java.util.Random;
import java.util.ArrayList;

public class QLearningAgent implements Agent {
  public QLearningAgent() {
    this.rand = new Random();
  }

  public void initialize(int numOfStates, int numOfActions) {
    this.qValue = new double[numOfStates][numOfActions];
    this.numOfStates = numOfStates;
    this.numOfActions = numOfActions;
  }

  private double bestUtility(int state) {
    double maxQValue = -1.0;
    for(double aQValue : qValue[state]){
        if(aQValue > maxQValue)
            maxQValue = aQValue;
    }
    return maxQValue;
  }

  public int chooseAction(int state) {
    double prob = Math.random();
    if(prob - epsilon < 0.001){
        //Need to implement once TA responds
        double maxQValue = -1.0;
        int maxQValueIndex = -1;
        double[] actionsForThisState = qValue[state];
        int i = 0;
        for(double aQValue : actionsForThisState){
            if(aQValue > maxQValue){
               maxQValueIndex = i;
               maxQValue = aQValue;
            }
            i++;
        }
        return maxQValueIndex;
    }else{
        return rand.nextInt(this.numOfActions);
    }
  }

  public void updatePolicy(double reward, int action,
                           int oldState, int newState) {
    qValue[oldState][action] = (1.0 - rate) * qValue[oldState][action] + rate * ( reward + discount * bestUtility(newState));
  }

  public Policy getPolicy() {
      int[] actions = new int[this.numOfStates];
      for (int i = 0; i < this.numOfStates; i++) {
          actions[i] = bestAction(qValue[i]);
      }
      return new Policy(actions);
  }

  private int bestAction(double[] qValueForState){
      int bestAction = -1;
      double maxQValue = -1.0;
      int i =0;
      for(double aQValue : qValueForState){
          if(aQValue > maxQValue){
              bestAction = i;
              maxQValue = aQValue;
          }
          i++;
      }
      return bestAction;
  }
  private static final double discount = 0.9;
  private static final double rate = 0.1;
  private static final double epsilon = 0.00;

  private int numOfStates;
  private int numOfActions;
  private double[][] qValue;
  private Random rand;
}
