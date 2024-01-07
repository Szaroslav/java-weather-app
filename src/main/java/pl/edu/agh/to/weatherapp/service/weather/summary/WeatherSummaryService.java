package pl.edu.agh.to.weatherapp.service.weather.summary;

import pl.edu.agh.to.weatherapp.model.dto.ForecastWeatherApiDto;
import pl.edu.agh.to.weatherapp.model.internal.Weather;

import java.util.List;

public interface WeatherSummaryService {
    Weather getSummary(List<ForecastWeatherApiDto> weatherDataList);
}
