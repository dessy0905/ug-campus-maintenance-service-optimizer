package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MapTest {

    @Test
    public void testPutAndGet() {
        Map<String, String> map = new Map<>();
        assertNull(map.put("L1", "Legon Hall"));
        assertNull(map.put("L2", "Balme Library"));
        assertEquals("Legon Hall", map.get("L1"));
        assertEquals("Balme Library", map.get("L2"));
        assertEquals(2, map.size());
    }

    @Test
    public void testPutOverwritesReturnsPrevious() {
        Map<String, Integer> map = new Map<>();
        assertNull(map.put("M001", 1));
        assertEquals(Integer.valueOf(1), map.put("M001", 2));
        assertEquals(Integer.valueOf(2), map.get("M001"));
        assertEquals(1, map.size());
    }

    @Test
    public void testGetMissingReturnsNull() {
        Map<String, String> map = new Map<>();
        map.put("L1", "Legon Hall");
        assertNull(map.get("L99"));
    }

    @Test
    public void testRemove() {
        Map<String, String> map = new Map<>();
        map.put("L1", "Legon Hall");
        map.put("L2", "Balme Library");
        assertEquals("Legon Hall", map.remove("L1"));
        assertEquals(1, map.size());
        assertFalse(map.containsKey("L1"));
        assertTrue(map.containsKey("L2"));
    }

    @Test
    public void testRemoveMissingReturnsNull() {
        Map<String, String> map = new Map<>();
        map.put("L1", "Legon Hall");
        assertNull(map.remove("L99"));
        assertEquals(1, map.size());
    }

    @Test
    public void testContainsKey() {
        Map<String, String> map = new Map<>();
        map.put("L1", "Legon Hall");
        assertTrue(map.containsKey("L1"));
        assertFalse(map.containsKey("L2"));
    }

    @Test
    public void testEmptyMapBoundary() {
        Map<String, String> map = new Map<>();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("anything"));
        assertNull(map.remove("anything"));
        assertFalse(map.containsKey("anything"));
        assertTrue(map.keySet().isEmpty());
        assertTrue(map.values().isEmpty());
    }

    @Test
    public void testClear() {
        Map<String, String> map = new Map<>();
        map.put("L1", "Legon Hall");
        map.put("L2", "Balme Library");
        map.clear();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("L1"));
    }

    @Test
    public void testKeySetContainsAllKeys() {
        Map<String, String> map = new Map<>();
        map.put("L1", "Legon Hall");
        map.put("L2", "Balme Library");
        map.put("L3", "CS Department");
        Set<String> keys = map.keySet();
        assertEquals(3, keys.size());
        assertTrue(keys.contains("L1"));
        assertTrue(keys.contains("L2"));
        assertTrue(keys.contains("L3"));
        assertFalse(keys.contains("L9"));
    }

    @Test
    public void testValuesContainsAllValues() {
        Map<String, String> map = new Map<>();
        map.put("L1", "Legon Hall");
        map.put("L2", "Balme Library");
        List<String> values = map.values();
        assertEquals(2, values.size());
        assertTrue(values.contains("Legon Hall"));
        assertTrue(values.contains("Balme Library"));
    }

    @Test
    public void testManyEntriesResize() {
        Map<Integer, Integer> map = new Map<>();
        for (int i = 0; i < 400; i++) {
            map.put(i, i * i);
        }
        assertEquals(400, map.size());
        for (int i = 0; i < 400; i++) {
            assertEquals(Integer.valueOf(i * i), map.get(i));
        }
        assertEquals(400, map.keySet().size());
    }

    @Test
    public void testNullValueAllowed() {
        Map<String, Integer> map = new Map<>();
        map.put("A", null);
        assertTrue(map.containsKey("A"));
        assertNull(map.get("A"));
        assertEquals(1, map.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyPutThrows() {
        new Map<String, String>().put(null, "x");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyGetThrows() {
        new Map<String, String>().get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyRemoveThrows() {
        new Map<String, String>().remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyContainsThrows() {
        new Map<String, String>().containsKey(null);
    }
}
