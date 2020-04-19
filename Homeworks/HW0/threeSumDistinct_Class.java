public class threeSumDistinct_Class {

    public static void main (String[] args) {
        int [] test_list = {0, 1, 2, 3};
        System.out.println(threeSumDistinct(test_list));
    }

    public static boolean threeSumDistinct (int[] num_list) {
        for (int i = 0; i < (num_list.length - 2) ; i += 1) {
            for (int j = 1; j < (num_list.length - 1); j += 1) {
                for (int k = 2; k < num_list.length; k += 1) {
                    if (num_list[i] + num_list[j] + num_list[k] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}