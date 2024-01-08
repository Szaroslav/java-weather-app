package pl.edu.agh.to.weatherapp.service.persistence;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import pl.edu.agh.to.weatherapp.model.internal.Trip;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonPersistenceService implements TripPersistenceService {
    private static final String FILENAME = "memory.json";

    @SneakyThrows
    public Trip[] load() {
        File file = new File(FILENAME);
        if( !file.exists() ) {
            return new Trip[]{};
        }
        Gson gson = new Gson();
        Trip[] trips = gson.fromJson(Files.readString(Path.of(FILENAME)), Trip[].class);
        return trips != null ? trips : new Trip[] {};
    }

    @SneakyThrows
    public void save(List<Trip> tripList) {
        Gson gson = new Gson();
        String json = gson.toJson(tripList);
        PrintWriter out = new PrintWriter(FILENAME);
        out.print(json);
        out.close();
    }
}
