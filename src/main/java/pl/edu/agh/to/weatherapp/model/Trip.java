package pl.edu.agh.to.weatherapp.model;

import lombok.Getter;

import java.util.List;

public class Trip {
    @Getter
    private final List<String> locationNames;

    public Trip(List<String> locationNames) {
        this.locationNames = List.copyOf(locationNames);
    }
}