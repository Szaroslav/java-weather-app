package pl.edu.agh.to.weatherapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


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
  public String fetchCurrent(String cityName) throws MalformedURLException {
    String url = BASE_API_URL + String.format(
            "current.json?key=%s&q=%s",
            apiKey,
            cityName
    );

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println)
            .join();

    return null;
  }

  @Override
  public String fetchForecast(String cityName, int daysNumber) throws MalformedURLException {

    String url = BASE_API_URL + String.format(
      "forecast.json?key=%s&q=%s&days=%d",
      apiKey,
      cityName,
      daysNumber
    );

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println)
            .join();

    return null;
  }
}
