package pl.edu.agh.to.weatherapp.api;

import java.net.MalformedURLException;

public interface IWeatherFetcher {
  String fetchCurrent(String cityName) throws MalformedURLException;
  String fetchForecast(String cityName, int daysNumber) throws MalformedURLException;
}
