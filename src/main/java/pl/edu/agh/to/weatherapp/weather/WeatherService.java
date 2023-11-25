package pl.edu.agh.to.weatherapp.weather;

import pl.edu.agh.to.weatherapp.WeatherData;

public interface WeatherService {
    WeatherData getWeatherData(String loc);
}
