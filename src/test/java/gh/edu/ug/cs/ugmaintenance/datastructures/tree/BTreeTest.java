package gh.edu.ug.cs.ugmaintenance.datastructures.tree;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BTreeTest {

    private BTree<Integer> btree;

    @BeforeEach
    public void setUp() {
        btree = new BTree<>(2); // Minimum degree t = 2 (2-3-4 tree)
    }

    @Test
    public void testEmptyTree() {
        assertTrue(btree.isEmpty());
        assertEquals(0, btree.size());
        assertEquals(-1, btree.height());
        assertFalse(btree.contains(10));
        assertNull(btree.search(10));
        assertEquals(0, btree.traversal().size());
    }

    @Test
    public void testInsertAndSearchSmall() {
        btree.insert(10);
        btree.insert(20);
        btree.insert(5);

        assertFalse(btree.isEmpty());
        assertEquals(3, btree.size());
        assertTrue(btree.contains(10));
        assertTrue(btree.contains(20));
        assertTrue(btree.contains(5));
        assertFalse(btree.contains(100));

        assertEquals(10, btree.search(10));
        assertEquals(20, btree.search(20));
        assertEquals(5, btree.search(5));
        assertNull(btree.search(999));
    }

    @Test
    public void testRootSplitting() {
        // For t = 2, max keys in a node is 2t - 1 = 3 keys.
        // Inserting a 4th key triggers a root split.
        btree.insert(10);
        btree.insert(20);
        btree.insert(30);

        assertEquals(3, btree.size());
        assertEquals(0, btree.height()); // Still a single node root

        // Insert 4th element -> root splits into new root (height 1) with 2 children
        btree.insert(40);

        assertEquals(4, btree.size());
        assertEquals(1, btree.height());
        assertNotNull(btree.getRoot());
        assertFalse(btree.getRoot().isLeaf);
        assertEquals(2, btree.getRoot().getChildCount());
    }

    @Test
    public void testInternalNodeSplittingAndMultipleLevels() {
        // Insert values: 10, 20, 30, 40, 50, 60, 70, 80, 90, 100
        int[] values = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        for (int v : values) {
            btree.insert(v);
        }

        assertEquals(10, btree.size());
        assertTrue(btree.height() >= 1);

        // Verify every element can be searched
        for (int v : values) {
            assertTrue(btree.contains(v), "Key " + v + " should be in B-Tree");
            assertEquals(v, btree.search(v));
        }

        // Verify traversal is sorted
        DynamicArray<Integer> traversal = btree.traversal();
        assertEquals(10, traversal.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], traversal.get(i));
        }
    }

    @Test
    public void testDuplicateValues() {
        btree.insert(50);
        btree.insert(30);
        btree.insert(50); // duplicate
        btree.insert(30); // duplicate

        assertEquals(2, btree.size());
        DynamicArray<Integer> traversal = btree.traversal();
        assertEquals(2, traversal.size());
        assertEquals(30, traversal.get(0));
        assertEquals(50, traversal.get(1));
    }

    @Test
    public void testHigherDegreeBTree() {
        // B-Tree with degree t = 3 (each node can hold up to 5 keys, 6 children)
        BTree<Integer> degree3Tree = new BTree<>(3);
        assertEquals(3, degree3Tree.getDegree());

        for (int i = 1; i <= 20; i++) {
            degree3Tree.insert(i * 5);
        }

        assertEquals(20, degree3Tree.size());
        for (int i = 1; i <= 20; i++) {
            assertTrue(degree3Tree.contains(i * 5));
        }

        DynamicArray<Integer> traversal = degree3Tree.traversal();
        assertEquals(20, traversal.size());
        for (int i = 0; i < 20; i++) {
            assertEquals((i + 1) * 5, traversal.get(i));
        }
    }

    @Test
    public void testLargeInsertionSequence() {
        // Insert 100 pseudo-random numbers
        BTree<Integer> largeTree = new BTree<>(3);
        int count = 100;
        int[] data = new int[count];
        for (int i = 0; i < count; i++) {
            data[i] = (i * 37 + 13) % 500;
            largeTree.insert(data[i]);
        }

        // Verify traversal is strictly sorted
        DynamicArray<Integer> traversal = largeTree.traversal();
        assertTrue(traversal.size() > 0);
        for (int i = 0; i < traversal.size() - 1; i++) {
            assertTrue(traversal.get(i) < traversal.get(i + 1),
                    "Traversal must be strictly ascending: " + traversal.get(i) + " vs " + traversal.get(i + 1));
        }

        // Verify all inserted values exist
        for (int v : data) {
            assertTrue(largeTree.contains(v));
        }
    }

    @Test
    public void testStringKeys() {
        BTree<String> stringTree = new BTree<>(2);
        stringTree.insert("Balme Library");
        stringTree.insert("Legon Hall");
        stringTree.insert("Akuafo Hall");
        stringTree.insert("Commonwealth Hall");
        stringTree.insert("Volta Hall");

        assertEquals(5, stringTree.size());
        assertTrue(stringTree.contains("Balme Library"));
        assertTrue(stringTree.contains("Legon Hall"));
        assertFalse(stringTree.contains("Night Market"));

        DynamicArray<String> traversal = stringTree.traversal();
        assertEquals(5, traversal.size());
        // Verify alphabetical order
        for (int i = 0; i < traversal.size() - 1; i++) {
            assertTrue(traversal.get(i).compareTo(traversal.get(i + 1)) < 0);
        }
    }

    @Test
    public void testInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> new BTree<>(1)); // t < 2
        assertThrows(IllegalArgumentException.class, () -> btree.insert(null));
        assertThrows(IllegalArgumentException.class, () -> btree.search(null));
    }

    @Test
    public void testClear() {
        btree.insert(10);
        btree.insert(20);
        btree.insert(30);
        btree.insert(40);

        assertEquals(4, btree.size());
        btree.clear();
        assertEquals(0, btree.size());
        assertTrue(btree.isEmpty());
        assertEquals(-1, btree.height());
        assertNull(btree.getRoot());
    }
}
