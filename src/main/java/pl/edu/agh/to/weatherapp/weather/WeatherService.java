package pl.edu.agh.to.weatherapp.weather;

import pl.edu.agh.to.weatherapp.model.ForecastWeatherData;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;

public interface WeatherService {
    //TODO: Change return type to DisplayWeatherData
    ForecastWeatherData getWeatherForecast(String location);
    InternalWeatherData getSummaryWeatherData(String startLocation, String endLocation);
}
