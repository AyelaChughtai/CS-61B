public class threeSum_Class {

    public static void main (String[] args) {
        int [] test_list = {0, 1, 2, 3};
        System.out.println(threeSum(test_list));
    }

    public static boolean threeSum (int[] num_list) {
        for (int i = 0; i < num_list.length; i += 1) {
            for (int j = 0; j < num_list.length; j += 1) {
                for (int k = 0; k < num_list.length; k += 1) {
                    if (num_list[i] + num_list[j] + num_list[k] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}