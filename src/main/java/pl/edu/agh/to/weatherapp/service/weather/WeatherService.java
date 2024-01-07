package pl.edu.agh.to.weatherapp.service.weather;

import pl.edu.agh.to.weatherapp.model.internal.Weather;

import java.util.List;

public interface WeatherService {
    Weather getForecastSummaryWeatherData(List<String> locations);
    Weather getForecastSummaryWeatherData(List<String> locations, int startHour, int endHour);
    boolean wasPrecipitationDaysBefore(String locationName, int daysNumber);
}
