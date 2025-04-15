package com.bobocode.cs;

import com.bobocode.util.ExerciseNotCompletedException;

import java.util.function.Consumer;

/**
 * {@link RecursiveBinarySearchTree} is an implementation of a {@link BinarySearchTree} that is based on a linked nodes
 * and recursion. A tree node is represented as a nested class {@link Node}. It holds an element (a value) and
 * two references to the left and right child nodes.
 * <p><p>
 * <strong>TODO: to get the most out of your learning, <a href="https://www.bobocode.com">visit our website</a></strong>
 * <p>
 *
 * @param <T> a type of elements that are stored in the tree
 * @author Taras Boychuk
 * @author Maksym Stasiuk
 */
public class RecursiveBinarySearchTree<T extends Comparable<T>> implements BinarySearchTree<T> {
    private static class Node<T> {
        T element;
        Node<T> leftChild;
        Node<T> rightChild;

        private Node(T element) {
            this.element = element;
        }
    }

    private Node<T> root;
    private int size;

    public static <T extends Comparable<T>> RecursiveBinarySearchTree<T> of(T... elements) {
        RecursiveBinarySearchTree<T> tree = new RecursiveBinarySearchTree<>();
        for (T element : elements) {
            tree.insert(element);
        }
        return tree;
    }

    private boolean makeInsert(Node<T> node, T element) {
        int compare = element.compareTo(node.element);
        if (compare == 0)
            return false;
        if (compare < 0 && node.leftChild == null) {
            node.leftChild = new Node<>(element);
            return true;
        } else if (compare > 0 && node.rightChild == null) {
            node.rightChild = new Node<>(element);
            return true;
        }
        return compare < 0 ? makeInsert(node.leftChild, element) : makeInsert(node.rightChild, element);
    }

    @Override
    public boolean insert(T element) {
        if (root == null) {
            root = new Node<>(element);
            size++;
            return true;
        }
        boolean result = makeInsert(root, element);
        if (result)
            size++;
        return result;
    }

    private boolean elementExists(Node<T> node, T element) {
        if (node == null)
            return false;
        int compare = element.compareTo(node.element);
        if (compare == 0)
            return true;
        return elementExists(compare < 0 ? node.leftChild : node.rightChild, element);
    }

    @Override
    public boolean contains(T element) {
        return elementExists(root, java.util.Objects.requireNonNull(element));
    }

    @Override
    public int size() {
        return size;
    }

    private int calcDepth(Node<T> node, int depth) {
        if (node == null)
            return Math.max(depth - 1, 0);
        return Math.max(calcDepth(node.leftChild, depth + 1), calcDepth(node.rightChild, depth + 1));
    }

    @Override
    public int depth() {
        return calcDepth(root, 0);
    }

    private void traverseInAsc(Node<T> node, Consumer<T> consumer) {
        if (node == null)
            return;
        traverseInAsc(node.leftChild, consumer);
        consumer.accept(node.element);
        traverseInAsc(node.rightChild, consumer);
    }

    @Override
    public void inOrderTraversal(Consumer<T> consumer) {
        traverseInAsc(root, consumer);
    }
}
