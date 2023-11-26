package pl.edu.agh.to.weatherapp.api;

import java.net.MalformedURLException;

public interface IWeatherFetcher {
  String fetchCurrent(String locationName) throws MalformedURLException;
  String fetchForecast(String locationName, int daysNumber) throws MalformedURLException;
}
