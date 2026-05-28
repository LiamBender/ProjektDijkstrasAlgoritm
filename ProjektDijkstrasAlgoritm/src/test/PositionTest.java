package test;

import model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void distanceToShouldReturnEuclideanDistance() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(3, 4);

        assertEquals(5.0, p1.distanceTo(p2));
    }

    @Test
    void distanceToSamePositionShouldBeZero() {
        Position p = new Position(2, 7);

        assertEquals(0.0, p.distanceTo(p));
    }
}