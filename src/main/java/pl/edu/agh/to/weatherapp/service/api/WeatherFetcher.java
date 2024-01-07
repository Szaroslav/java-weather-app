package pl.edu.agh.to.weatherapp.service.api;

import java.util.concurrent.CompletableFuture;

public interface WeatherFetcher {
    CompletableFuture<String> fetchCurrent(String locationName);
    CompletableFuture<String> fetchForecast(String locationName, int daysNumber);
}
