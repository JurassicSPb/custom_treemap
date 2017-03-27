package com.epam.java.se;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Мария on 26.03.2017.
 */
public class CustomTreeMap<K extends Comparable<K>, V> implements Map<K, V> {
    private Node<K, V> root;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        } else return node.size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean containsKey(Object key) {
        Objects.requireNonNull(key);

        if (root == null) {
            return false;
        }
        return find(root, (K) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (root == null) return false;
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

    @Override
    public V put(K key, V value) {
        Objects.requireNonNull(key);
        root = put(root, key, value);
        root.color = BLACK;
        return value;
    }

    private Node<K, V> put(Node<K, V> node, K key, V value) {
        if (node == null) {
            return new Node<>(key, value, RED, 1);
        }
        if (node.key.equals(key)) {
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

    private Node rotateRight(Node node) {
        Node newNode = node.left;
        node.left = newNode.right;
        newNode.right = node;
        newNode.color = newNode.right.color;
        newNode.right.color = RED;
        newNode.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return newNode;
    }

    private Node rotateLeft(Node node) {
        Node newNode = node.right;
        node.right = newNode.left;
        newNode.left = node;
        newNode.color = newNode.left.color;
        newNode.left.color = RED;
        newNode.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return newNode;
    }

    private void flipColors(Node node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }
        return node.color == RED;
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