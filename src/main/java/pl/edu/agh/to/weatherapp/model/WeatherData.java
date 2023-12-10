package pl.edu.agh.to.weatherapp.model;

import java.util.Date;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {
    private Date date;
    private String conditionIconUrl;

    private int temperatureC;
    private float windKph;
    private float precipitationMm;
    private boolean willRain;
    private boolean willSnow;
}
