package gh.edu.ug.cs.ugmaintenance.algorithms.utils;

import java.io.FileWriter;
import java.io.IOException;

import gh.edu.ug.cs.ugmaintenance.datastructures.hashtable.HashTable;
import gh.edu.ug.cs.ugmaintenance.datastructures.hashtable.HashTableParameters;

/**
 * Empirical-efficiency experiment for the custom hash table (M10 lab,
 * Section 9: "Hash table load factor — 100 to 20,000 keys with different
 * table sizes"). Every configuration is run {@link #RUNS} times and the
 * averages are exported to {@code performance_member9_hash.csv}.
 *
 * <p>Three experiments:</p>
 * <ol>
 *   <li><b>Controlled load-factor sweep</b> — fixed table sizes with the
 *       growth threshold set to 1.0 (no resize), keys inserted up to load
 *       factors 0.25/0.50/0.75/0.90. Shows how collision count and insert
 *       time rise with load factor for an otherwise ideal hash.</li>
 *   <li><b>Key growth in a fixed table</b> — the table size and threshold
 *       come from {@link HashTableParameters} (team index number), so the
 *       growth curve and resize events are team-specific. 100 up to 20,000
 *       keys as required.</li>
 *   <li><b>Hash quality</b> — same size/load factors across three key types:
 *       consecutive {@code Integer}s (near-perfect spread), realistic
 *       {@code String} request ids (mild collisions that grow with load), and
 *       a deliberately poor key whose hash uses only 16 distinct values
 *       (severe clustering). Shows how hash quality and load factor drive
 *       collision count and insert time (evidence for the responsible-selection
 *       section of the report).</li>
 * </ol>
 *
 * <p>Machine specification (Section 9.ii) is printed to the console at the
 * start of the run.</p>
 */
public class HashTableBenchmark {

    /** Number of repetitions per configuration; averages are reported. */
    public static final int RUNS = 3;

    private static final String CSV_FILE = "performance_member9_hash.csv";

    private static final int[] TABLE_SIZES = {200, 500, 1000, 2000, 5000};
    private static final double[] LOAD_FACTORS = {0.25, 0.50, 0.75, 0.90};
    private static final int[] GROWTH_SIZES = {100, 500, 1000, 5000, 10000, 20000};

    public static void main(String[] args) throws IOException {
        runBenchmark();
    }

    /**
     * Runs the three experiments and writes the averaged results to
     * {@code performance_member9_hash.csv}. Safe to call from the console
     * menu.
     */
    public static void runBenchmark() throws IOException {
        System.out.println();
        System.out.println("==============================================================");
        System.out.println("  MEMBER 9 - HASH TABLE EFFICIENCY EXPERIMENT (M10)");
        System.out.println("  Index " + HashTableParameters.CHERYL_INDEX_NUMBER
                + " | derived table size " + HashTableParameters.tableSize()
                + " | derived threshold " + HashTableParameters.loadFactorThreshold());
        System.out.println("  Machine: " + System.getProperty("os.name")
                + " (" + System.getProperty("os.arch") + "), Java "
                + System.getProperty("java.version"));
        System.out.println("  Each configuration averaged over " + RUNS + " runs.");
        System.out.println("==============================================================");

        warmUpJit();

        try (FileWriter csv = new FileWriter(CSV_FILE)) {
            csv.write("Experiment,KeyType,TableSize,NominalLoadFactor,KeysInserted,"
                    + "CollisionsAvg,MaxBucketAvg,AvgTimePerInsertNs,ResizesAvg\n");

            experimentControlledLoadFactor(csv);
            experimentKeyGrowth(csv);
            experimentWeakVsGoodHash(csv);

            csv.flush();
        }
        System.out.println("CSV written to " + CSV_FILE);
    }

