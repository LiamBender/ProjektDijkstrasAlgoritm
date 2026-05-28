package algorithm;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Stores the result of a shortest path calculation.
 *
 * @param <T> the node type used in the graph
 */
public class ShortestPathResult<T> {

    private final Map<T, Double> distances;
    private final Map<T, T> previousNodes;

    /**
     * Creates a shortest path result.
     *
     * @param distances the shortest known distance to each node
     * @param previousNodes the previous node used to reach each node
     */
    public ShortestPathResult(final Map<T, Double> distances,
                              final Map<T, T> previousNodes) {
        this.distances = distances;
        this.previousNodes = previousNodes;
    }

    /**
     * Gets the shortest distance to a specific node.
     *
     * @param node the target node
     * @return the shortest distance, or positive infinity if no path exists
     */
    public double getDistanceTo(final T node) {
        return distances.getOrDefault(node, Double.POSITIVE_INFINITY);
    }

    /**
     * Gets the previous node in the shortest path to a specific node.
     *
     * @param node the node to check
     * @return the previous node, or null if no previous node exists
     */
    public T getPreviousNode(final T node) {
        return previousNodes.get(node);
    }

    /**
     * Gets all shortest distances.
     *
     * @return an unmodifiable map of nodes and their distances
     */
    public Map<T, Double> getDistances() {
        return Collections.unmodifiableMap(distances);
    }

    /**
     * Gets all previous-node mappings.
     *
     * @return an unmodifiable map of nodes and their previous nodes
     */
    public Map<T, T> getPreviousNodes() {
        return Collections.unmodifiableMap(previousNodes);
    }

    /**
     * Builds the shortest path to a target node.
     *
     * <p>If the target node is unreachable, an empty list is returned.</p>
     *
     * @param target the target node
     * @return a list containing the path from the start node to the target node,
     *         or an empty list if no path exists
     */
    public List<T> buildPathTo(final T target) {
        if (Double.isInfinite(getDistanceTo(target))) {
            return Collections.emptyList();
        }

        List<T> path = new java.util.ArrayList<>();
        T current = target;

        while (current != null) {
            path.add(current);
            current = previousNodes.get(current);
        }

        Collections.reverse(path);
        return path;
    }
}