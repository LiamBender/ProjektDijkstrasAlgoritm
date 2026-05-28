package test;

import algorithm.DijkstraShortestPath;
import algorithm.ShortestPathResult;
import graph.AdjacencyListGraph;
import graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraShortestPathTest {

    @Test
    void shouldFindShortestPath() {
        Graph<String> graph = new AdjacencyListGraph<>();

        graph.addEdge("A", "B", 2.0);
        graph.addEdge("B", "C", 3.0);
        graph.addEdge("A", "C", 10.0);

        DijkstraShortestPath<String> algorithm = new DijkstraShortestPath<>();
        ShortestPathResult<String> result =
                algorithm.findShortestPaths(graph, "A");

        assertEquals(0.0, result.getDistanceTo("A"));
        assertEquals(2.0, result.getDistanceTo("B"));
        assertEquals(5.0, result.getDistanceTo("C"));
        assertEquals("B", result.getPreviousNode("C"));
        assertEquals(List.of("A", "B", "C"), result.buildPathTo("C"));
    }

    @Test
    void unreachableNodeShouldHaveInfinityDistance() {
        Graph<String> graph = new AdjacencyListGraph<>();

        graph.addEdge("A", "B", 1.0);
        graph.addNode("C");

        DijkstraShortestPath<String> algorithm = new DijkstraShortestPath<>();
        ShortestPathResult<String> result =
                algorithm.findShortestPaths(graph, "A");

        assertEquals(Double.POSITIVE_INFINITY, result.getDistanceTo("C"));
        assertNull(result.getPreviousNode("C"));
    }

    @Test
    void missingStartNodeShouldThrowException() {
        Graph<String> graph = new AdjacencyListGraph<>();
        graph.addNode("A");

        DijkstraShortestPath<String> algorithm = new DijkstraShortestPath<>();

        assertThrows(IllegalArgumentException.class,
                () -> algorithm.findShortestPaths(graph, "X"));
    }

    @Test
    void nullGraphShouldThrowException() {
        DijkstraShortestPath<String> algorithm = new DijkstraShortestPath<>();

        assertThrows(NullPointerException.class,
                () -> algorithm.findShortestPaths(null, "A"));
    }

    @Test
    void nullStartNodeShouldThrowException() {
        Graph<String> graph = new AdjacencyListGraph<>();
        DijkstraShortestPath<String> algorithm = new DijkstraShortestPath<>();

        assertThrows(NullPointerException.class,
                () -> algorithm.findShortestPaths(graph, null));
    }
}