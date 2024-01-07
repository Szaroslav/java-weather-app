package pl.edu.agh.to.weatherapp.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ForecastWeatherApiDto {
    private String locationName;
    private final List<WeatherApiDto> hourlyWeatherForecasts = new ArrayList<>();
}
