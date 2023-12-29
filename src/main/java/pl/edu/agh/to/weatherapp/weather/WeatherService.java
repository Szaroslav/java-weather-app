package pl.edu.agh.to.weatherapp.weather;

import pl.edu.agh.to.weatherapp.model.Weather;

public interface WeatherService {
    Weather getWeatherData(String location);
    Weather getWeatherData(String location, int startHour, int endHour);
    Weather getSummaryWeatherData(String startLocation, String endLocation);
    Weather getSummaryWeatherData(String startLocation, String endLocation, int startHour, int endHour);
}
