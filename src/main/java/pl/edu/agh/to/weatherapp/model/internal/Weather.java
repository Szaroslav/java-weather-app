package pl.edu.agh.to.weatherapp.model.internal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.internal.enums.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.internal.enums.WindIntensity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class Weather {
    private final List<String> locationNames = new ArrayList<>();
    private TemperatureLevel temperatureLevel;
    private int temperature;
    private int apparentTemperature;
    private PrecipitationIntensity precipitationIntensity;
    private PrecipitationType precipitationType;
    private int precipitationInMm;
    private WindIntensity windIntensity;
    private int windInMps;
    private boolean mud;
    public Weather setLocationNames(List<String> names) {
        this.getLocationNames().clear();
        this.getLocationNames().addAll(names);
        return this;
    }
}
