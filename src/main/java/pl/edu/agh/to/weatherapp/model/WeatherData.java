package pl.edu.agh.to.weatherapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherData {
    private int temp;
    private String locationName;
    private String conditionIconUrl;
}
