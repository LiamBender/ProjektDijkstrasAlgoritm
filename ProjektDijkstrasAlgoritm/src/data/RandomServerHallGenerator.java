package data;

import graph.AdjacencyListGraph;
import graph.Graph;
import model.Position;
import model.ServerHall;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates random server hall data and connected weighted graphs.
 *
 * <p>The generator is intended for larger test data sets and benchmark tests.
 * It creates server halls with random two-dimensional positions and can build
 * a connected graph where edge weights are based on Euclidean distance between
 * server hall positions.</p>
 */
public class RandomServerHallGenerator {

    private final Random random;
    private final double maxX;
    private final double maxY;

    /**
     * Creates a generator with a random seed.
     *
     * @param maxX the maximum x-coordinate
     * @param maxY the maximum y-coordinate
     */
    public RandomServerHallGenerator(final double maxX,
                                     final double maxY) {
        this(new Random(), maxX, maxY);
    }

    /**
     * Creates a generator with a fixed seed.
     *
     * <p>A fixed seed is useful when the same random data should be generated
     * several times, for example during repeatable benchmark tests.</p>
     *
     * @param seed the random seed
     * @param maxX the maximum x-coordinate
     * @param maxY the maximum y-coordinate
     */
    public RandomServerHallGenerator(final long seed,
                                     final double maxX,
                                     final double maxY) {
        this(new Random(seed), maxX, maxY);
    }

    /**
     * Creates a generator with a supplied random object.
     *
     * @param random the random object to use
     * @param maxX the maximum x-coordinate
     * @param maxY the maximum y-coordinate
     * @throws NullPointerException if random is null
     * @throws IllegalArgumentException if maxX or maxY is not positive
     */
    public RandomServerHallGenerator(final Random random,
                                     final double maxX,
                                     final double maxY) {
        if (random == null) {
            throw new NullPointerException("random must not be null");
        }

        if (maxX <= 0 || maxY <= 0) {
            throw new IllegalArgumentException(
                    "maxX and maxY must be positive"
            );
        }

        this.random = random;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Generates a list of random server halls.
     *
     * @param amount the number of server halls to generate
     * @return a list of generated server halls
     * @throws IllegalArgumentException if amount is negative
     */
    public List<ServerHall> generateServerHalls(final int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }

        List<ServerHall> serverHalls = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            String id = "S" + i;
            String name = "Serverhall " + i;
            double x = random.nextDouble() * maxX;
            double y = random.nextDouble() * maxY;

            serverHalls.add(
                    new ServerHall(
                            id,
                            name,
                            new Position(x, y)
                    )
            );
        }

        return serverHalls;
    }

    /**
     * Generates a connected graph with random server halls.
     *
     * <p>The method first connects every generated server hall in a chain. This
     * guarantees that all nodes are reachable. It then adds extra random edges
     * based on the requested number of extra edges.</p>
     *
     * @param nodeCount the number of server halls to generate
     * @param extraEdgeCount the number of extra random edges to add
     * @return a connected graph with weighted edges
     * @throws IllegalArgumentException if nodeCount or extraEdgeCount is negative
     */
    public Graph<ServerHall> generateConnectedGraph(final int nodeCount,
                                                     final int extraEdgeCount) {
        if (nodeCount < 0) {
            throw new IllegalArgumentException(
                    "nodeCount must not be negative"
            );
        }

        if (extraEdgeCount < 0) {
            throw new IllegalArgumentException(
                    "extraEdgeCount must not be negative"
            );
        }

        List<ServerHall> serverHalls = generateServerHalls(nodeCount);
        Graph<ServerHall> graph = new AdjacencyListGraph<>();

        for (ServerHall hall : serverHalls) {
            graph.addNode(hall);
        }

        addChainEdges(graph, serverHalls);
        addRandomEdges(graph, serverHalls, extraEdgeCount);

        return graph;
    }

    /**
     * Adds chain edges to guarantee that the graph is connected.
     *
     * @param graph the graph to modify
     * @param serverHalls the server halls to connect
     */
    private void addChainEdges(final Graph<ServerHall> graph,
                               final List<ServerHall> serverHalls) {
        for (int i = 0; i < serverHalls.size() - 1; i++) {
            ServerHall from = serverHalls.get(i);
            ServerHall to = serverHalls.get(i + 1);
            addDistanceEdge(graph, from, to);
        }
    }

    /**
     * Adds extra random edges to the graph.
     *
     * @param graph the graph to modify
     * @param serverHalls the server halls in the graph
     * @param extraEdgeCount the number of extra edges to add
     */
    private void addRandomEdges(final Graph<ServerHall> graph,
                                final List<ServerHall> serverHalls,
                                final int extraEdgeCount) {
        if (serverHalls.size() < 2) {
            return;
        }

        int addedEdges = 0;
        int attempts = 0;
        int maxAttempts = Math.max(100, extraEdgeCount * 20);

        while (addedEdges < extraEdgeCount && attempts < maxAttempts) {
            ServerHall from = getRandomServerHall(serverHalls);
            ServerHall to = getRandomServerHall(serverHalls);

            attempts++;

            if (from.equals(to) || graph.hasEdge(from, to)) {
                continue;
            }

            addDistanceEdge(graph, from, to);
            addedEdges++;
        }
    }

    /**
     * Gets a random server hall from a list.
     *
     * @param serverHalls the server halls to choose from
     * @return a randomly selected server hall
     */
    private ServerHall getRandomServerHall(
            final List<ServerHall> serverHalls) {
        int index = random.nextInt(serverHalls.size());
        return serverHalls.get(index);
    }

    /**
     * Adds an edge whose weight is the Euclidean distance between two server
     * halls.
     *
     * @param graph the graph to modify
     * @param from the first server hall
     * @param to the second server hall
     */
    private void addDistanceEdge(final Graph<ServerHall> graph,
                                 final ServerHall from,
                                 final ServerHall to) {
        double distance = from.getPosition().distanceTo(to.getPosition());
        graph.addEdge(from, to, distance);
    }
}