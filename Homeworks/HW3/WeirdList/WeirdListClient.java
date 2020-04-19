import net.sf.saxon.expr.accum.Accumulator;
import net.sf.saxon.functions.AccumulatorFn;

/** Functions to increment and sum the elements of a WeirdList. */
class WeirdListClient {

    /** Return the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        helpAdd x = new helpAdd(n);
        return L.map(x);
    }

    static class helpAdd implements IntUnaryFunction {
        int adder;
        public helpAdd(int n) {
            adder = n;
        }

        @Override
        public int apply(int x){
            return adder + x;
        }

    }

    static class helpSum implements IntUnaryFunction {
        public int summer;
        public helpSum(int n) {
            summer = n;
        }

        @Override
        public int apply(int x){
            return summer += x;
        }

    }

    /** Return the sum of all the elements in L. */
    static int sum(WeirdList L) {
        helpSum x = new helpSum(0);
        L.map(x);
        return x.summer;
    }

    /* IMPORTANT: YOU ARE NOT ALLOWED TO USE RECURSION IN ADD AND SUM
     *
     * As with WeirdList, you'll need to add an additional class or
     * perhaps more for WeirdListClient to work. Again, you may put
     * those classes either inside WeirdListClient as private static
     * classes, or in their own separate files.

     * You are still forbidden to use any of the following:
     *       if, switch, while, for, do, try, or the ?: operator.
     *
     * HINT: Try checking out the IntUnaryFunction interface.
     *       Can we use it somehow?
     */
}
