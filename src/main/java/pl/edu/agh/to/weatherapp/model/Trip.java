package pl.edu.agh.to.weatherapp.model;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class Trip {
    private final HashSet<String> locationNames;

    public Trip(List<String> locationNames) {
        this.locationNames = new HashSet<>(locationNames);
    }

    public List<String> getLocationNames() {
        return locationNames.stream().toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return locationNames.containsAll(trip.locationNames) && trip.locationNames.containsAll(locationNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationNames);
    }
}