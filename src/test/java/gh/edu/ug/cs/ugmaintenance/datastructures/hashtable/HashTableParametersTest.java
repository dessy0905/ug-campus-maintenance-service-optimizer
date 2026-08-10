package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HashTableParametersTest {

    @Test
    public void testTableSizeDerivedFromIndexNumber() {
        assertEquals(22081315L, HashTableParameters.CHERYL_INDEX_NUMBER);
        assertEquals(5315, HashTableParameters.tableSize());
    }

    @Test
    public void testLoadFactorThresholdDerivedFromLastDigit() {
        assertEquals(0.75, HashTableParameters.loadFactorThreshold(), 1e-9);
    }

    @Test
    public void testDerivedSizeFeedsTheHashTableConstructor() {
        // The nominal parameter is 5315; the table rounds up to a power of two.
        HashTable<Integer, String> table = new HashTable<>(HashTableParameters.tableSize());
        assertEquals(8192, table.capacity());
        assertEquals(0.75, HashTableParameters.loadFactorThreshold(), 1e-9);
    }
}
