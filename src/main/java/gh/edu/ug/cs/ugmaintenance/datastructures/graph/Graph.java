package gh.edu.ug.cs.ugmaintenance.datastructures.graph;

import gh.edu.ug.cs.ugmaintenance.datastructures.hashtable.Map;
import gh.edu.ug.cs.ugmaintenance.datastructures.hashtable.Set;

import java.util.List;

public class Graph {

   
    private final Map<Integer, Set<Integer>> adjacencyList;

    
    public Graph() {
        adjacencyList = new Map<>();
    }

    
    public void addVertex(int locationId) {
        if (locationId < 0) {
            throw new IllegalArgumentException(
                    "Location ID cannot be negative: " + locationId
            );
        }

        if (!adjacencyList.containsKey(locationId)) {
            adjacencyList.put(locationId, new Set<>());
        }
    }

    
    public void addEdge(int fromLocationId, int toLocationId) {
        validateLocationId(fromLocationId);
        validateLocationId(toLocationId);

        if (fromLocationId == toLocationId) {
            throw new IllegalArgumentException(
                    "A location cannot have an edge to itself: " + fromLocationId
            );
        }

       
        addVertex(fromLocationId);
        addVertex(toLocationId);

        
        adjacencyList.get(fromLocationId).add(toLocationId);
        adjacencyList.get(toLocationId).add(fromLocationId);
    }

    
    public Set<Integer> getNeighbours(int locationId) {
        validateLocationId(locationId);

        Set<Integer> neighbours = adjacencyList.get(locationId);

        if (neighbours == null) {
            throw new IllegalArgumentException(
                    "Location does not exist in graph: " + locationId
            );
        }

        return neighbours;
    }

    
    public boolean containsVertex(int locationId) {
        validateLocationId(locationId);
        return adjacencyList.containsKey(locationId);
    }

    
    public boolean hasEdge(int fromLocationId, int toLocationId) {
        validateLocationId(fromLocationId);
        validateLocationId(toLocationId);

        if (!adjacencyList.containsKey(fromLocationId)) {
            return false;
        }

        return adjacencyList.get(fromLocationId).contains(toLocationId);
    }

    
    public boolean removeEdge(int fromLocationId, int toLocationId) {
        validateLocationId(fromLocationId);
        validateLocationId(toLocationId);

        if (!adjacencyList.containsKey(fromLocationId)
                || !adjacencyList.containsKey(toLocationId)) {
            return false;
        }

        boolean removedFromFirst =
                adjacencyList.get(fromLocationId).remove(toLocationId);

        boolean removedFromSecond =
                adjacencyList.get(toLocationId).remove(fromLocationId);

        return removedFromFirst || removedFromSecond;
    }

    public boolean removeVertex(int locationId) {
        validateLocationId(locationId);

        if (!adjacencyList.containsKey(locationId)) {
            return false;
        }

        
        Set<Integer> neighbours = adjacencyList.get(locationId);

        List<Integer> neighbourList = neighbours.toList();

        for (Integer neighbourId : neighbourList) {
            Set<Integer> neighbourSet = adjacencyList.get(neighbourId);

            if (neighbourSet != null) {
                neighbourSet.remove(locationId);
            }
        }

       
        adjacencyList.remove(locationId);

        return true;
    }

   
    public int size() {
        return adjacencyList.size();
    }

    
    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }

    
    public void clear() {
        adjacencyList.clear();
    }

    
    public List<Integer> getVertices() {
        return adjacencyList.keySet();
    }

    
    public int getDegree(int locationId) {
        return getNeighbours(locationId).size();
    }

    
    public void display() {
        System.out.println("Graph Adjacency List:");

        List<Integer> vertices = adjacencyList.keySet();

        for (Integer vertex : vertices) {
            System.out.println(
                    vertex + " -> " + adjacencyList.get(vertex).toList()
            );
        }
    }

    
    private void validateLocationId(int locationId) {
        if (locationId < 0) {
            throw new IllegalArgumentException(
                    "Location ID cannot be negative: " + locationId
            );
        }
    }
}
