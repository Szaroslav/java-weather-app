package pl.edu.agh.to.weatherapp.model.internal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InternalWeatherData {
    private List<String> locationNames;
    private TemperatureLevel temperatureLevel;
    private int temperature;
    private String conditionIconUrl;
    private PrecipitationIntensity precipitationIntensity;
    private PrecipitationType precipitationType;
    private int precipitationInMm;
    private WindIntensity windIntensity;
    private int windInMps;
}
