package pl.edu.agh.to.weatherapp.model.internal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class InternalWeatherData {
    private final List<String> locationNames = new ArrayList<>();
    private TemperatureLevel temperatureLevel;
    private int temperature;
    private int apparentTemperature;
    private PrecipitationIntensity precipitationIntensity;
    private PrecipitationType precipitationType;
    private int precipitationInMm;
    private WindIntensity windIntensity;
    private int windInMps;
}
