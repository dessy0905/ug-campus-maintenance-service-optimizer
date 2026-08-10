package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class HashTableTest {

    // ------------------------------------------------------------------
    // Basic put / get
    // ------------------------------------------------------------------

    @Test
    public void testPutAndGet() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.put("Legon Hall", 1));
        assertNull(table.put("Balme Library", 2));
        assertEquals(Integer.valueOf(1), table.get("Legon Hall"));
        assertEquals(Integer.valueOf(2), table.get("Balme Library"));
        assertEquals(2, table.size());
    }

    @Test
    public void testPutOverwritesReturnsPreviousValue() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.put("M001", 1));
        assertEquals(Integer.valueOf(1), table.put("M001", 2));
        assertEquals(Integer.valueOf(2), table.get("M001"));
    }

    @Test
    public void testPutExistingKeyKeepsSize() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("A", 1);
        table.put("B", 2);
        table.put("A", 99);
        assertEquals(2, table.size());
    }

    @Test
    public void testGetMissingReturnsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("A", 1);
        assertNull(table.get("absent"));
    }

    @Test
    public void testContainsKey() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("JQB-19", 5);
        assertTrue(table.containsKey("JQB-19"));
        assertFalse(table.containsKey("CS-Office"));
    }

    // ------------------------------------------------------------------
    // Remove
    // ------------------------------------------------------------------

    @Test
    public void testRemoveExistingKey() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("A", 1);
        table.put("B", 2);
        assertEquals(Integer.valueOf(1), table.remove("A"));
        assertEquals(1, table.size());
        assertFalse(table.containsKey("A"));
        assertTrue(table.containsKey("B"));
    }

    @Test
    public void testRemoveMissingReturnsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("A", 1);
        assertNull(table.remove("absent"));
        assertEquals(1, table.size());
    }

    // ------------------------------------------------------------------
    // Boundary cases
    // ------------------------------------------------------------------

    @Test
    public void testEmptyTableBoundary() {
        HashTable<String, Integer> table = new HashTable<>();
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
        assertNull(table.get("anything"));
        assertNull(table.remove("anything"));
        assertFalse(table.containsKey("anything"));
        assertEquals(0, table.getMaxBucketLength());
        assertEquals(0.0, table.loadFactor(), 1e-9);
    }

    @Test
    public void testSingleElementBoundary() {
        HashTable<String, Integer> table = new HashTable<>(1);
        table.put("only", 7);
        assertEquals(1, table.size());
        assertEquals(Integer.valueOf(7), table.get("only"));
        assertTrue(table.containsKey("only"));
        assertEquals(Integer.valueOf(7), table.remove("only"));
        assertTrue(table.isEmpty());
    }

    @Test
    public void testNullValueAllowed() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("A", null);
        assertTrue(table.containsKey("A"));
        assertNull(table.get("A"));
        assertEquals(1, table.size());
    }

    @Test
    public void testClearResets() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("A", 1);
        table.put("B", 2);
        table.clear();
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
        assertNull(table.get("A"));
        assertEquals(0, table.getCollisionCount());
    }

    @Test
    public void testKeysAndValuesMatch() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.put("b", 2);
        table.put("c", 3);
        List<String> keys = table.keys();
        List<Integer> values = table.values();
        assertEquals(3, keys.size());
        assertEquals(3, values.size());
        assertTrue(keys.contains("a") && keys.contains("b") && keys.contains("c"));
        assertTrue(values.contains(1) && values.contains(2) && values.contains(3));
    }

    // ------------------------------------------------------------------
    // Growth / resizing
    // ------------------------------------------------------------------

    @Test
    public void testManyInsertsResizeKeepValues() {
        // Default capacity is 16; 500 inserts force several doublings.
        HashTable<Integer, String> table = new HashTable<>();
        for (int i = 0; i < 500; i++) {
            table.put(i, "v" + i);
        }
        assertEquals(500, table.size());
        assertTrue(table.getResizeCount() > 0);
        assertTrue(table.capacity() > 16);
        for (int i = 0; i < 500; i++) {
            assertEquals("v" + i, table.get(i));
        }
    }

    @Test
    public void testGrowthDoublesCapacity() {
        HashTable<Integer, Integer> table = new HashTable<>(4);
        for (int i = 0; i < 4; i++) {
            table.put(i, i);
        }
        // loadFactor 4/4 = 1.0 >= 0.75 triggers the first doubling.
        assertEquals(8, table.capacity());
        for (int i = 0; i < 4; i++) {
            assertEquals(Integer.valueOf(i), table.get(i));
        }
    }

    // ------------------------------------------------------------------
    // Collision handling and statistics
    // ------------------------------------------------------------------

    /** A key type whose hashCode is constant, forcing every key into one bucket. */
    private static final class CollidingKey {
        private final int id;

        CollidingKey(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return 42; // deliberate: all keys collide
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CollidingKey)) {
                return false;
            }
            return ((CollidingKey) other).id == id;
        }
    }

    @Test
    public void testCollidingKeysChainTogether() {
        HashTable<CollidingKey, String> table = new HashTable<>(4);
        for (int i = 0; i < 10; i++) {
            table.put(new CollidingKey(i), "k" + i);
        }
        assertEquals(10, table.size());
        // First insert is a fresh bucket; the other nine collide.
        assertEquals(9, table.getCollisionCount());
        assertEquals(10, table.getMaxBucketLength());
        for (int i = 0; i < 10; i++) {
            assertEquals("k" + i, table.get(new CollidingKey(i)));
        }
    }

    @Test
    public void testNoCollisionsForDistinctBuckets() {
        // Capacity 64, all keys distinct and spread across buckets.
        HashTable<Integer, Integer> table = new HashTable<>(64);
        for (int i = 0; i < 10; i++) {
            table.put(i, i);
        }
        assertEquals(0, table.getCollisionCount());
        assertEquals(1, table.getMaxBucketLength());
    }

    @Test
    public void testRemoveFromMiddleOfChain() {
        HashTable<CollidingKey, String> table = new HashTable<>(4);
        for (int i = 0; i < 5; i++) {
            table.put(new CollidingKey(i), "k" + i);
        }
        table.remove(new CollidingKey(2)); // somewhere in the middle of the chain
        assertEquals(4, table.size());
        assertFalse(table.containsKey(new CollidingKey(2)));
        assertEquals("k" + 0, table.get(new CollidingKey(0)));
        assertEquals("k" + 4, table.get(new CollidingKey(4)));
        assertEquals(4, table.getMaxBucketLength());
    }

    @Test
    public void testLoadFactorReporting() {
        HashTable<Integer, Integer> table = new HashTable<>(8);
        for (int i = 0; i < 4; i++) {
            table.put(i, i);
        }
        assertEquals(8, table.capacity());
        assertEquals(0.5, table.loadFactor(), 1e-9);
    }

    @Test
    public void testToStringSummarisesState() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("A", 1);
        String summary = table.toString();
        assertTrue(summary.contains("capacity=16"));
        assertTrue(summary.contains("size=1"));
        assertTrue(summary.contains("collisions=0"));
    }

    // ------------------------------------------------------------------
    // Invalid inputs
    // ------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyPutThrows() {
        new HashTable<String, Integer>().put(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyGetThrows() {
        new HashTable<String, Integer>().get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyRemoveThrows() {
        new HashTable<String, Integer>().remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyContainsThrows() {
        new HashTable<String, Integer>().containsKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroCapacityThrows() {
        new HashTable<>(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCapacityThrows() {
        new HashTable<>(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroLoadFactorThrows() {
        new HashTable<>(16, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactorThrows() {
        new HashTable<>(16, -0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFactorAboveOneThrows() {
        new HashTable<>(16, 1.5);
    }
}
