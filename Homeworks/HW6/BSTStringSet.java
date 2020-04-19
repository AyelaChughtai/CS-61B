import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Ayela Chughtai
 */
public class BSTStringSet implements StringSet, Iterable<String>, SortedStringSet {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        if (this.contains(s)) {
        } else {
            _root = putHelper(s, _root);
        }
    }

    private Node putHelper(String s, Node root) {
        if (root == null) {
            return new Node(s);
        } else {
            if (s.compareTo(root.s) < 0) {
                root.left = putHelper(s, root.left);
            } else if (s.compareTo(root.s) > 0) {
                root.right = putHelper(s, root.right);
            }
        }
        return root;
    }

    @Override
    public boolean contains(String s) {
        return containsHelper(s, _root);
    }

    public boolean containsHelper(String s, Node root) {
        boolean output = false;
        if (root == null) {
            return false;
        }
        if (s.compareTo(root.s) == 0) {
            output = true;
        }
        if (s.compareTo(root.s) < 0) {
            return containsHelper(s, root.left);
        }
        if (s.compareTo(root.s) > 0) {
            return containsHelper(s, root.right);
        }
        return output;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> output = new ArrayList<>();
        for (String i : this) {
            output.add(i);
        }
        return output;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTBounded(_root, low, high);
    }

    public static class BSTBounded implements Iterator<String> {

        public BSTBounded(Node root, String low, String high) {
            _low = low;
            _high = high;
            addTree(root);
        }

        @Override
        public boolean hasNext() {
            return !position.empty()
                    && position.peek().s.compareTo(_high) <= 0;
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node node = position.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void addTree(Node node) {
            while (node != null) {
                if (node.s.compareTo(_low) >= 0) {
                    while (node != null) {
                        position.push(node);
                        node = node.left;
                    }
                } else {
                    addTree(node.right);
                }
            }
        }

        /** String low. */
        private String _low;

        /** String high. */
        private String _high;

        /** Position of node in tree. */
        private Stack<Node> position = new Stack<>();
    }


    /** Root node of the tree. */
    private Node _root;
}