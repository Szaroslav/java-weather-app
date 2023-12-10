package pl.edu.agh.to.weatherapp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WeatherForecastData {
    private String locationName;
    private final List<WeatherData> hourlyWeatherForecasts = new ArrayList<>();
}
