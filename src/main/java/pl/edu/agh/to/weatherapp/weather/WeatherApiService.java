package pl.edu.agh.to.weatherapp.weather;

import lombok.SneakyThrows;
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

    @Override
    @SneakyThrows
    public InternalWeatherData getWeatherData(String location) {
        ForecastWeatherData forecast = getForecastWeatherData(location);
        InternalWeatherData weather = weatherSummaryService.getSummary(Collections.singletonList(forecast));
        weather.getLocationNames().add(location);
        return weather;
    }

    @Override
    @SneakyThrows
    public InternalWeatherData getSummaryWeatherData(String startLocation, String endLocation) {
        List<ForecastWeatherData> weatherList = new ArrayList<>();
        weatherList.add(getForecastWeatherData(startLocation));
        weatherList.add(getForecastWeatherData(endLocation));
        InternalWeatherData summary = weatherSummaryService.getSummary(weatherList);
        summary.getLocationNames().add(startLocation);
        summary.getLocationNames().add(endLocation);
        return summary;
    }
}
