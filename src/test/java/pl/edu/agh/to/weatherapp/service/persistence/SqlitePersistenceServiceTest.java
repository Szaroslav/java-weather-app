package pl.edu.agh.to.weatherapp.service.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to.weatherapp.model.internal.Trip;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SqlitePersistenceServiceTest {
    private static final String JDBC = "jdbc:sqlite::memory:";
    private static final List<Trip> DB_TRIPS_1 = List.of(new Trip(List.of("l1")));
    private static final List<Trip> DB_TRIPS_2 = List.of(new Trip(List.of("l1")),
            new Trip(List.of("l1", "l2")));
    private static final List<Trip> DB_TRIPS_3 = List.of(new Trip(List.of("l1")),
            new Trip(List.of("l1", "l2")),
            new Trip(List.of("l1", "l2", "l3")));
    private static SqlitePersistenceService service;

    @BeforeEach
    public void setup() {
        service = new SqlitePersistenceService(JDBC);
    }

    @Test
    void loadTripsTest() {
        List<Trip> trips = service.load();
        assertThat(trips).isEqualTo(List.of());
    }

    @Test
    void addTripsTest() {
        Trip trip1 = new Trip(List.of("l1"));
        service.add(trip1);
        List<Trip> trips1 = service.load();
        assertThat(trips1).isEqualTo(DB_TRIPS_1);

        Trip trip2 = new Trip(List.of("l1", "l2"));
        service.add(trip2);
        List<Trip> trips2 = service.load();
        assertThat(trips2).isEqualTo(DB_TRIPS_2);

        Trip trip3 = new Trip(List.of("l1", "l2", "l3"));
        service.add(trip3);
        List<Trip> trips3 = service.load();
        assertThat(trips3).isEqualTo(DB_TRIPS_3);
    }

    @Test
    void deleteTripsTest() {
        for (Trip trip : DB_TRIPS_3) {
            service.add(trip);
        }
        List<Trip> trips0 = service.load();
        assertThat(trips0).isEqualTo(DB_TRIPS_3);

        Trip trip1 = new Trip(List.of("l1", "l2", "l3"));
        service.delete(trip1);
        List<Trip> trips1 = service.load();
        assertThat(trips1).isEqualTo(DB_TRIPS_2);

        Trip trip2 = new Trip(List.of("l1", "l2"));
        service.delete(trip2);
        List<Trip> trips2 = service.load();
        assertThat(trips2).isEqualTo(DB_TRIPS_1);

        Trip trip3 = new Trip(List.of("l1"));
        service.delete(trip3);
        List<Trip> trips3 = service.load();
        assertThat(trips3).isEqualTo(List.of());
    }

    @AfterEach
    public void teardown() {
        service.close();
    }
}
