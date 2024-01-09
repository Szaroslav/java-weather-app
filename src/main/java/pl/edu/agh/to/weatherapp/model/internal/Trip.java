package pl.edu.agh.to.weatherapp.model.internal;

import java.util.List;

public record Trip(List<String> locationNames) {
    public Trip(List<String> locationNames) {
        this.locationNames = List.copyOf(locationNames);
    }

    public List<String> getLocationNames() {
        return locationNames;
    }
}