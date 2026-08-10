package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

import java.time.LocalDateTime;
import java.util.List;

import gh.edu.ug.cs.ugmaintenance.models.Location;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.enums.LocationType;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;

/**
 * Console demonstration of the custom {@link HashTable}, {@link Set} and
 * {@link Map} applied to UG campus maintenance data — the membership and
 * lookup use case required by the assignment (Section 6).
 *
 * <p>Shows five operations:</p>
 * <ol>
 *   <li>a {@link Map} indexing locations by id (O(1) name lookup);</li>
 *   <li>a {@link Map} indexing service requests by id (O(1) request lookup);</li>
 *   <li>a {@link Set} of service categories with open requests (deduplication
 *       and membership tests);</li>
 *   <li>a {@link Map} counting open requests per campus location;</li>
 *   <li>a hash-table insertion trace showing bucket assignment, collisions
 *       and the final statistics (trace-table evidence for the report).</li>
 * </ol>
 *
 * <p>Table sizes come from {@link HashTableParameters} (derived from the
 * index number), so the trace output is team-specific as the assignment
 * requires.</p>
 */
public class HashTableDemo {

    public static void main(String[] args) {
        runDemo();
    }

    public static void runDemo() {
        System.out.println();
        System.out.println("==============================================================");
        System.out.println("  MEMBER 9 DEMO  -  Hash Table, Set, Map");
        System.out.println("  Cheryl Abena Asantewaa Kwakye  |  index " + HashTableParameters.CHERYL_INDEX_NUMBER);
        System.out.println("  Derived parameters: nominal table size = " + HashTableParameters.tableSize()
                + ", load-factor threshold = " + HashTableParameters.loadFactorThreshold());
        System.out.println("==============================================================");

        List<Location> locations = sampleLocations();
        List<ServiceRequest> requests = sampleRequests();

        demoLocationLookup(locations);
        demoRequestIndex(requests);
        demoOpenCategorySet(requests);
        demoCountsByLocation(requests, locations);
        demoInsertionTrace(requests);

        System.out.println("Demo complete.");
        System.out.println();
    }

    // ------------------------------------------------------------------
    // 1. Location lookup map  (Map<Integer, Location>)
    // ------------------------------------------------------------------

    private static void demoLocationLookup(List<Location> locations) {
        System.out.println();
        System.out.println("--- 1. Location lookup index (Map<Integer, Location>) ---");
        Map<Integer, Location> index = new Map<>();
        for (Location location : locations) {
            index.put(location.getLocationId(), location);
        }

        System.out.println("Indexed " + index.size() + " campus locations.");
        for (int id : new int[]{29, 58, 12, 999}) {
            Location found = index.get(id);
            System.out.println("  Lookup locationId " + id + " -> "
                    + (found == null ? "NOT FOUND" : found.getLocationName() + " (" + found.getLocationType().getDbValue() + ")"));
        }
        System.out.println("  containsKey(58) = " + index.containsKey(58)
                + " | containsKey(999) = " + index.containsKey(999));
    }

    // ------------------------------------------------------------------
    // 2. Request lookup map  (Map<Integer, ServiceRequest>)
    // ------------------------------------------------------------------

    private static void demoRequestIndex(List<ServiceRequest> requests) {
        System.out.println();
        System.out.println("--- 2. Request lookup index (Map<Integer, ServiceRequest>) ---");
        Map<Integer, ServiceRequest> index = new Map<>();
        for (ServiceRequest request : requests) {
            index.put(request.getRequestId(), request);
        }

        System.out.println("Indexed " + index.size() + " service requests (lookup in O(1) expected).");
        for (int id : new int[]{1, 5, 10}) {
            ServiceRequest found = index.get(id);
            System.out.println("  Request " + id + " -> "
                    + (found == null ? "NOT FOUND" : "\"" + found.getRequestTitle()
                    + "\" urgency=" + found.getUrgencyLevel()
                    + " status=" + found.getStatus()));
        }
        System.out.println("  Removing request 3 ... " + index.remove(3).getRequestTitle());
        System.out.println("  Index size after removal = " + index.size());
    }

    // ------------------------------------------------------------------
    // 3. Set of categories with open work  (Set<String>)
    // ------------------------------------------------------------------

