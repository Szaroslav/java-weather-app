package pl.edu.agh.to.weatherapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import pl.edu.agh.to.weatherapp.model.enums.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.enums.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.enums.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.enums.WindIntensity;

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
