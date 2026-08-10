package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

/**
 * Team-specific algorithm parameters derived from Cheryl's index number, as
 * required by the assignment (Section 2.iii: at least three algorithm
 * parameters must come from member index numbers — hash-table size is the
 * worked example).
 *
 * <p>Index number: {@value #CHERYL_INDEX_NUMBER}. The derivations are simple
 * modular-arithmetic formulas so they can be traced and re-verified in the
 * report:</p>
 * <ul>
 *   <li><b>table size</b> = {@code 1000 + (index % 9000)} &rarr; a nominal
 *       capacity in the range [1000, 9999]. {@link HashTable} rounds this up
 *       to a power of two internally for O(1) bucket masking, so the nominal
 *       value is what appears in the report while the actual capacity is
 *       reported by {@link HashTable#capacity()}.</li>
 *   <li><b>load factor threshold</b> = {@code 0.50 + (last digit * 0.05)}
 *       &rarr; a growth threshold in [0.50, 0.95] at which the table
 *       doubles.</li>
 * </ul>
 *
 * <p>The demo ({@link HashTableDemo}) and the efficiency experiment
 * ({@code HashTableBenchmark}) both read these values, so every trace and
 * CSV row is generated with team-specific parameters.</p>
 */
public final class HashTableParameters {

    /** Cheryl's UG index number — do not change. */
    public static final long CHERYL_INDEX_NUMBER = 22081315L;

    private HashTableParameters() {
        // static utility
    }

    /**
     * Nominal hash-table capacity derived from the index number:
     * {@code 1000 + (index % 9000)} = 5315 for 22081315.
     */
    public static int tableSize() {
        return 1000 + (int) (CHERYL_INDEX_NUMBER % 9000);
    }

    /**
     * Growth threshold derived from the last digit of the index number:
     * {@code 0.50 + (lastDigit * 0.05)} = 0.75 for 22081315.
     */
    public static double loadFactorThreshold() {
        int lastDigit = (int) (CHERYL_INDEX_NUMBER % 10);
        return 0.50 + (lastDigit * 0.05);
    }
}