    private static void demoOpenCategorySet(List<ServiceRequest> requests) {
        System.out.println();
        System.out.println("--- 3. Open service categories (Set<String>) ---");
        Set<String> openCategories = new Set<>();
        for (ServiceRequest request : requests) {
            if (request.getStatus() != RequestStatus.COMPLETED
                    && request.getStatus() != RequestStatus.CANCELLED) {
                openCategories.add(categoryName(request.getCategoryId()));
            }
        }

        System.out.println("Distinct categories with open work (" + openCategories.size() + "):");
        for (Object category : openCategories.toArray()) {
            System.out.println("  - " + category);
        }
        System.out.println("  Membership tests -> contains \"Plumbing\": " + openCategories.contains("Plumbing")
                + " | contains \"Electrical\": " + openCategories.contains("Electrical")
                + " | contains \"ICT/Network\": " + openCategories.contains("ICT/Network"));
    }

    // ------------------------------------------------------------------
    // 4. Open request counts per location  (Map<String, Integer>)
    // ------------------------------------------------------------------

    private static void demoCountsByLocation(List<ServiceRequest> requests, List<Location> locations) {
        System.out.println();
        System.out.println("--- 4. Open requests per location (Map<String, Integer>) ---");
        Map<Integer, Location> locationIndex = new Map<>();
        for (Location location : locations) {
            locationIndex.put(location.getLocationId(), location);
        }

        Map<String, Integer> counts = new Map<>();
        for (ServiceRequest request : requests) {
            if (request.getStatus() == RequestStatus.COMPLETED
                    || request.getStatus() == RequestStatus.CANCELLED) {
                continue;
            }
            Location location = locationIndex.get(request.getLocationId());
            String name = location == null ? "Unknown location" : location.getLocationName();
            Integer previous = counts.get(name);
            counts.put(name, (previous == null ? 0 : previous) + 1);
        }

        System.out.println("Open requests by campus location:");
        for (Object name : counts.keySet().toArray()) {
            System.out.println("  " + name + " : " + counts.get((String) name));
        }
    }

    // ------------------------------------------------------------------
    // 5. Insertion trace  (HashTable<Integer, String>)
    // ------------------------------------------------------------------

    private static void demoInsertionTrace(List<ServiceRequest> requests) {
        System.out.println();
        System.out.println("--- 5. Hash-table insertion trace (team-specific parameters) ---");
        int nominalSize = HashTableParameters.tableSize();
        HashTable<Integer, String> table =
                new HashTable<>(nominalSize, HashTableParameters.loadFactorThreshold());
        System.out.println("Nominal table size (from index number) = " + nominalSize
                + " | actual capacity (power of two) = " + table.capacity()
                + " | load-factor threshold = " + HashTableParameters.loadFactorThreshold());

        for (ServiceRequest request : requests) {
            table.put(request.getRequestId(), request.getRequestTitle());
        }

        System.out.println("Inserting request id -> title ...");
        for (ServiceRequest request : requests) {
            System.out.println("  put(" + request.getRequestId() + ", \""
                    + request.getRequestTitle() + "\")  -> bucket " + findBucket(table, request.getRequestId()));
        }

        System.out.println("Bucket contents (non-empty buckets):");
        for (int i = 0; i < table.capacity(); i++) {
            HashTable.HashEntry<Integer, String> entry = table.bucket(i);
            if (entry != null) {
                StringBuilder chain = new StringBuilder("  bucket " + i + ":");
                for (HashTable.HashEntry<Integer, String> e = entry; e != null; e = e.next) {
                    chain.append(" [").append(e.key).append(" -> ").append(e.value).append(']');
                }
                System.out.println(chain);
            }
        }

        System.out.println("Final statistics -> " + table);
    }

    private static int findBucket(HashTable<Integer, String> table, int key) {
        for (int i = 0; i < table.capacity(); i++) {
            for (HashTable.HashEntry<Integer, String> entry = table.bucket(i); entry != null; entry = entry.next) {
                if (entry.key == key) {
                    return i;
                }
            }
        }
        return -1;
    }

    // ------------------------------------------------------------------
    // Sample data (mirrors the seed CSV flavour: UG campus locations)
    // ------------------------------------------------------------------

    private static String categoryName(int categoryId) {
        return switch (categoryId) {
            case 1 -> "Plumbing";
            case 2 -> "Electrical";
            case 3 -> "ICT/Network";
            case 4 -> "Furniture";
            case 5 -> "Drainage";
            case 6 -> "Security";
            default -> "General";
        };
    }

