package pl.edu.agh.to.weatherapp.service.weather;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.model.dto.DailyWeatherApiDto;
import pl.edu.agh.to.weatherapp.model.internal.Weather;
import pl.edu.agh.to.weatherapp.service.api.WeatherFetcher;
import pl.edu.agh.to.weatherapp.service.parser.JsonParser;
import pl.edu.agh.to.weatherapp.service.weather.summary.WeatherSummaryService;

import java.util.List;

@Service
public class WeatherApiService implements WeatherService {
    private final int noDaysToCheckPrecipitation;
    private final WeatherFetcher weatherFetcher;
    private final JsonParser responseParser;
    private final WeatherSummaryService weatherSummaryService;

    public WeatherApiService(
        int noDaysToCheckPrecipitation,
        WeatherFetcher weatherFetcher,
        JsonParser responseParser,
        WeatherSummaryService weatherSummaryService
    ) {
        this.noDaysToCheckPrecipitation = noDaysToCheckPrecipitation;
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
    public Weather getForecastSummaryWeatherData(List<String> locationNames) {
        return getForecastSummaryWeatherData(locationNames, 0, 24);
    }

    @Override
    public Weather getForecastSummaryWeatherData(List<String> locationNames, int startHour, int endHour) {
        List<DailyWeatherApiDto> weatherList = locationNames.stream()
                .map(locationName -> getForecastWeatherData(locationName, startHour, endHour))
                .toList();
        return weatherSummaryService.getSummary(weatherList)
                .setLocationNames(locationNames)
                .setMud(wasMudDaysBefore(locationNames.get(0), noDaysToCheckPrecipitation));
    }

    public boolean wasMudDaysBefore(String locationName, int daysNumber) {
        DateTime now = DateTime.now();
        for (int i = 1; i <= daysNumber; i++) {
            DailyWeatherApiDto dailyWeatherDto = getHistoryWeatherData(locationName, now.minusDays(i));
            long hoursWithPrecipitationCount = dailyWeatherDto.getHourlyWeatherForecasts()
                    .stream()
                    .filter(hourlyWeather -> hourlyWeather.getPrecipitationMm() > 0)
                    .count();
            if (hoursWithPrecipitationCount > 0) {
                return true;
            }
        }
        return false;
    }
}
