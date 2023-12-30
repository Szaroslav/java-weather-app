package pl.edu.agh.to.weatherapp.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TripMemory {
    private final ObservableList<Trip> trips = FXCollections.observableArrayList();
    private final BooleanProperty currentTripFavourite = new SimpleBooleanProperty(false);
    private Trip leasedTrip;
    public BooleanProperty getCurrentTripFavourite(){
        return currentTripFavourite;
    }
    public void addTrip(Trip trip) {
        trips.add(trip);
        leasedTrip = trip;
        currentTripFavourite.set(true);
    }

    public void deleteTrip(Trip trip) {
        if(leasedTrip.equals(trip)){
            currentTripFavourite.setValue(false);
        }
        trips.remove(trip);
    }

    public ObservableList<Trip> getTrips() {
        return trips;
    }

    public Trip checkIfTripPresent(Trip trip) {
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).getLocationNames().equals(trip.getLocationNames())) {
                currentTripFavourite.set(true);
                leasedTrip = trips.get(i);
                return trips.get(i);
            }
        }
        currentTripFavourite.set(false);
        return null;
    }

    public void clear() {
        trips.clear();
    }
}
