package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

import gh.edu.ug.cs.ugmaintenance.models.Location;
import gh.edu.ug.cs.ugmaintenance.models.enums.LocationType;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Unit tests for the custom Hash Table, Set and Map (member #9).
 *
 * <p>Every custom structure must have tests for the normal case, the boundary
 * case and the invalid input case (project specification, Section 8.iii).</p>
 */
public class HashTableTest {

    // =====================================================================
    // HashTable - normal cases
    // =====================================================================

    @Test
    public void testPutAndGet() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.put("a", 1));
        assertNull(table.put("b", 2));
        assertEquals(Integer.valueOf(1), table.get("a"));
        assertEquals(Integer.valueOf(2), table.get("b"));
        assertEquals(2, table.size());
    }

    @Test
    public void testPutOverwritesExistingKey() {
        HashTable<String, String> table = new HashTable<>();
        table.put("gate", "UG Main Gate");
        assertEquals("UG Main Gate", table.get("gate"));

        String previous = table.put("gate", "UG Main Gate Annex");
        assertEquals("UG Main Gate", previous);
        assertEquals("UG Main Gate Annex", table.get("gate"));
        assertEquals(1, table.size()); // still one entry
    }

    @Test
    public void testRemoveExistingKey() {
        HashTable<String, String> table = new HashTable<>();
        table.put("hall", "Legon Hall");
        table.put("lib", "Balme Library");

        assertEquals("Legon Hall", table.remove("hall"));
        assertFalse(table.containsKey("hall"));
        assertTrue(table.containsKey("lib"));
        assertEquals(1, table.size());
    }

    @Test
    public void testContainsValue() {
        HashTable<String, String> table = new HashTable<>();
        table.put("hall", "Legon Hall");
        table.put("lib", "Balme Library");
        assertTrue(table.containsValue("Legon Hall"));
        assertFalse(table.containsValue("Night Market"));
    }

    @Test
    public void testKeySetAndValues() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.put("b", 2);
        table.put("c", 3);

        assertEquals(3, table.keySet().size());
        assertTrue(table.keySet().contains("a"));
        assertTrue(table.keySet().contains("b"));
        assertTrue(table.keySet().contains("c"));
        assertEquals(3, table.values().size());
        assertTrue(table.values().contains(1));
        assertTrue(table.values().contains(2));
        assertTrue(table.values().contains(3));
    }

    @Test
    public void testLocationIndexUseCase() {
        // Hash index over campus locations (M6 indexing use case).
        HashTable<Integer, Location> index = new HashTable<>();
        index.put(101, new Location(101, "Balme Library", LocationType.LIBRARY, "Main library", LocalDateTime.now()));
        index.put(102, new Location(102, "Legon Hall", LocationType.HALL, "Residence hall", LocalDateTime.now()));
        index.put(103, new Location(103, "JQB Lab", LocationType.LABORATORY, "Computer lab", LocalDateTime.now()));

        assertEquals("Balme Library", index.get(101).getLocationName());
        assertEquals(LocationType.HALL, index.get(102).getLocationType());
        assertEquals("JQB Lab", index.get(103).getLocationName());
        assertNull(index.get(999));
        assertEquals(3, index.size());
    }

    // =====================================================================
    // HashTable - boundary cases
    // =====================================================================

    @Test
    public void testEmptyTable() {
        HashTable<String, String> table = new HashTable<>();
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
        assertNull(table.get("anything"));
        assertFalse(table.containsKey("anything"));
        assertNull(table.remove("anything"));
        assertTrue(table.keySet().isEmpty());
        assertTrue(table.values().isEmpty());
    }

    @Test
    public void testSingleEntry() {
        HashTable<String, String> table = new HashTable<>();
        table.put("only", "one");
        assertEquals(1, table.size());
        assertEquals("one", table.get("only"));
        assertEquals("one", table.remove("only"));
        assertTrue(table.isEmpty());
    }

    @Test
    public void testClear() {
        HashTable<String, String> table = new HashTable<>();
        table.put("a", "1");
        table.put("b", "2");
        table.clear();
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
        assertNull(table.get("a"));
    }

    @Test
    public void testNullValueAllowed() {
        // Null values are permitted; containsKey still distinguishes presence.
        HashTable<String, String> table = new HashTable<>();
        table.put("a", null);
        assertTrue(table.containsKey("a"));
        assertNull(table.get("a"));
        assertEquals(1, table.size());
    }

    @Test
    public void testResizeRehashesAllEntries() {
        // 100 inserts into a tiny initial table force several resizes.
        HashTable<Integer, String> table = new HashTable<>(4);
        for (int i = 0; i < 100; i++) {
            table.put(i, "v" + i);
        }
        assertEquals(100, table.size());
        assertTrue("table should have grown beyond initial capacity",
                table.getCapacity() > 4);
        for (int i = 0; i < 100; i++) {
            assertEquals("v" + i, table.get(i));
        }
    }

    @Test
    public void testChurnRemoveHalf() {
        HashTable<Integer, String> table = new HashTable<>();
        for (int i = 0; i < 200; i++) {
            table.put(i, "v" + i);
        }
        for (int i = 0; i < 200; i += 2) {
            table.remove(i);
        }
        assertEquals(100, table.size());
        assertNull(table.get(0));
        assertEquals("v1", table.get(1));
        for (int i = 1; i < 200; i += 2) {
            assertEquals("v" + i, table.get(i));
        }
    }

    @Test
    public void testCollisionStatsKnownScenario() {
        // Capacity 4; keys 0, 4 and 8 all hash to bucket 0 (mod 4).
        // size 3 -> 1 non-empty bucket -> collisions = 3 - 1 = 2, max chain = 3.
        HashTable<Integer, String> table = new HashTable<>(4);
        table.put(0, "a");
        table.put(4, "b");
        table.put(8, "c");

        assertEquals(3, table.size());
        assertEquals(0.75, table.getLoadFactor(), 0.0001);
        assertEquals(2, table.getCollisionCount());
        assertEquals(3, table.getMaxChainLength());
        assertEquals(3.0, table.getAverageChainLength(), 0.0001);

        // All collided keys are still individually retrievable.
        assertEquals("a", table.get(0));
        assertEquals("b", table.get(4));
        assertEquals("c", table.get(8));
    }

    @Test
    public void testNoCollisionsForDistinctBuckets() {
        // Keys 0, 1, 2, 3 map to four distinct buckets in a capacity-4 table.
        HashTable<Integer, String> table = new HashTable<>(4);
        table.put(0, "a");
        table.put(1, "b");
        table.put(2, "c");
        table.put(3, "d");
        assertEquals(0, table.getCollisionCount());
        assertEquals(1, table.getMaxChainLength());
        assertEquals(1.0, table.getAverageChainLength(), 0.0001);
    }

    @Test
    public void testStatsAfterResizeTriggeringInsert() {
        // The 4th insert pushes the load factor of a capacity-4 table past 0.75,
        // triggering a resize to 8. Stats must be recomputed on the new table and
        // every key must survive the rehash.
        HashTable<Integer, String> table = new HashTable<>(4);
        table.put(0, "a");
        table.put(1, "b");
        table.put(2, "c");
        table.put(3, "d");

        assertEquals(8, table.getCapacity());
        assertEquals(4, table.size());
        assertEquals(0.5, table.getLoadFactor(), 0.0001);
        assertEquals(0, table.getCollisionCount());
        assertEquals(1, table.getMaxChainLength());
        for (int i = 0; i < 4; i++) {
            assertNotNull(table.get(i));
        }
    }

    @Test
    public void testReinsertAfterRemove() {
        // Removing a key and putting it back must work (chain re-add path).
        HashTable<String, String> table = new HashTable<>();
        table.put("hall", "Legon Hall");
        table.remove("hall");
        assertNull(table.put("hall", "Commonwealth Hall"));
        assertEquals("Commonwealth Hall", table.get("hall"));
        assertEquals(1, table.size());
    }

    @Test
    public void testContainsValueNull() {
        HashTable<String, String> table = new HashTable<>();
        table.put("a", null);
        assertTrue(table.containsValue(null));
        assertFalse(table.containsValue("anything"));
    }

    // =====================================================================
    // HashTable - invalid input cases
    // =====================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyPutThrows() {
        new HashTable<String, String>().put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyGetThrows() {
        new HashTable<String, String>().get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyRemoveThrows() {
        new HashTable<String, String>().remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialCapacityThrows() {
        new HashTable<String, String>(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLoadFactorThresholdThrows() {
        new HashTable<String, String>(16, 1.5);
    }

    // =====================================================================
    // Set - normal cases
    // =====================================================================

    @Test
    public void testSetAddContains() {
        Set<String> set = new Set<>();
        assertTrue(set.add("Kwame"));
        assertTrue(set.add("Ama"));
        assertTrue(set.contains("Kwame"));
        assertTrue(set.contains("Ama"));
        assertFalse(set.contains("Esi"));
        assertEquals(2, set.size());
    }

    @Test
    public void testSetRejectsDuplicates() {
        Set<String> set = new Set<>();
        assertTrue(set.add("Kwame"));
        assertFalse(set.add("Kwame")); // duplicate
        assertEquals(1, set.size());
    }

    @Test
    public void testSetRemove() {
        Set<String> set = new Set<>();
        set.add("Kwame");
        set.add("Ama");
        assertTrue(set.remove("Kwame"));
        assertFalse(set.contains("Kwame"));
        assertFalse(set.remove("Kwame")); // already gone
        assertEquals(1, set.size());
    }

    @Test
    public void testSetUnion() {
        Set<Integer> a = new Set<>();
        a.add(1);
        a.add(2);
        Set<Integer> b = new Set<>();
        b.add(2);
        b.add(3);

        Set<Integer> union = a.union(b);
        assertEquals(3, union.size());
        assertTrue(union.contains(1));
        assertTrue(union.contains(2));
        assertTrue(union.contains(3));
    }

    @Test
    public void testSetIntersection() {
        Set<Integer> a = new Set<>();
        a.add(1);
        a.add(2);
        a.add(3);
        Set<Integer> b = new Set<>();
        b.add(2);
        b.add(3);
        b.add(4);

        Set<Integer> intersection = a.intersection(b);
        assertEquals(2, intersection.size());
        assertTrue(intersection.contains(2));
        assertTrue(intersection.contains(3));
        assertFalse(intersection.contains(1));
    }

    @Test
    public void testSetDifference() {
        Set<Integer> a = new Set<>();
        a.add(1);
        a.add(2);
        a.add(3);
        Set<Integer> b = new Set<>();
        b.add(2);

        Set<Integer> difference = a.difference(b);
        assertEquals(2, difference.size());
        assertTrue(difference.contains(1));
        assertTrue(difference.contains(3));
        assertFalse(difference.contains(2));
    }

    @Test
    public void testSetIsSubsetOf() {
        Set<Integer> a = new Set<>();
        a.add(1);
        a.add(2);
        Set<Integer> b = new Set<>();
        b.add(1);
        b.add(2);
        b.add(3);

        assertTrue(a.isSubsetOf(b));
        assertFalse(b.isSubsetOf(a));
    }

    // =====================================================================
    // Set - boundary and invalid input cases
    // =====================================================================

    @Test
    public void testSetEmptyBoundary() {
        Set<String> set = new Set<>();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertFalse(set.contains("anything"));
        assertFalse(set.remove("anything"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullElementThrows() {
        new Set<String>().add(null);
    }

    @Test
    public void testSetManyElements() {
        Set<Integer> set = new Set<>();
        for (int i = 0; i < 1_000; i++) {
            set.add(i);
        }
        assertEquals(1_000, set.size());
        assertTrue(set.contains(0));
        assertTrue(set.contains(999));
        assertFalse(set.contains(1_000));
    }

    // =====================================================================
    // Map - normal cases
    // =====================================================================

    @Test
    public void testMapPutGetRemove() {
        Map<String, String> map = new Map<>();
        assertNull(map.put("Kwame", "Toyota Hilux"));
        assertEquals("Toyota Hilux", map.get("Kwame"));
        assertEquals("Toyota Hilux", map.remove("Kwame"));
        assertFalse(map.containsKey("Kwame"));
        assertTrue(map.isEmpty());
    }

    @Test
    public void testMapGetOrDefault() {
        Map<String, Integer> map = new Map<>();
        map.put("hall", 102);
        assertEquals(Integer.valueOf(102), map.getOrDefault("hall", -1));
        assertEquals(Integer.valueOf(-1), map.getOrDefault("missing", -1));
    }

    @Test
    public void testMapPutIfAbsent() {
        Map<String, String> map = new Map<>();
        map.put("gate", "UG Main Gate");
        // Existing key is not overwritten.
        assertEquals("UG Main Gate", map.putIfAbsent("gate", "Annex"));
        // New key is inserted.
        assertNull(map.putIfAbsent("lib", "Balme Library"));
        assertEquals("Balme Library", map.get("lib"));
        assertEquals(2, map.size());
    }

    @Test
    public void testMapContainsKeyValue() {
        Map<String, String> map = new Map<>();
        map.put("hall", "Legon Hall");
        assertTrue(map.containsKey("hall"));
        assertFalse(map.containsKey("lib"));
        assertTrue(map.containsValue("Legon Hall"));
        assertFalse(map.containsValue("Balme Library"));
    }

    @Test
    public void testMapKeySetAndValues() {
        Map<String, Integer> map = new Map<>();
        map.put("a", 1);
        map.put("b", 2);
        assertEquals(2, map.keySet().size());
        assertTrue(map.keySet().contains("a"));
        assertTrue(map.values().contains(1));
        assertTrue(map.values().contains(2));
    }

    @Test
    public void testMapClear() {
        Map<String, String> map = new Map<>();
        map.put("a", "1");
        map.put("b", "2");
        map.clear();
        assertTrue(map.isEmpty());
        assertNull(map.get("a"));
    }

    // =====================================================================
    // Map - invalid input cases (delegated to HashTable)
    // =====================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testMapNullKeyPutThrows() {
        new Map<String, String>().put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapNullKeyGetThrows() {
        new Map<String, String>().get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapNullKeyRemoveThrows() {
        new Map<String, String>().remove(null);
    }
}
