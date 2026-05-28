package algorithm;

import graph.Graph;

/**
 * Defines an algorithm for finding shortest paths in a graph.
 *
 * @param <T> the node type used in the graph
 */
public interface ShortestPathAlgorithm<T> {

    /**
     * Finds the shortest paths from a start node to all reachable nodes.
     *
     * @param graph the graph to search in
     * @param startNode the node to start from
     * @return the shortest path result
     */
    ShortestPathResult<T>
    findShortestPaths(
            Graph<T> graph,
            T startNode
    );

}