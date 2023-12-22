package pl.edu.agh.to.weatherapp.weather;

import pl.edu.agh.to.weatherapp.model.InternalWeatherData;

public interface WeatherService {
    InternalWeatherData getWeatherData(String location);
    InternalWeatherData getWeatherData(String location, int startHour, int endHour);
    InternalWeatherData getSummaryWeatherData(String startLocation, String endLocation);
    InternalWeatherData getSummaryWeatherData(String startLocation, String endLocation, int startHour, int endHour);
}
