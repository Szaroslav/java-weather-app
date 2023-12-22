package pl.edu.agh.to.weatherapp.weather.summary;

import pl.edu.agh.to.weatherapp.dto.ForecastWeatherApiDto;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;

import java.util.List;

public interface WeatherSummaryService {
    InternalWeatherData getSummary(List<ForecastWeatherApiDto> weatherDataList);
}
