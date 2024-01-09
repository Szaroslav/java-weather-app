package pl.edu.agh.to.weatherapp.service.persistence;

import pl.edu.agh.to.weatherapp.model.internal.Trip;

import java.util.List;

public interface TripPersistenceService {
    List<Trip> load();

    void add(Trip trip);

    void delete(Trip trip);
}
