package ui;

import model.ServerHall;

/**
 * Stores one visited step in Dijkstra's algorithm.
 */
class VisitedStep {

    final int stepNumber;
    final ServerHall node;
    final double distance;
    final ServerHall previous;

    /**
     * Creates a visited step.
     *
     * @param stepNumber the step number
     * @param node the visited node
     * @param distance the shortest known distance
     * @param previous the previous node in the path
     */
    VisitedStep(final int stepNumber,
                final ServerHall node,
                final double distance,
                final ServerHall previous) {
        this.stepNumber = stepNumber;
        this.node = node;
        this.distance = distance;
        this.previous = previous;
    }
}