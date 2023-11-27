package pl.edu.agh.to.weatherapp.api;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;


@Component
public class WeatherApiFetcher implements IWeatherFetcher {
    private static final String BASE_API_URL = "http://api.weatherapi.com/v1/";
    private final String apiKey;
    private final HttpClient client;

    public WeatherApiFetcher(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public CompletableFuture<String> fetchCurrent(String cityName) {
        String url = BASE_API_URL + String.format(
            "current.json?key=%s&q=%s",
            apiKey,
            cityName
        );

        return fetchFromUrl(url);
    }

    @Override
    public CompletableFuture<String> fetchForecast(String cityName, int daysNumber) {
        String url = BASE_API_URL + String.format(
            "forecast.json?key=%s&q=%s&days=%d",
            apiKey,
            cityName,
            daysNumber
        );

        return fetchFromUrl(url);
    }

    private CompletableFuture<String> fetchFromUrl(String url) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body);
    }
}
