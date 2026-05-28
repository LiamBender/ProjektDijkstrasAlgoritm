package app;

import algorithm.DijkstraShortestPath;
import algorithm.ShortestPathAlgorithm;
import algorithm.ShortestPathResult;
import graph.AdjacencyListGraph;
import graph.Graph;
import model.Position;
import model.ServerHall;
import ui.GraphFrame;

import javax.swing.SwingUtilities;
import java.util.List;

/**
 * Demonstrates graph creation, shortest path calculation and the GUI.
 */
public final class Main {

    /**
     * Prevents instantiation.
     */
    private Main() {

    }

    /**
     * Starts the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {

        Graph<ServerHall> graph =
                new AdjacencyListGraph<>();

        ServerHall a = new ServerHall(
                "A",
                "Serverhall A",
                new Position(0, 0)
        );

        ServerHall b = new ServerHall(
                "B",
                "Serverhall B",
                new Position(3, 4)
        );

        ServerHall c = new ServerHall(
                "C",
                "Serverhall C",
                new Position(8, 2)
        );

        ServerHall d = new ServerHall(
                "D",
                "Serverhall D",
                new Position(12, 7)
        );

        graph.addEdge(
                a,
                b,
                a.getPosition().distanceTo(
                        b.getPosition()
                )
        );

        graph.addEdge(
                b,
                c,
                b.getPosition().distanceTo(
                        c.getPosition()
                )
        );

        graph.addEdge(
                c,
                d,
                c.getPosition().distanceTo(
                        d.getPosition()
                )
        );

        graph.addEdge(
                a,
                c,
                a.getPosition().distanceTo(
                        c.getPosition()
                )
        );

        printShortestPaths(graph, a);
        startGui(graph);
    }

    /**
     * Prints shortest path information to the console.
     *
     * @param graph the graph to search in
     * @param start the start node
     */
    private static void printShortestPaths(final Graph<ServerHall> graph,
                                           final ServerHall start) {

        ShortestPathAlgorithm<ServerHall> algorithm =
                new DijkstraShortestPath<>();

        ShortestPathResult<ServerHall> result =
                algorithm.findShortestPaths(
                        graph,
                        start
                );

        System.out.println(
                "Shortest paths from "
                        + start.getName()
        );

        System.out.println();

        for (ServerHall hall : graph.getNodes()) {

            double distance =
                    result.getDistanceTo(
                            hall
                    );

            List<ServerHall> path =
                    result.buildPathTo(
                            hall
                    );

            System.out.println(
                    "Destination: "
                            + hall.getName()
            );

            System.out.println(
                    "Distance: "
                            + distance
            );

            System.out.println(
                    "Path: "
                            + path
            );

            System.out.println();
        }
    }

    /**
     * Starts the Swing GUI on the event dispatch thread.
     *
     * @param graph the graph to show in the GUI
     */
    private static void startGui(final Graph<ServerHall> graph) {
        SwingUtilities.invokeLater(() -> {
            GraphFrame frame = new GraphFrame(graph);
            frame.setVisible(true);
        });
    }
}