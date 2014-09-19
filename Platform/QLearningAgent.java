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
    double maxQValue = Double.NEGATIVE_INFINITY;
    for(double aQValue : qValue[state]){
        if(Double.compare(aQValue, maxQValue) > 0)
            maxQValue = aQValue;
    }
    return maxQValue;
  }

  public int chooseAction(int state) {
    double prob = rand.nextDouble();
    ArrayList<Integer> actionsWithMaxQValue = new ArrayList<Integer>();
    // Choose random action with epsilon probability
    if(prob < epsilon){
        return rand.nextInt(this.numOfActions);
    }else{ //Be greedy with (1-epsilon) probability
        double maxQValue = Double.NEGATIVE_INFINITY;
        double[] actionsForThisState = qValue[state];
        for(double aQValue : actionsForThisState){
            if(aQValue > maxQValue){
                maxQValue = aQValue;
            }
        }
        int i = 0;
        for(double aQValue : actionsForThisState){
            if(Double.compare(aQValue, maxQValue) == 0){
                actionsWithMaxQValue.add(i);
            }
            i++;
        }
        return actionsWithMaxQValue.get(rand.nextInt(actionsWithMaxQValue.size()));
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
      double maxQValue = Double.NEGATIVE_INFINITY;
      int i = 0;
      for(double aQValue : qValueForState){
          if(Double.compare(aQValue, maxQValue) > 0){
              bestAction = i;
              maxQValue = aQValue;
          }
          i++;
      }
      return bestAction;
  }
  private static final double discount = 0.9;
  private static final double rate = 0.1;
  private static final double epsilon = 0.1;

  private int numOfStates;
  private int numOfActions;
  private double[][] qValue;
  private Random rand;
}
