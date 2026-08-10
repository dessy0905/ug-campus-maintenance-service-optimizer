# UG Campus Maintenance Service Optimizer

A Java-based Data Structures and Algorithms project that optimizes maintenance operations across the University of Ghana campus using custom data structures, graph algorithms, database integration, and performance analysis.

---

## Project Overview

The **UG Campus Maintenance Service Optimizer** is designed to improve the management of maintenance requests originating from various locations across the University of Ghana, including lecture halls, hostels, libraries, laboratories, administrative offices, and other campus facilities.

The system demonstrates the practical application of Data Structures and Algorithms by implementing custom data structures and algorithms to efficiently manage maintenance requests, assign technicians, optimize routes, and analyze algorithm performance.

---

## Features

- Register maintenance requests
- Manage technicians and maintenance resources
- Prioritize requests based on urgency
- Search and sort maintenance records
- Calculate shortest routes across campus
- Analyze campus connectivity
- Measure algorithm performance
- Generate maintenance reports

---

## Technologies

- Java 17
- Maven
- MySQL
- Git & GitHub
- JUnit 5

---

## Project Structure

```
src
├── main
│   ├── java
│   │   └── gh.edu.ug.cs.ugmaintenance
│   │       ├── algorithms
│   │       ├── datastructures
│   │       ├── database
│   │       ├── models
│   │       ├── services
│   │       ├── ui
│   │       └── utils
│   └── resources
└── test
```

---

## Core Data Structures

- Dynamic Array
- Linked List
- Stack
- Queue
- Circular Queue
- Deque
- Priority Queue
- Heap
- Binary Search Tree
- Red-Black Tree
- B-Tree
- Hash Table
- Graph
- Disjoint Set

---

## Hash Table, Set and Map (Member 9 - Cheryl Abena Asantewaa Kwakye)

Implemented from scratch (no `java.util.HashMap`/`HashSet` used) under
`src/main/java/gh/edu/ug/cs/ugmaintenance/datastructures/hashtable/`:

| Class | Description | Key operations |
|---|---|---|
| `HashTable<K, V>` | Core hash table with **separate chaining** for collisions and automatic **resize/rehash** when the load factor exceeds a configurable threshold (default 0.75). Exposes collision statistics for the efficiency lab. | `put`, `get`, `remove`, `containsKey`, `containsValue`, `clear`, `getLoadFactor`, `getCollisionCount`, `getMaxChainLength`, `getAverageChainLength` |
| `Set<T>` | Set built on top of `HashTable` (elements stored as keys). | `add`, `remove`, `contains`, `union`, `intersection`, `difference`, `isSubsetOf` |
| `Map<K, V>` | Map built on top of `HashTable` (thin delegation with map conveniences). | `put`, `get`, `getOrDefault`, `putIfAbsent`, `remove`, `keySet`, `values` |

### Run the demo

```bash
mvn compile
java -cp target/classes gh.edu.ug.cs.ugmaintenance.datastructures.hashtable.HashTableDemo
```

The demo shows a campus location look-up index, set algebra on technician
specialisations, a technician-to-vehicle map, load factor vs collision
statistics, and insert/look-up timing that is exported to
`performance_member9_hashtable.csv`.

### Run the tests

```bash
mvn test -Dtest=HashTableTest
```

41 unit tests cover normal, boundary and invalid-input cases (null keys,
collisions, resize/rehash, empty and single-element tables).

---

## Algorithms

### Searching

- Linear Search
- Binary Search

### Sorting

- Selection Sort
- Insertion Sort
- Merge Sort
- Quick Sort

### Graph

- BFS
- DFS
- Dijkstra
- Prim
- Kruskal

### Optimization

- Greedy Algorithms
- Dynamic Programming

---

## Team Workflow

- Each member develops on a separate feature branch.
- Pull Requests are reviewed before merging.
- The `main` branch always contains stable code.

---

## Development Team

Group 17 - One Stack

---

## License

This project is developed solely for academic purposes.