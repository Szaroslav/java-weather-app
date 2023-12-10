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
    private int minTemperature = Integer.MAX_VALUE;
    private float minApparentTemperature = Integer.MAX_VALUE;
    private float maxWindInMps = 0;
    private float maxPrecipitationMm = 0;
    private boolean willRain = false;
    private boolean willSnow = false;
    private String dominatingConditionIconUrl = null;

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
        initializeSummaryData(weatherDataList);
        return initializeInternalWeatherData();
    }

    private void initializeSummaryData(List<ForecastWeatherData> weatherDataList) {
        for(ForecastWeatherData forecastWeatherData : weatherDataList) {
            for(WeatherData weatherData : forecastWeatherData.getHourlyWeatherForecasts()) {
                analyzeWeatherData(weatherData);
            }
        }
    }

    private void analyzeWeatherData(WeatherData weatherData) {
        minTemperature = Math.min(minTemperature, weatherData.getTemperatureC());
        minApparentTemperature = Math.min(minApparentTemperature, getApparentTemperature(weatherData.getTemperatureC(), weatherData.getWindKph()));
        maxPrecipitationMm = Math.max(maxPrecipitationMm, weatherData.getPrecipitationMm());
        maxWindInMps = Math.max(maxWindInMps, kphToMps(weatherData.getWindKph()));

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
    }

    private InternalWeatherData initializeInternalWeatherData() {
        InternalWeatherData extremeWeatherData = new InternalWeatherData();

        initializeTemperatureData(extremeWeatherData);
        initializePrecipitationData(extremeWeatherData);
        initializeWindData(extremeWeatherData);

        extremeWeatherData.setConditionIconUrl(dominatingConditionIconUrl);

        return extremeWeatherData;
    }

    private void initializeTemperatureData(InternalWeatherData weatherData) {
        for (Map.Entry<int[], TemperatureLevel> entry : temperatureLevelBoundaries.entrySet()) {
            if (entry.getKey()[0] <= minApparentTemperature && minApparentTemperature <= entry.getKey()[1]) {
                weatherData.setTemperatureLevel(entry.getValue());
                break;
            }
        }
        weatherData.setTemperature(minTemperature);
    }

    private void initializePrecipitationData(InternalWeatherData weatherData) {
        if (willRain && willSnow) {
            weatherData.setPrecipitationType(PrecipitationType.BOTH);
        }
        else if (willRain) {
            weatherData.setPrecipitationType(PrecipitationType.RAIN);
        }
        else if (willSnow) {
            weatherData.setPrecipitationType(PrecipitationType.SNOW);
        }
        else {
            weatherData.setPrecipitationType(PrecipitationType.NONE);
        }

        for (Map.Entry<int[], PrecipitationIntensity> entry : precipitationIntensityBoundaries.entrySet()) {
            if (entry.getKey()[0] <= maxPrecipitationMm && maxPrecipitationMm <= entry.getKey()[1]) {
                weatherData.setPrecipitationIntensity(entry.getValue());
                break;
            }
        }
        weatherData.setPrecipitationInMm((int) maxPrecipitationMm);
    }

    private void initializeWindData(InternalWeatherData weatherData) {
        for (Map.Entry<int[], WindIntensity> entry : windIntensityBoundaries.entrySet()) {
            if (entry.getKey()[0] <= maxWindInMps && maxWindInMps <= entry.getKey()[1]) {
                weatherData.setWindIntensity(entry.getValue());
                break;
            }
        }
        weatherData.setWindInMps((int) maxWindInMps);
    }
}
