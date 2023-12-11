package pl.edu.agh.to.weatherapp.weather;

import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;

public interface WeatherService {
    InternalWeatherData getWeatherData(String location);
    InternalWeatherData getSummaryWeatherData(String startLocation, String endLocation);
}
