/** Multidimensional array 
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3
    
    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        int rows = arr.length;
        int columns = 0;
        int i = arr.length;
        while (i > 0){
            if (arr[i].length > columns){
                columns = arr[i].length;
            }
            i -= 1;
        }
        System.out.println("Rows: " + rows);
        System.out.println("Columns: " + columns);
    } 

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr) {
        int max_num = arr[0][0];
        for (int i = 0; i < arr.length; i+=1){
            for (int j = 0; j < arr[i].length; j+=1){
                if (arr[i][j] > max_num){
                    max_num = arr[i][j];
                }
            }
        }
        return max_num;
    }

    /**Return an array where each element is the sum of the 
    corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr) {
        int ans[] = new int [arr.length];
        for (int i = 0; i < arr.length; i+=1){
            for (int j = 0; j < arr[i].length; j+=1){
                ans[i] = ans[i] + arr[i][j];
            }

            }
        return ans;
    }
}