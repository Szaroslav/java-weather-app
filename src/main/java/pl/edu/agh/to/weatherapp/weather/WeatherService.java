package pl.edu.agh.to.weatherapp.weather;

import pl.edu.agh.to.weatherapp.model.WeatherData;

public interface WeatherService {
    WeatherData getWeatherData(String location);
}
