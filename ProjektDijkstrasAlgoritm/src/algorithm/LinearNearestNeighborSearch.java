package algorithm;

import model.ServerHall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Finds the nearest server halls by checking every available server hall.
 *
 * <p>This implementation uses a simple linear search followed by sorting. It is
 * easy to understand and works well for a prototype, but it has higher time
 * complexity than more advanced spatial data structures such as quadtrees or
 * k-d trees.</p>
 */
public class LinearNearestNeighborSearch
        implements NearestNeighborSearch<ServerHall> {

    private final List<ServerHall> serverHalls;

    /**
     * Creates a nearest-neighbor search object.
     *
     * @param serverHalls the server halls that should be searched
     * @throws NullPointerException if the collection or any server hall is null
     */
    public LinearNearestNeighborSearch(
            final Collection<ServerHall> serverHalls) {
        Objects.requireNonNull(serverHalls, "serverHalls must not be null");

        this.serverHalls = new ArrayList<>();

        for (ServerHall hall : serverHalls) {
            this.serverHalls.add(
                    Objects.requireNonNull(
                            hall,
                            "serverHalls must not contain null values"
                    )
            );
        }
    }

    /**
     * Finds the nearest server halls to a center server hall.
     *
     * <p>The center server hall itself is not included in the result. If the
     * requested count is larger than the number of available neighbors, all
     * available neighbors are returned.</p>
     *
     * @param center the center server hall
     * @param count the maximum number of server halls to return
     * @return a list of nearest server halls ordered from nearest to farthest
     * @throws NullPointerException if the center server hall is null
     * @throws IllegalArgumentException if count is negative
     */
    @Override
    public List<ServerHall> findNearest(final ServerHall center,
                                        final int count) {
        Objects.requireNonNull(center, "center must not be null");

        if (count < 0) {
            throw new IllegalArgumentException("count must not be negative");
        }

        List<ServerHall> candidates = new ArrayList<>();

        for (ServerHall hall : serverHalls) {
            if (!hall.equals(center)) {
                candidates.add(hall);
            }
        }

        candidates.sort(Comparator.comparingDouble(
                hall -> center.getPosition().distanceTo(hall.getPosition())
        ));

        int resultSize = Math.min(count, candidates.size());
        return new ArrayList<>(candidates.subList(0, resultSize));
    }
}