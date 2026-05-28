package algorithm;

import graph.Graph;
import graph.WeightedEdge;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * Finds shortest paths in a weighted graph using Dijkstra's algorithm.
 *
 * <p>The algorithm assumes that all edge weights are non-negative. The graph
 * implementation should prevent negative weights, but this class also checks
 * encountered edges to avoid producing incorrect results.</p>
 *
 * @param <T> the node type used in the graph
 */
public class DijkstraShortestPath<T> implements ShortestPathAlgorithm<T> {

    /**
     * Finds the shortest paths from one start node to all other reachable nodes
     * in the graph.
     *
     * @param graph the graph to search in
     * @param startNode the node where the search starts
     * @return an object containing distances and previous-node links
     * @throws NullPointerException if the graph or start node is null
     * @throws IllegalArgumentException if the start node is not in the graph or
     *                                  if a negative edge weight is found
     */
    @Override
    public ShortestPathResult<T> findShortestPaths(final Graph<T> graph,
                                                   final T startNode) {
        Objects.requireNonNull(graph, "graph must not be null");
        Objects.requireNonNull(startNode, "startNode must not be null");

        if (!graph.contains(startNode)) {
            throw new IllegalArgumentException("start node must exist in graph");
        }

        Map<T, Double> distances = createInitialDistances(graph, startNode);
        Map<T, T> previousNodes = new HashMap<>();
        PriorityQueue<NodeDistance<T>> queue = new PriorityQueue<>();

        queue.add(new NodeDistance<>(startNode, 0.0));

        while (!queue.isEmpty()) {
            NodeDistance<T> current = queue.poll();

            if (current.getDistance() > distances.get(current.getNode())) {
                continue;
            }

            updateNeighborDistances(
                    graph,
                    current.getNode(),
                    distances,
                    previousNodes,
                    queue
            );
        }

        return new ShortestPathResult<>(distances, previousNodes);
    }

    /**
     * Creates the initial distance map used by Dijkstra's algorithm.
     *
     * @param graph the graph whose nodes should be initialized
     * @param startNode the start node that receives distance zero
     * @return a map containing the initial distance for each node
     */
    private Map<T, Double> createInitialDistances(final Graph<T> graph,
                                                  final T startNode) {
        Map<T, Double> distances = new HashMap<>();

        for (T node : graph.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }

        distances.put(startNode, 0.0);
        return distances;
    }

    /**
     * Relaxes all outgoing edges from a node and updates the priority queue when
     * a shorter path is found.
     *
     * @param graph the graph being searched
     * @param currentNode the node whose outgoing edges should be checked
     * @param distances the current shortest known distances
     * @param previousNodes the previous-node map used to rebuild paths
     * @param queue the priority queue used by Dijkstra's algorithm
     * @throws IllegalArgumentException if a negative edge weight is found
     */
    private void updateNeighborDistances(
            final Graph<T> graph,
            final T currentNode,
            final Map<T, Double> distances,
            final Map<T, T> previousNodes,
            final PriorityQueue<NodeDistance<T>> queue) {

        for (WeightedEdge<T> edge : graph.getEdgesFrom(currentNode)) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException(
                        "Dijkstra's algorithm does not allow negative weights"
                );
            }

            T neighbor = edge.getTo();
            double newDistance = distances.get(currentNode) + edge.getWeight();

            if (newDistance < distances.get(neighbor)) {
                distances.put(neighbor, newDistance);
                previousNodes.put(neighbor, currentNode);
                queue.add(new NodeDistance<>(neighbor, newDistance));
            }
        }
    }

    /**
     * Stores a node together with its currently known distance.
     *
     * @param <T> the node type
     */
    private static class NodeDistance<T>
            implements Comparable<NodeDistance<T>> {

        private final T node;
        private final double distance;

        /**
         * Creates a node-distance pair.
         *
         * @param node the node
         * @param distance the currently known distance to the node
         */
        NodeDistance(final T node, final double distance) {
            this.node = node;
            this.distance = distance;
        }

        /**
         * Gets the node.
         *
         * @return the node
         */
        T getNode() {
            return node;
        }

        /**
         * Gets the currently known distance.
         *
         * @return the distance
         */
        double getDistance() {
            return distance;
        }

        /**
         * Compares this pair with another pair by distance.
         *
         * @param other the other node-distance pair
         * @return a negative value, zero or a positive value depending on order
         */
        @Override
        public int compareTo(final NodeDistance<T> other) {
            return Double.compare(distance, other.distance);
        }
    }
}