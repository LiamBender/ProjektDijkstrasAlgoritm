package test;

import algorithm.LinearNearestNeighborSearch;
import model.Position;
import model.ServerHall;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinearNearestNeighborSearchTest {

    @Test
    void shouldFindNearestServerHall() {
        ServerHall center =
                new ServerHall("A", "Center", new Position(0, 0));

        ServerHall near =
                new ServerHall("B", "Near", new Position(3, 4));

        ServerHall far =
                new ServerHall("C", "Far", new Position(10, 10));

        LinearNearestNeighborSearch search =
                new LinearNearestNeighborSearch(
                        List.of(center, near, far)
                );

        List<ServerHall> result =
                search.findNearest(center, 1);

        assertEquals(1, result.size());
        assertEquals(near, result.get(0));
    }

    @Test
    void shouldReturnRequestedNumberOfNearestServerHalls() {
        ServerHall center =
                new ServerHall("A", "Center", new Position(0, 0));

        ServerHall first =
                new ServerHall("B", "First", new Position(1, 0));

        ServerHall second =
                new ServerHall("C", "Second", new Position(2, 0));

        ServerHall third =
                new ServerHall("D", "Third", new Position(3, 0));

        LinearNearestNeighborSearch search =
                new LinearNearestNeighborSearch(
                        List.of(center, third, first, second)
                );

        List<ServerHall> result =
                search.findNearest(center, 2);

        assertEquals(2, result.size());
        assertEquals(first, result.get(0));
        assertEquals(second, result.get(1));
    }

    @Test
    void shouldNotReturnCenterServerHall() {
        ServerHall center =
                new ServerHall("A", "Center", new Position(0, 0));

        ServerHall other =
                new ServerHall("B", "Other", new Position(1, 1));

        LinearNearestNeighborSearch search =
                new LinearNearestNeighborSearch(
                        List.of(center, other)
                );

        List<ServerHall> result =
                search.findNearest(center, 2);

        assertFalse(result.contains(center));
        assertEquals(1, result.size());
    }

    @Test
    void countLargerThanAvailableNeighborsShouldReturnAllNeighbors() {
        ServerHall center =
                new ServerHall("A", "Center", new Position(0, 0));

        ServerHall first =
                new ServerHall("B", "First", new Position(1, 0));

        ServerHall second =
                new ServerHall("C", "Second", new Position(2, 0));

        LinearNearestNeighborSearch search =
                new LinearNearestNeighborSearch(
                        List.of(center, first, second)
                );

        List<ServerHall> result =
                search.findNearest(center, 10);

        assertEquals(2, result.size());
        assertEquals(first, result.get(0));
        assertEquals(second, result.get(1));
    }

    @Test
    void negativeCountShouldThrowException() {
        ServerHall center =
                new ServerHall("A", "Center", new Position(0, 0));

        LinearNearestNeighborSearch search =
                new LinearNearestNeighborSearch(List.of(center));

        assertThrows(
                IllegalArgumentException.class,
                () -> search.findNearest(center, -1)
        );
    }

    @Test
    void nullCenterShouldThrowException() {
        ServerHall center =
                new ServerHall("A", "Center", new Position(0, 0));

        LinearNearestNeighborSearch search =
                new LinearNearestNeighborSearch(List.of(center));

        assertThrows(
                NullPointerException.class,
                () -> search.findNearest(null, 1)
        );
    }

    @Test
    void nullCollectionShouldThrowException() {
        assertThrows(
                NullPointerException.class,
                () -> new LinearNearestNeighborSearch(null)
        );
    }

    @Test
    void collectionWithNullServerHallShouldThrowException() {
        ServerHall center =
                new ServerHall("A", "Center", new Position(0, 0));

        assertThrows(
                NullPointerException.class,
                () -> new LinearNearestNeighborSearch(
                        List.of(center, null)
                )
        );
    }
}