package pl.edu.agh.to.weatherapp.weather.summary;

import pl.edu.agh.to.weatherapp.model.ForecastWeatherData;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.model.internal.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dataprocessing.WeatherDataProcessing.getApparentTemperature;
import static dataprocessing.WeatherDataProcessing.kphToMps;

public class ExtremeWeatherService implements WeatherSummaryService {

    private final Map<int[], TemperatureLevel> temperatureLevelBoundaries = new HashMap<>();
    private final Map<int[], PrecipitationIntensity> precipitationIntensityBoundaries = new HashMap<>();
    private final Map<int[], WindIntensity> windIntensityBoundaries = new HashMap<>();
    public ExtremeWeatherService() {

        // set temperatureLevel boundaries
        temperatureLevelBoundaries.put(new int[]{20, Integer.MAX_VALUE}, TemperatureLevel.HOT);
        temperatureLevelBoundaries.put(new int[]{5, 20}, TemperatureLevel.WARM);
        temperatureLevelBoundaries.put(new int[]{Integer.MIN_VALUE, 5}, TemperatureLevel.COLD);

        // set precipitationIntensity boundaries
        precipitationIntensityBoundaries.put(new int[]{0, 5}, PrecipitationIntensity.WEAK);
        precipitationIntensityBoundaries.put(new int[]{5, 10}, PrecipitationIntensity.MEDIUM);
        precipitationIntensityBoundaries.put(new int[]{10, Integer.MAX_VALUE}, PrecipitationIntensity.STRONG);

        // set windIntensity boundaries
        windIntensityBoundaries.put(new int[]{0, 5}, WindIntensity.BREEZE);
        windIntensityBoundaries.put(new int[]{5, 10}, WindIntensity.WINDY);
        windIntensityBoundaries.put(new int[]{10, Integer.MAX_VALUE}, WindIntensity.STORM);
    }

    @Override
    public InternalWeatherData getSummary(List<ForecastWeatherData> weatherDataList) {

        int minTemperature = Integer.MAX_VALUE;
        float minApparentTemperature = Integer.MAX_VALUE;
        float maxWindInMps = 0;
        float maxPrecipitationMm = 0;
        boolean willRain = false;
        boolean willSnow = false;
        String dominatingConditionIconUrl = null;

        for(ForecastWeatherData forecastWeatherData : weatherDataList) {
            for(WeatherData weatherData : forecastWeatherData.getHourlyWeatherForecasts()) {
                minTemperature = Math.min(minTemperature, weatherData.getTemperatureC());
                minApparentTemperature = Math.min(minApparentTemperature, getApparentTemperature(weatherData.getTemperatureC(), weatherData.getWindKph()));
                if (!willRain && weatherData.isWillRain()) {
                    willRain = true;
                    if (!willSnow) {
                        dominatingConditionIconUrl = weatherData.getConditionIconUrl();
                    }
                }
                if (!willSnow && weatherData.isWillSnow()) {
                    willSnow = true;
                    dominatingConditionIconUrl = weatherData.getConditionIconUrl();
                }
                if (!willRain && !willSnow) {
                    dominatingConditionIconUrl = weatherData.getConditionIconUrl();
                }
                maxPrecipitationMm = Math.max(maxPrecipitationMm, weatherData.getPrecipitationMm());
                maxWindInMps = Math.max(maxWindInMps, kphToMps(weatherData.getWindKph()));
            }
        }


        InternalWeatherData extremeWeatherData = new InternalWeatherData();

        for (Map.Entry<int[], TemperatureLevel> entry : temperatureLevelBoundaries.entrySet()) {
            if (entry.getKey()[0] <= minApparentTemperature && minApparentTemperature <= entry.getKey()[1]) {
                extremeWeatherData.setTemperatureLevel(entry.getValue());
                break;
            }
        }
        extremeWeatherData.setTemperature(minTemperature);

        extremeWeatherData.setConditionIconUrl(dominatingConditionIconUrl);

        if (willRain && willSnow) {
            extremeWeatherData.setPrecipitationType(PrecipitationType.BOTH);
        }
        else if (willRain) {
            extremeWeatherData.setPrecipitationType(PrecipitationType.RAIN);
        }
        else if (willSnow) {
            extremeWeatherData.setPrecipitationType(PrecipitationType.SNOW);
        }
        else {
            extremeWeatherData.setPrecipitationType(PrecipitationType.NONE);
        }

        for (Map.Entry<int[], PrecipitationIntensity> entry : precipitationIntensityBoundaries.entrySet()) {
            if (entry.getKey()[0] <= maxPrecipitationMm && maxPrecipitationMm <= entry.getKey()[1]) {
                extremeWeatherData.setPrecipitationIntensity(entry.getValue());
                break;
            }
        }
        extremeWeatherData.setPrecipitationInMm((int) maxPrecipitationMm);

        for (Map.Entry<int[], WindIntensity> entry : windIntensityBoundaries.entrySet()) {
            if (entry.getKey()[0] <= maxWindInMps && maxWindInMps <= entry.getKey()[1]) {
                extremeWeatherData.setWindIntensity(entry.getValue());
                break;
            }
        }
        extremeWeatherData.setWindInMps((int) maxWindInMps);

        return extremeWeatherData;
    }
}
