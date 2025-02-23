package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author Ayela
 */
class Lists {

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        /* *Replace this body with the solution. */

        IntListList lstNew = new IntListList();
        IntListList tempLstNew = lstNew;

        if (L.tail == null) {
            lstNew.head = L;
            return lstNew;
        }

        IntList tempL = L;
        for (IntList rest = L.tail; rest != null; rest = rest.tail) {
            lstNew.head = L;
            if (tempL.head >= rest.head) {
                tempLstNew.tail = new IntListList();
                tempLstNew = tempLstNew.tail;
                tempLstNew.head = rest;
                tempL.tail = null;
                tempL = rest;
            } else {
                tempL = rest;
            }
        }
        return lstNew;
    }
}
