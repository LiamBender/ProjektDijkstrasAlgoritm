package ui;

import graph.Graph;
import graph.WeightedEdge;
import model.ServerHall;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Panel responsible for drawing a weighted graph of server halls.
 */
public class GraphPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 900;
    private static final int HEIGHT = 650;
    private static final int PADDING = 80;
    private static final int NODE_RADIUS = 18;

    private Graph<ServerHall> graph;
    private List<ServerHall> highlightedPath;

    /**
     * Creates a panel for drawing a graph.
     *
     * @param graph the graph to draw
     */
    public GraphPanel(final Graph<ServerHall> graph) {
        this.graph = graph;
        this.highlightedPath = new ArrayList<>();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
    }

    /**
     * Sets the highlighted path or highlighted nodes.
     *
     * <p>When the list represents a shortest path, both the nodes and the
     * connecting edges are highlighted. When the list represents nearest
     * neighbors, the nodes are highlighted and only existing consecutive edges
     * are highlighted.</p>
     *
     * @param highlightedPath the path or nodes to highlight
     */
    public void setHighlightedPath(final List<ServerHall> highlightedPath) {
        this.highlightedPath = new ArrayList<>(highlightedPath);
        repaint();
    }

    /**
     * Updates the graph shown in the panel.
     *
     * @param graph the new graph
     */
    public void setGraph(final Graph<ServerHall> graph) {
        this.graph = graph;
        this.highlightedPath = new ArrayList<>();
        repaint();
    }

    /**
     * Draws the graph.
     *
     * @param graphics the graphics context
     */
    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawEdges(g2);
        drawNodes(g2);
    }

    /**
     * Draws all graph edges.
     *
     * @param g2 the graphics context
     */
    private void drawEdges(final Graphics2D g2) {
        Set<String> drawnEdges = new HashSet<>();

        for (ServerHall from : graph.getNodes()) {
            for (WeightedEdge<ServerHall> edge : graph.getEdgesFrom(from)) {
                ServerHall to = edge.getTo();
                String edgeKey = createEdgeKey(from, to);

                if (!drawnEdges.contains(edgeKey)) {
                    drawnEdges.add(edgeKey);
                    drawEdge(g2, from, to, edge.getWeight());
                }
            }
        }
    }

    /**
     * Draws one edge with its weight label.
     *
     * @param g2 the graphics context
     * @param from the first server hall
     * @param to the second server hall
     * @param weight the edge weight
     */
    private void drawEdge(final Graphics2D g2,
                          final ServerHall from,
                          final ServerHall to,
                          final double weight) {
        int x1 = toScreenX(from);
        int y1 = toScreenY(from);
        int x2 = toScreenX(to);
        int y2 = toScreenY(to);

        if (isEdgeInHighlightedPath(from, to)) {
            g2.setColor(new Color(220, 50, 47));
            g2.setStroke(new BasicStroke(4));
        } else {
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(2));
        }

        g2.drawLine(x1, y1, x2, y2);

        int labelX = (x1 + x2) / 2;
        int labelY = (y1 + y2) / 2;

        g2.setColor(Color.BLACK);
        g2.drawString(String.format("%.1f", weight), labelX, labelY - 5);
    }

    /**
     * Draws all server hall nodes.
     *
     * @param g2 the graphics context
     */
    private void drawNodes(final Graphics2D g2) {
        for (ServerHall hall : graph.getNodes()) {
            int x = toScreenX(hall);
            int y = toScreenY(hall);

            if (highlightedPath.contains(hall)) {
                g2.setColor(new Color(255, 204, 102));
            } else {
                g2.setColor(new Color(90, 160, 220));
            }

            g2.fillOval(
                    x - NODE_RADIUS,
                    y - NODE_RADIUS,
                    NODE_RADIUS * 2,
                    NODE_RADIUS * 2
            );

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(
                    x - NODE_RADIUS,
                    y - NODE_RADIUS,
                    NODE_RADIUS * 2,
                    NODE_RADIUS * 2
            );

            drawCenteredText(g2, hall.getId(), x, y + 5);
            drawCenteredText(g2, hall.getName(), x, y - NODE_RADIUS - 8);
        }
    }

    /**
     * Checks whether an edge is part of the highlighted path.
     *
     * @param first the first server hall
     * @param second the second server hall
     * @return true if the edge is part of the highlighted path
     */
    private boolean isEdgeInHighlightedPath(final ServerHall first,
                                            final ServerHall second) {
        for (int i = 0; i < highlightedPath.size() - 1; i++) {
            ServerHall a = highlightedPath.get(i);
            ServerHall b = highlightedPath.get(i + 1);

            if ((a.equals(first) && b.equals(second))
                    || (a.equals(second) && b.equals(first))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Draws text centered around an x-coordinate.
     *
     * @param g2 the graphics context
     * @param text the text to draw
     * @param x the center x-coordinate
     * @param y the y-coordinate
     */
    private void drawCenteredText(final Graphics2D g2,
                                  final String text,
                                  final int x,
                                  final int y) {
        FontMetrics metrics = g2.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        g2.drawString(text, x - textWidth / 2, y);
    }

    /**
     * Creates a key for an undirected edge so it is only drawn once.
     *
     * @param first the first server hall
     * @param second the second server hall
     * @return a stable edge key
     */
    private String createEdgeKey(final ServerHall first,
                                 final ServerHall second) {
        String firstId = first.getId();
        String secondId = second.getId();

        if (firstId.compareTo(secondId) <= 0) {
            return firstId + "-" + secondId;
        }

        return secondId + "-" + firstId;
    }

    /**
     * Converts a model x-coordinate to a screen x-coordinate.
     *
     * @param hall the server hall
     * @return the screen x-coordinate
     */
    private int toScreenX(final ServerHall hall) {
        double minX = getMinX();
        double maxX = getMaxX();
        double range = Math.max(1.0, maxX - minX);
        double normalized = (hall.getPosition().getX() - minX) / range;

        return PADDING
                + (int) Math.round(
                normalized * (getWidth() - 2.0 * PADDING)
        );
    }

    /**
     * Converts a model y-coordinate to a screen y-coordinate.
     *
     * @param hall the server hall
     * @return the screen y-coordinate
     */
    private int toScreenY(final ServerHall hall) {
        double minY = getMinY();
        double maxY = getMaxY();
        double range = Math.max(1.0, maxY - minY);
        double normalized = (hall.getPosition().getY() - minY) / range;

        return getHeight()
                - PADDING
                - (int) Math.round(
                normalized * (getHeight() - 2.0 * PADDING)
        );
    }

    /**
     * Gets the smallest x-coordinate in the graph.
     *
     * @return the smallest x-coordinate
     */
    private double getMinX() {
        double min = Double.POSITIVE_INFINITY;

        for (ServerHall hall : graph.getNodes()) {
            min = Math.min(min, hall.getPosition().getX());
        }

        return min;
    }

    /**
     * Gets the largest x-coordinate in the graph.
     *
     * @return the largest x-coordinate
     */
    private double getMaxX() {
        double max = Double.NEGATIVE_INFINITY;

        for (ServerHall hall : graph.getNodes()) {
            max = Math.max(max, hall.getPosition().getX());
        }

        return max;
    }

    /**
     * Gets the smallest y-coordinate in the graph.
     *
     * @return the smallest y-coordinate
     */
    private double getMinY() {
        double min = Double.POSITIVE_INFINITY;

        for (ServerHall hall : graph.getNodes()) {
            min = Math.min(min, hall.getPosition().getY());
        }

        return min;
    }

    /**
     * Gets the largest y-coordinate in the graph.
     *
     * @return the largest y-coordinate
     */
    private double getMaxY() {
        double max = Double.NEGATIVE_INFINITY;

        for (ServerHall hall : graph.getNodes()) {
            max = Math.max(max, hall.getPosition().getY());
        }

        return max;
    }
}