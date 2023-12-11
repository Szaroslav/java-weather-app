package pl.edu.agh.to.weatherapp.weather.summary;

import pl.edu.agh.to.weatherapp.model.ForecastWeatherData;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.model.internal.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataprocessing.WeatherDataProcessing;

public class ExtremeWeatherService implements WeatherSummaryService {
    private int minTemperature = Integer.MAX_VALUE;
    private float minApparentTemperature = Integer.MAX_VALUE;
    private float maxWindInMps = 0;
    private float maxPrecipitationMm = 0;
    private boolean willRain = false;
    private boolean willSnow = false;

    private TemperatureLevel getTemperatureLevel(int minTemperature) {
        if (20 <= minTemperature) return TemperatureLevel.HOT;
        else if (minTemperature <= 5) return TemperatureLevel.COLD;
        else return TemperatureLevel.WARM;
    }

    private PrecipitationIntensity getPrecipitationIntensity(int precipitationMm) {
        if (precipitationMm <= 5) return PrecipitationIntensity.WEAK;
        else if (precipitationMm <= 10) return PrecipitationIntensity.MEDIUM;
        else return PrecipitationIntensity.STRONG;
    }

    private WindIntensity getWindIntensity(int maxWindInMps) {
        if (maxWindInMps <= 5) return WindIntensity.BREEZE;
        else if (maxWindInMps <= 10) return WindIntensity.WINDY;
        else return WindIntensity.STORM;
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
        minApparentTemperature = Math.min(minApparentTemperature, WeatherDataProcessing.getApparentTemperature(weatherData.getTemperatureC(), weatherData.getWindKph()));
        maxPrecipitationMm = Math.max(maxPrecipitationMm, weatherData.getPrecipitationMm());
        maxWindInMps = Math.max(maxWindInMps, WeatherDataProcessing.kphToMps(weatherData.getWindKph()));
        if (weatherData.isWillRain()) willRain = true;
        if (weatherData.isWillSnow()) willSnow = true;
    }

    private InternalWeatherData initializeInternalWeatherData() {
        InternalWeatherData extremeWeatherData = new InternalWeatherData();

        extremeWeatherData.setTemperatureLevel(getTemperatureLevel((int) minApparentTemperature));
        extremeWeatherData.setTemperature(minTemperature);
        extremeWeatherData.setWindIntensity(getWindIntensity((int) maxWindInMps));
        extremeWeatherData.setWindInMps((int) maxWindInMps);
        initializePrecipitationData(extremeWeatherData);

        return extremeWeatherData;
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

        weatherData.setPrecipitationIntensity(getPrecipitationIntensity((int) maxPrecipitationMm));
        weatherData.setPrecipitationInMm((int) maxPrecipitationMm);
    }
}
