package util;

import graph.AdjacencyListGraph;
import graph.Graph;
import model.Position;
import model.ServerHall;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads server hall graph data from TXT, CSV and JSON files.
 *
 * <p>The file must contain both nodes and edges. Edges define which server
 * halls are connected. The edge weight is calculated automatically as the
 * Euclidean distance between the connected server halls.</p>
 */
public final class GraphFileLoader {

    /**
     * Prevents instantiation.
     */
    private GraphFileLoader() {

    }

    /**
     * Loads a graph from a TXT, CSV or JSON file.
     *
     * @param file the file to load
     * @return a graph containing server halls and selected weighted edges
     * @throws IOException if the file cannot be read
     */
    public static Graph<ServerHall> loadGraph(final File file)
            throws IOException {

        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".txt")) {
            return loadTxt(file);
        }

        if (fileName.endsWith(".csv")) {
            return loadCsv(file);
        }

        if (fileName.endsWith(".json")) {
            return loadJson(file);
        }

        throw new IllegalArgumentException(
                "Filformatet stöds inte. Använd .txt, .csv eller .json."
        );
    }

    /**
     * Loads graph data from a TXT file.
     *
     * <p>Node format: node;id;name;x;y</p>
     * <p>Edge format: edge;from;to</p>
     *
     * @param file the TXT file
     * @return a loaded graph
     * @throws IOException if the file cannot be read
     */
    private static Graph<ServerHall> loadTxt(final File file)
            throws IOException {

        Graph<ServerHall> graph = new AdjacencyListGraph<>();
        Map<String, ServerHall> halls = new HashMap<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");

                if (parts[0].equalsIgnoreCase("node")) {
                    ServerHall hall = createServerHall(
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4]
                    );

                    halls.put(hall.getId(), hall);
                    graph.addNode(hall);
                } else if (parts[0].equalsIgnoreCase("edge")) {
                    addDistanceEdge(graph, halls, parts[1], parts[2]);
                }
            }
        }

        return graph;
    }

    /**
     * Loads graph data from a CSV file.
     *
     * <p>Node format: node,id,name,x,y,,</p>
     * <p>Edge format: edge,,,,,from,to</p>
     *
     * @param file the CSV file
     * @return a loaded graph
     * @throws IOException if the file cannot be read
     */
    private static Graph<ServerHall> loadCsv(final File file)
            throws IOException {

        Graph<ServerHall> graph = new AdjacencyListGraph<>();
        Map<String, ServerHall> halls = new HashMap<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);

                if (parts[0].equalsIgnoreCase("node")) {
                    ServerHall hall = createServerHall(
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4]
                    );

                    halls.put(hall.getId(), hall);
                    graph.addNode(hall);
                } else if (parts[0].equalsIgnoreCase("edge")) {
                    addDistanceEdge(graph, halls, parts[5], parts[6]);
                }
            }
        }

        return graph;
    }

    /**
     * Loads graph data from a simple JSON file.
     *
     * @param file the JSON file
     * @return a loaded graph
     * @throws IOException if the file cannot be read
     */
    private static Graph<ServerHall> loadJson(final File file)
            throws IOException {

        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line.trim());
            }
        }

        Graph<ServerHall> graph = new AdjacencyListGraph<>();
        Map<String, ServerHall> halls = new HashMap<>();
        String json = builder.toString();

        String nodesText = getArrayContent(json, "nodes");
        String[] nodes = nodesText.split("\\},");

        for (String node : nodes) {
            ServerHall hall = createServerHall(
                    getJsonStringValue(node, "id"),
                    getJsonStringValue(node, "name"),
                    getJsonNumberValue(node, "x"),
                    getJsonNumberValue(node, "y")
            );

            halls.put(hall.getId(), hall);
            graph.addNode(hall);
        }

        String edgesText = getArrayContent(json, "edges");
        String[] edges = edgesText.split("\\},");

        for (String edge : edges) {
            String from = getJsonStringValue(edge, "from");
            String to = getJsonStringValue(edge, "to");

            addDistanceEdge(graph, halls, from, to);
        }

        return graph;
    }

    /**
     * Creates a server hall from text values.
     *
     * @param idText the id text
     * @param nameText the name text
     * @param xText the x-coordinate text
     * @param yText the y-coordinate text
     * @return a server hall
     */
    private static ServerHall createServerHall(final String idText,
                                               final String nameText,
                                               final String xText,
                                               final String yText) {
        String id = idText.trim();
        String name = nameText.trim();
        double x = Double.parseDouble(xText.trim());
        double y = Double.parseDouble(yText.trim());

        return new ServerHall(id, name, new Position(x, y));
    }

    /**
     * Adds a weighted edge between two already loaded server halls.
     *
     * @param graph the graph
     * @param halls all loaded server halls by id
     * @param fromId the start id
     * @param toId the target id
     */
    private static void addDistanceEdge(final Graph<ServerHall> graph,
                                        final Map<String, ServerHall> halls,
                                        final String fromId,
                                        final String toId) {
        ServerHall from = halls.get(fromId.trim());
        ServerHall to = halls.get(toId.trim());

        if (from == null || to == null) {
            throw new IllegalArgumentException(
                    "Edge refers to unknown node: "
                            + fromId
                            + " - "
                            + toId
            );
        }

        double distance = from.getPosition().distanceTo(to.getPosition());
        graph.addEdge(from, to, distance);
    }

    /**
     * Extracts an array body from a JSON string.
     *
     * @param json the full JSON text
     * @param key the array key
     * @return the content inside the array
     */
    private static String getArrayContent(final String json,
                                          final String key) {
        String search = "\"" + key + "\"";
        int keyIndex = json.indexOf(search);
        int startIndex = json.indexOf("[", keyIndex);
        int depth = 0;

        for (int i = startIndex; i < json.length(); i++) {
            char character = json.charAt(i);

            if (character == '[') {
                depth++;
            } else if (character == ']') {
                depth--;

                if (depth == 0) {
                    return json.substring(startIndex + 1, i);
                }
            }
        }

        throw new IllegalArgumentException(
                "Could not find JSON array: " + key
        );
    }

    /**
     * Extracts a string value from a JSON object string.
     *
     * @param object the JSON object text
     * @param key the key
     * @return the extracted value
     */
    private static String getJsonStringValue(final String object,
                                             final String key) {
        String search = "\"" + key + "\"";
        int keyIndex = object.indexOf(search);
        int colonIndex = object.indexOf(":", keyIndex);
        int firstQuote = object.indexOf("\"", colonIndex + 1);
        int secondQuote = object.indexOf("\"", firstQuote + 1);

        return object.substring(firstQuote + 1, secondQuote).trim();
    }

    /**
     * Extracts a numeric value from a JSON object string.
     *
     * @param object the JSON object text
     * @param key the key
     * @return the extracted number as text
     */
    private static String getJsonNumberValue(final String object,
                                             final String key) {
        String search = "\"" + key + "\"";
        int keyIndex = object.indexOf(search);
        int colonIndex = object.indexOf(":", keyIndex);
        int endIndex = colonIndex + 1;

        while (endIndex < object.length()
                && object.charAt(endIndex) != ','
                && object.charAt(endIndex) != '}') {
            endIndex++;
        }

        return object.substring(colonIndex + 1, endIndex)
                .replace("\"", "")
                .trim();
    }
}