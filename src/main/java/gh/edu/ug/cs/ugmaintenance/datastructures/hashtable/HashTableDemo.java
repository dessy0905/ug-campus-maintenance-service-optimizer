package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Runnable demonstration of the custom Hash Table, Set and Map
 * (member #9 - Cheryl Abena Asantewaa Kwakye).
 *
 * <p>Run: {@code java gh.edu.ug.cs.ugmaintenance.datastructures.hashtable.HashTableDemo}</p>
 *
 * <p>Shows:</p>
 * <ol>
 *   <li>A campus look-up index built with {@link HashTable}.</li>
 *   <li>Set membership and set algebra on technician specialisations.</li>
 *   <li>A technician-vehicle {@link Map}.</li>
 *   <li>Collision statistics for different load factors (Section 9 experiment).</li>
 *   <li>Insert and look-up timing as the number of keys grows.</li>
 * </ol>
 */
public class HashTableDemo {

    public static void main(String[] args) {
        System.out.println("=== UG Campus Maintenance - Hash Table, Set & Map Demo (Member 9) ===\n");

        demonstrateHashTableIndex();
        demonstrateSet();
        demonstrateMap();
        collisionExperiment();
        timingExperiment();
    }

    /** Prints the machine specification (Section 9 requires stating it with results). */
    private static void printMachineSpec() {
        System.out.println("Machine specification: "
                + System.getProperty("os.name") + " " + System.getProperty("os.arch")
                + ", Java " + System.getProperty("java.version")
                + ", " + Runtime.getRuntime().availableProcessors() + " cores");
    }

    /** Look-up index: locationId -> campus location name. */
    private static void demonstrateHashTableIndex() {
        System.out.println("--- 1. HashTable as a campus look-up index (locationId -> name) ---");
        HashTable<Integer, String> locations = new HashTable<>();
        locations.put(101, "Balme Library");
        locations.put(102, "Legon Hall");
        locations.put(103, "CS Department - JQB");
        locations.put(104, "UG Main Gate");
        locations.put(105, "Night Market");

        System.out.println("get(103)      = " + locations.get(103));
        System.out.println("get(999)      = " + locations.get(999) + " (absent)");
        System.out.println("containsKey(102) = " + locations.containsKey(102));

        // Overwrite an existing key and see the previous value returned.
        System.out.println("put(101, 'Balme Library Annex') returned old value: "
                + locations.put(101, "Balme Library Annex"));
        System.out.println("get(101)      = " + locations.get(101));

        locations.remove(105);
        System.out.println("after remove(105), containsKey(105) = " + locations.containsKey(105));
        System.out.println("size          = " + locations.size());
        locations.display();
        System.out.println();
    }

    /** Set membership and set algebra on technician specialisations. */
    private static void demonstrateSet() {
        System.out.println("--- 2. Set: technician specialisations (membership + set algebra) ---");
        Set<String> electrical = new Set<>();
        electrical.add("Kwame");
        electrical.add("Ama");
        electrical.add("Kofi");

        Set<String> plumbing = new Set<>();
        plumbing.add("Ama");
        plumbing.add("Esi");
        plumbing.add("Yaw");

        System.out.println("electrical = " + electrical.toList());
        System.out.println("plumbing   = " + plumbing.toList());
        System.out.println("electrical.contains(\"Kwame\") = " + electrical.contains("Kwame"));
        System.out.println("add duplicate 'Ama' again      = " + electrical.add("Ama") + " (false = already present)");
        System.out.println("union          = " + electrical.union(plumbing).toList());
        System.out.println("intersection   = " + electrical.intersection(plumbing).toList());
        System.out.println("difference     = " + electrical.difference(plumbing).toList());
        System.out.println();
    }

    /** Map: technician -> assigned vehicle (constant-time look-up). */
    private static void demonstrateMap() {
        System.out.println("--- 3. Map: technician -> assigned vehicle ---");
        Map<String, String> vehicles = new Map<>();
        vehicles.put("Kwame", "Toyota Hilux - GH 2031-20");
        vehicles.put("Ama",   "Kia Bongo Truck - GH 1144-21");
        vehicles.put("Esi",   "Motorcycle - GH 8822-19");

        System.out.println("vehicles.get(\"Kwame\") = " + vehicles.get("Kwame"));
        System.out.println("getOrDefault(\"Yaw\", \"No vehicle\") = "
                + vehicles.getOrDefault("Yaw", "No vehicle"));
        System.out.println("putIfAbsent(\"Ama\", \"New van\") = " + vehicles.putIfAbsent("Ama", "New van")
                + " (existing value kept)");
        System.out.println("keySet   = " + vehicles.keySet());
        System.out.println("values   = " + vehicles.values());
        vehicles.display();
        System.out.println();
    }

    /**
     * Section 9 experiment: collision statistics for different load factors.
     *
     * Part A keeps the bucket count fixed (threshold 1.0, so no resize) and
     * inserts more String keys, so the load factor rises from 0.25 to 1.0 and
     * the collision count grows with it.
     *
     * Part B shows why automatic resizing matters: 10,000 keys under the
     * default threshold end in a table whose load factor stays bounded.
     */
    private static void collisionExperiment() {
        System.out.println("--- 4. Load factor vs collision statistics (String keys) ---");
        int capacity = 2_000;
        int[] keyCounts = {500, 1_000, 1_500, 2_000}; // load factors 0.25 .. 1.00

        System.out.println("capacity | keys  | loadFactor | collisions | maxChain | avgChain");
        for (int count : keyCounts) {
            HashTable<String, String> table = new HashTable<>(capacity, 1.0); // no resize
            for (int i = 0; i < count; i++) {
                table.put("loc-" + i, "Location " + i);
            }
            System.out.printf("%8d | %5d | %10.3f | %10d | %8d | %8.3f%n",
                    capacity,
                    count,
                    table.getLoadFactor(),
                    table.getCollisionCount(),
                    table.getMaxChainLength(),
                    table.getAverageChainLength());
        }
        System.out.println("(Same bucket count, more keys -> higher load factor -> more collisions.)");
        System.out.println();

        System.out.println("--- 4b. Auto-resize keeps the load factor bounded ---");
        HashTable<String, String> resizing = new HashTable<>(4); // tiny start
        for (int i = 0; i < 10_000; i++) {
            resizing.put("loc-" + i, "Location " + i);
        }
        System.out.printf("10,000 keys, threshold %.2f -> final capacity %d, load factor %.3f, collisions %d, maxChain %d%n",
                resizing.getLoadFactorThreshold(),
                resizing.getCapacity(),
                resizing.getLoadFactor(),
                resizing.getCollisionCount(),
                resizing.getMaxChainLength());
        System.out.println("All " + resizing.size() + " keys were still retrievable after resizing: "
                + ("Location 9999".equals(resizing.get("loc-9999"))));
        System.out.println();
    }

    /**
     * Section 9 experiment: insert and look-up time as the input size grows.
     * Runs each size three times, reports the average, prints the machine
     * specification and exports the raw results to a CSV file.
     */
    private static void timingExperiment() {
        System.out.println("--- 5. Insert and look-up time vs input size (average of 3 runs) ---");
        printMachineSpec();

        int[] sizes = {100, 500, 1_000, 5_000, 10_000, 20_000};
        final int runs = 3;

        System.out.println("inputSize | avg insert ns/key | avg look-up ns/key");
        StringBuilder csv = new StringBuilder("InputSize,AvgInsertNsPerKey,AvgLookupNsPerKey\n");
        for (int size : sizes) {
            long insertNs = 0;
            long lookupNs = 0;
            for (int run = 0; run < runs; run++) {
                HashTable<Integer, Integer> table = new HashTable<>();
                long start = System.nanoTime();
                for (int i = 0; i < size; i++) {
                    table.put(i, i);
                }
                insertNs += System.nanoTime() - start;

                start = System.nanoTime();
                for (int i = 0; i < size; i++) {
                    table.get(i);
                }
                lookupNs += System.nanoTime() - start;
            }
            double avgInsert = (double) insertNs / runs / size;
            double avgLookup = (double) lookupNs / runs / size;
            System.out.printf("%9d | %17.0f | %18.0f%n", size, avgInsert, avgLookup);
            csv.append(size).append(',').append(avgInsert).append(',').append(avgLookup).append('\n');
        }

        try (FileWriter writer = new FileWriter("performance_member9_hashtable.csv")) {
            writer.write(csv.toString());
            System.out.println("Results exported to performance_member9_hashtable.csv");
        } catch (IOException e) {
            System.out.println("Could not export CSV: " + e.getMessage());
        }
        System.out.println();
        System.out.println("Note: plot the CSV with Excel/Python for the efficiency lab (Section 9/10).");
    }
}
