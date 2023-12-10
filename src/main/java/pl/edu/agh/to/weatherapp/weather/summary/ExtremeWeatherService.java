package pl.edu.agh.to.weatherapp.weather.summary;

import pl.edu.agh.to.weatherapp.model.ForecastWeatherData;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;
import pl.edu.agh.to.weatherapp.model.internal.TemperatureLevel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtremeWeatherService implements WeatherSummaryService {

    private final Map<int[], TemperatureLevel> temperatureLevelBoundaries = new HashMap<>();
    public ExtremeWeatherService() {
        temperatureLevelBoundaries.put(new int[]{20, Integer.MAX_VALUE}, TemperatureLevel.HOT);
        temperatureLevelBoundaries.put(new int[]{5, 20}, TemperatureLevel.WARM);
        temperatureLevelBoundaries.put(new int[]{Integer.MIN_VALUE, 5}, TemperatureLevel.COLD);
    }

    @Override
    public InternalWeatherData getSummary(List<ForecastWeatherData> weatherDataList) {

        int minTemperatureC = Integer.MAX_VALUE;
        int maxWindInMps = 0;

        for(ForecastWeatherData forecastWeatherData : weatherDataList) {
            // set location names?

            for(WeatherData weatherData : forecastWeatherData.getHourlyWeatherForecasts()) {

                minTemperatureC = Math.min(minTemperatureC, weatherData.getTemperatureC());
                maxWindInMps = (int) Math.max(maxWindInMps, weatherData.getWindKph());

            }
        }


        InternalWeatherData extremeWeatherData = new InternalWeatherData();

        for (Map.Entry<int[], TemperatureLevel> entry : temperatureLevelBoundaries.entrySet()) {
            if (entry.getKey()[0] <= minTemperatureC && minTemperatureC <= entry.getKey()[1]) {
                extremeWeatherData.setTemperatureLevel(entry.getValue());
                break;
            }
        }
        extremeWeatherData.setTemperature(minTemperatureC);

        //extremeWeatherData.setConditionIconUrl();

        //extremeWeatherData.setPrecipitationIntensity();
        //extremeWeatherData.setPrecipitationType();
        //extremeWeatherData.setPrecipitationInMm();

        //extremeWeatherData.setWindIntensity();
        extremeWeatherData.setWindInMps(maxWindInMps);

        return extremeWeatherData;
    }
}
