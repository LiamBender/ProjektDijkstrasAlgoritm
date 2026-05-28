package ui;

import algorithm.LinearNearestNeighborSearch;
import data.RandomServerHallGenerator;
import graph.Graph;
import graph.WeightedEdge;
import model.ServerHall;
import util.GraphFileLoader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * A graphical window for visualizing a weighted graph of server halls.
 *
 * <p>The GUI displays server halls as nodes, connections as weighted edges,
 * calculates the shortest path using Dijkstra's algorithm, highlights the
 * shortest path and shows visited nodes in a table. It can also find nearest
 * server halls and generate random connected graphs for larger data sets.</p>
 */
public class GraphFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private Graph<ServerHall> graph;
    private final GraphPanel graphPanel;
    private final JComboBox<ServerHall> startBox;
    private final JComboBox<ServerHall> targetBox;
    private final JComboBox<Integer> nearestCountBox;
    private final JComboBox<Integer> randomNodeCountBox;
    private final JComboBox<Integer> randomExtraEdgeCountBox;
    private final JTextArea resultArea;
    private final DefaultTableModel tableModel;

    /**
     * Creates a new graph window.
     *
     * @param graph the graph to visualize
     */
    public GraphFrame(final Graph<ServerHall> graph) {
        super("Serverhallar - grafvisualisering");

        this.graph = graph;
        this.graphPanel = new GraphPanel(graph);
        this.startBox = new JComboBox<>();
        this.targetBox = new JComboBox<>();
        this.nearestCountBox = new JComboBox<>();
        this.randomNodeCountBox = new JComboBox<>();
        this.randomExtraEdgeCountBox = new JComboBox<>();
        this.resultArea = new JTextArea(6, 30);
        this.tableModel = new DefaultTableModel(
                new String[]{
                    "Steg",
                    "Besökt nod",
                    "Kortaste kända distans",
                    "Föregående nod"
                },
                0
        );

        createNearestCountSelector();
        createRandomGraphSelectors();
        createNodeSelectors();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createControlPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Adds selectable values for the nearest-neighbor count.
     */
    private void createNearestCountSelector() {
        for (int i = 1; i <= 5; i++) {
            nearestCountBox.addItem(i);
        }
    }

    /**
     * Adds selectable values for random graph generation.
     */
    private void createRandomGraphSelectors() {
        int[] nodeCounts = {10, 25, 50, 100, 250, 500};
        int[] edgeCounts = {0, 10, 25, 50, 100, 250, 500};

        for (int count : nodeCounts) {
            randomNodeCountBox.addItem(count);
        }

        for (int count : edgeCounts) {
            randomExtraEdgeCountBox.addItem(count);
        }

        randomNodeCountBox.setSelectedItem(50);
        randomExtraEdgeCountBox.setSelectedItem(50);
    }

    /**
     * Adds all server halls to the start and target selectors.
     */
    private void createNodeSelectors() {
        startBox.removeAllItems();
        targetBox.removeAllItems();

        for (ServerHall hall : graph.getNodes()) {
            startBox.addItem(hall);
            targetBox.addItem(hall);
        }

        if (targetBox.getItemCount() > 1) {
            targetBox.setSelectedIndex(1);
        }
    }

    /**
     * Loads graph data from a file selected by the user.
     */
    private void loadGraphFile() {

        JFileChooser chooser =
                new JFileChooser();

        int result =
                chooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file =
                chooser.getSelectedFile();

        try {

            this.graph =
                    GraphFileLoader.loadGraph(file);

            graphPanel.setGraph(this.graph);

            createNodeSelectors();
            clearResult();

            repaint();

        } catch (Exception exception) {

            resultArea.setText(
                    "Kunde inte läsa fil:\n"
                            + exception.getMessage()
            );
        }
    }

    /**
     * Creates the control panel.
     *
     * @return the control panel
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();

        JButton calculateButton = new JButton("Beräkna kortaste väg");
        JButton nearestButton = new JButton("Hitta närmaste");
        JButton randomGraphButton = new JButton("Skapa slumpgraf");
        JButton clearButton = new JButton("Rensa");
        JButton loadButton = new JButton("Ladda fil");

        calculateButton.addActionListener(this::calculateShortestPath);
        nearestButton.addActionListener(event -> findNearest());
        randomGraphButton.addActionListener(event -> generateRandomGraph());
        clearButton.addActionListener(event -> clearResult());
        loadButton.addActionListener(event -> loadGraphFile());

        panel.add(new JLabel("Start:"));
        panel.add(startBox);
        panel.add(new JLabel("Mål:"));
        panel.add(targetBox);
        panel.add(calculateButton);

        panel.add(new JLabel("Antal närmaste:"));
        panel.add(nearestCountBox);
        panel.add(nearestButton);

        panel.add(new JLabel("Slumpnoder:"));
        panel.add(randomNodeCountBox);
        panel.add(new JLabel("Extra kanter:"));
        panel.add(randomExtraEdgeCountBox);
        panel.add(randomGraphButton);

        panel.add(clearButton);
        panel.add(loadButton);

        return panel;
    }

    /**
     * Creates the main panel containing the graph and result information.
     *
     * @return the main panel
     */
    private JSplitPane createMainPanel() {
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(
                BorderFactory.createTitledBorder("Besökta noder")
        );

        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(
                BorderFactory.createTitledBorder("Resultat")
        );

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(resultScrollPane, BorderLayout.NORTH);
        rightPanel.add(tableScrollPane, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(390, 650));

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                graphPanel,
                rightPanel
        );

        splitPane.setResizeWeight(1.0);
        return splitPane;
    }

    /**
     * Calculates the shortest path and updates the GUI.
     *
     * @param event the button click event
     */
    private void calculateShortestPath(final ActionEvent event) {
        ServerHall start = (ServerHall) startBox.getSelectedItem();
        ServerHall target = (ServerHall) targetBox.getSelectedItem();

        if (start == null || target == null) {
            resultArea.setText("Välj både start och mål.");
            return;
        }

        DijkstraGuiResult result = runDijkstra(start);
        List<ServerHall> path = buildPath(start, target, result.previousNodes);
        double distance = result.distances.get(target);

        tableModel.setRowCount(0);

        for (VisitedStep step : result.visitedSteps) {
            tableModel.addRow(new Object[]{
                step.stepNumber,
                step.node.getName(),
                String.format("%.2f", step.distance),
                step.previous == null ? "-" : step.previous.getName()
            });
        }

        graphPanel.setHighlightedPath(path);

        if (Double.isInfinite(distance)) {
            resultArea.setText(
                    "Det finns ingen väg från "
                            + start.getName()
                            + " till "
                            + target.getName()
                            + "."
            );
            return;
        }

        resultArea.setText(
                "Kortaste väg:\n"
                        + formatPath(path)
                        + "\n\nTotal distans: "
                        + String.format("%.2f", distance)
                        + "\n\nUträkning:\n"
                        + "Dijkstra väljer alltid den obesökta nod som just nu har "
                        + "lägst känd distans från startnoden. Därefter uppdateras "
                        + "grannarnas distanser om en kortare väg hittas."
        );
    }

    /**
     * Finds the nearest server halls to the selected start node and updates the
     * GUI.
     *
     * <p>The selected start node itself is not included in the result. The
     * number of returned server halls is selected in the nearest-neighbor count
     * box.</p>
     */
    private void findNearest() {
        ServerHall start = (ServerHall) startBox.getSelectedItem();
        Integer count = (Integer) nearestCountBox.getSelectedItem();

        if (start == null || count == null) {
            resultArea.setText("Välj en startnod och antal närmaste.");
            return;
        }

        LinearNearestNeighborSearch search =
                new LinearNearestNeighborSearch(graph.getNodes());

        List<ServerHall> nearest =
                search.findNearest(start, count);

        List<ServerHall> highlightedNodes = new ArrayList<>();
        highlightedNodes.add(start);
        highlightedNodes.addAll(nearest);

        graphPanel.setHighlightedPath(highlightedNodes);
        tableModel.setRowCount(0);

        resultArea.setText(
                "Närmaste serverhallar från "
                        + start.getName()
                        + ":\n\n"
                        + formatNearestResult(start, nearest)
        );
    }

    /**
     * Generates a random connected graph and updates the GUI.
     */
    private void generateRandomGraph() {
        Integer nodeCount =
                (Integer) randomNodeCountBox.getSelectedItem();

        Integer extraEdgeCount =
                (Integer) randomExtraEdgeCountBox.getSelectedItem();

        if (nodeCount == null || extraEdgeCount == null) {
            resultArea.setText("Välj antal noder och extra kanter.");
            return;
        }

        RandomServerHallGenerator generator =
                new RandomServerHallGenerator(
                        1000.0,
                        1000.0
                );

        this.graph =
                generator.generateConnectedGraph(
                        nodeCount,
                        extraEdgeCount
                );

        graphPanel.setGraph(this.graph);
        createNodeSelectors();
        clearResult();

        resultArea.setText(
                "Slumpgraf skapad.\n\n"
                        + "Antal noder: "
                        + graph.size()
                        + "\nAntal kanter: "
                        + graph.edgeCount()
        );

        repaint();
    }

    /**
     * Formats nearest-neighbor results as readable text.
     *
     * @param start the selected start server hall
     * @param nearest the nearest server halls
     * @return formatted nearest-neighbor result text
     */
    private String formatNearestResult(final ServerHall start,
                                       final List<ServerHall> nearest) {
        StringBuilder builder = new StringBuilder();

        if (nearest.isEmpty()) {
            return "Inga andra serverhallar hittades.";
        }

        for (int i = 0; i < nearest.size(); i++) {
            ServerHall hall = nearest.get(i);
            double distance =
                    start.getPosition().distanceTo(hall.getPosition());

            builder.append(i + 1)
                    .append(". ")
                    .append(hall.getName())
                    .append(" - distans: ")
                    .append(String.format("%.2f", distance));

            if (i < nearest.size() - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    /**
     * Clears highlighted path and result data.
     */
    private void clearResult() {
        graphPanel.setHighlightedPath(new ArrayList<>());
        resultArea.setText("");
        tableModel.setRowCount(0);
    }

    /**
     * Runs Dijkstra's algorithm and stores information useful for the GUI.
     *
     * @param start the start node
     * @return result information from the algorithm
     */
    private DijkstraGuiResult runDijkstra(final ServerHall start) {
        Map<ServerHall, Double> distances = new HashMap<>();
        Map<ServerHall, ServerHall> previousNodes = new HashMap<>();
        List<VisitedStep> visitedSteps = new ArrayList<>();
        Set<ServerHall> visited = new HashSet<>();

        PriorityQueue<NodeDistance> queue =
                new PriorityQueue<>();

        for (ServerHall hall : graph.getNodes()) {
            distances.put(hall, Double.POSITIVE_INFINITY);
        }

        distances.put(start, 0.0);
        queue.add(new NodeDistance(start, 0.0));

        int stepNumber = 1;

        while (!queue.isEmpty()) {
            NodeDistance current = queue.poll();
            ServerHall currentNode = current.node;

            if (visited.contains(currentNode)) {
                continue;
            }

            visited.add(currentNode);

            visitedSteps.add(new VisitedStep(
                    stepNumber,
                    currentNode,
                    distances.get(currentNode),
                    previousNodes.get(currentNode)
            ));

            stepNumber++;

            for (WeightedEdge<ServerHall> edge
                    : graph.getEdgesFrom(currentNode)) {
                ServerHall neighbor = edge.getTo();

                if (visited.contains(neighbor)) {
                    continue;
                }

                double newDistance =
                        distances.get(currentNode) + edge.getWeight();

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previousNodes.put(neighbor, currentNode);
                    queue.add(new NodeDistance(neighbor, newDistance));
                }
            }
        }

        return new DijkstraGuiResult(distances, previousNodes, visitedSteps);
    }

    /**
     * Builds a path from start node to target node.
     *
     * @param start the start node
     * @param target the target node
     * @param previousNodes map containing previous-node links
     * @return the shortest path
     */
    private List<ServerHall> buildPath(final ServerHall start,
                                       final ServerHall target,
                                       final Map<ServerHall, ServerHall>
                                               previousNodes) {
        List<ServerHall> path = new ArrayList<>();
        ServerHall current = target;

        while (current != null) {
            path.add(current);

            if (current.equals(start)) {
                break;
            }

            current = previousNodes.get(current);
        }

        Collections.reverse(path);

        if (path.isEmpty() || !path.get(0).equals(start)) {
            return new ArrayList<>();
        }

        return path;
    }

    /**
     * Formats a path as readable text.
     *
     * @param path the path to format
     * @return formatted path text
     */
    private String formatPath(final List<ServerHall> path) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < path.size(); i++) {
            builder.append(path.get(i).getName());

            if (i < path.size() - 1) {
                builder.append(" -> ");
            }
        }

        return builder.toString();
    }
}