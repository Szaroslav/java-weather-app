package pl.edu.agh.to.weatherapp.service.persistence;

import pl.edu.agh.to.weatherapp.model.internal.Trip;

public interface TripPersistenceService {
    Trip[] load();

    void add(Trip trip);

    void delete(Trip trip);
}
