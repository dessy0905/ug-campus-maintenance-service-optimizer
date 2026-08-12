package gh.edu.ug.cs.ugmaintenance.datastructures.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    @Test
    void testAddAndGet() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);
        array.add(20);
        array.add(30);

        assertEquals(3, array.size());
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
    }

    @Test
    void testAddAtIndex() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);
        array.add(30);
        array.add(1, 20);

        assertEquals(3, array.size());
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
    }

    @Test
    void testSet() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);
        array.add(20);

        array.set(1, 25);

        assertEquals(25, array.get(1));
    }

    @Test
    void testRemove() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);
        array.add(20);
        array.add(30);

        Integer removed = array.remove(1);

        assertEquals(20, removed);
        assertEquals(2, array.size());
        assertEquals(30, array.get(1));
    }

    @Test
    void testContains() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);
        array.add(20);

        assertTrue(array.contains(10));
        assertTrue(array.contains(20));
        assertFalse(array.contains(50));
    }

    @Test
    void testClear() {
        DynamicArray<Integer> array = new DynamicArray<>();

        array.add(10);
        array.add(20);

        array.clear();

        assertEquals(0, array.size());
        assertTrue(array.isEmpty());
    }

    @Test
    void testIsEmpty() {
        DynamicArray<Integer> array = new DynamicArray<>();

        assertTrue(array.isEmpty());

        array.add(10);

        assertFalse(array.isEmpty());
    }
}