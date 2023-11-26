package pl.edu.agh.to.weatherapp.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;


public class WeatherApiFetcher implements IWeatherFetcher {
  private final static String BASE_API_URL = "http://api.weatherapi.com/v1/";
  private final String apiKey;
  private final HttpClient client;

  public WeatherApiFetcher(String apiKey) {
    this.apiKey = apiKey;
    this.client = HttpClient.newHttpClient();
  }

  @Override
  public String fetchCurrent(String cityName) throws MalformedURLException {
    throw new RuntimeException("Not implemented");
  }

  @Override
  public String fetchForecast(String cityName, int daysNumber) throws MalformedURLException {
    String url = BASE_API_URL + String.format(
      "forecast.json?key=%s&q=%s&days=%d",
      apiKey,
      cityName,
      daysNumber
    );
    HttpRequest request = HttpRequest.newBuilder(URI.create(url))
      .build();
    // var response = client.send(request, new JsonBod)
  }
}
