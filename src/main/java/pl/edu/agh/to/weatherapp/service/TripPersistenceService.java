package pl.edu.agh.to.weatherapp.service;

import pl.edu.agh.to.weatherapp.model.Trip;

import java.util.List;

public class TripPersistenceService {
    public Trip[] load() {
        return new Trip[]{new Trip(List.of("Tarnów", "Ryglice")), new Trip(List.of("Tarnów", "Bistuszowa"))};
    }

    public void save(List<Trip> tripList) {
        //TODO
    }
}
