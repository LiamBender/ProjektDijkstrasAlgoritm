package model;

/**
 * Represents a two-dimensional position.
 */
public class Position {

    private final double x;
    private final double y;

    /**
     * Creates a position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Position(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the Euclidean distance to another position.
     *
     * @param other the other position
     * @return the distance between this position and the other position
     */
    public double distanceTo(final Position other) {
        double dx = x - other.x;
        double dy = y - other.y;

        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Gets the x-coordinate.
     *
     * @return the x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y-coordinate.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return y;
    }
}