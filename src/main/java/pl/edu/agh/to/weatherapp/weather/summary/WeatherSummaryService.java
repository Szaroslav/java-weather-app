package pl.edu.agh.to.weatherapp.weather.summary;

import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;

import java.util.List;

public interface WeatherSummaryService {
    InternalWeatherData getSummary(List<WeatherData> weatherDataList);
}
