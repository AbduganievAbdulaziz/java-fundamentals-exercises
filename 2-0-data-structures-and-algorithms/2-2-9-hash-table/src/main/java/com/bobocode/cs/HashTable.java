package com.bobocode.cs;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

/**
 * {@link HashTable} is a simple Hashtable-based implementation of {@link Map} interface with some additional methods.
 * It is based on the array of {@link Node} objects. Both {@link HashTable} and {@link Node} have two type parameters:
 * K and V, which represent key and value.
 * <p>
 * Elements are stored int the table by their key. A table is basically an array, and fast access is possible due to
 * array capabilities. (You can access an array element by its index in O(1) time). In order to find an index for any
 * given key, it uses calculateIndex method which is based on the element's hash code.
 * <p>
 * If two elements (keys) have the same array index, they form a linked list. That's why class {@link Node} requires
 * a reference to the next field.
 * <p>
 * Since you don't always know the number of elements in advance, the table can be resized. You can do that manually by
 * calling method resizeTable, or it will be done automatically once the table reach resize threshold.
 * <p>
 * The initial array size (initial capacity) is 8.
 * <p><p>
 * <strong>TODO: to get the most out of your learning, <a href="https://www.bobocode.com">visit our website</a></strong>
 * <p>
 *
 * @param <K> key type
 * @param <V> value type
 * @author Taras Boychuk
 */
public class HashTable<K, V> implements Map<K, V> {
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int INITIAL_CAPACITY = 8;
    private Node<K, V>[] table;
    private int size;

    public HashTable() {
        this(INITIAL_CAPACITY);
    }

    public HashTable(int initialCapacity) {
        if (initialCapacity <= 0)
            throw new IllegalArgumentException();
        table = new Node[initialCapacity];
    }
    /**
     * This method is a critical part of the hast table. The main idea is that having a key, you can calculate its index
     * in the array using the hash code. Since the computation is done in constant time (O(1)), it's faster than
     * any other kind search.
     * <p>
     * It's a function that accepts a key and calculates its index using a hash code. Please note that index cannot be
     * equal or greater than array size (table capacity).
     * <p>
     * This method is used for all other operations (put, get, remove).
     *
     * @param key
     * @param tableCapacity underlying array size
     * @return array index of the given key
     */
    public static int calculateIndex(Object key, int tableCapacity) {
        int index = key.hashCode()%tableCapacity;
        if (index < 0)
            index += tableCapacity;

        return index;
    }

    private Node<K, V> getNodeByKey(Node<K, V> head, K key) {
        while(head != null) {
            if (head.key.equals(key))
                return head;
            head = head.next;
        }
        return null;
    }

    /**
     * Creates a mapping between provided key and value, and returns the old value. If there was no such key, it returns
     * null. {@link HashTable} does not support duplicate keys, so if you put the same key it just overrides the value.
     * <p>
     * It uses calculateIndex method to find the corresponding array index. Please note, that even different keys can
     * produce the same array index.
     *
     * @param key
     * @param value
     * @return old value or null
     */
    @Override
    public V put(K key, V value) {
        requireNonNull(key);
        requireNonNull(value);

        if (size == table.length)
            resizeTable(2 * size);

        int index = calculateIndex(key, table.length);
        Node<K, V> oldNode = getNodeByKey(table[index], key);
        V oldValue = null;
        if (oldNode != null) {
            oldValue = oldNode.value;
            oldNode.value = value;
        } else {
            oldNode = table[index];
            table[index] = new Node<>(key, value);
            table[index].next = oldNode;
            size++;
        }
        return oldValue;
    }

    /**
     * Retrieves a value by the given key. It uses calculateIndex method to find the corresponding array index.
     * Then it iterates though all elements that are stored by that index, and uses equals to compare its keys.
     *
     * @param key
     * @return value stored in the table by the given key or null if there is no such key
     */
    @Override
    public V get(K key) {
        int index = calculateIndex(requireNonNull(key), table.length);
        Node<K, V> node = getNodeByKey(table[index], key);
        return node == null ? null : node.value;
    }

    /**
     * Checks if the table contains a given key.
     *
     * @param key
     * @return true is there is such key in the table or false otherwise
     */
    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Checks if the table contains a given value.
     *
     * @param value
     * @return true is there is such value in the table or false otherwise
     */
    @Override
    public boolean containsValue(V value) {
        for (var listNode : table) {
            while(listNode != null) {
                if (listNode.value.equals(value))
                    return true;
                listNode = listNode.next;
            }
        }
        return false;
    }

    /**
     * Return a number of elements in the table.
     *
     * @return size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks is the table is empty.
     *
     * @return true is table size is zero or false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes an element by its key and returns a removed value. If there is no such key in the table, it returns null.
     *
     * @param key
     * @return removed value or null
     */
    @Override
    public V remove(K key) {
        int index = calculateIndex(key, table.length);
        var iterator = table[index];
        if (iterator == null)
            return null;
        Node<K, V> deletedNode;
        if (iterator.key.equals(key)) {
            deletedNode = iterator;
        } else {
            while(iterator.next != null && !iterator.next.key.equals(key))
                iterator = iterator.next;
            if (iterator.next == null)
                return null;
            deletedNode = iterator.next;
        }
        iterator.next = deletedNode.next;
        deletedNode.next = null;
        size--;
        return deletedNode.value;
    }

    /**
     * It's a special toString method dedicated to help you visualize a hash table. It creates a string that represents
     * an underlying array as a table. It has multiples rows. Every row starts with an array index followed by ": ".
     * Then it adds every key and value (key=value) that have a corresponding index. Every "next" reference is
     * represented as an arrow like this " -> ".
     * <p>
     * E.g. imagine a table, where the key is a string username, and the value is the number of points of that user.
     * Is this case method toString can return something like this:
     * <pre>
     * 0: johnny=439
     * 1:
     * 2: madmax=833 -> leon=886
     * 3:
     * 4: altea=553
     * 5:
     * 6:
     * 7:
     * </pre>
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> arrayList;
        for (int i = 0; i < table.length; i++) {
            stringBuilder.append(String.format("%d: ", i));
            arrayList = new ArrayList<>();
            Node<K, V> iterator = table[i];
            while (iterator != null) {
                arrayList.add(String.format("%s=%d", iterator.key, iterator.value));
                iterator = iterator.next;
            }
            stringBuilder.append(String.join(" -> ", arrayList)).append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Creates a new underlying table with a given size and adds all elements to the new table.
     * <p>
     * In order to allow a fast access, this hash table needs to have a sufficient capacity.
     * (You can imagine a hash table, with a default capacity of 8 that stores hundreds of thousands of elements.
     * In that case it's just 8 huge linked lists. That's why we need this method.)
     * <p>
     * PLEASE NOTE that such method <strong>should not be a part of the public API</strong>, but it was made public
     * for learning purposes. You can create a table, print it using toString, then resizeTable and print it again.
     * It will help you to understand how it works.
     *
     * @param newCapacity a size of the new underlying array
     */
    public void resizeTable(int newCapacity) {
        HashTable<K, V> newHashTable = new HashTable<>(newCapacity);

        for (int i = 0; i < table.length; i++) {
            var node = table[i];
            while(node != null) {
                newHashTable.put(node.key, node.value);
                node = node.next;
            }
        }
        this.table = newHashTable.table;
    }
}