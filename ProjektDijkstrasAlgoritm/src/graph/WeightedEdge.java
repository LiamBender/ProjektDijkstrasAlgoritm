package graph;

/**
 * Represents a weighted edge between two nodes in a graph.
 *
 * @param <T> the node type used by the edge
 */
public class WeightedEdge<T> {

    private final T from;
    private final T to;
    private final double weight;

    /**
     * Creates a weighted edge.
     *
     * @param from the start node
     * @param to the destination node
     * @param weight the weight of the edge
     */
    public WeightedEdge(final T from,
                        final T to,
                        final double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    /**
     * Gets the start node.
     *
     * @return the start node
     */
    public T getFrom() {
        return from;
    }

    /**
     * Gets the destination node.
     *
     * @return the destination node
     */
    public T getTo() {
        return to;
    }

    /**
     * Gets the edge weight.
     *
     * @return the edge weight
     */
    public double getWeight() {
        return weight;
    }
}