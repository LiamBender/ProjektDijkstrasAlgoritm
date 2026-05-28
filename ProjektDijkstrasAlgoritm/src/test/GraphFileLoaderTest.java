package test;

import graph.Graph;
import model.ServerHall;
import org.junit.jupiter.api.Test;
import util.GraphFileLoader;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GraphFileLoaderTest {

    @Test
    void shouldLoadTxtFile() throws Exception {
        File file = new File("data/gavle.txt");

        Graph<ServerHall> graph = GraphFileLoader.loadGraph(file);

        assertTrue(graph.size() > 0);
        assertTrue(graph.edgeCount() > 0);
    }

    @Test
    void shouldLoadCsvFile() throws Exception {
        File file = new File("data/gavle.csv");

        Graph<ServerHall> graph = GraphFileLoader.loadGraph(file);

        assertTrue(graph.size() > 0);
        assertTrue(graph.edgeCount() > 0);
    }

    @Test
    void shouldLoadJsonFile() throws Exception {
        File file = new File("data/gavle.json");

        Graph<ServerHall> graph = GraphFileLoader.loadGraph(file);

        assertTrue(graph.size() > 0);
        assertTrue(graph.edgeCount() > 0);
    }

    @Test
    void unsupportedFileFormatShouldThrowException() {
        File file = new File("data/gavle.xml");

        assertThrows(IllegalArgumentException.class,
                () -> GraphFileLoader.loadGraph(file));
    }
}