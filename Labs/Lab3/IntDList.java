/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        int total = 0;
        DNode x = _front;
        while (x != null) {
            total += 1;
            x = x._next;
        }
        return total;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        if (i == 0) {
            return _front._val;
        } if (i < 0) {
            i += size();
        }
        DNode x = _front;
        while (i > 0) {
            x = x._next;
            i = i - 1;
        }
        return x._val; // FIXME: Implement this method and return correct value
    }

    /**
     * @param d value to be inserted in the front
     * @return
     */
    public void insertFront(int d) {
        _front = new DNode(null, d, _front);
        if (_back == null) {
            _back = _front;
        } else {
            _front._next._prev = _front;
        } // FIXME: Implement this method
    }

    /**
     * @param d value to be inserted in the back
     * @return
     */
    public void insertBack(int d) {
        _back = new DNode(_back, d, null);
        if (_front == null) {
            _front = _back ;
        } else {
            _back._prev._next = _back;
        } // FIXME: Implement this method
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     * @return
     */
    public void insertAtIndex(int d, int index) {
        if (index == 0 || index == -size()-1) {
            insertFront(d);
        } else if (index == -1 || index == size()) {
            insertBack(d);
        } else if (index < -1) {
            index += size();
            DNode x = _front;
            while (index > 0) {
                x = x._next;
                index -= 1;
            }
            DNode p = new DNode(x, d, x._next);
            x._next._prev = p;
            x._next = p;

        } else {
            DNode x = _front;
            while (index > 0) {
                x = x._next;
                index -= 1;
            }
            DNode p = new DNode(x._prev, d, x);
            x._prev._next = p;
            x._prev = p;
            // FIXME: Implement this method
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        if (_front == null) {
            return 1;
        } if (size() == 1) {
            int ans = _front._val;
            _front = null;
            _back = null;
            return ans;
        } else {
            int ans = _front._val;
            _front = _front._next;
            _front._prev = null;
            return ans;
        }
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        if (_back == null) {
            return -1;
        } if (size() == 1) {
            int del_item = _back._val;
            _back = null;
            _front = null;
            return del_item;
        } else {
            int del_item = _back._val;
            _back = _back._prev;
            _back._next = null;
            return del_item;
        }
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        // FIXME: Implement this method and return correct value
        return 0;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val +"]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        public DNode _value;
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
