package pl.edu.agh.to.weatherapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ForecastWeatherData {
    private String locationName;
    private final List<WeatherData> hourlyWeatherForecasts = new ArrayList<>();
}
