package gh.edu.ug.cs.ugmaintenance.datastructures.disjointset;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DisjointSetTest {

    @Test
    public void testInitiallyAllSeparate() {
        DisjointSet set = new DisjointSet(5);
        assertEquals(5, set.countComponents());
        assertFalse(set.isConnected(0, 1));
        assertEquals(1, set.getSize(0));
    }

    @Test
    public void testUnionTwoElements() {
        DisjointSet set = new DisjointSet(5);
        set.union(0, 1);
        assertTrue(set.isConnected(0, 1));
        assertEquals(4, set.countComponents());
        assertEquals(2, set.getSize(0));
    }

    @Test
    public void testUnionTransitive() {
        DisjointSet set = new DisjointSet(6);
        set.union(0, 1);
        set.union(1, 2);
        assertTrue(set.isConnected(0, 2));
        assertTrue(set.isConnected(2, 1));
        assertEquals(3, set.getSize(0));
        assertEquals(4, set.countComponents());
    }

    @Test
    public void testUnionSameSetIsNoOp() {
        DisjointSet set = new DisjointSet(5);
        set.union(0, 1);
        set.union(0, 1);
        assertEquals(4, set.countComponents());
        assertEquals(2, set.getSize(0));
    }

    @Test
    public void testUnionByRankKeepsTreeShallow() {
        DisjointSet set = new DisjointSet(8);
        set.union(0, 1);
        set.union(2, 3);
        set.union(0, 2);
        set.union(4, 5);
        set.union(5, 6);
        set.union(6, 7);
        assertEquals(2, set.countComponents());
        assertEquals(4, set.getSize(0));
        assertEquals(4, set.getSize(7));
        assertTrue(set.isConnected(1, 3));
        assertFalse(set.isConnected(0, 4));
    }

    @Test
    public void testFindConsistencyAfterPathCompression() {
        DisjointSet set = new DisjointSet(8);
        set.union(0, 1);
        set.union(2, 3);
        set.union(1, 3);
        set.union(4, 5);
        set.union(6, 7);
        set.union(5, 7);

        // Repeated finds exercise (and depend on) path compression internally.
        int rootA = set.find(0);
        int rootB = set.find(2);
        int rootC = set.find(3);
        assertEquals(rootA, rootB);
        assertEquals(rootA, rootC);
        assertEquals(2, set.countComponents());
        assertTrue(set.isConnected(0, 3));
    }

    @Test
    public void testMakeSetResetsElement() {
        DisjointSet set = new DisjointSet(4);
        set.union(0, 1);
        set.union(1, 2);
        assertEquals(2, set.countComponents());

        set.makeSet(2);
        assertFalse(set.isConnected(0, 2));
        assertEquals(3, set.countComponents());
        assertEquals(1, set.getSize(2));
    }

    @Test
    public void testSingleElementBoundary() {
        DisjointSet set = new DisjointSet(1);
        assertEquals(1, set.countComponents());
        assertEquals(0, set.find(0));
        assertTrue(set.isConnected(0, 0));
        assertEquals(1, set.getSize(0));

        set.union(0, 0); // self-union is a no-op
        assertEquals(1, set.countComponents());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructorSize() {
        new DisjointSet(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindOutOfRangeThrows() {
        new DisjointSet(3).find(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindNegativeThrows() {
        new DisjointSet(3).find(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionOutOfRangeThrows() {
        new DisjointSet(3).union(0, 5);
    }

    @Test
    public void testKruskalStyleConnectivity() {
        // Same merge pattern Kruskal performs while building an MST.
        DisjointSet set = new DisjointSet(4);
        assertEquals(4, set.countComponents());
        set.union(0, 1);
        assertEquals(3, set.countComponents());
        set.union(1, 2);
        assertEquals(2, set.countComponents());
        set.union(2, 3);
        assertEquals(1, set.countComponents());
        assertTrue(set.isConnected(0, 3));
    }
}
