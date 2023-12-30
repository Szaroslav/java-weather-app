package pl.edu.agh.to.weatherapp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public Trip checkIfTripPresent(Trip trip) {
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).getLocationNames().equals(trip.getLocationNames())) {
                return trips.get(i);
            }
        }
        return null;
    }

    public void clear() {
        trips.clear();
    }
}
