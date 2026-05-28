package algorithm;

import java.util.List;

/**
 * Defines an algorithm for finding nearest neighbors.
 *
 * @param <T> the object type to search among
 */
public interface NearestNeighborSearch<T> {

    /**
     * Finds the nearest objects to a center object.
     *
     * @param center the center object
     * @param count the maximum number of objects to return
     * @return a list of the nearest objects
     */
    List<T> findNearest(
            T center,
            int count
    );

}