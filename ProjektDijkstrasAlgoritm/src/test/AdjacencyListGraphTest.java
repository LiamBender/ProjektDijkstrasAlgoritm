package test;

import graph.AdjacencyListGraph;
import graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyListGraphTest {

    @Test
    void addNodeShouldAddNode() {
        Graph<String> graph = new AdjacencyListGraph<>();

        graph.addNode("A");

        assertTrue(graph.contains("A"));
        assertEquals(1, graph.size());
    }

    @Test
    void addSameNodeTwiceShouldNotDuplicate() {
        Graph<String> graph = new AdjacencyListGraph<>();

        graph.addNode("A");
        graph.addNode("A");

        assertEquals(1, graph.size());
    }

    @Test
    void addEdgeShouldCreateUndirectedEdge() {
        Graph<String> graph = new AdjacencyListGraph<>();

        graph.addEdge("A", "B", 5.0);

        assertTrue(graph.hasEdge("A", "B"));
        assertTrue(graph.hasEdge("B", "A"));
        assertEquals(5.0, graph.getWeight("A", "B"));
        assertEquals(5.0, graph.getWeight("B", "A"));
        assertEquals(1, graph.edgeCount());
    }

    @Test
    void addExistingEdgeShouldReplaceWeight() {
        Graph<String> graph = new AdjacencyListGraph<>();

        graph.addEdge("A", "B", 5.0);
        graph.addEdge("A", "B", 2.0);

        assertEquals(2.0, graph.getWeight("A", "B"));
        assertEquals(1, graph.edgeCount());
    }

    @Test
    void negativeWeightShouldThrowException() {
        Graph<String> graph = new AdjacencyListGraph<>();

        assertThrows(IllegalArgumentException.class,
                () -> graph.addEdge("A", "B", -1.0));
    }

    @Test
    void removeEdgeShouldRemoveBothDirections() {
        Graph<String> graph = new AdjacencyListGraph<>();
        graph.addEdge("A", "B", 5.0);

        graph.removeEdge("A", "B");

        assertFalse(graph.hasEdge("A", "B"));
        assertFalse(graph.hasEdge("B", "A"));
        assertEquals(0, graph.edgeCount());
    }

    @Test
    void removeNodeShouldRemoveConnectedEdges() {
        Graph<String> graph = new AdjacencyListGraph<>();
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "C", 2.0);

        graph.removeNode("B");

        assertFalse(graph.contains("B"));
        assertFalse(graph.hasEdge("A", "B"));
        assertFalse(graph.hasEdge("C", "B"));
        assertEquals(2, graph.size());
        assertEquals(0, graph.edgeCount());
    }
}