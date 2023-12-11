package pl.edu.agh.to.weatherapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {
    private DateTime date;
    private String conditionIconUrl;
    private int temperatureC;
    private float windKph;
    private float precipitationMm;
    private boolean willRain;
    private boolean willSnow;
}
