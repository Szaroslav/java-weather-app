package pl.edu.agh.to.weatherapp.weather.summary;

import pl.edu.agh.to.weatherapp.model.WeatherForecastData;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;

import java.util.List;

public interface WeatherSummaryService {
    InternalWeatherData getSummary(List<WeatherForecastData> weatherDataList);
}