    /**
     * Inserts into throwaway tables before any timing so the JIT compiler
     * warms up the hash/put code paths; otherwise the first measured rows
     * would be inflated by interpreter + JIT costs and the small-N timings
     * would not be comparable to the large-N ones.
     */
    private static void warmUpJit() {
        HashTable<Integer, Integer> ints = new HashTable<>(1000, 1.0);
        HashTable<String, Integer> strings = new HashTable<>(1000, 1.0);
        HashTable<WeakHashKey, Integer> weak = new HashTable<>(1000, 1.0);
        for (int pass = 0; pass < 3; pass++) {
            for (int i = 0; i < 2000; i++) {
                ints.put(i, i);
                strings.put("REQ-" + i, i);
                weak.put(new WeakHashKey(i), i);
            }
            ints.clear();
            strings.clear();
            weak.clear();
        }
    }

    // ------------------------------------------------------------------
    // Experiment 1: controlled load-factor sweep (no resizing)
    //
    // Note: consecutive Integer keys spread so evenly through the bucket
    // mask that this dataset reports zero collisions at every load factor.
    // The collision-vs-load-factor story is carried by Experiment 3, which
    // compares Integer, realistic String and deliberately weak keys.
    // ------------------------------------------------------------------

    private static void experimentControlledLoadFactor(FileWriter csv) throws IOException {
        for (int size : TABLE_SIZES) {
            for (double loadFactor : LOAD_FACTORS) {
                int keys = (int) Math.round(loadFactor * size);
                long collisionsSum = 0;
                long maxBucketSum = 0;
                long timeSum = 0;

                for (int run = 0; run < RUNS; run++) {
                    HashTable<Integer, Integer> table = new HashTable<>(size, 1.0);
                    long start = System.nanoTime();
                    for (int i = 0; i < keys; i++) {
                        table.put(i, i);
                    }
                    long elapsed = System.nanoTime() - start;
                    collisionsSum += table.getCollisionCount();
                    maxBucketSum += table.getMaxBucketLength();
                    timeSum += elapsed;
                }

                Row row = new Row("LoadFactorSweep", "Integer", size, loadFactor, keys,
                        collisionsSum / (double) RUNS,
                        maxBucketSum / (double) RUNS,
                        (timeSum / RUNS) / Math.max(1, keys),
                        0);
                row.write(csv);
            }
        }
    }

    // ------------------------------------------------------------------
    // Experiment 2: key growth in a team-specific fixed table
    // ------------------------------------------------------------------

    private static void experimentKeyGrowth(FileWriter csv) throws IOException {
        int size = HashTableParameters.tableSize();
        double threshold = HashTableParameters.loadFactorThreshold();

        for (int keys : GROWTH_SIZES) {
            long collisionsSum = 0;
            long maxBucketSum = 0;
            long timeSum = 0;
            long resizesSum = 0;
            double loadFactorSum = 0;

            for (int run = 0; run < RUNS; run++) {
                HashTable<Integer, Integer> table = new HashTable<>(size, threshold);
                long start = System.nanoTime();
                for (int i = 0; i < keys; i++) {
                    table.put(i, i);
                }
                long elapsed = System.nanoTime() - start;
                collisionsSum += table.getCollisionCount();
                maxBucketSum += table.getMaxBucketLength();
                timeSum += elapsed;
                resizesSum += table.getResizeCount();
                loadFactorSum += table.loadFactor();
            }

            Row row = new Row("KeyGrowth", "Integer", size, loadFactorSum / RUNS, keys,
                    collisionsSum / (double) RUNS,
                    maxBucketSum / (double) RUNS,
                    (timeSum / RUNS) / Math.max(1, keys),
                    resizesSum / (double) RUNS);
            row.write(csv);
        }
    }

    // ------------------------------------------------------------------
    // Experiment 3: weak hash vs good hash
    // ------------------------------------------------------------------

