package pl.edu.agh.to.weatherapp.service.persistence;

import pl.edu.agh.to.weatherapp.exceptions.DatabaseFailure;
import pl.edu.agh.to.weatherapp.model.internal.Trip;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlitePersistenceService implements TripPersistenceService {
    private static final String DB_FILENAME = "trips.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILENAME;

    @Override
    public Trip[] load() {
        createTableIfNotExists();
        String sql = "SELECT location1, location2, location3 FROM trips";

        try (Connection conn = this.connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            List<Trip> tripList = new ArrayList<>();

            while (rs.next()) {
                List<String> locationNames = new ArrayList<>();
                locationNames.add(rs.getString("location1"));
                locationNames.add(rs.getString("location2"));
                locationNames.add(rs.getString("location3"));
                tripList.add(new Trip(locationNames));
            }

            return tripList.toArray(tripList.toArray(new Trip[0]));
        } catch (SQLException e) {
            throw new DatabaseFailure(e.getMessage());
        }
    }

    @Override
    public void save(List<Trip> tripList) {
        createTableIfNotExists();
        String sql = "INSERT INTO trips(location1,location2,location3) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, "Tarnow");
            statement.setString(2, "Tarnow");
            statement.setString(3, "Tarnow");
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseFailure(e.getMessage());
        }
    }

    private Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new DatabaseFailure(e.getMessage());
        }
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS trips (id integer PRIMARY KEY, location1 text NOT NULL, location2 text, location3 text);";

        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseFailure(e.getMessage());
        }
    }
}
