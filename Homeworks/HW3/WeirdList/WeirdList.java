/** A WeirdList holds a sequence of integers.
 * @author Ayela Chughtai
 */
public class WeirdList {
    /**
     * The empty sequence of integers.
     */
    int _head;
    WeirdList _tail;
    public static final WeirdList EMPTY = new WeirdListHelper();

    /** A new WeirdList whose head is HEAD and tail is TAIL. */
    public WeirdList(int head, WeirdList tail) {
        this._head = head;
        this._tail = tail;
    }

    /** Returns the number of elements in the sequence that
     *  starts with THIS. */
    public int length() {
        return 1 + this._tail.length();
    }

    /** Return a string containing my contents as a sequence of numerals
     *  each preceded by a blank.  Thus, if my list contains
     *  5, 4, and 2, this returns " 5 4 2". */
    @Override
    public String toString() {
        return (" " + this._head) + this._tail.toString();
    }

    /** Part 3b: Apply FUNC.apply to every element of THIS WeirdList in
     *  sequence, and return a WeirdList of the resulting values. */
    public WeirdList map(IntUnaryFunction func) {
        return new WeirdList(func.apply(this._head), this._tail.map(func));
    }

    /** Creates an empty WeirdList.*/
    private static class WeirdListHelper extends WeirdList {

        /** Creates an empty WeirdList with head as 0 and tail as null.*/
        public WeirdListHelper() {
            super(0, null);
        }

        @Override
        /** Makes length equal 0.*/
        public int length() {
            return 0;
        }

        @Override
        /** Returns empty string.*/
        public String toString() {
            return "";
        }

        @Override
        /** Returns empty WeirdList.*/
        public WeirdList map(IntUnaryFunction func) {
            return new WeirdListHelper();
        }
    }

    /*
     * You should not add any methods to WeirdList, but you will need
     * to add private fields (e.g. head).

     * But that's not all!

     * You will need to create at least one additional class for WeirdList
     * to work. This is because you are forbidden to use any of the
     * following in ANY of the code for HW3:
     *       if, switch, while, for, do, try, or the ?: operator.

     * If you'd like an obtuse hint, scroll to the very bottom of this
     * file.

     * You can create this hypothetical class (or classes) in separate
     * files like you usually do, or if you're feeling bold you can
     * actually stick them INSIDE of this class. Yes, nested classes
     * are a thing in Java.

     * As an example:
     * class Garden {
     *     private static class Potato {
     *        int n;
     *        public Potato(int nval) {
     *           n = nval;
     *        }
     *     }
     * }
     * You are NOT required to do this, just an extra thing you can
     * do if you want to avoid making a separate .java file. */

}

/*
 * Hint: The first non-trivial thing you'll probably do to WeirdList
 * is to fix the EMPTY static variable so that it points at something
 * useful. */
