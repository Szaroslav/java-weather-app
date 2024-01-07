package pl.edu.agh.to.weatherapp.service.weather;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.model.dto.HourlyWeatherApiDto;
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

    private DailyWeatherApiDto getHistoryWeatherData(String location, DateTime date) {
        return weatherFetcher.fetchHistory(location, date)
            .thenApply(responseParser::parseForecast)
            .join();
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

    @Override
    public boolean wasPrecipitationDaysBefore(String locationName, int daysNumber) {
        DateTime now = DateTime.now();

        for (int i = 1; i <= daysNumber; i++) {
            DailyWeatherApiDto dailyWeatherDto = getHistoryWeatherData(locationName, now.minusDays(1));
            long hoursWithPrecipitation = dailyWeatherDto.getHourlyWeatherForecasts()
                .stream()
                .filter(hourlyWeather -> hourlyWeather.getPrecipitationMm() > 0)
                .count();
            if (hoursWithPrecipitation > 0) {
                return true;
            }
        }

        return false;
    }
}
