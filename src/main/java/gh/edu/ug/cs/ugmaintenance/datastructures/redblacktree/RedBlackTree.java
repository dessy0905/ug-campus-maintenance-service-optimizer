package gh.edu.ug.cs.ugmaintenance.datastructures.redblacktree;

/**
 * Custom implementation of a Red-Black Tree.
 *
 * The tree stores key-value pairs while maintaining balance
 * through recoloring and rotations.
 *
 * Search, insertion and deletion operate in O(log n)
 * when the Red-Black Tree properties are maintained.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class RedBlackTree<K extends Comparable<K>, V> {

    private RedBlackTreeNode<K, V> root;
    private int size;

    /**
     * Creates an empty Red-Black Tree.
     */
    public RedBlackTree() {
        this.root = null;
        this.size = 0;
    }


    // =========================================================
    // BASIC OPERATIONS
    // =========================================================

    /**
     * Returns the number of elements in the tree.
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether the tree is empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }


    // =========================================================
    // INSERTION
    // =========================================================

    /**
     * Inserts a key-value pair into the tree.
     *
     * If the key already exists, its value is updated.
     *
     * @param key   key to insert
     * @param value value associated with the key
     */
    public void put(K key, V value) {

        requireKey(key);

        // First node becomes the root
        if (root == null) {

            root = new RedBlackTreeNode<>(key, value);

            root.color = RedBlackTreeNode.BLACK;

            size = 1;

            return;
        }

        RedBlackTreeNode<K, V> current = root;
        RedBlackTreeNode<K, V> parent = null;

        int comparison = 0;

        /*
         * Perform normal Binary Search Tree insertion
         * to find the correct position.
         */
        while (current != null) {

            parent = current;

            comparison = key.compareTo(current.key);

            // Key already exists - update its value
            if (comparison == 0) {

                current.value = value;

                return;
            }

            if (comparison < 0) {

                current = current.left;

            } else {

                current = current.right;
            }
        }

        // Create the new RED node
        RedBlackTreeNode<K, V> newNode =
                new RedBlackTreeNode<>(key, value);

        newNode.parent = parent;

        if (comparison < 0) {

            parent.left = newNode;

        } else {

            parent.right = newNode;
        }

        size++;

        // Repair any Red-Black Tree violations
        fixAfterInsertion(newNode);
    }


    // =========================================================
    // SEARCHING
    // =========================================================

    /**
     * Returns the value associated with a key.
     *
     * @param key key to search for
     * @return value associated with the key or null if not found
     */
    public V get(K key) {

        requireKey(key);

        RedBlackTreeNode<K, V> node =
                searchNode(key);

        return node == null
                ? null
                : node.value;
    }

    /**
     * Checks whether a key exists in the tree.
     */
    public boolean containsKey(K key) {

        requireKey(key);

        return searchNode(key) != null;
    }

    /**
     * Searches for a node using Binary Search Tree logic.
     */
    private RedBlackTreeNode<K, V> searchNode(K key) {

        RedBlackTreeNode<K, V> current =
                root;

        while (current != null) {

            int comparison =
                    key.compareTo(current.key);

            if (comparison == 0) {

                return current;
            }

            if (comparison < 0) {

                current = current.left;

            } else {

                current = current.right;
            }
        }

        return null;
    }


    // =========================================================
    // DELETION
    // =========================================================

    /**
     * Removes a key from the tree.
     *
     * @param key key to remove
     * @return removed value or null if the key does not exist
     */
    public V remove(K key) {

        requireKey(key);

        RedBlackTreeNode<K, V> node =
                searchNode(key);

        if (node == null) {

            return null;
        }

        V removedValue =
                node.value;

        deleteNode(node);

        size--;

        return removedValue;
    }

    /**
     * Deletes a node and repairs the tree when necessary.
     */
    private void deleteNode(
            RedBlackTreeNode<K, V> node) {

        /*
         * If the node has two children,
         * replace its data with its successor.
         */
        if (node.left != null
                && node.right != null) {

            RedBlackTreeNode<K, V> successor =
                    minimumNode(node.right);

            node.key = successor.key;
            node.value = successor.value;

            node = successor;
        }

        /*
         * After replacing a two-child node,
         * node now has at most one child.
         */
        RedBlackTreeNode<K, V> replacement =
                node.left != null
                        ? node.left
                        : node.right;


        // -----------------------------------------------------
        // CASE 1: Node has one child
        // -----------------------------------------------------

        if (replacement != null) {

            replacement.parent =
                    node.parent;

            if (node.parent == null) {

                root = replacement;

            } else if (node
                    == node.parent.left) {

                node.parent.left =
                        replacement;

            } else {

                node.parent.right =
                        replacement;
            }

            // Disconnect removed node
            node.left = null;
            node.right = null;
            node.parent = null;

            /*
             * Removing a BLACK node may violate
             * Red-Black Tree properties.
             */
            if (node.color
                    == RedBlackTreeNode.BLACK) {

                fixAfterDeletion(
                        replacement
                );
            }
        }


        // -----------------------------------------------------
        // CASE 2: Node is root with no children
        // -----------------------------------------------------

        else if (node.parent == null) {

            root = null;
        }


        // -----------------------------------------------------
        // CASE 3: Node is a leaf
        // -----------------------------------------------------

        else {

            /*
             * A BLACK leaf requires balancing.
             *
             * The node temporarily remains connected
             * while fixAfterDeletion() runs.
             */
            if (node.color
                    == RedBlackTreeNode.BLACK) {

                fixAfterDeletion(node);
            }

            // Remove leaf from parent
            if (node.parent != null) {

                if (node
                        == node.parent.left) {

                    node.parent.left = null;

                } else if (node
                        == node.parent.right) {

                    node.parent.right = null;
                }

                node.parent = null;
            }
        }
    }


    // =========================================================
    // INSERTION BALANCING
    // =========================================================

    /**
     * Repairs Red-Black Tree properties after insertion.
     */
    private void fixAfterInsertion(
            RedBlackTreeNode<K, V> node) {

        node.color =
                RedBlackTreeNode.RED;

        /*
         * A violation occurs when a RED node
         * has a RED parent.
         */
        while (node != null
                && node != root
                && colorOf(parentOf(node))
                == RedBlackTreeNode.RED) {


            /*
             * Parent is LEFT child
             * of grandparent.
             */
            if (parentOf(node)
                    == leftOf(
                    parentOf(
                            parentOf(node)))) {

                RedBlackTreeNode<K, V> uncle =
                        rightOf(
                                parentOf(
                                        parentOf(node)));


                // ---------------------------------------------
                // CASE 1: Uncle is RED
                // ---------------------------------------------

                if (colorOf(uncle)
                        == RedBlackTreeNode.RED) {

                    setColor(
                            parentOf(node),
                            RedBlackTreeNode.BLACK
                    );

                    setColor(
                            uncle,
                            RedBlackTreeNode.BLACK
                    );

                    setColor(
                            parentOf(
                                    parentOf(node)),
                            RedBlackTreeNode.RED
                    );

                    node =
                            parentOf(
                                    parentOf(node));
                }


                // ---------------------------------------------
                // Uncle is BLACK
                // ---------------------------------------------

                else {

                    /*
                     * CASE 2:
                     *
                     * Node is RIGHT child.
                     *
                     * Convert triangle into
                     * straight-line configuration.
                     */
                    if (node
                            == rightOf(
                            parentOf(node))) {

                        node =
                                parentOf(node);

                        leftRotate(node);
                    }


                    /*
                     * CASE 3:
                     *
                     * Recolor and rotate
                     * the grandparent.
                     */
                    setColor(
                            parentOf(node),
                            RedBlackTreeNode.BLACK
                    );

                    setColor(
                            parentOf(
                                    parentOf(node)),
                            RedBlackTreeNode.RED
                    );

                    rightRotate(
                            parentOf(
                                    parentOf(node))
                    );
                }
            }


            /*
             * Parent is RIGHT child
             * of grandparent.
             *
             * Mirror of previous cases.
             */
            else {

                RedBlackTreeNode<K, V> uncle =
                        leftOf(
                                parentOf(
                                        parentOf(node)));


                // ---------------------------------------------
                // CASE 1: Uncle is RED
                // ---------------------------------------------

                if (colorOf(uncle)
                        == RedBlackTreeNode.RED) {

                    setColor(
                            parentOf(node),
                            RedBlackTreeNode.BLACK
                    );

                    setColor(
                            uncle,
                            RedBlackTreeNode.BLACK
                    );

                    setColor(
                            parentOf(
                                    parentOf(node)),
                            RedBlackTreeNode.RED
                    );

                    node =
                            parentOf(
                                    parentOf(node));
                }


                // ---------------------------------------------
                // Uncle is BLACK
                // ---------------------------------------------

                else {

                    /*
                     * CASE 2:
                     *
                     * Node is LEFT child.
                     */
                    if (node
                            == leftOf(
                            parentOf(node))) {

                        node =
                                parentOf(node);

                        rightRotate(node);
                    }


                    /*
                     * CASE 3
                     */
                    setColor(
                            parentOf(node),
                            RedBlackTreeNode.BLACK
                    );

                    setColor(
                            parentOf(
                                    parentOf(node)),
                            RedBlackTreeNode.RED
                    );

                    leftRotate(
                            parentOf(
                                    parentOf(node))
                    );
                }
            }
        }

        // Root must always be BLACK
        setColor(
                root,
                RedBlackTreeNode.BLACK
        );
    }


    // =========================================================
    // DELETION BALANCING
    // =========================================================

    /**
     * Repairs Red-Black Tree properties after deletion.
     */
    private void fixAfterDeletion(
            RedBlackTreeNode<K, V> node) {

        while (node != root
                && colorOf(node)
                == RedBlackTreeNode.BLACK) {


            /*
             * Node is LEFT child.
             */
            if (node
                    == leftOf(
                    parentOf(node))) {

                RedBlackTreeNode<K, V> sibling =
                        rightOf(
                                parentOf(node));


                // ---------------------------------------------
                // CASE 1:
                // Sibling is RED
                // ---------------------------------------------

                if (colorOf(sibling)
                        == RedBlackTreeNode.RED) {

                    setColor(
                            sibling,
                            RedBlackTreeNode.BLACK
                    );

                    setColor(
                            parentOf(node),
                            RedBlackTreeNode.RED
                    );

                    leftRotate(
                            parentOf(node)
                    );

                    sibling =
                            rightOf(
                                    parentOf(node));
                }


                // ---------------------------------------------
                // CASE 2:
                // Sibling and its children are BLACK
                // ---------------------------------------------

                if (colorOf(
                        leftOf(sibling))
                        == RedBlackTreeNode.BLACK

                        &&

                        colorOf(
                                rightOf(sibling))
                                == RedBlackTreeNode.BLACK) {

                    setColor(
                            sibling,
                            RedBlackTreeNode.RED
                    );

                    node =
                            parentOf(node);
                }


                else {

                    // -----------------------------------------
                    // CASE 3:
                    // Sibling's far child is BLACK
                    // -----------------------------------------

                    if (colorOf(
                            rightOf(sibling))
                            == RedBlackTreeNode.BLACK) {

                        setColor(
                                leftOf(sibling),
                                RedBlackTreeNode.BLACK
                        );

                        setColor(
                                sibling,
                                RedBlackTreeNode.RED
                        );

                        rightRotate(sibling);

                        sibling =
                                rightOf(
                                        parentOf(node));
                    }


                    // -----------------------------------------
                    // CASE 4
                    // -----------------------------------------

                    setColor(
                            sibling,
                            colorOf(
                                    parentOf(node))
                    );

                    setColor(
                            parentOf(node),
                            RedBlackTreeNode.BLACK
                    );

                    setColor(
                            rightOf(sibling),
                            RedBlackTreeNode.BLACK
                    );

                    leftRotate(
                            parentOf(node)
                    );

                    node = root;
                }
            }


            /*
             * Node is RIGHT child.
             *
             * Mirror of previous cases.
             */
            else {

                RedBlackTreeNode<K, V> sibling =
                        leftOf(
                                parentOf(node));


                // ---------------------------------------------
                // CASE 1:
                // Sibling is RED
                // ---------------------------------------------

                if (colorOf(sibling)
                        == RedBlackTreeNode.RED) {

                    setColor(
                            sibling,
                            RedBlackTreeNode.BLACK
                    );

                    setColor(
                            parentOf(node),
                            RedBlackTreeNode.RED
                    );

                    rightRotate(
                            parentOf(node)
                    );

                    sibling =
                            leftOf(
                                    parentOf(node));
                }


                // ---------------------------------------------
                // CASE 2
                // ---------------------------------------------

                if (colorOf(
                        rightOf(sibling))
                        == RedBlackTreeNode.BLACK

                        &&

                        colorOf(
                                leftOf(sibling))
                                == RedBlackTreeNode.BLACK) {

                    setColor(
                            sibling,
                            RedBlackTreeNode.RED
                    );

                    node =
                            parentOf(node);
                }


                else {

                    // -----------------------------------------
                    // CASE 3
                    // -----------------------------------------

                    if (colorOf(
                            leftOf(sibling))
                            == RedBlackTreeNode.BLACK) {

                        setColor(
                                rightOf(sibling),
                                RedBlackTreeNode.BLACK
                        );

                        setColor(
                                sibling,
                                RedBlackTreeNode.RED
                        );

                        leftRotate(sibling);

                        sibling =
                                leftOf(
                                        parentOf(node));
                    }


                    // -----------------------------------------
                    // CASE 4
                    // -----------------------------------------

                    setColor(
                            sibling,
                            colorOf(
                                    parentOf(node))
                    );

                    setColor(
                            parentOf(node),
                            RedBlackTreeNode.BLACK
                    );

                    setColor(
                            leftOf(sibling),
                            RedBlackTreeNode.BLACK
                    );

                    rightRotate(
                            parentOf(node)
                    );

                    node = root;
                }
            }
        }

        setColor(
                node,
                RedBlackTreeNode.BLACK
        );
    }


    // =========================================================
    // ROTATIONS
    // =========================================================

    /**
     * Performs a left rotation around a node.
     */
    private void leftRotate(
            RedBlackTreeNode<K, V> node) {

        if (node == null) {
            return;
        }

        RedBlackTreeNode<K, V> rightChild =
                node.right;

        if (rightChild == null) {
            return;
        }

        /*
         * Move right child's left subtree
         * to node's right side.
         */
        node.right =
                rightChild.left;

        if (rightChild.left != null) {

            rightChild.left.parent =
                    node;
        }

        /*
         * Connect right child
         * to node's previous parent.
         */
        rightChild.parent =
                node.parent;

        if (node.parent == null) {

            root = rightChild;

        } else if (node
                == node.parent.left) {

            node.parent.left =
                    rightChild;

        } else {

            node.parent.right =
                    rightChild;
        }

        /*
         * Move node underneath
         * right child.
         */
        rightChild.left =
                node;

        node.parent =
                rightChild;
    }


    /**
     * Performs a right rotation around a node.
     */
    private void rightRotate(
            RedBlackTreeNode<K, V> node) {

        if (node == null) {
            return;
        }

        RedBlackTreeNode<K, V> leftChild =
                node.left;

        if (leftChild == null) {
            return;
        }

        /*
         * Move left child's right subtree
         * to node's left side.
         */
        node.left =
                leftChild.right;

        if (leftChild.right != null) {

            leftChild.right.parent =
                    node;
        }

        /*
         * Connect left child
         * to node's previous parent.
         */
        leftChild.parent =
                node.parent;

        if (node.parent == null) {

            root = leftChild;

        } else if (node
                == node.parent.right) {

            node.parent.right =
                    leftChild;

        } else {

            node.parent.left =
                    leftChild;
        }

        /*
         * Move node underneath
         * left child.
         */
        leftChild.right =
                node;

        node.parent =
                leftChild;
    }


    // =========================================================
    // MINIMUM AND MAXIMUM
    // =========================================================

    /**
     * Returns the smallest key.
     */
    public K minKey() {

        if (root == null) {

            return null;
        }

        return minimumNode(root).key;
    }


    /**
     * Returns the largest key.
     */
    public K maxKey() {

        if (root == null) {

            return null;
        }

        RedBlackTreeNode<K, V> current =
                root;

        while (current.right != null) {

            current =
                    current.right;
        }

        return current.key;
    }


    /**
     * Finds the smallest node in a subtree.
     */
    private RedBlackTreeNode<K, V> minimumNode(
            RedBlackTreeNode<K, V> node) {

        RedBlackTreeNode<K, V> current =
                node;

        while (current.left != null) {

            current =
                    current.left;
        }

        return current;
    }


    // =========================================================
    // HEIGHT
    // =========================================================

    /**
     * Returns the height of the tree.
     */
    public int height() {

        return height(root);
    }

    private int height(
            RedBlackTreeNode<K, V> node) {

        if (node == null) {

            return 0;
        }

        int leftHeight =
                height(node.left);

        int rightHeight =
                height(node.right);

        return 1
                + Math.max(
                leftHeight,
                rightHeight
        );
    }


    // =========================================================
    // CLEAR
    // =========================================================

    /**
     * Removes all entries from the tree.
     */
    public void clear() {

        root = null;

        size = 0;
    }


    // =========================================================
    // TRAVERSALS
    // =========================================================

    /**
     * Prints the keys in sorted order.
     */
    public void inOrderTraversal() {

        inOrderTraversal(root);

        System.out.println();
    }

    private void inOrderTraversal(
            RedBlackTreeNode<K, V> node) {

        if (node == null) {

            return;
        }

        inOrderTraversal(node.left);

        System.out.print(
                node.key + " "
        );

        inOrderTraversal(node.right);
    }


    /**
     * Prints root-left-right.
     */
    public void preOrderTraversal() {

        preOrderTraversal(root);

        System.out.println();
    }

    private void preOrderTraversal(
            RedBlackTreeNode<K, V> node) {

        if (node == null) {

            return;
        }

        System.out.print(
                node.key + " "
        );

        preOrderTraversal(node.left);

        preOrderTraversal(node.right);
    }


    /**
     * Prints left-right-root.
     */
    public void postOrderTraversal() {

        postOrderTraversal(root);

        System.out.println();
    }

    private void postOrderTraversal(
            RedBlackTreeNode<K, V> node) {

        if (node == null) {

            return;
        }

        postOrderTraversal(node.left);

        postOrderTraversal(node.right);

        System.out.print(
                node.key + " "
        );
    }


    // =========================================================
    // TREE VISUALIZATION
    // =========================================================

    /**
     * Prints the tree together with node colors.
     */
    public void printTree() {

        if (root == null) {

            System.out.println(
                    "Tree is empty."
            );

            return;
        }

        printTree(
                root,
                "",
                true
        );
    }

    private void printTree(
            RedBlackTreeNode<K, V> node,
            String indent,
            boolean isRight) {

        if (node == null) {

            return;
        }

        System.out.println(
                indent
                        + (isRight
                        ? "R----"
                        : "L----")
                        + node.key
                        + "("
                        + (node.color
                        == RedBlackTreeNode.RED
                        ? "RED"
                        : "BLACK")
                        + ")"
        );

        printTree(
                node.left,
                indent + "     ",
                false
        );

        printTree(
                node.right,
                indent + "     ",
                true
        );
    }


    // =========================================================
    // RED-BLACK HELPER METHODS
    // =========================================================

    /**
     * Null nodes are considered BLACK.
     */
    private boolean colorOf(
            RedBlackTreeNode<K, V> node) {

        return node == null
                ? RedBlackTreeNode.BLACK
                : node.color;
    }


    private RedBlackTreeNode<K, V> parentOf(
            RedBlackTreeNode<K, V> node) {

        return node == null
                ? null
                : node.parent;
    }


    private RedBlackTreeNode<K, V> leftOf(
            RedBlackTreeNode<K, V> node) {

        return node == null
                ? null
                : node.left;
    }


    private RedBlackTreeNode<K, V> rightOf(
            RedBlackTreeNode<K, V> node) {

        return node == null
                ? null
                : node.right;
    }


    private void setColor(
            RedBlackTreeNode<K, V> node,
            boolean color) {

        if (node != null) {

            node.color = color;
        }
    }


    // =========================================================
    // VALIDATION
    // =========================================================

    /**
     * Prevents null keys from being inserted or searched.
     */
    private void requireKey(K key) {

        if (key == null) {

            throw new IllegalArgumentException(
                    "Key cannot be null."
            );
        }
    }
}