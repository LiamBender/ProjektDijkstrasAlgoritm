package benchmark;

import algorithm.DijkstraShortestPath;
import algorithm.LinearNearestNeighborSearch;
import algorithm.ShortestPathResult;
import data.RandomServerHallGenerator;
import graph.Graph;
import model.ServerHall;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Runs benchmark tests for Dijkstra and nearest-neighbor search.
 */
public final class BenchmarkRunner {

    private static final int WARMUP_RUNS = 5;
    private static final int MEASURED_RUNS = 30;

    private static double blackhole;

    private BenchmarkRunner() {

    }

    /**
     * Runs the benchmark and writes the result to benchmark_results.csv.
     * NOTE: Will overwrite the benchmark_results.csv file everytime
     * it's run. Move the file or change the name if you want to keep
     * old results.
     *
     * @param args command line arguments
     * @throws IOException if the result file cannot be written
     */
    public static void main(final String[] args)
            throws IOException {

        int[] nodeCounts = {
                10,
                50,
                100,
                500,
                1000,
                2500,
                5000,
                10000,
                50000,
                100000,
                500000
            };

        File resultsDirectory = new File("results");

        if (!resultsDirectory.exists()) {
            resultsDirectory.mkdirs();
        }

        try (PrintWriter writer =
                     new PrintWriter(
                             new FileWriter(
                                     "results/benchmark_results.csv"
                             ))) {

            writer.println(
                    "nodes,edges,dijkstra_avg_ms,nearest_neighbor_avg_ms"
            );

            for (int nodes : nodeCounts) {

                int extraEdges = nodes * 2;

                RandomServerHallGenerator generator =
                        new RandomServerHallGenerator(
                                12345L,
                                1000.0,
                                1000.0
                        );

                Graph<ServerHall> graph =
                        generator.generateConnectedGraph(
                                nodes,
                                extraEdges
                        );

                List<ServerHall> halls =
                        graph.getNodes();

                ServerHall start =
                        halls.get(0);

                double dijkstraTime =
                        benchmarkDijkstra(
                                graph,
                                start
                        );

                double nearestTime =
                        benchmarkNearestNeighbor(
                                graph,
                                start
                        );

                writer.println(
                        nodes
                                + ","
                                + graph.edgeCount()
                                + ","
                                + dijkstraTime
                                + ","
                                + nearestTime
                );

                System.out.println(
                        "Nodes: "
                                + nodes
                                + ", edges: "
                                + graph.edgeCount()
                                + ", Dijkstra ms: "
                                + dijkstraTime
                                + ", nearest ms: "
                                + nearestTime
                );
            }
        }

        System.out.println(
                "Benchmark complete."
        );

        System.out.println(
                "Results saved to results/benchmark_results.csv"
        );

        System.out.println(
                "Ignore: " + blackhole
        );
    }

    /**
     * Benchmarks Dijkstra's shortest path algorithm.
     *
     * @param graph the graph to test
     * @param start the start node
     * @return average runtime in milliseconds
     */
    private static double benchmarkDijkstra(final Graph<ServerHall> graph,
                                            final ServerHall start) {
        DijkstraShortestPath<ServerHall> algorithm =
                new DijkstraShortestPath<>();

        for (int i = 0; i < WARMUP_RUNS; i++) {
            ShortestPathResult<ServerHall> result =
                    algorithm.findShortestPaths(graph, start);
            blackhole += result.getDistances().size();
        }

        long totalTime = 0;

        for (int i = 0; i < MEASURED_RUNS; i++) {
            long before = System.nanoTime();

            ShortestPathResult<ServerHall> result =
                    algorithm.findShortestPaths(graph, start);

            long after = System.nanoTime();

            blackhole += result.getDistances().size();
            totalTime += after - before;
        }

        return totalTime / 1_000_000.0 / MEASURED_RUNS;
    }

    /**
     * Benchmarks linear nearest-neighbor search.
     *
     * @param graph the graph containing server halls
     * @param start the center node
     * @return average runtime in milliseconds
     */
    private static double benchmarkNearestNeighbor(
            final Graph<ServerHall> graph,
            final ServerHall start) {

        LinearNearestNeighborSearch search =
                new LinearNearestNeighborSearch(graph.getNodes());

        for (int i = 0; i < WARMUP_RUNS; i++) {
            List<ServerHall> nearest = search.findNearest(start, 5);
            blackhole += nearest.size();
        }

        long totalTime = 0;

        for (int i = 0; i < MEASURED_RUNS; i++) {
            long before = System.nanoTime();

            List<ServerHall> nearest = search.findNearest(start, 5);

            long after = System.nanoTime();

            blackhole += nearest.size();
            totalTime += after - before;
        }

        return totalTime / 1_000_000.0 / MEASURED_RUNS;
    }
}