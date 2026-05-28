package util;

import model.Position;

/**
 * Defines a distance calculation between two positions.
 */
public interface DistanceCalculator {

    /**
     * Calculates the distance between two positions.
     *
     * @param p1 the first position
     * @param p2 the second position
     * @return the calculated distance
     */
    double calculate(
            Position p1,
            Position p2
    );

}