/**
 * Created by Parag on 17-09-2014.
 */
public class test {
    public static void main(String[] args) {
        int[][] a = new int[][]{
                {1,2},
                {3,4,5,6,7,4,3,2,4,5,55}
        };
        a = new int[2][3];
        for(int value: a[1]){
            System.out.println(value);
        }
        //test(a[1]);
    }
    public static void test(int[] arg){
        for(int value: arg){
            System.out.println(value);
        }
    }
}
