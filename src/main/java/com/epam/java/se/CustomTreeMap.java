package com.epam.java.se;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class represents an implementation of Custom TreeMap which is
 * a Binary Search Tree with balanced Red-Black Tree put method.
 * <p>
 * Created by Мария on 26.03.2017.
 */
public class CustomTreeMap<K extends Comparable<K>, V> implements Map<K, V> {
    private Node<K, V> root;
    private V savePreviousValue;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    /**
     * Calculates a size of TreeMap
     * Associated methods:
     *
     * @return the number of key-value mappings in this map
     * @see CustomTreeMap#size(Node)
     */
    @Override
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        } else return node.size;
    }

    /**
     * @return true if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * @param key key whose presence in this map is to be tested
     * @return true if this map contains a mapping for
     * the specified key.
     * @throws NullPointerException if the specified key is null.
     */
    @Override
    public boolean containsKey(Object key) {
        Objects.requireNonNull(key);

        if (root == null) {
            return false;
        }
        return find(root, (K) key) != null;
    }

    /**
     * Searches values through entire TreeMap
     * Associated methods:
     *
     * @param value value whose presence in this map is to be tested,
     *              allows null values.
     * @return true specified value is presented in one or more keys
     * @see CustomTreeMap#getLeftValue(Node, Object)
     * @see CustomTreeMap#getRightValue(Node, Object)
     */
    @Override
    public boolean containsValue(Object value) {
        if (root == null) {
            return false;
        }
        if (root.value == null) {
            return value == null;
        }
        return getRightValue(root, (V) value) != null ||
                getLeftValue(root, (V) value) != null;
    }

    private V getRightValue(Node<K, V> node, V value) {
        if (node == null) {
            return null;
        }
        if (!node.value.equals(value)) {
            return getRightValue(node.right, value);
        }
        return node.value;
    }

    private V getLeftValue(Node<K, V> node, V value) {
        if (node == null) {
            return null;
        }
        if (!node.value.equals(value)) {
            return getLeftValue(node.left, value);
        }
        return node.value;
    }

    /**
     * @param key the key whose associated value is to be returned
     * @return value to which the specified key is or null
     * if this map contains no mapping for the key.
     * @throws NullPointerException if the specified key is null.
     */
    @Override
    public V get(Object key) {
        Objects.requireNonNull(key);
        return get(root, (K) key);
    }

    private V get(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        if (node.key.compareTo(key) > 0) {
            return get(node.left, key);
        } else if (node.key.compareTo(key) < 0) {
            return get(node.right, key);
        } else return node.value;
    }

    /**
     * Put key-value pairs in this map. Allows null values.
     * If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.
     * Keys are sorted by concrete rules: if child key is smaller
     * than parent key it will be placed left in tree and visa versa.
     * This method is balanced by implementation of red-black sorting
     * rules, so the height of branches of this map has minimum difference.
     * Associated methods:
     *
     * @param key   key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     * @return the previous value associated with key, or
     * null if there was no mapping for key.
     * @throws NullPointerException if the specified key is null.
     * @see CustomTreeMap#put(Node, Comparable, Object)
     * @see CustomTreeMap#find(Node, Comparable)
     * @see CustomTreeMap#rotateLeft(Node)
     * @see CustomTreeMap#rotateRight(Node)
     * @see CustomTreeMap#flipColors(Node)
     * @see CustomTreeMap#isRed(Node)
     */
    @Override
    public V put(K key, V value) {
        Objects.requireNonNull(key);
        root = put(root, key, value);
        root.color = BLACK;
        if (savePreviousValue !=null){
            return savePreviousValue;
        }
        return null;
    }

    private Node<K, V> put(Node<K, V> node, K key, V value) {
        if (node == null) {
            return new Node<>(key, value, RED, 1);
        }
        if (node.key.equals(key)) {
            savePreviousValue = node.value;
            node.value = value;
        } else if (node.key.compareTo(key) > 0) {
            node.left = put(node.left, key, value);
        } else {
            node.right = put(node.right, key, value);
        }

        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    private Node<K, V> find(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        if (node.key.equals(key)) {
            return node;
        } else if (node.key.compareTo(key) > 0) {
            return find(node.left, key);
        } else {
            return find(node.right, key);
        }
    }

    private Node rotateRight(Node<K, V> node) {
        Node<K, V> newNode = node.left;
        node.left = newNode.right;
        newNode.right = node;
        newNode.color = newNode.right.color;
        newNode.right.color = RED;
        newNode.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return newNode;
    }

    private Node rotateLeft(Node<K, V> node) {
        Node<K, V> newNode = node.right;
        node.right = newNode.left;
        newNode.left = node;
        newNode.color = newNode.left.color;
        newNode.left.color = RED;
        newNode.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return newNode;
    }

    private void flipColors(Node<K, V> node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    private boolean isRed(Node<K, V> node) {
        if (node == null) {
            return false;
        }
        return node.color == RED;
    }

    /**
     * Removes key-value pairs in this map. And than rebuild
     * this map for keep sorting order.
     * Associated methods:
     *
     * @param key key whose mapping is to be removed from the map
     * @return null, there is no way to return old value.
     * @throws NullPointerException if the specified key is null.
     * @see CustomTreeMap#remove(Node, Comparable)
     * @see CustomTreeMap#removeMin(Node)
     * @see CustomTreeMap#findMin(Node)
     */
    @Override
    public V remove(Object key) {
        Objects.requireNonNull(key);
        root = remove(root, (K) key);
        return null;
    }

    private Node remove(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        if (node.key.compareTo(key) > 0) {
            node.left = remove(node.left, key);
        } else if (node.key.compareTo(key) < 0) {
            node.right = remove(node.right, key);
        } else {
            if (node.right == null) {
                return node.left;
            }
            if (node.left == null) {
                return node.right;
            }
            Node temp = node;
            node = findMin(temp.right);
            node.right = removeMin(temp.right);
            node.left = temp.left;
        }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    private Node removeMin(Node<K, V> node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    private Node findMin(Node<K, V> node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node;
        } else return findMin(node.left);
    }

    /**
     * @return common height of this TreeMap
     */
    public int commonHeight() {
        return Math.max(heightOfLeftSubtree(), heightOfRightSubtree());
    }

    /**
     * @return height of left branches of this TreeMap
     */
    public int heightOfLeftSubtree() {
        return leftHeight(root);
    }

    /**
     * @return height of right branches of this TreeMap
     */
    public int heightOfRightSubtree() {
        return rightHeight(root);
    }

    private int leftHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return leftHeight(node.left) + 1;
    }

    private int rightHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return rightHeight(node.right) + 1;
    }

    /**
     * @return true if height of left branches equals or one less
     * than height of right branches and visa versa.
     */
    public boolean isBalanced() {
        if (heightOfLeftSubtree() == heightOfRightSubtree()) {
            return true;
        }
        if (heightOfRightSubtree() - heightOfLeftSubtree() == 1 ||
                (heightOfRightSubtree() - heightOfLeftSubtree() == -1)) {
            return true;
        }
        return false;
    }

    /**
     * Clears entire map.
     */
    @Override
    public void clear() {
        if (root != null) {
            root = null;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    private class Node<K extends Comparable<K>, V> {

        private final K key;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;
        private int size;
        private boolean color;


        public Node(K key, V value, boolean color, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
            this.color = color;
        }
    }
}