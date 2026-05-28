package test;

import model.Position;
import model.ServerHall;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerHallTest {

    @Test
    void serverHallsWithSameIdShouldBeEqual() {
        ServerHall hall1 =
                new ServerHall("A", "Serverhall A", new Position(0, 0));

        ServerHall hall2 =
                new ServerHall("A", "Annat namn", new Position(5, 5));

        assertEquals(hall1, hall2);
        assertEquals(hall1.hashCode(), hall2.hashCode());
    }

    @Test
    void serverHallsWithDifferentIdShouldNotBeEqual() {
        ServerHall hall1 =
                new ServerHall("A", "Serverhall A", new Position(0, 0));

        ServerHall hall2 =
                new ServerHall("B", "Serverhall B", new Position(0, 0));

        assertNotEquals(hall1, hall2);
    }

    @Test
    void toStringShouldReturnName() {
        ServerHall hall =
                new ServerHall("A", "Serverhall A", new Position(0, 0));

        assertEquals("Serverhall A", hall.toString());
    }
}