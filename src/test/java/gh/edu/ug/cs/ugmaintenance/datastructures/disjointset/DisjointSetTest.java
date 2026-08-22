package gh.edu.ug.cs.ugmaintenance.datastructures.disjointset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DisjointSetTest {

    private DisjointSet<Integer> ds;

    @BeforeEach
    public void setUp() {
        ds = new DisjointSet<>();
    }

    @Test
    public void testMakeSetAndSize() {
        assertTrue(ds.isEmpty());
        assertEquals(0, ds.size());
        assertEquals(0, ds.getSetCount());

        ds.makeSet(1);
        ds.makeSet(2);
        ds.makeSet(3);

        assertEquals(3, ds.size());
        assertEquals(3, ds.getSetCount());
        assertFalse(ds.isEmpty());
        assertTrue(ds.contains(1));
        assertTrue(ds.contains(2));
        assertTrue(ds.contains(3));
        assertFalse(ds.contains(4));
    }

    @Test
    public void testDuplicateMakeSet() {
        ds.makeSet(1);
        assertEquals(1, ds.size());
        assertEquals(1, ds.getSetCount());

        // Duplicate makeSet should not increase size or count
        ds.makeSet(1);
        assertEquals(1, ds.size());
        assertEquals(1, ds.getSetCount());
    }

    @Test
    public void testFindSelfRepresentativeInitially() {
        ds.makeSet(10);
        ds.makeSet(20);

        assertEquals(10, ds.find(10));
        assertEquals(20, ds.find(20));
        assertFalse(ds.connected(10, 20));
    }

    @Test
    public void testUnionAndConnected() {
        ds.makeSet(1);
        ds.makeSet(2);
        ds.makeSet(3);
        ds.makeSet(4);

        assertEquals(4, ds.getSetCount());

        // Union {1} and {2}
        assertTrue(ds.union(1, 2));
        assertTrue(ds.connected(1, 2));
        assertEquals(3, ds.getSetCount());

        // Union {3} and {4}
        assertTrue(ds.union(3, 4));
        assertTrue(ds.connected(3, 4));
        assertFalse(ds.connected(1, 3));
        assertFalse(ds.connected(2, 4));
        assertEquals(2, ds.getSetCount());

        // Union {2} and {3} => merges all into one set {1, 2, 3, 4}
        assertTrue(ds.union(2, 3));
        assertTrue(ds.connected(1, 4));
        assertTrue(ds.connected(1, 3));
        assertTrue(ds.connected(2, 4));
        assertEquals(1, ds.getSetCount());

        // Redundant union should return false
        assertFalse(ds.union(1, 4));
        assertEquals(1, ds.getSetCount());
    }

    @Test
    public void testMultipleIndependentSets() {
        // Create 3 distinct campus clusters: [1,2], [3,4], [5,6]
        for (int i = 1; i <= 6; i++) {
            ds.makeSet(i);
        }

        ds.union(1, 2);
        ds.union(3, 4);
        ds.union(5, 6);

        assertEquals(3, ds.getSetCount());
        assertTrue(ds.connected(1, 2));
        assertTrue(ds.connected(3, 4));
        assertTrue(ds.connected(5, 6));

        assertFalse(ds.connected(1, 3));
        assertFalse(ds.connected(1, 5));
        assertFalse(ds.connected(3, 5));
    }

    @Test
    public void testPathCompressionAndUnionByRank() {
        // Create a chain of elements
        for (int i = 1; i <= 8; i++) {
            ds.makeSet(i);
        }

        ds.union(1, 2);
        ds.union(3, 4);
        ds.union(1, 3); // Set {1, 2, 3, 4}

        ds.union(5, 6);
        ds.union(7, 8);
        ds.union(5, 7); // Set {5, 6, 7, 8}

        // Merge both trees
        ds.union(1, 5); // Entire set {1..8}

        Integer root = ds.find(8);
        assertNotNull(root);

        // Path compression: find(8) compresses path to root
        for (int i = 1; i <= 8; i++) {
            assertEquals(root, ds.find(i));
        }
        assertEquals(1, ds.getSetCount());
    }

    @Test
    public void testGenericStringElements() {
        DisjointSet<String> campusSet = new DisjointSet<>();
        campusSet.makeSet("Balme Library");
        campusSet.makeSet("Legon Hall");
        campusSet.makeSet("JQB");
        campusSet.makeSet("CS Department");

        campusSet.union("Balme Library", "Legon Hall");
        campusSet.union("JQB", "CS Department");

        assertTrue(campusSet.connected("Balme Library", "Legon Hall"));
        assertTrue(campusSet.connected("JQB", "CS Department"));
        assertFalse(campusSet.connected("Balme Library", "JQB"));

        campusSet.union("Legon Hall", "CS Department");
        assertTrue(campusSet.connected("Balme Library", "CS Department"));
        assertEquals(1, campusSet.getSetCount());
    }

    @Test
    public void testInvalidInputs() {
        // Null makeSet
        assertThrows(IllegalArgumentException.class, () -> ds.makeSet(null));

        // Find non-existent element
        assertThrows(IllegalArgumentException.class, () -> ds.find(99));

        // Find null
        assertThrows(IllegalArgumentException.class, () -> ds.find(null));

        // Union with unregistered element
        ds.makeSet(1);
        assertThrows(IllegalArgumentException.class, () -> ds.union(1, 99));
        assertThrows(IllegalArgumentException.class, () -> ds.union(99, 1));
        assertThrows(IllegalArgumentException.class, () -> ds.union(null, 1));

        // Connected with unregistered element
        assertThrows(IllegalArgumentException.class, () -> ds.connected(1, 99));
        assertThrows(IllegalArgumentException.class, () -> ds.connected(null, 1));
    }

    @Test
    public void testClear() {
        ds.makeSet(1);
        ds.makeSet(2);
        ds.union(1, 2);

        assertEquals(2, ds.size());
        assertEquals(1, ds.getSetCount());

        ds.clear();

        assertEquals(0, ds.size());
        assertEquals(0, ds.getSetCount());
        assertTrue(ds.isEmpty());
    }
}
