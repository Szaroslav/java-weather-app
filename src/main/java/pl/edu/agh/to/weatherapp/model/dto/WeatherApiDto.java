package pl.edu.agh.to.weatherapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeatherApiDto {
    private DateTime date;
    private String conditionIconUrl;
    private int temperatureC;
    private float windKph;
    private float precipitationMm;
    private boolean willRain;
    private boolean willSnow;
}
