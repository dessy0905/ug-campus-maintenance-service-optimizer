package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SetTest {

    @Test
    public void testAddAndContains() {
        Set<String> set = new Set<>();
        assertTrue(set.add("Electrical"));
        assertTrue(set.add("Plumbing"));
        assertTrue(set.contains("Electrical"));
        assertTrue(set.contains("Plumbing"));
        assertFalse(set.contains("Furniture"));
        assertEquals(2, set.size());
    }

    @Test
    public void testAddDuplicateReturnsFalse() {
        Set<String> set = new Set<>();
        assertTrue(set.add("Electrical"));
        assertFalse(set.add("Electrical"));
        assertEquals(1, set.size());
    }

    @Test
    public void testRemoveExistingElement() {
        Set<String> set = new Set<>();
        set.add("A");
        set.add("B");
        assertTrue(set.remove("A"));
        assertEquals(1, set.size());
        assertFalse(set.contains("A"));
        assertTrue(set.contains("B"));
    }

    @Test
    public void testRemoveMissingReturnsFalse() {
        Set<String> set = new Set<>();
        set.add("A");
        assertFalse(set.remove("absent"));
        assertEquals(1, set.size());
    }

    @Test
    public void testEmptySetBoundary() {
        Set<String> set = new Set<>();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertFalse(set.contains("anything"));
        assertFalse(set.remove("anything"));
    }

    @Test
    public void testSingleElementBoundary() {
        Set<Integer> set = new Set<>(1);
        set.add(1);
        assertEquals(1, set.size());
        assertTrue(set.contains(1));
        set.remove(1);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testClear() {
        Set<String> set = new Set<>();
        set.add("A");
        set.add("B");
        set.clear();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertFalse(set.contains("A"));
    }

    @Test
    public void testToArray() {
        Set<String> set = new Set<>();
        set.add("Legon Hall");
        set.add("Balme Library");
        Object[] elements = set.toArray();
        assertEquals(2, elements.length);
        assertTrue(Arrays.asList(elements).contains("Legon Hall"));
        assertTrue(Arrays.asList(elements).contains("Balme Library"));
    }

    @Test
    public void testManyAddsDeduplicate() {
        // Large enough to force the backing table to resize.
        Set<Integer> set = new Set<>();
        for (int i = 0; i < 300; i++) {
            set.add(i % 50); // only 50 distinct values
        }
        assertEquals(50, set.size());
        assertTrue(set.contains(0));
        assertTrue(set.contains(49));
        assertFalse(set.contains(50));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullThrows() {
        new Set<String>().add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsNullThrows() {
        new Set<String>().contains(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroCapacityThrows() {
        new Set<>(0);
    }
}
