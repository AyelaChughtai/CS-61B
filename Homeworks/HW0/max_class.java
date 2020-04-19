public class max_class {

    public static void main (String[] args) {
        int[] test_list = {1, 2, 3, 1};
        System.out.println(max(test_list));
    }

    public static int max (int[] num_list) {
        int i = 0;
        int max_num = num_list[0];
        while (i < num_list.length) {
            if (num_list[i] > max_num) {
                max_num = num_list[i];
            }
            i += 1;
        }
        return max_num;
    }
}





