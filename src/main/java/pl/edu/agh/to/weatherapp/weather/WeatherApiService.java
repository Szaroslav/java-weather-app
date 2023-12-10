package pl.edu.agh.to.weatherapp.weather;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.api.WeatherFetcher;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;
import pl.edu.agh.to.weatherapp.parser.JsonParser;
import pl.edu.agh.to.weatherapp.weather.summary.WeatherSummaryService;

import java.util.ArrayList;
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

    @Override
    @SneakyThrows
    public WeatherData getWeatherData(String location) {
        return weatherFetcher.fetchCurrent(location)
            .thenApply(responseParser::parse)
            .join();
    }

    @Override
    @SneakyThrows
    public InternalWeatherData getSummaryWeatherData(String startLocation, String endLocation) {
        List<WeatherData> weatherList = new ArrayList<>();
        weatherList.add(getWeatherData(startLocation));
        weatherList.add(getWeatherData(endLocation));
        return weatherSummaryService.getSummary(weatherList);
    }
}
