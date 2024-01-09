package pl.edu.agh.to.weatherapp.gui.presenters;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.edu.agh.to.weatherapp.model.internal.Trip;
import pl.edu.agh.to.weatherapp.service.persistence.TripPersistenceService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FavouriteTripsTest {
    private final static Trip EXAMPLE_TRIP_1 = new Trip(List.of("Tarnów", "Cracow"));
    private final static Trip EXAMPLE_TRIP_2 = new Trip(List.of("Tarnów", "Cracow", "Rzeszów"));
    @Mock
    private final TripPersistenceService tripPersistenceService = Mockito.mock(TripPersistenceService.class);

    @Test
    void testMemoryEmpty() {
        // given
        Mockito.when(tripPersistenceService.load()).thenReturn(List.of());

        // when
        FavouriteTrips favouriteTrips = new FavouriteTrips(tripPersistenceService);

        // then
        assertThat(favouriteTrips.getTrips()).isEmpty();
    }

    @Test
    void testMemoryNotEmpty() {
        // given
        Mockito.when(tripPersistenceService.load()).thenReturn(List.of(EXAMPLE_TRIP_1));

        // when
        FavouriteTrips favouriteTrips = new FavouriteTrips(tripPersistenceService);

        // then
        assertThat(favouriteTrips.getTrips()).containsExactly(EXAMPLE_TRIP_1);
    }

    @Test
    void testMemoryAddDelete() {
        // given
        Mockito.when(tripPersistenceService.load()).thenReturn(List.of(EXAMPLE_TRIP_1));

        // when
        FavouriteTrips favouriteTrips = new FavouriteTrips(tripPersistenceService);
        favouriteTrips.addTrip(EXAMPLE_TRIP_2);
        favouriteTrips.deleteTrip(EXAMPLE_TRIP_1);

        // then
        assertThat(favouriteTrips.getTrips()).containsExactly(EXAMPLE_TRIP_2);
    }
}