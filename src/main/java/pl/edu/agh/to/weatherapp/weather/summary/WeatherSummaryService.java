package pl.edu.agh.to.weatherapp.weather.summary;

import pl.edu.agh.to.weatherapp.dto.ForecastWeatherApiDto;
import pl.edu.agh.to.weatherapp.model.Weather;

import java.util.List;

public interface WeatherSummaryService {
    Weather getSummary(List<ForecastWeatherApiDto> weatherDataList);
}
