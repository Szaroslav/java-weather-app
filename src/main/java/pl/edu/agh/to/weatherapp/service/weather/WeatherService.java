package pl.edu.agh.to.weatherapp.service.weather;

import pl.edu.agh.to.weatherapp.model.internal.Weather;

import java.util.List;

public interface WeatherService {
    Weather getForecastSummaryWeatherData(List<String> locationNames);
    Weather getForecastSummaryWeatherData(List<String> locationNames, int startHour, int endHour);
}
