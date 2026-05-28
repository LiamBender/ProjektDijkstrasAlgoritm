package ui;

import model.ServerHall;

/**
 * Stores a server hall together with a distance for priority queue ordering.
 */
class NodeDistance implements Comparable<NodeDistance> {

    final ServerHall node;
    final double distance;

    /**
     * Creates a node-distance pair.
     *
     * @param node the node
     * @param distance the distance
     */
    NodeDistance(final ServerHall node, final double distance) {
        this.node = node;
        this.distance = distance;
    }

    /**
     * Compares two node-distance pairs by distance.
     *
     * @param other the other node-distance pair
     * @return comparison result
     */
    @Override
    public int compareTo(final NodeDistance other) {
        return Double.compare(distance, other.distance);
    }
}