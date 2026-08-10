package gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTest {

    @Test
    void testAddAndGet() {
        LinkedList<Integer> list = new LinkedList<>();

        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testAddAtIndex() {
        LinkedList<Integer> list = new LinkedList<>();

        list.add(10);
        list.add(30);
        list.add(1, 20);

        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testSet() {
        LinkedList<Integer> list = new LinkedList<>();

        list.add(10);
        list.add(20);
        list.add(30);

        list.set(1, 25);

        assertEquals(25, list.get(1));
    }

    @Test
    void testRemove() {
        LinkedList<Integer> list = new LinkedList<>();

        list.add(10);
        list.add(20);
        list.add(30);

        Integer removed = list.remove(1);

        assertEquals(20, removed);
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
    }

    @Test
    void testContains() {
        LinkedList<Integer> list = new LinkedList<>();

        list.add(10);
        list.add(20);
        list.add(30);

        assertTrue(list.contains(10));
        assertTrue(list.contains(20));
        assertTrue(list.contains(30));
        assertFalse(list.contains(40));
    }

    @Test
    void testFirstAndLast() {
        LinkedList<Integer> list = new LinkedList<>();

        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(10, list.getFirst());
        assertEquals(30, list.getLast());
    }

    @Test
    void testClearAndIsEmpty() {
        LinkedList<Integer> list = new LinkedList<>();

        list.add(10);
        list.add(20);
        list.add(30);

        assertFalse(list.isEmpty());

        list.clear();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }
}