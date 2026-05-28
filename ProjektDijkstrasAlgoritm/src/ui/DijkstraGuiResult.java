package ui;

import model.ServerHall;

import java.util.List;
import java.util.Map;

/**
 * Stores Dijkstra result data for the GUI.
 */
class DijkstraGuiResult {

    final Map<ServerHall, Double> distances;
    final Map<ServerHall, ServerHall> previousNodes;
    final List<VisitedStep> visitedSteps;

    /**
     * Creates a GUI result object.
     *
     * @param distances shortest distances
     * @param previousNodes previous-node links
     * @param visitedSteps visited nodes in order
     */
    DijkstraGuiResult(final Map<ServerHall, Double> distances,
                      final Map<ServerHall, ServerHall> previousNodes,
                      final List<VisitedStep> visitedSteps) {
        this.distances = distances;
        this.previousNodes = previousNodes;
        this.visitedSteps = visitedSteps;
    }
}