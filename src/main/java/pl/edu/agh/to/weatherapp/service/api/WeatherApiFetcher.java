package pl.edu.agh.to.weatherapp.service.api;

import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Component
public class WeatherApiFetcher implements WeatherFetcher {
    private static final String BASE_API_URL = "https://api.weatherapi.com/v1/";
    private static final String CURRENT_WEATHER_URL = BASE_API_URL + "current.json";
    private static final String FORECAST_WEATHER_URL = BASE_API_URL + "forecast.json";
    private static final String HISTORY_WEATHER_URL = BASE_API_URL + "history.json";

    private final String apiKey;
    private final HttpClient client;

    public WeatherApiFetcher(String apiKey, HttpClient client) {
        this.apiKey = apiKey;
        this.client = client;
    }

    @Override
    @SneakyThrows
    public CompletableFuture<String> fetchCurrent(String locationName) {
        URI uri = new URIBuilder(CURRENT_WEATHER_URL)
            .addParameter("key", apiKey)
            .addParameter("q",   locationName)
            .build();

        return fetchFromUri(uri);
    }

    @Override
    @SneakyThrows
    public CompletableFuture<String> fetchForecast(String locationName, int daysNumber) {
        String daysNumberString = Integer.toString(daysNumber);
        URI uri = new URIBuilder(FORECAST_WEATHER_URL)
            .addParameter("key",  apiKey)
            .addParameter("q",    locationName)
            .addParameter("days", daysNumberString)
            .build();

        return fetchFromUri(uri);
    }

    @Override
    @SneakyThrows
    public CompletableFuture<String> fetchHistory(String locationName, DateTime date) {
        String dateString = DateTimeFormat.forPattern("yyyy-MM-dd").print(date);
        URI uri = new URIBuilder(HISTORY_WEATHER_URL)
            .addParameter("key", apiKey)
            .addParameter("q",   locationName)
            .addParameter("dt",  dateString)
            .build();

        return fetchFromUri(uri);
    }

    private CompletableFuture<String> fetchFromUri(URI uri) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body);
    }
}
