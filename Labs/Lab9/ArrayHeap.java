import java.util.ArrayList;
import java.util.Objects;

/** A Generic heap class. Unlike Java's priority queue, this heap doesn't just
 * store Comparable objects. Instead, it can store any type of object
 * (represented by type T) and an associated priority value.
 * @author Ayela Chughtai */
public class ArrayHeap<T> {

    /* DO NOT CHANGE THESE METHODS. */

    /** An ArrayList that stores the nodes in this binary heap. */
    private ArrayList<Node> contents;

    /** A constructor that initializes an empty ArrayHeap. */
    public ArrayHeap() {
        contents = new ArrayList<>();
        contents.add(null);
    }

    /** Returns the number of elements in the priority queue. */
    public int size() {
        return contents.size() - 1;
    }

    /** Returns the node at index INDEX. */
    private Node getNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /** Sets the node at INDEX to N */
    private void setNode(int index, Node n) {
        // In the case that the ArrayList is not big enough
        // add null elements until it is the right size
        while (index + 1 > contents.size()) {
            contents.add(null);
        }
        contents.set(index, n);
    }

    /** Returns and removes the node located at INDEX. */
    private Node removeNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.remove(index);
        }
    }

    /** Swap the nodes at the two indices. */
    private void swap(int index1, int index2) {
        Node node1 = getNode(index1);
        Node node2 = getNode(index2);
        this.contents.set(index1, node2);
        this.contents.set(index2, node1);
    }

    /** Prints out the heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /** Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getNode(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getNode(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getNode(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getNode(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /** A Node class that stores items and their associated priorities. */
    public class Node {
        private T _item;
        private double _priority;

        private Node(T item, double priority) {
            this._item = item;
            this._priority = priority;
        }

        public T item() {
            return this._item;
        }

        public double priority() {
            return this._priority;
        }

        public void setPriority(double priority) {
            this._priority = priority;
        }

        @Override
        public String toString() {
            return this._item.toString() + ", " + this._priority;
        }
    }

    /* FILL IN THE METHODS BELOW. */

    /** Returns the index of the left child of the node at i. */
    private int getLeftOf(int i) {
        return i * 2;
    }

    /** Returns the index of the right child of the node at i. */
    private int getRightOf(int i) {
        return (i * 2) + 1;
    }

    /** Returns the index of the node that is the parent of the
     *  node at i. */
    private int getParentOf(int i) {
        return i / 2;
    }

    /** Returns the index of the node with smaller priority. If one
     * node is null, then returns the index of the non-null node.
     * Precondition: at least one of the nodes is not null. */
    private int min(int index1, int index2) {
        int output = Integer.MAX_VALUE;
        if (!(this.getNode(index1) == null && this.getNode(index2) == null)) {
            if (this.getNode(index1) == null) {
                output = index2;
            } else if (this.getNode(index2) == null) {
                output = index1;
            } else if (Objects.requireNonNull(this.getNode(index1)).priority()
                    > Objects.requireNonNull(this.getNode(index2)).priority()) {
                output = index1;
            } else {
                output = index2;
            }
        }
        return output;
    }

    /** Returns the item with the smallest priority value, but does
     * not remove it from the heap. If multiple items have the minimum
     * priority value, returns any of them. Returns null if heap is
     * empty. */
    public T peek() {
        int minPriority = Integer.MAX_VALUE;
        int outputIndex = 0;
        T output = null;
        for (int i = 1; i < this.size() + 1; i++) {
            if (Objects.requireNonNull(this.getNode(i)).priority()
                    < minPriority) {
                minPriority = (int) Objects.requireNonNull(this.getNode(i)).priority();
                outputIndex = i;
            }
            output = Objects.requireNonNull(getNode(outputIndex)).item();
        }
        return output;
    }

    /** Bubbles up the node currently at the given index until no longer
     *  needed. */
    private void bubbleUp(int index) {
        if (index > 1) {
            int left = this.getLeftOf(index);
            int right = this.getLeftOf(index);
            int minIndex = min(left, right);
            if (this.getNode(this.getLeftOf(index)) == null
                    && this.getNode(this.getLeftOf(index)) == null) {
                return;
            } else {
                if (Objects.requireNonNull(getNode(index)).priority()
                        > Objects.requireNonNull(getNode(minIndex)).priority()) {
                    this.swap(index, minIndex);
                }
                bubbleUp(minIndex);
            }
        }
    }

    /** Bubbles down the node currently at the given index until no longer
     *  needed. */
    private void bubbleDown(int index) {
        int left = this.getLeftOf(index);
        int right = this.getLeftOf(index);
        int minIndex = min(left, right);
        if (this.getNode(this.getLeftOf(index)) == null
                && this.getNode(this.getLeftOf(index)) == null) {
            return;
        } else {
            if (Objects.requireNonNull(getNode(index)).priority()
                    > Objects.requireNonNull(getNode(minIndex)).priority()) {
                this.swap(index, minIndex);
            }
            bubbleDown(minIndex);
        }
    }

    /** Inserts an item with the given priority value. Assume that item is
     * not already in the heap. Same as enqueue, or offer. */
    public void insert(T item, double priority) {
        contents.add(new Node(item, priority));
        bubbleDown(this.size() + 1);
    }

    /** Returns the element with the smallest priority value, and removes
     * it from the heap. If multiple items have the minimum priority value,
     * removes any of them. Returns null if the heap is empty. Same as
     * dequeue, or poll. */
    public T removeMin() {
        int minPriority = Integer.MAX_VALUE;
        int outputIndex = 0;
        T output = null;
        for (int i = 1; i < this.size() + 1; i++) {
            if (Objects.requireNonNull(this.getNode(i)).priority()
                    < minPriority) {
                minPriority = (int) Objects.requireNonNull(this.getNode(i)).priority();
                outputIndex = i;
            }
        }
        if (outputIndex != 0) {
            output = Objects.requireNonNull(this.removeNode(outputIndex)).item();
        }
        this.bubbleDown(1);
        return output;
    }

    /** Changes the node in this heap with the given item to have the given
     * priority. You can assume the heap will not have two nodes with the
     * same item. Does nothing if the item is not in the heap. Check for
     * item equality with .equals(), not == */
    public void changePriority(T item, double priority) {
        for (int i = 1; i < this.size() + 1; i++) {
            if (Objects.requireNonNull(this.getNode(i)).item().equals(item)) {
                Objects.requireNonNull(this.getNode(i))._priority = priority;
                break;
            }
        }
    }
}
