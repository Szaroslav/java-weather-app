package pl.edu.agh.to.weatherapp.service.persistence;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.edu.agh.to.weatherapp.model.internal.Trip;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqlitePersistenceServiceTest {
    private static final String DB_FILENAME = "test.db";
    private static final List<Trip> DB_TRIPS_LOAD = List.of();
    private static final List<Trip> DB_TRIPS_ADD_1 = List.of(new Trip(List.of("Krakow")));
    private static final List<Trip> DB_TRIPS_ADD_2 = List.of(new Trip(List.of("Krakow")), new Trip(List.of("Krakow", "Tarnow")));
    private static final List<Trip> DB_TRIPS_ADD_3 = List.of(new Trip(List.of("Krakow")), new Trip(List.of("Krakow", "Tarnow")), new Trip(List.of("Krakow", "Tarnow", "Rzeszow")));
    private static final List<Trip> DB_TRIPS_DELETE_1 = List.of(new Trip(List.of("Krakow", "Tarnow")), new Trip(List.of("Krakow", "Tarnow", "Rzeszow")));
    private static final List<Trip> DB_TRIPS_DELETE_2 = List.of(new Trip(List.of("Krakow", "Tarnow", "Rzeszow")));
    private static final List<Trip> DB_TRIPS_DELETE_3 = List.of();
    private static SqlitePersistenceService service;

    @BeforeAll
    public static void init() {
        service = new SqlitePersistenceService(DB_FILENAME);
    }

    @Test
    @Order(1)
    void loadTripsTest() {
        List<Trip> trips = service.load();
        assertThat(trips).isEqualTo(DB_TRIPS_LOAD);
    }

    @Test
    @Order(2)
    void addTrip1locationTest() {
        Trip trip = new Trip(List.of("Krakow"));
        service.add(trip);
        List<Trip> trips = service.load();
        assertThat(trips).isEqualTo(DB_TRIPS_ADD_1);
    }

    @Test
    @Order(3)
    void addTrip2locationTest() {
        Trip trip = new Trip(List.of("Krakow", "Tarnow"));
        service.add(trip);
        List<Trip> trips = service.load();
        assertThat(trips).isEqualTo(DB_TRIPS_ADD_2);
    }

    @Test
    @Order(4)
    void addTrip3locationTest() {
        Trip trip = new Trip(List.of("Krakow", "Tarnow", "Rzeszow"));
        service.add(trip);
        List<Trip> trips = service.load();
        assertThat(trips).isEqualTo(DB_TRIPS_ADD_3);
    }

    @Test
    @Order(5)
    void deleteTrip1locationTest() {
        Trip trip = new Trip(List.of("Krakow"));
        service.delete(trip);
        List<Trip> trips = service.load();
        assertThat(trips).isEqualTo(DB_TRIPS_DELETE_1);
    }

    @Test
    @Order(6)
    void deleteTrip2locationTest() {
        Trip trip = new Trip(List.of("Krakow", "Tarnow"));
        service.delete(trip);
        List<Trip> trips = service.load();
        assertThat(trips).isEqualTo(DB_TRIPS_DELETE_2);
    }

    @Test
    @Order(7)
    void deleteTrip3locationTest() {
        Trip trip = new Trip(List.of("Krakow", "Tarnow", "Rzeszow"));
        service.delete(trip);
        List<Trip> trips = service.load();
        assertThat(trips).isEqualTo(DB_TRIPS_DELETE_3);
    }

    @AfterAll
    @SneakyThrows
    public static void deinit() {
        Path path = Paths.get(DB_FILENAME);
        Files.delete(path);
    }
}
