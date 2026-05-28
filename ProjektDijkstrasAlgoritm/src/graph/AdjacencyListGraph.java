package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A weighted, undirected graph implementation based on an adjacency list.
 *
 * @param <T> the node type used in the graph
 */
public class AdjacencyListGraph<T> implements Graph<T> {

    private final Map<T, List<WeightedEdge<T>>> adjacencyList;

    /**
     * Creates an empty adjacency list graph.
     */
    public AdjacencyListGraph() {
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Adds a node to the graph if it does not already exist.
     *
     * @param node the node to add
     * @throws NullPointerException if the node is null
     */
    @Override
    public void addNode(final T node) {
        Objects.requireNonNull(node, "node must not be null");
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    /**
     * Adds an undirected weighted edge between two nodes.
     * If the nodes do not already exist, they are added first. If an edge
     * already exists between the nodes, the old edge is replaced with the new
     * weight instead of creating a duplicate edge.
     *
     * @param from the first node
     * @param to the second node
     * @param weight the edge weight
     * @throws NullPointerException if either node is null
     * @throws IllegalArgumentException if the weight is negative or not a number
     */
    @Override
    public void addEdge(final T from,
                        final T to,
                        final double weight) {
        Objects.requireNonNull(from, "from must not be null");
        Objects.requireNonNull(to, "to must not be null");

        if (Double.isNaN(weight) || weight < 0) {
            throw new IllegalArgumentException(
                    "weight must be a non-negative number"
            );
        }

        addNode(from);
        addNode(to);

        removeEdge(from, to);

        adjacencyList.get(from).add(new WeightedEdge<>(from, to, weight));
        adjacencyList.get(to).add(new WeightedEdge<>(to, from, weight));
    }

    /**
     * Removes a node and all edges connected to it.
     *
     * @param node the node to remove
     * @throws NullPointerException if the node is null
     */
    @Override
    public void removeNode(final T node) {
        Objects.requireNonNull(node, "node must not be null");
        adjacencyList.remove(node);

        for (List<WeightedEdge<T>> edges : adjacencyList.values()) {
            edges.removeIf(edge -> edge.getTo().equals(node));
        }
    }

    /**
     * Removes the edge between two nodes.
     *
     * @param from the first node
     * @param to the second node
     * @throws NullPointerException if either node is null
     */
    @Override
    public void removeEdge(final T from,
                           final T to) {
        Objects.requireNonNull(from, "from must not be null");
        Objects.requireNonNull(to, "to must not be null");

        if (adjacencyList.containsKey(from)) {
            adjacencyList.get(from).removeIf(edge -> edge.getTo().equals(to));
        }

        if (adjacencyList.containsKey(to)) {
            adjacencyList.get(to).removeIf(edge -> edge.getTo().equals(from));
        }
    }

    /**
     * Gets all nodes in the graph.
     *
     * @return a list of all nodes
     */
    @Override
    public List<T> getNodes() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    /**
     * Gets all edges from a specific node.
     *
     * @param node the node to get edges from
     * @return a list of outgoing edges
     * @throws NullPointerException if the node is null
     */
    @Override
    public List<WeightedEdge<T>> getEdgesFrom(final T node) {
        Objects.requireNonNull(node, "node must not be null");

        if (!adjacencyList.containsKey(node)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(adjacencyList.get(node));
    }

    /**
     * Checks if the graph contains a node.
     *
     * @param node the node to check
     * @return true if the node exists, otherwise false
     * @throws NullPointerException if the node is null
     */
    @Override
    public boolean contains(final T node) {
        Objects.requireNonNull(node, "node must not be null");
        return adjacencyList.containsKey(node);
    }

    /**
     * Checks whether an edge exists between two nodes.
     *
     * @param from the first node
     * @param to the second node
     * @return true if the edge exists, otherwise false
     * @throws NullPointerException if either node is null
     */
    @Override
    public boolean hasEdge(final T from, final T to) {
        Objects.requireNonNull(from, "from must not be null");
        Objects.requireNonNull(to, "to must not be null");

        if (!adjacencyList.containsKey(from)) {
            return false;
        }

        for (WeightedEdge<T> edge : adjacencyList.get(from)) {
            if (edge.getTo().equals(to)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the weight of an edge between two nodes.
     *
     * @param from the first node
     * @param to the second node
     * @return the edge weight
     * @throws NullPointerException if either node is null
     * @throws IllegalArgumentException if the edge does not exist
     */
    @Override
    public double getWeight(final T from, final T to) {
        Objects.requireNonNull(from, "from must not be null");
        Objects.requireNonNull(to, "to must not be null");

        if (!adjacencyList.containsKey(from)) {
            throw new IllegalArgumentException("edge does not exist");
        }

        for (WeightedEdge<T> edge : adjacencyList.get(from)) {
            if (edge.getTo().equals(to)) {
                return edge.getWeight();
            }
        }

        throw new IllegalArgumentException("edge does not exist");
    }

    /**
     * Gets the number of nodes in the graph.
     *
     * @return the number of nodes
     */
    @Override
    public int size() {
        return adjacencyList.size();
    }

    /**
     * Gets the number of undirected edges in the graph.
     *
     * @return the number of edges
     */
    @Override
    public int edgeCount() {
        int directedEdgeCount = 0;

        for (List<WeightedEdge<T>> edges : adjacencyList.values()) {
            directedEdgeCount += edges.size();
        }

        return directedEdgeCount / 2;
    }
}