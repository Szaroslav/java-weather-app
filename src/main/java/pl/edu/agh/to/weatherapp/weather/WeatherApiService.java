package pl.edu.agh.to.weatherapp.weather;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.api.WeatherFetcher;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.parser.JsonParser;

@Service
public class WeatherApiService implements WeatherService {
    private final WeatherFetcher weatherFetcher;
    private final JsonParser responseParser;

    public WeatherApiService(WeatherFetcher weatherFetcher, JsonParser responseParser) {
        this.weatherFetcher = weatherFetcher;
        this.responseParser = responseParser;
    }

    @Override
    @SneakyThrows
    public WeatherData getWeatherData(String location) {
        return weatherFetcher.fetchCurrent(location)
            .thenApply(responseParser::parse)
            .join();
    }
}
