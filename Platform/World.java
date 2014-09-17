/* Author: Mingcheng Chen */

public interface World {
  public void beAwareOfThief();     // called at most once for each World object
  public void beIgnorantOfThief();  // called at most once for each World object
  public void initialize();
  public boolean hasThief();
  public boolean knowsThief();
  public boolean inCompany(int row, int col);
  public int hasCustomer(int row, int col);  // 0: no customer, 1: customer 1, 2: customer 2
  public int getThiefRow();
  public int getNumberOfRows();
  public int getNumberOfCols();
  public int getNumberOfStates();
  public int getInitialState();
  public int getState(int robotRow, int robotCol, boolean hasP1, boolean hasP2);
  public int getState(int robotRow, int robotCol, int thiefRow, boolean hasP1, boolean hasP2);
  public void evolve();
  public double slipperiness(int row, int col);
  public boolean meetsThief(int row, int col);
}
