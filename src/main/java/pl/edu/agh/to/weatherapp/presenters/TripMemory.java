package pl.edu.agh.to.weatherapp.presenters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.edu.agh.to.weatherapp.model.Trip;
import pl.edu.agh.to.weatherapp.service.TripPersistenceService;

public class TripMemory {
    private final TripPersistenceService tripPersistenceService;
    private final ObservableList<Trip> trips = FXCollections.observableArrayList();

    public TripMemory(TripPersistenceService tripPersistenceService) {
        this.tripPersistenceService = tripPersistenceService;
        trips.addAll(this.tripPersistenceService.load());
    }
    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    public void deleteTrip(Trip trip) {
        trips.remove(trip);
    }

    public ObservableList<Trip> getTrips() {
        return trips;
    }

    public void save() {
        tripPersistenceService.save(trips);
    }
}
