package pl.edu.agh.to.weatherapp.presenters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.edu.agh.to.weatherapp.model.Trip;

public class TripMemory {
    private final ObservableList<Trip> trips = FXCollections.observableArrayList();

    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    public void deleteTrip(Trip trip) {
        trips.remove(trip);
    }

    public ObservableList<Trip> getTrips() {
        return trips;
    }

    public void clear() {
        trips.clear();
    }
}
