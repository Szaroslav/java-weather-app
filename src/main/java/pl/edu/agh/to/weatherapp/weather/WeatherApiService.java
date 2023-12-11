package pl.edu.agh.to.weatherapp.weather;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.api.WeatherFetcher;
import pl.edu.agh.to.weatherapp.model.ForecastWeatherData;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;
import pl.edu.agh.to.weatherapp.parser.JsonParser;
import pl.edu.agh.to.weatherapp.weather.summary.WeatherSummaryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WeatherApiService implements WeatherService {
    private final WeatherFetcher weatherFetcher;
    private final JsonParser responseParser;
    private final WeatherSummaryService weatherSummaryService;

    public WeatherApiService(WeatherFetcher weatherFetcher, JsonParser responseParser, WeatherSummaryService weatherSummaryService) {
        this.weatherFetcher = weatherFetcher;
        this.responseParser = responseParser;
        this.weatherSummaryService = weatherSummaryService;
    }

    private ForecastWeatherData getForecastWeatherData(String location) {
        final int daysNumber = 1;
        return weatherFetcher.fetchForecast(location, daysNumber)
                .thenApply(responseParser::parseForecast)
                .join();
    }

    private ForecastWeatherData getForecastWeatherData(String location, int startHour, int endHour) {
        var forecastWeatherData = getForecastWeatherData(location);
        forecastWeatherData.getHourlyWeatherForecasts()
                .removeIf(weatherData -> !between(weatherData.getDate().getHourOfDay(), startHour, endHour));
        return forecastWeatherData;
    }

    private boolean between(int val, int start, int end) {
        return val >= start && val < end;
    }

    @Override
    public InternalWeatherData getWeatherData(String location) {
        return getWeatherData(location, 0, 24);
    }

    @Override
    public InternalWeatherData getWeatherData(String location, int startHour, int endHour) {
        ForecastWeatherData forecast = getForecastWeatherData(location, startHour, endHour);
        InternalWeatherData weather = weatherSummaryService.getSummary(Collections.singletonList(forecast));
        weather.getLocationNames().add(location);
        return weather;
    }

    @Override
    public InternalWeatherData getSummaryWeatherData(String startLocation, String endLocation) {
        return getSummaryWeatherData(startLocation, endLocation, 0, 24);
    }

    @Override
    public InternalWeatherData getSummaryWeatherData(String startLocation, String endLocation, int startHour, int endHour) {
        List<ForecastWeatherData> weatherList = new ArrayList<>();
        weatherList.add(getForecastWeatherData(startLocation, startHour, endHour));
        weatherList.add(getForecastWeatherData(endLocation, startHour, endHour));
        InternalWeatherData summary = weatherSummaryService.getSummary(weatherList);
        summary.getLocationNames().add(startLocation);
        summary.getLocationNames().add(endLocation);
        return summary;
    }
}
