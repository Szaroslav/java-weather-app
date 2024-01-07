package pl.edu.agh.to.weatherapp.service.api;

import java.util.concurrent.CompletableFuture;
import org.joda.time.DateTime;

public interface WeatherFetcher {
    CompletableFuture<String> fetchCurrent(String locationName);
    CompletableFuture<String> fetchForecast(String locationName, int daysNumber);
    CompletableFuture<String> fetchHistory(String locationName, DateTime date);
}