    private static void experimentWeakVsGoodHash(FileWriter csv) throws IOException {
        int size = 1000;

        for (double loadFactor : LOAD_FACTORS) {
            int keys = (int) Math.round(loadFactor * size);

            // Ideal hash: consecutive Integer keys spread almost perfectly.
            long goodCollisions = 0;
            long goodTime = 0;
            // Realistic hash: String request ids collide mildly, and more as
            // the table fills.
            long realisticCollisions = 0;
            long realisticTime = 0;
            // Weak hash: only 16 distinct hash codes -> severe clustering.
            long weakCollisions = 0;
            long weakTime = 0;

            for (int run = 0; run < RUNS; run++) {
                HashTable<Integer, Integer> good = new HashTable<>(size, 1.0);
                long start = System.nanoTime();
                for (int i = 0; i < keys; i++) {
                    good.put(i, i);
                }
                goodTime += System.nanoTime() - start;
                goodCollisions += good.getCollisionCount();

                HashTable<String, Integer> realistic = new HashTable<>(size, 1.0);
                start = System.nanoTime();
                for (int i = 0; i < keys; i++) {
                    realistic.put("REQ-" + i, i);
                }
                realisticTime += System.nanoTime() - start;
                realisticCollisions += realistic.getCollisionCount();

                HashTable<WeakHashKey, Integer> weak = new HashTable<>(size, 1.0);
                start = System.nanoTime();
                for (int i = 0; i < keys; i++) {
                    weak.put(new WeakHashKey(i), i);
                }
                weakTime += System.nanoTime() - start;
                weakCollisions += weak.getCollisionCount();
            }

            new Row("HashQuality", "Integer(good)", size, loadFactor, keys,
                    goodCollisions / (double) RUNS, -1, (goodTime / RUNS) / Math.max(1, keys), 0).write(csv);
            new Row("HashQuality", "String(realistic)", size, loadFactor, keys,
                    realisticCollisions / (double) RUNS, -1, (realisticTime / RUNS) / Math.max(1, keys), 0).write(csv);
            new Row("HashQuality", "id%16(weak)", size, loadFactor, keys,
                    weakCollisions / (double) RUNS, -1, (weakTime / RUNS) / Math.max(1, keys), 0).write(csv);
        }
    }

    /** A key type deliberately given a poor hash: only 16 distinct codes. */
    private static final class WeakHashKey {
        private final int id;

        WeakHashKey(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return id % 16; // deliberate: severe clustering
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof WeakHashKey)) {
                return false;
            }
            return ((WeakHashKey) other).id == id;
        }
    }

    // ------------------------------------------------------------------
    // Output row
    // ------------------------------------------------------------------

    private static final class Row {
        final String experiment;
        final String keyType;
        final int tableSize;
        final double loadFactor;
        final int keys;
        final double collisions;
        final double maxBucket;
        final double avgTimeNs;
        final double resizes;

        Row(String experiment, String keyType, int tableSize, double loadFactor, int keys,
            double collisions, double maxBucket, double avgTimeNs, double resizes) {
            this.experiment = experiment;
            this.keyType = keyType;
            this.tableSize = tableSize;
            this.loadFactor = loadFactor;
            this.keys = keys;
            this.collisions = collisions;
            this.maxBucket = maxBucket;
            this.avgTimeNs = avgTimeNs;
            this.resizes = resizes;
        }

        void write(FileWriter csv) throws IOException {
            String maxBucketCell = maxBucket < 0 ? "n/a" : String.format("%.1f", maxBucket);
            csv.write(experiment + "," + keyType + "," + tableSize + "," + loadFactor + ","
                    + keys + "," + collisions + "," + maxBucketCell + "," + avgTimeNs + ","
                    + resizes + "\n");
            System.out.printf("  %-15s %-12s size=%-5d L=%-5.2f keys=%-6d "
                            + "collisions=%-7.1f maxBucket=%-6s time/insert=%-8.1fns resizes=%s%n",
                    experiment, keyType, tableSize, loadFactor, keys, collisions,
                    maxBucketCell, avgTimeNs, resizes == 0 ? "0" : String.format("%.1f", resizes));
        }
    }
}
