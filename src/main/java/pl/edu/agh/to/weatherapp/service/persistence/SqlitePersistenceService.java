package pl.edu.agh.to.weatherapp.service.persistence;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.exceptions.DatabaseFailureException;
import pl.edu.agh.to.weatherapp.model.internal.Trip;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Service
public class SqlitePersistenceService implements TripPersistenceService, Closeable {
    private static final String CONNECTION_ERROR = "Couldn't connect to database";
    private static final String QUERY_ERROR = "Couldn't execute query";
    private static final String LOCATION_LABEL = "location";
    private final Connection connection;

    public SqlitePersistenceService(String dbJdbc) {
        try {
            connection = DriverManager.getConnection(dbJdbc);
        } catch (SQLException e) {
            throw new DatabaseFailureException(CONNECTION_ERROR, e);
        }
        createTableIfNotExists();
    }

    @Override
    public List<Trip> load() {
        String sql = "SELECT location1, location2, location3 FROM trips";
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            List<Trip> tripList = new ArrayList<>();
            while (rs.next()) {
                Trip trip = getTripFromResultSet(rs);
                tripList.add(trip);
            }
            return tripList;
        } catch (SQLException e) {
            throw new DatabaseFailureException(QUERY_ERROR, e);
        }
    }

    @Override
    public void add(Trip trip) {
        String sql = "INSERT INTO trips(location1,location2,location3) VALUES(?,?,?)";
        fillAndExecuteStatement(sql, trip);
    }

    @Override
    public void delete(Trip trip) {
        String sql = "DELETE FROM trips WHERE location1 IS ? AND location2 IS ? AND location3 IS ?";
        fillAndExecuteStatement(sql, trip);
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS " +
                "trips (id integer PRIMARY KEY, location1 text NOT NULL, location2 text, location3 text);";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseFailureException(QUERY_ERROR, e);
        }
    }

    private void fillAndExecuteStatement(String sql, Trip trip) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            List<String> locationNames = trip.locationNames();
            int paramCount = Math.min(locationNames.size(), 3);
            for (int i = 0; i < paramCount; i++) {
                statement.setString(i + 1, locationNames.get(i));
            }
            for (int i = paramCount; i < 3; i++) {
                statement.setNull(i + 1, Types.VARCHAR);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseFailureException(QUERY_ERROR, e);
        }
    }

    private Trip getTripFromResultSet(ResultSet resultSet) throws SQLException {
        List<String> locationNames = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String location = resultSet.getString(LOCATION_LABEL + i);
            if (location == null) {
                break;
            }
            locationNames.add(location);
        }
        return new Trip(locationNames);
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseFailureException(e);
        }
    }
}
