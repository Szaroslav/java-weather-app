package pl.edu.agh.to.weatherapp.service.weather;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.service.api.WeatherFetcher;
import pl.edu.agh.to.weatherapp.model.dto.DailyWeatherApiDto;
import pl.edu.agh.to.weatherapp.model.internal.Weather;
import pl.edu.agh.to.weatherapp.service.parser.JsonParser;
import pl.edu.agh.to.weatherapp.service.weather.summary.WeatherSummaryService;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

    private DailyWeatherApiDto getForecastWeatherData(String location) {
        final int daysNumber = 1;
        return weatherFetcher.fetchForecast(location, daysNumber)
                .thenApply(responseParser::parseForecast)
                .join();
    }

    private DailyWeatherApiDto getForecastWeatherData(String location, int startHour, int endHour) {
        var forecastWeatherData = getForecastWeatherData(location);
        forecastWeatherData.getHourlyWeatherForecasts()
                .removeIf(weatherData -> !between(weatherData.getDate().getHourOfDay(), startHour, endHour));
        return forecastWeatherData;
    }

    private boolean between(int val, int start, int end) {
        return val >= start && val <= end;
    }

    @Override
    public Weather getForecastSummaryWeatherData(List<String> locations) {
        return getForecastSummaryWeatherData(locations, 0, 24);
    }

    @Override
    public Weather getForecastSummaryWeatherData(List<String> locations, int startHour, int endHour) {
        List<DailyWeatherApiDto> weatherList = locations.stream()
                .map(x->getForecastWeatherData(x,startHour,endHour))
                .toList();
        Weather summary = weatherSummaryService.getSummary(weatherList);
        summary.getLocationNames().addAll(locations);
        //TODO: implement + test
        return summary.setMud(ThreadLocalRandom.current().nextBoolean());
    }
}
