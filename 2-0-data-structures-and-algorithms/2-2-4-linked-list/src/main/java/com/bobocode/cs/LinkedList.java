package com.bobocode.cs;

import java.util.NoSuchElementException;

/**
 * {@link LinkedList} is a list implementation that is based on singly linked generic nodes. A node is implemented as
 * inner static class {@link Node<T>}.
 * <p><p>
 * <strong>TODO: to get the most out of your learning, <a href="https://www.bobocode.com">visit our website</a></strong>
 * <p>
 *
 * @param <T> generic type parameter
 * @author Taras Boychuk
 * @author Serhii Hryhus
 */
public class LinkedList<T> implements List<T> {
    private static class Node<T> {
        T element;
        Node<T> next;

        Node(T element) {
            this.element = element;
        }

        static<T> Node<T> valueOf(T element) {
            return new Node<T>(element);
        }

        T getElement() {
            return this.element;
        }

        Node<T> getNext() {
            return this.next;
        }

        void setElement(T element) {
            this.element = element;
        }

        void setNext(Node<T> next) {
            this.next = next;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;
    /**
     * This method creates a list of provided elements
     *
     * @param elements elements to add
     * @param <T>      generic type
     * @return a new list of elements the were passed as method parameters
     */
    public static <T> LinkedList<T> of(T... elements) {
        LinkedList<T> linkedList = new LinkedList<>();
        for(T element : elements)
            linkedList.add(element);

        return linkedList;
    }

    private Node<T> getNode(int index) {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        Node<T> iterator = head;
        while(index > 0) {
            iterator = iterator.getNext();
            index--;
        }

        return iterator;
    }

    /**
     * Adds an element to the end of the list.
     *
     * @param element element to add
     */
    @Override
    public void add(T element) {
        Node<T> newNode = Node.valueOf(element);

        if (head == null)
            head = tail = newNode;
        else {
            tail.next = newNode;
            tail = tail.next;
        }
        size++;
    }

    /**
     * Adds a new element to the specific position in the list. In case provided index in out of the list bounds it
     * throws {@link IndexOutOfBoundsException}
     *
     * @param index   an index of new element
     * @param element element to add
     */
    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        Node<T> newNode = Node.valueOf(element), beforeNewNode;
        if (index == 0) {
            newNode.setNext(head);
            head = newNode;
        } else {
            beforeNewNode = getNode(index - 1);
            newNode.setNext(beforeNewNode.getNext());
            beforeNewNode.setNext(newNode);
            if (newNode.getNext() == null)
                tail = newNode;
        }
        size++;
    }

    /**
     * Changes the value of an list element at specific position. In case provided index in out of the list bounds it
     * throws {@link IndexOutOfBoundsException}
     *
     * @param index   an position of element to change
     * @param element a new element value
     */
    @Override
    public void set(int index, T element) {
        getNode(index).setElement(element);
    }

    /**
     * Retrieves an elements by its position index. In case provided index in out of the list bounds it
     * throws {@link IndexOutOfBoundsException}
     *
     * @param index element index
     * @return an element value
     */
    @Override
    public T get(int index) {
        return getNode(index).getElement();
    }

    /**
     * Returns the first element of the list. Operation is performed in constant time O(1)
     *
     * @return the first element of the list
     * @throws java.util.NoSuchElementException if list is empty
     */
    @Override
    public T getFirst() {
        if (head == null)
            throw new NoSuchElementException();
        return head.getElement();
    }

    /**
     * Returns the last element of the list. Operation is performed in constant time O(1)
     *
     * @return the last element of the list
     * @throws java.util.NoSuchElementException if list is empty
     */
    @Override
    public T getLast() {
        if (tail == null)
            throw new NoSuchElementException();
        return tail.getElement();
    }

    /**
     * Removes an elements by its position index. In case provided index in out of the list bounds it
     * throws {@link IndexOutOfBoundsException}
     *
     * @param index element index
     * @return deleted element
     */
    @Override
    public T remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        Node<T> deletedNode, beforeDeleteNode;
        if (index == 0) {
            deletedNode = head;
            head = head.getNext();
            if (head == null)
                tail = null;
        } else {
            beforeDeleteNode = getNode(index - 1);
            deletedNode = beforeDeleteNode.getNext();
            beforeDeleteNode.setNext(deletedNode.getNext());

            if (deletedNode.equals(tail))
                tail = beforeDeleteNode.getNext() == null ? beforeDeleteNode : beforeDeleteNode.getNext();
        }
        deletedNode.setNext(null);
        size--;
        return deletedNode.getElement();
    }

    /**
     * Checks if a specific exists in he list
     *
     * @return {@code true} if element exist, {@code false} otherwise
     */
    @Override
    public boolean contains(T element) {
        Node<T> iterator = head;
        while(iterator != null && !iterator.getElement().equals(element))
            iterator = iterator.getNext();
        return iterator != null;
    }

    /**
     * Checks if a list is empty
     *
     * @return {@code true} if list is empty, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns the number of elements in the list
     *
     * @return number of elements
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Removes all list elements
     */
    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }
}
