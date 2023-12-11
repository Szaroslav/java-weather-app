package pl.edu.agh.to.weatherapp.weather.summary;

import pl.edu.agh.to.weatherapp.model.ForecastWeatherData;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.model.internal.*;

import java.util.Comparator;
import java.util.List;

import dataprocessing.WeatherDataProcessing;

public class ExtremeWeatherService implements WeatherSummaryService {
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
        List<InternalWeatherData> summarisedLocations
                = weatherDataList.stream().map(x->mapLocation(x.getHourlyWeatherForecasts())).toList();
        return getExtremeWeather(summarisedLocations);
    }

    private InternalWeatherData mapLocation(List<WeatherData> weatherDataList) {
        int minTemperature = weatherDataList.stream().map(WeatherData::getTemperatureC).min(Comparator.comparing(Integer::valueOf)).orElseThrow();
        float minApparentTemperature = weatherDataList.stream().map(data -> WeatherDataProcessing.getApparentTemperature(data.getTemperatureC(), data.getWindKph())).min(Comparator.comparing(Float::valueOf)).orElseThrow();
        float maxPrecipitationMm = weatherDataList.stream().map(WeatherData::getPrecipitationMm).max(Comparator.comparing(Float::valueOf)).orElseThrow();
        float maxWindInMps = weatherDataList.stream().map(data -> WeatherDataProcessing.kphToMps(data.getWindKph())).max(Comparator.comparing(Float::valueOf)).orElseThrow();
        boolean willRain = weatherDataList.stream().map(WeatherData::isWillRain).max(Comparator.comparing(Boolean::valueOf)).orElseThrow();
        boolean willSnow = weatherDataList.stream().map(WeatherData::isWillSnow).max(Comparator.comparing(Boolean::valueOf)).orElseThrow();

        InternalWeatherData extremeWeatherData = new InternalWeatherData();
        extremeWeatherData.setTemperatureLevel(getTemperatureLevel((int) minApparentTemperature));
        extremeWeatherData.setTemperature(minTemperature);
        extremeWeatherData.setWindIntensity(getWindIntensity((int) maxWindInMps));
        extremeWeatherData.setWindInMps((int) maxWindInMps);
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
        extremeWeatherData.setPrecipitationIntensity(getPrecipitationIntensity((int) maxPrecipitationMm));
        extremeWeatherData.setPrecipitationInMm((int) maxPrecipitationMm);

        return extremeWeatherData;
    }

    private InternalWeatherData getExtremeWeather(List<InternalWeatherData> summarisedLocations) {
        int minTemperature = summarisedLocations.stream().map(InternalWeatherData::getTemperature).min(Comparator.comparing(Integer::valueOf)).orElseThrow();
        int maxPrecipitationMm = summarisedLocations.stream().map(InternalWeatherData::getPrecipitationInMm).max(Comparator.comparing(Integer::valueOf)).orElseThrow();
        int maxWindInMps = summarisedLocations.stream().map(InternalWeatherData::getWindInMps).max(Comparator.comparing(Integer::valueOf)).orElseThrow();
        TemperatureLevel temperatureLevel = summarisedLocations.stream().map(InternalWeatherData::getTemperatureLevel).max(Comparator.comparing(TemperatureLevel::ordinal)).orElseThrow();
        PrecipitationType precipitationType = summarisedLocations.stream().map(InternalWeatherData::getPrecipitationType).max(Comparator.comparing(PrecipitationType::ordinal)).orElseThrow();

        InternalWeatherData extremeWeatherData = new InternalWeatherData();
        extremeWeatherData.setTemperatureLevel(temperatureLevel);
        extremeWeatherData.setTemperature(minTemperature);
        extremeWeatherData.setWindIntensity(getWindIntensity(maxWindInMps));
        extremeWeatherData.setWindInMps(maxWindInMps);
        extremeWeatherData.setPrecipitationType(precipitationType);
        extremeWeatherData.setPrecipitationIntensity(getPrecipitationIntensity(maxPrecipitationMm));
        extremeWeatherData.setPrecipitationInMm(maxPrecipitationMm);

        return extremeWeatherData;
    }
}
