package gh.edu.ug.cs.ugmaintenance.datastructures.tree;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BinarySearchTreeTest {

    private BinarySearchTree<Integer> bst;

    @BeforeEach
    public void setUp() {
        bst = new BinarySearchTree<>();
    }

    @Test
    public void testEmptyTree() {
        assertTrue(bst.isEmpty());
        assertEquals(0, bst.size());
        assertEquals(-1, bst.height());
        assertFalse(bst.contains(10));
        assertNull(bst.search(10));
        assertFalse(bst.delete(10));

        assertThrows(IllegalStateException.class, () -> bst.min());
        assertThrows(IllegalStateException.class, () -> bst.max());
    }

    @Test
    public void testInsertAndSize() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);
        bst.insert(40);
        bst.insert(60);
        bst.insert(80);

        assertFalse(bst.isEmpty());
        assertEquals(7, bst.size());
        assertEquals(2, bst.height());
    }

    @Test
    public void testDuplicateValues() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(50); // duplicate
        bst.insert(30); // duplicate

        assertEquals(2, bst.size());
    }

    @Test
    public void testSearchAndContains() {
        bst.insert(50);
        bst.insert(25);
        bst.insert(75);

        assertTrue(bst.contains(50));
        assertTrue(bst.contains(25));
        assertTrue(bst.contains(75));
        assertFalse(bst.contains(100));

        assertEquals(50, bst.search(50));
        assertEquals(25, bst.search(25));
        assertEquals(75, bst.search(75));
        assertNull(bst.search(999));
    }

    @Test
    public void testMinAndMax() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(10);
        bst.insert(90);

        assertEquals(10, bst.min());
        assertEquals(90, bst.max());
    }

    @Test
    public void testTraversals() {
        /*
         * Tree structure:
         *        50
         *       /  \
         *     30    70
         *    /  \   / \
         *   20  40 60  80
         */
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);
        bst.insert(40);
        bst.insert(60);
        bst.insert(80);

        // Inorder should be sorted: [20, 30, 40, 50, 60, 70, 80]
        DynamicArray<Integer> inorder = bst.inorder();
        assertEquals(7, inorder.size());
        int[] expectedInorder = {20, 30, 40, 50, 60, 70, 80};
        for (int i = 0; i < expectedInorder.length; i++) {
            assertEquals(expectedInorder[i], inorder.get(i));
        }

        // Preorder: Root, Left, Right => [50, 30, 20, 40, 70, 60, 80]
        DynamicArray<Integer> preorder = bst.preorder();
        int[] expectedPreorder = {50, 30, 20, 40, 70, 60, 80};
        for (int i = 0; i < expectedPreorder.length; i++) {
            assertEquals(expectedPreorder[i], preorder.get(i));
        }

        // Postorder: Left, Right, Root => [20, 40, 30, 60, 80, 70, 50]
        DynamicArray<Integer> postorder = bst.postorder();
        int[] expectedPostorder = {20, 40, 30, 60, 80, 70, 50};
        for (int i = 0; i < expectedPostorder.length; i++) {
            assertEquals(expectedPostorder[i], postorder.get(i));
        }
    }

    @Test
    public void testDeleteLeaf() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);

        assertTrue(bst.delete(20)); // leaf
        assertFalse(bst.contains(20));
        assertEquals(3, bst.size());

        // Inorder still sorted
        DynamicArray<Integer> inorder = bst.inorder();
        assertEquals(3, inorder.size());
        assertEquals(30, inorder.get(0));
        assertEquals(50, inorder.get(1));
        assertEquals(70, inorder.get(2));
    }

    @Test
    public void testDeleteNodeWithOneChild() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20); // 30 has only left child (20)

        assertTrue(bst.delete(30));
        assertFalse(bst.contains(30));
        assertTrue(bst.contains(20));
        assertEquals(3, bst.size());

        DynamicArray<Integer> inorder = bst.inorder();
        assertEquals(3, inorder.size());
        assertEquals(20, inorder.get(0));
        assertEquals(50, inorder.get(1));
        assertEquals(70, inorder.get(2));
    }

    @Test
    public void testDeleteNodeWithTwoChildren() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);
        bst.insert(40);
        bst.insert(60);
        bst.insert(80);

        // Delete 30 (has two children: 20 and 40). Inorder successor is 40.
        assertTrue(bst.delete(30));
        assertFalse(bst.contains(30));
        assertEquals(6, bst.size());

        DynamicArray<Integer> inorder = bst.inorder();
        int[] expected = {20, 40, 50, 60, 70, 80};
        assertEquals(6, inorder.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], inorder.get(i));
        }
    }

    @Test
    public void testDeleteRoot() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);

        // Delete root (50)
        assertTrue(bst.delete(50));
        assertFalse(bst.contains(50));
        assertEquals(2, bst.size());

        // Inorder successor 70 should become root
        assertEquals(70, bst.getRoot().data);

        DynamicArray<Integer> inorder = bst.inorder();
        assertEquals(2, inorder.size());
        assertEquals(30, inorder.get(0));
        assertEquals(70, inorder.get(1));
    }

    @Test
    public void testDeleteSingleRoot() {
        bst.insert(100);
        assertEquals(1, bst.size());

        assertTrue(bst.delete(100));
        assertEquals(0, bst.size());
        assertTrue(bst.isEmpty());
        assertNull(bst.getRoot());
    }

    @Test
    public void testInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> bst.insert(null));
        assertThrows(IllegalArgumentException.class, () -> bst.search(null));
        assertThrows(IllegalArgumentException.class, () -> bst.delete(null));
    }
}
