package graph;

import java.util.List;

/**
 * Defines the operations required for a weighted graph.
 *
 * @param <T> the node type used in the graph
 */
public interface Graph<T> {

    /**
     * Adds a node to the graph.
     *
     * @param node the node to add
     */
    void addNode(T node);

    /**
     * Adds a weighted edge between two nodes.
     *
     * @param from the start node
     * @param to the destination node
     * @param weight the edge weight
     */
    void addEdge(
            T from,
            T to,
            double weight
    );

    /**
     * Removes a node from the graph.
     *
     * @param node the node to remove
     */
    void removeNode(T node);

    /**
     * Removes an edge between two nodes.
     *
     * @param from the start node
     * @param to the destination node
     */
    void removeEdge(
            T from,
            T to
    );

    /**
     * Gets all nodes in the graph.
     *
     * @return a list of all nodes
     */
    List<T> getNodes();

    /**
     * Gets all outgoing edges from a node.
     *
     * @param node the node to get edges from
     * @return a list of outgoing weighted edges
     */
    List<WeightedEdge<T>> getEdgesFrom(T node);

    /**
     * Checks whether the graph contains a node.
     *
     * @param node the node to check
     * @return true if the node exists in the graph, otherwise false
     */
    boolean contains(T node);

    /**
     * Checks whether an edge exists between two nodes.
     *
     * @param from the start node
     * @param to the destination node
     * @return true if the edge exists, otherwise false
     */
    boolean hasEdge(T from, T to);

    /**
     * Gets the weight of an edge between two nodes.
     *
     * @param from the start node
     * @param to the destination node
     * @return the edge weight
     */
    double getWeight(T from, T to);

    /**
     * Gets the number of nodes in the graph.
     *
     * @return the number of nodes
     */
    int size();

    /**
     * Gets the number of edges in the graph.
     *
     * @return the number of edges
     */
    int edgeCount();
}