package pl.edu.agh.to.weatherapp.weather;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.api.WeatherFetcher;
import pl.edu.agh.to.weatherapp.model.ForecastWeatherData;
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
    public ForecastWeatherData getWeatherForecast(String location) {
        return weatherFetcher.fetchForecast(location, 1)
            .thenApply(responseParser::parse)
            .join();
    }

    @Override
    @SneakyThrows
    public InternalWeatherData getSummaryWeatherData(String startLocation, String endLocation) {
        List<ForecastWeatherData> weatherList = new ArrayList<>();
//        weatherList.add(getWeatherForecast(startLocation));
//        weatherList.add(getWeatherForecast(endLocation));
        return weatherSummaryService.getSummary(weatherList);
    }
}