    private static List<Location> sampleLocations() {
        return List.of(
                new Location(58, "Legon Hall", LocationType.HALL, "Student residence hall", LocalDateTime.of(2026, 1, 10, 8, 57)),
                new Location(29, "Balme Library", LocationType.LIBRARY, "Main university library", LocalDateTime.of(2026, 1, 10, 8, 28)),
                new Location(12, "JQB (Jones Quartey Building)", LocationType.LECTURE_HALL, "Academic building", LocalDateTime.of(2026, 1, 10, 8, 11)),
                new Location(2, "Computer Science Department", LocationType.DEPARTMENT, "Campus academic department", LocalDateTime.of(2026, 1, 10, 8, 1)),
                new Location(57, "Akuafo Hall", LocationType.HALL, "Student residence hall", LocalDateTime.of(2026, 1, 10, 8, 57)),
                new Location(68, "UG Clinic", LocationType.OFFICE, "Campus health center", LocalDateTime.of(2026, 1, 10, 9, 7)),
                new Location(39, "Volta Hall", LocationType.HALL, "Student residence hall", LocalDateTime.of(2026, 1, 10, 8, 38)),
                new Location(64, "Mensah Sarbah Hall", LocationType.HALL, "Student residence hall", LocalDateTime.of(2026, 1, 10, 9, 3)),
                new Location(66, "Central Cafeteria (CC)", LocationType.OFFICE, "Campus dining facility", LocalDateTime.of(2026, 1, 10, 9, 5)),
                new Location(49, "GCB", LocationType.LECTURE_HALL, "Lecture hall complex", LocalDateTime.of(2026, 1, 10, 8, 48))
        );
    }

    private static List<ServiceRequest> sampleRequests() {
        return List.of(
                new ServiceRequest(1, 1, 29, 1, "Leaking pipe in library", "Water leakage near the study area", 4, RequestStatus.PENDING, LocalDateTime.of(2026, 2, 1, 8, 0), null),
                new ServiceRequest(2, 2, 12, 2, "Power outage in JQB", "Several lights are off in the lecture hall", 4, RequestStatus.ASSIGNED, LocalDateTime.of(2026, 2, 2, 9, 30), null),
                new ServiceRequest(3, 7, 57, 5, "Blocked drain at Akuafo Hall", "Drainage blocked near the dormitory entrance", 2, RequestStatus.IN_PROGRESS, LocalDateTime.of(2026, 2, 3, 10, 15), null),
                new ServiceRequest(4, 4, 2, 3, "Computer lab network issue", "Students cannot access the lab network", 5, RequestStatus.COMPLETED, LocalDateTime.of(2026, 2, 4, 11, 0), LocalDateTime.of(2026, 2, 4, 14, 30)),
                new ServiceRequest(5, 8, 66, 4, "Broken chair in cafeteria", "Dining chairs are damaged and unstable", 1, RequestStatus.PENDING, LocalDateTime.of(2026, 2, 5, 12, 30), null),
                new ServiceRequest(6, 5, 68, 6, "UG Clinic gate malfunction", "Gate access card reader is failing", 4, RequestStatus.CANCELLED, LocalDateTime.of(2026, 2, 6, 13, 0), null),
                new ServiceRequest(7, 3, 58, 2, "Faulty socket in Legon Hall", "Sparks from a wall socket in a room", 5, RequestStatus.PENDING, LocalDateTime.of(2026, 2, 7, 7, 45), null),
                new ServiceRequest(8, 6, 39, 1, "Burst pipe in Volta Hall", "Water running down the corridor", 5, RequestStatus.ASSIGNED, LocalDateTime.of(2026, 2, 8, 6, 20), null),
                new ServiceRequest(9, 9, 64, 5, "Flooded bathroom Mensah Sarbah", "Drain overflows after heavy rain", 3, RequestStatus.IN_PROGRESS, LocalDateTime.of(2026, 2, 9, 8, 50), null),
                new ServiceRequest(10, 10, 49, 3, "No internet at GCB", "Lecturers cannot connect to Wi-Fi", 2, RequestStatus.PENDING, LocalDateTime.of(2026, 2, 10, 9, 10), null)
        );
    }
}
