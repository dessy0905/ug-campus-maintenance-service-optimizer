package gh.edu.ug.cs.ugmaintenance.datastructures.redblacktree;

import org.junit.Test;

import static org.junit.Assert.*;

public class RedBlackTreeTest {

    // =========================================================
    // EMPTY TREE TESTS
    // =========================================================

    @Test
    public void testNewTreeIsEmpty() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
    }


    @Test
    public void testGetFromEmptyTree() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        assertNull(tree.get(100));
    }


    @Test
    public void testRemoveFromEmptyTree() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        assertNull(tree.remove(100));
    }


    @Test
    public void testMinKeyOnEmptyTree() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        assertNull(tree.minKey());
    }


    @Test
    public void testMaxKeyOnEmptyTree() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        assertNull(tree.maxKey());
    }


    // =========================================================
    // INSERTION TESTS
    // =========================================================

    @Test
    public void testInsertSingleElement() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(
                1042,
                "Broken pipe - Legon Hall"
        );

        assertEquals(1, tree.size());

        assertFalse(tree.isEmpty());

        assertEquals(
                "Broken pipe - Legon Hall",
                tree.get(1042)
        );
    }


    @Test
    public void testInsertMultipleElements() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(30, "Request 30");
        tree.put(10, "Request 10");
        tree.put(50, "Request 50");
        tree.put(20, "Request 20");
        tree.put(40, "Request 40");

        assertEquals(5, tree.size());

        assertEquals(
                "Request 10",
                tree.get(10)
        );

        assertEquals(
                "Request 40",
                tree.get(40)
        );
    }


    @Test
    public void testDuplicateKeyUpdatesValue() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(
                1042,
                "Broken pipe"
        );

        tree.put(
                1042,
                "Broken pipe repaired"
        );

        assertEquals(1, tree.size());

        assertEquals(
                "Broken pipe repaired",
                tree.get(1042)
        );
    }


    // =========================================================
    // SEARCH TESTS
    // =========================================================

    @Test
    public void testContainsExistingKey() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(
                1015,
                "Electrical fault"
        );

        assertTrue(
                tree.containsKey(1015)
        );
    }


    @Test
    public void testContainsMissingKey() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(
                1015,
                "Electrical fault"
        );

        assertFalse(
                tree.containsKey(5000)
        );
    }


    @Test
    public void testGetMissingKeyReturnsNull() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(
                100,
                "Request"
        );

        assertNull(
                tree.get(999)
        );
    }


    // =========================================================
    // MINIMUM AND MAXIMUM
    // =========================================================

    @Test
    public void testMinimumKey() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(50, "A");
        tree.put(20, "B");
        tree.put(70, "C");
        tree.put(10, "D");
        tree.put(40, "E");

        assertEquals(
                Integer.valueOf(10),
                tree.minKey()
        );
    }


    @Test
    public void testMaximumKey() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(50, "A");
        tree.put(20, "B");
        tree.put(70, "C");
        tree.put(10, "D");
        tree.put(90, "E");

        assertEquals(
                Integer.valueOf(90),
                tree.maxKey()
        );
    }


    // =========================================================
    // DELETION TESTS
    // =========================================================

    @Test
    public void testRemoveExistingElement() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(
                1042,
                "Broken pipe"
        );

        String removed =
                tree.remove(1042);

        assertEquals(
                "Broken pipe",
                removed
        );

        assertEquals(
                0,
                tree.size()
        );

        assertFalse(
                tree.containsKey(1042)
        );
    }


    @Test
    public void testRemoveNonExistingElement() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(100, "A");

        String removed =
                tree.remove(500);

        assertNull(removed);

        assertEquals(
                1,
                tree.size()
        );
    }


    @Test
    public void testDeleteLeafNode() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(20, "A");
        tree.put(10, "B");
        tree.put(30, "C");

        tree.remove(10);

        assertFalse(
                tree.containsKey(10)
        );

        assertEquals(
                2,
                tree.size()
        );
    }


    @Test
    public void testDeleteNodeWithOneChild() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(20, "A");
        tree.put(10, "B");
        tree.put(30, "C");
        tree.put(25, "D");

        tree.remove(30);

        assertFalse(
                tree.containsKey(30)
        );

        assertTrue(
                tree.containsKey(25)
        );

        assertEquals(
                3,
                tree.size()
        );
    }


    @Test
    public void testDeleteNodeWithTwoChildren() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(40, "A");
        tree.put(20, "B");
        tree.put(60, "C");
        tree.put(50, "D");
        tree.put(70, "E");

        tree.remove(60);

        assertFalse(
                tree.containsKey(60)
        );

        assertTrue(
                tree.containsKey(50)
        );

        assertTrue(
                tree.containsKey(70)
        );

        assertEquals(
                4,
                tree.size()
        );
    }


    @Test
    public void testDeleteRoot() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(20, "Root");
        tree.put(10, "Left");
        tree.put(30, "Right");

        tree.remove(20);

        assertFalse(
                tree.containsKey(20)
        );

        assertEquals(
                2,
                tree.size()
        );

        assertTrue(
                tree.containsKey(10)
        );

        assertTrue(
                tree.containsKey(30)
        );
    }


    // =========================================================
    // HEIGHT
    // =========================================================

    @Test
    public void testHeightOfEmptyTree() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        assertEquals(
                0,
                tree.height()
        );
    }


    @Test
    public void testHeightOfSingleNode() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(
                10,
                "A"
        );

        assertEquals(
                1,
                tree.height()
        );
    }


    @Test
    public void testTreeDoesNotBecomeLinear() {

        RedBlackTree<Integer, Integer> tree =
                new RedBlackTree<>();

        /*
         * Insert vaslues in sorted order.
         *
         * A normal unbalanced BST could
         * become height 100 here.
         */
        for (int i = 1; i <= 100; i++) {

            tree.put(i, i);
        }

        /*
         * A valid Red-Black Tree should
         * remain dramatically shorter.
         */
        assertTrue(
                tree.height() < 20
        );
    }


    // =========================================================
    // CLEAR
    // =========================================================

    @Test
    public void testClearTree() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        tree.put(10, "A");
        tree.put(20, "B");
        tree.put(30, "C");

        tree.clear();

        assertTrue(
                tree.isEmpty()
        );

        assertEquals(
                0,
                tree.size()
        );

        assertNull(
                tree.get(10)
        );

        assertNull(
                tree.minKey()
        );

        assertNull(
                tree.maxKey()
        );
    }


    // =========================================================
    // INVALID INPUT
    // =========================================================

    @Test
    public void testNullKeyInsertionThrowsException() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> tree.put(
                        null,
                        "Invalid"
                )
        );
    }


    @Test
    public void testNullKeySearchThrowsException() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> tree.get(null)
        );
    }


    @Test
    public void testNullKeyRemovalThrowsException() {

        RedBlackTree<Integer, String> tree =
                new RedBlackTree<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> tree.remove(null)
        );
    }


    // =========================================================
    // LARGE DATA TEST
    // =========================================================

    @Test
    public void testLargeNumberOfInsertions() {

        RedBlackTree<Integer, Integer> tree =
                new RedBlackTree<>();

        for (int i = 1; i <= 10000; i++) {

            tree.put(i, i);
        }

        assertEquals(
                10000,
                tree.size()
        );

        assertEquals(
                Integer.valueOf(1),
                tree.minKey()
        );

        assertEquals(
                Integer.valueOf(10000),
                tree.maxKey()
        );

        assertEquals(
                Integer.valueOf(5000),
                tree.get(5000)
        );
    }


    @Test
    public void testLargeNumberOfDeletions() {

        RedBlackTree<Integer, Integer> tree =
                new RedBlackTree<>();

        for (int i = 1; i <= 1000; i++) {

            tree.put(i, i);
        }

        for (int i = 1; i <= 500; i++) {

            tree.remove(i);
        }

        assertEquals(
                500,
                tree.size()
        );

        for (int i = 1; i <= 500; i++) {

            assertFalse(
                    tree.containsKey(i)
            );
        }

        for (int i = 501; i <= 1000; i++) {

            assertTrue(
                    tree.containsKey(i)
            );
        }
    }
}