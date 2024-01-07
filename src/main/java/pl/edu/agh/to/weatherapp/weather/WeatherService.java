package pl.edu.agh.to.weatherapp.weather;

import pl.edu.agh.to.weatherapp.model.Weather;

import java.util.List;

public interface WeatherService {
    Weather getForecastSummaryWeatherData(List<String> locations);
    Weather getForecastSummaryWeatherData(List<String> locations, int startHour, int endHour);
}
