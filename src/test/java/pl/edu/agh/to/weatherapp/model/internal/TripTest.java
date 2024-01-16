package pl.edu.agh.to.weatherapp.model.internal;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TripTest {
    private static final List<String> SHORT_TRIP_1 = List.of("Tarnów", "Cracow");
    private static final List<String> SHORT_TRIP_1_COPY = List.of("Tarnów", "Cracow");
    private static final List<String> SHORT_TRIP_2 = List.of("Cracow","Tarnów");
    private static final List<String> LONG_TRIP_1 = List.of("Tarnów", "Cracow", "Rzeszów");
    private static final List<String> LONG_TRIP_1_COPY = List.of("Tarnów", "Cracow", "Rzeszów");
    private static final List<String> LONG_TRIP_2 = List.of("Rzeszów", "Cracow","Tarnów");

    @Test
    void testCreatesCorrectObject() {
        assertDoesNotThrow(() -> new Trip(SHORT_TRIP_1));
    }

    @Test
    void testEquals() {
        assertEquals(SHORT_TRIP_1, SHORT_TRIP_1_COPY);
        assertEquals(LONG_TRIP_1, LONG_TRIP_1_COPY);

        assertNotEquals(SHORT_TRIP_1,SHORT_TRIP_2);
        assertNotEquals(LONG_TRIP_1,LONG_TRIP_2);
    }
}