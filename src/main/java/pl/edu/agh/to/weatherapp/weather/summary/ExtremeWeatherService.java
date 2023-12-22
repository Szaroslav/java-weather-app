package pl.edu.agh.to.weatherapp.weather.summary;

import pl.edu.agh.to.weatherapp.dataprocessing.WeatherDataProcessing;
import pl.edu.agh.to.weatherapp.dto.ForecastWeatherApiDto;
import pl.edu.agh.to.weatherapp.dto.WeatherApiDto;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;
import pl.edu.agh.to.weatherapp.model.internal.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.internal.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.internal.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.internal.WindIntensity;

import java.util.Comparator;
import java.util.List;

public class ExtremeWeatherService implements WeatherSummaryService {
    @Override
    public InternalWeatherData getSummary(List<ForecastWeatherApiDto> weatherDataList) {
        List<InternalWeatherData> summarisedLocations = weatherDataList.stream()
                .map(x -> mapLocation(x.getHourlyWeatherForecasts()))
                .toList();
        return getExtremeWeather(summarisedLocations);
    }

    private InternalWeatherData mapLocation(List<WeatherApiDto> weatherApiDtoList) {
        int minTemperature = weatherApiDtoList.stream()
                .map(WeatherApiDto::getTemperatureC)
                .min(Comparator.comparing(Integer::valueOf))
                .orElseThrow();

        float minApparentTemperature = weatherApiDtoList.stream()
                .map(data -> WeatherDataProcessing.getApparentTemperature(data.getTemperatureC(), data.getWindKph()))
                .min(Comparator.comparing(Float::valueOf))
                .orElseThrow();

        int maxPrecipitationMm = weatherApiDtoList.stream()
                .map(WeatherApiDto::getPrecipitationMm)
                .max(Comparator.comparing(Float::valueOf))
                .map(data -> (int) Math.ceil(data))
                .orElseThrow();

        int maxWindInMps = weatherApiDtoList.stream()
                .map(data -> WeatherDataProcessing.kphToMps(data.getWindKph()))
                .max(Comparator.comparing(Float::valueOf))
                .map(data -> (int) Math.ceil(data))
                .orElseThrow();

        boolean willRain = weatherApiDtoList.stream()
                .map(WeatherApiDto::isWillRain)
                .max(Comparator.comparing(Boolean::valueOf))
                .orElseThrow();

        boolean willSnow = weatherApiDtoList.stream()
                .map(WeatherApiDto::isWillSnow)
                .max(Comparator.comparing(Boolean::valueOf))
                .orElseThrow();

        PrecipitationType precipitationType = PrecipitationType.NONE;

        if (willRain && willSnow) {
            precipitationType = PrecipitationType.BOTH;
        } else if (willRain) {
            precipitationType = PrecipitationType.RAIN;
        } else if (willSnow) {
            precipitationType = PrecipitationType.SNOW;
        }

        return new InternalWeatherData()
                .setTemperatureLevel(getTemperatureLevel((int) minApparentTemperature))
                .setTemperature(minTemperature)
                .setApparentTemperature((int) minApparentTemperature)
                .setWindIntensity(getWindIntensity(maxWindInMps))
                .setWindInMps(maxWindInMps)
                .setPrecipitationType(precipitationType)
                .setPrecipitationIntensity(getPrecipitationIntensity(maxPrecipitationMm))
                .setPrecipitationInMm(maxPrecipitationMm);
    }

    private InternalWeatherData getExtremeWeather(List<InternalWeatherData> summarisedLocations) {
        int minTemperature = summarisedLocations.stream()
                .map(InternalWeatherData::getTemperature)
                .min(Comparator.comparing(Integer::valueOf))
                .orElseThrow();

        int minApparentTemperature = summarisedLocations.stream()
                .map(InternalWeatherData::getApparentTemperature)
                .min(Comparator.comparing(Integer::valueOf))
                .orElseThrow();

        int maxPrecipitationMm = summarisedLocations.stream()
                .map(InternalWeatherData::getPrecipitationInMm)
                .max(Comparator.comparing(Integer::valueOf))
                .orElseThrow();

        int maxWindInMps = summarisedLocations.stream()
                .map(InternalWeatherData::getWindInMps)
                .max(Comparator.comparing(Integer::valueOf))
                .orElseThrow();

        PrecipitationType precipitationType = summarisePrecipitation(summarisedLocations.stream()
                .map(InternalWeatherData::getPrecipitationType).toList());

        return new InternalWeatherData()
                .setTemperatureLevel(getTemperatureLevel(minApparentTemperature))
                .setTemperature(minTemperature)
                .setApparentTemperature(minApparentTemperature)
                .setWindIntensity(getWindIntensity(maxWindInMps))
                .setWindInMps(maxWindInMps)
                .setPrecipitationType(precipitationType)
                .setPrecipitationIntensity(getPrecipitationIntensity(maxPrecipitationMm))
                .setPrecipitationInMm(maxPrecipitationMm);
    }

    private TemperatureLevel getTemperatureLevel(int minTemperature) {
        if (minTemperature < 5) {
            return TemperatureLevel.COLD;
        }
        if (minTemperature < 20) {
            return TemperatureLevel.WARM;
        }
        return TemperatureLevel.HOT;
    }

    private PrecipitationIntensity getPrecipitationIntensity(int precipitationMm) {
        if (precipitationMm <= 5) {
            return PrecipitationIntensity.WEAK;
        }
        if (precipitationMm <= 10) {
            return PrecipitationIntensity.MEDIUM;
        }
        return PrecipitationIntensity.STRONG;
    }

    private WindIntensity getWindIntensity(int maxWindInMps) {
        if (maxWindInMps <= 5) {
            return WindIntensity.BREEZE;
        }
        if (maxWindInMps <= 10) {
            return WindIntensity.WINDY;
        }
        return WindIntensity.STORM;
    }

    private PrecipitationType summarisePrecipitation(List<PrecipitationType> precipitationList) {
        if (precipitationList.contains(PrecipitationType.BOTH)) {
            return PrecipitationType.BOTH;
        }
        if (precipitationList.contains(PrecipitationType.RAIN)
                && precipitationList.contains(PrecipitationType.SNOW)) {
            return PrecipitationType.BOTH;
        }
        if (precipitationList.contains(PrecipitationType.SNOW)) {
            return PrecipitationType.SNOW;
        }
        if (precipitationList.contains(PrecipitationType.RAIN)) {
            return PrecipitationType.RAIN;
        }
        return PrecipitationType.NONE;
    }
}
