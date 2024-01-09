package pl.edu.agh.to.weatherapp.gui.presenters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.edu.agh.to.weatherapp.model.internal.Trip;
import pl.edu.agh.to.weatherapp.service.persistence.TripPersistenceService;

public class FavouriteTrips {
    private final TripPersistenceService tripPersistenceService;
    private final ObservableList<Trip> trips = FXCollections.observableArrayList();

    public FavouriteTrips(TripPersistenceService tripPersistenceService) {
        this.tripPersistenceService = tripPersistenceService;
        trips.addAll(this.tripPersistenceService.load());
    }
    public void addTrip(Trip trip) {
        trips.add(trip);
        tripPersistenceService.add(trip);
    }

    public void deleteTrip(Trip trip) {
        trips.remove(trip);
        // TODO: delete trip
        // tripPersistenceService.save(trips);
    }

    public ObservableList<Trip> getTrips() {
        return trips;
    }
}
